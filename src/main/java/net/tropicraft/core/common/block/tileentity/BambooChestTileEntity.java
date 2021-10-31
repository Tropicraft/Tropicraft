package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.tropicraft.Constants;

public class BambooChestTileEntity extends ChestBlockEntity {

    /** Is this chest unbreakble (Koa chest) */
    private boolean unbreakable = false;

    public BambooChestTileEntity(BlockPos pos, BlockState blockstate) {
        super(TropicraftTileEntityTypes.BAMBOO_CHEST.get(), pos, blockstate);
    }

    @Override
    public Component getName() {
        return new TranslatableComponent(Constants.MODID + ".container.bambooChest");
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
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putBoolean("unbreakable", unbreakable);

        return compound;
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
