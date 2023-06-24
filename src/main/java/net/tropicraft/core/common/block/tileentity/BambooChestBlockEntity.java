package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.Constants;

public class BambooChestBlockEntity extends ChestBlockEntity {

    /** Is this chest unbreakble (Koa chest) */
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
    public void load(CompoundTag compound) {
        super.load(compound);
        unbreakable = compound.getBoolean("unbreakable");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("unbreakable", unbreakable);
    }

    /**
     *
     * @return Returns if this chest is unbreakable
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * Sets whether this chest is unbreakable or not
     * @param flag Value to set the unbreakable flag to
     */
    public void setIsUnbreakable(boolean flag) {
        unbreakable = flag;
    }
}
