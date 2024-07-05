package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.Constants;

public class BambooChestBlockEntity extends ChestBlockEntity {

    /**
     * Is this chest unbreakble (Koa chest)
     */
    private boolean unbreakable = false;

    public BambooChestBlockEntity(final BlockEntityType<BambooChestBlockEntity> type, final BlockPos pos, final BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Component getName() {
        return Component.translatable(Constants.MODID + ".container.bambooChest");
    }

    @Override
    protected Component getDefaultName() {
        return getName();
    }

    @Override
    protected void loadAdditional(final CompoundTag compound, final HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        unbreakable = compound.getBoolean("unbreakable");
    }

    @Override
    protected void saveAdditional(final CompoundTag compound, final HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putBoolean("unbreakable", unbreakable);
    }

    /**
     * @return Returns if this chest is unbreakable
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * Sets whether this chest is unbreakable or not
     *
     * @param flag Value to set the unbreakable flag to
     */
    public void setIsUnbreakable(boolean flag) {
        unbreakable = flag;
    }
}
