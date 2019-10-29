package net.tropicraft.core.common.block.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.tropicraft.Constants;

public class BambooChestTileEntity extends ChestTileEntity {

    /** Is this chest unbreakble (Koa chest) */
    private boolean unbreakable = false;

    public BambooChestTileEntity() {
        super(TropicraftTileEntityTypes.BAMBOO_CHEST.get());
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent(Constants.MODID + ".container.bambooChest");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return getName();
    }

    public void read(CompoundNBT compound) {
        super.read(compound);
        unbreakable = compound.getBoolean("unbreakable");
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
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
