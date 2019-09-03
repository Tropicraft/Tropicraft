package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

public abstract class MachineModel<T extends TileEntity & IMachineTile> extends Model {

    public abstract void renderAsBlock(T te);

    public abstract String getTexture(T te);
}
