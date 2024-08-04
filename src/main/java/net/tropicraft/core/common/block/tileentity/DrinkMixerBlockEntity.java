package net.tropicraft.core.common.block.tileentity;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.tropicraft.core.common.drinks.Ingredient;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.item.CocktailItem;
import net.tropicraft.core.common.network.message.ClientboundMixerInventoryPacket;
import net.tropicraft.core.common.network.message.ClientboundMixerStartPacket;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

public class DrinkMixerBlockEntity extends BlockEntity implements IMachineBlock {
    /**
     * Number of ticks to mix
     */
    private static final int TICKS_TO_MIX = 4 * 20;
    private static final int MAX_NUM_INGREDIENTS = 3;

    /**
     * Number of ticks the mixer has been mixin'
     */
    private int ticks;
    public NonNullList<ItemStack> ingredients;
    private boolean mixing;
    public ItemStack result = ItemStack.EMPTY;

    public DrinkMixerBlockEntity(BlockEntityType<DrinkMixerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        mixing = false;
        ingredients = NonNullList.withSize(MAX_NUM_INGREDIENTS, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        ticks = nbt.getInt("MixTicks");
        mixing = nbt.getBoolean("Mixing");

        for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
            if (nbt.contains("Ingredient" + i)) {
                ingredients.set(i, ItemStack.parse(registries, nbt.getCompound("Ingredient" + i)).orElse(ItemStack.EMPTY));
            } else {
                ingredients.set(i, ItemStack.EMPTY);
            }
        }

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

        for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
            ItemStack item = ingredients.get(i);
            if (!item.isEmpty()) {
                nbt.put("Ingredient" + i, item.save(registries, new CompoundTag()));
            }
        }

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

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
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
        for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
            if (!ingredients.get(i).isEmpty()) {
                dropItem(ingredients.get(i), at);
                ingredients.set(i, ItemStack.EMPTY);
            }
        }

        ticks = TICKS_TO_MIX;
        mixing = false;
        syncInventory();
    }

    public void retrieveResult(@Nullable Player at) {
        if (result.isEmpty()) {
            return;
        }

        dropItem(result, at);

        for (int i = 0; i < MAX_NUM_INGREDIENTS; i++) {
            // If we're not using one of the ingredient slots, just move along
            if (ingredients.get(i).isEmpty()) {
                continue;
            }

            ItemStack container = ingredients.get(i).getItem().getCraftingRemainingItem(ingredients.get(i));

            if (!container.isEmpty()) {
                dropItem(container, at);
            }
        }

        ingredients.clear();
        result = ItemStack.EMPTY;
        syncInventory();
    }

    public void finishMixing() {
        result = getResult(getIngredients());
        mixing = false;
        ticks = 0;
        syncInventory();
    }

    public boolean addToMixer(ItemStack itemStack) {
        boolean isDrink = CocktailItem.isDrink(itemStack);
        Ingredient ingredient = Ingredient.findMatchingIngredient(itemStack);
        if (ingredient == null && !isDrink) {
            return false;
        }

        Set<Ingredient> seenIngredients = new ObjectArraySet<>(ingredients.size());
        seenIngredients.add(ingredient);
        boolean hasDrink = isDrink;

        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack otherItemStack = ingredients.get(i);
            // Found an empty slot and no conflicts, place our new item in
            if (otherItemStack.isEmpty()) {
                ingredients.set(i, itemStack);
                syncInventory();
                return true;
            }

            if (CocktailItem.isDrink(otherItemStack)) {
                if (hasDrink) {
                    return false;
                }
                hasDrink = true;
            }
        }

        return false;
    }

    public boolean isMixing() {
        return mixing;
    }

    private boolean isMixerFull() {
        for (ItemStack ingredient : ingredients) {
            if (ingredient.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean canMix() {
        return !mixing && isMixerFull();
    }

    /* == IMachineTile == */

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

    public ItemStack getResult(NonNullList<ItemStack> ingredients2) {
        return MixerRecipes.getResult(level.registryAccess(), ingredients2);
    }
}
