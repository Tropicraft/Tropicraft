package net.tropicraft.core.common.block.tileentity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    /**
     * Number of ticks the mixer has been mixin'
     */
    private int ticks;
    private final List<ItemStack> drinkIngredients;
    private boolean mixing;
    public ItemStack result = ItemStack.EMPTY;

    public DrinkMixerBlockEntity(BlockEntityType<DrinkMixerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        mixing = false;
        drinkIngredients = new ArrayList<>();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ticks = input.getIntOr("MixTicks", 0);
        mixing = input.getBooleanOr("Mixing", false);

        setDrinkIngredients(input.read("ingredients", ItemStack.SINGLE_ITEM_CODEC.listOf()).orElse(List.of()));
        result = input.read("Result", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("MixTicks", ticks);
        output.putBoolean("Mixing", mixing);

        output.store("ingredients", ItemStack.SINGLE_ITEM_CODEC.listOf(), drinkIngredients);

        if (!result.isEmpty()) {
            output.store("Result", ItemStack.CODEC, result);
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

    public List<ItemStack> getDrinkIngredients() {
        return drinkIngredients;
    }

    public void setDrinkIngredients(List<ItemStack> ingredients) {
        drinkIngredients.clear();
        drinkIngredients.addAll(ingredients);
    }

    public boolean tryStartMixing() {
        if (mixing) {
            return false;
        }
        if (Drink.makeCocktail(level.registryAccess(), drinkIngredients) == null) {
            return false;
        }
        ticks = 0;
        mixing = true;
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new ClientboundMixerStartPacket(getBlockPos()));
        }
        return true;
    }

    public void setMixing() {
        ticks = 0;
        mixing = true;
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
        for (ItemStack stack : drinkIngredients) {
            dropItem(stack, at);
        }
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

        for (ItemStack ingredient : drinkIngredients) {
            ItemStack container = ingredient.getCraftingRemainder();
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

    public boolean tryTransferToMixer(Level level, ItemStack itemStack, LivingEntity entity) {
        if (mixing || drinkIngredients.size() >= Drink.MAX_INGREDIENTS) {
            return false;
        }
        Holder<DrinkIngredient> ingredient = DrinkIngredient.findMatchingIngredient(level.registryAccess(), itemStack);
        if (ingredient != null) {
            drinkIngredients.add(itemStack.consumeAndReturn(1, entity));
            syncInventory();
            return true;
        }
        return false;
    }

    public boolean isMixing() {
        return mixing;
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
    public void onDataPacket(Connection net, ValueInput input) {
        loadAdditional(input);
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
        return saveCustomOnly(registries);
    }

    public ItemStack getResult() {
        return Drink.getResult(level.registryAccess(), drinkIngredients);
    }
}
