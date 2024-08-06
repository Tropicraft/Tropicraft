package net.tropicraft.core.common.block.tileentity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tropicraft.core.common.block.DrinkMixerBlock;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.DrinkIngredient;
import net.tropicraft.core.common.network.message.ClientboundMixerInventoryPacket;
import net.tropicraft.core.common.network.message.ClientboundMixerStartPacket;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DrinkMixerBlockEntity extends BlockEntity implements IMachineBlock {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Number of ticks to mix
     */
    private static final int TICKS_TO_MIX = 4 * 20;
    private static final int MAX_NUM_INGREDIENTS = 3;

    /**
     * Number of ticks the mixer has been mixin'
     */
    private int ticks;
    private final List<Holder<DrinkIngredient>> drinkIngredients;
    private boolean mixing;
    public ItemStack result = ItemStack.EMPTY;

    public DrinkMixerBlockEntity(BlockEntityType<DrinkMixerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        mixing = false;
        drinkIngredients = new ArrayList<>();
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        ticks = nbt.getInt("MixTicks");
        mixing = nbt.getBoolean("Mixing");

        DrinkIngredient.LIST_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), nbt.get("ingredients"))
                .resultOrPartial(error -> LOGGER.error("Failed to parse drink mixer ingredients: '{}'", error))
                .ifPresent(this::setDrinkIngredients);

        if (nbt.contains("Result")) {
            result = ItemStack.parse(registries, nbt.getCompound("Result")).orElse(ItemStack.EMPTY);
        } else {
            result = ItemStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);

        nbt.putInt("MixTicks", ticks);
        nbt.putBoolean("Mixing", mixing);

        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        nbt.put("ingredients", DrinkIngredient.LIST_CODEC.encodeStart(registryOps, drinkIngredients).getOrThrow());

        if (!result.isEmpty()) {
            nbt.put("Result", result.save(registries, new CompoundTag()));
        }
    }

    public static void mixTick(Level level, BlockPos pos, BlockState state, DrinkMixerBlockEntity mixer) {
        mixer.tick();
    }

    private void tick() {
        if (ticks < TICKS_TO_MIX && mixing) {
            ticks++;
            if (ticks == TICKS_TO_MIX) {
                finishMixing();
            }
        }
    }

    public boolean isDoneMixing() {
        return !result.isEmpty();
    }

    public List<Holder<DrinkIngredient>> getDrinkIngredients() {
        return drinkIngredients;
    }

    public void setDrinkIngredients(List<Holder<DrinkIngredient>> ingredients) {
        drinkIngredients.clear();
        drinkIngredients.addAll(ingredients);
    }

    public void startMixing() {
        ticks = 0;
        mixing = true;
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new ClientboundMixerStartPacket(getBlockPos()));
        }
    }

    private void dropItem(ItemStack stack, @Nullable Player at) {
        if (at == null) {
            BlockPos pos = getBlockPos().relative(getBlockState().getValue(DrinkMixerBlock.FACING));
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        } else {
            Containers.dropItemStack(level, at.getX(), at.getY(), at.getZ(), stack);
        }
    }

    public void emptyMixer(@Nullable Player at) {
        drinkIngredients.forEach(drinkIngredientHolder -> dropItem(drinkIngredientHolder.value().getStack(), at));
        drinkIngredients.clear();

        ticks = TICKS_TO_MIX;
        mixing = false;
        syncInventory();
    }

    public void retrieveResult(@Nullable Player at) {
        if (result.isEmpty()) {
            return;
        }

        dropItem(result, at);

        for (Holder<DrinkIngredient> drinkIngredient : drinkIngredients) {
            ItemStack container = new ItemStack(drinkIngredient.value().item().value()).getCraftingRemainingItem();

            if (!container.isEmpty()) {
                dropItem(container, at);
            }
        }
        drinkIngredients.clear();
        result = ItemStack.EMPTY;
        syncInventory();
    }

    public void finishMixing() {
        result = getResult();
        mixing = false;
        ticks = 0;
        syncInventory();
    }

    public boolean addToMixer(Level level, ItemStack itemStack) {
        if (isMixerFull()) {
            return false;
        }

        Holder<DrinkIngredient> ingredientHolder = DrinkIngredient.findMatchingIngredient(level.registryAccess(), itemStack);
        if (ingredientHolder == null) {
            return false;
        }
        drinkIngredients.add(ingredientHolder);
        syncInventory();
        return true;
    }

    public boolean isMixing() {
        return mixing;
    }

    private boolean isMixerFull() {
        return drinkIngredients.size() >= MAX_NUM_INGREDIENTS;
    }

    public boolean canMix() {
        return !mixing && isMixerFull();
    }

    @Override
    public boolean isActive() {
        return isMixing();
    }

    @Override
    public float getProgress(float partialTicks) {
        return (ticks + partialTicks) / TICKS_TO_MIX;
    }

    @Override
    public Direction getDirection(BlockState state) {
        return state.getValue(DrinkMixerBlock.FACING);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        loadAdditional(pkt.getTag(), registries);
    }

    protected void syncInventory() {
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new ClientboundMixerInventoryPacket(this));
        }
        setChanged();
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return writeItems(new CompoundTag(), registries);
    }

    private CompoundTag writeItems(CompoundTag nbt, HolderLookup.Provider registries) {
        saveAdditional(nbt, registries);
        return nbt;
    }

    public ItemStack getResult() {
        return Drink.getResult(level.registryAccess(), drinkIngredients);
    }
}
