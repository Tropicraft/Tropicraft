package net.tropicraft.core.client.block.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

public abstract class MachineModel<T extends TileEntity & IMachineTile> extends ModelBase {

    public abstract void renderAsBlock(T te);

    public abstract String getTexture(T te);
}
