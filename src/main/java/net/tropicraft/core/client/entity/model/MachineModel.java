package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

import java.util.function.Function;

public abstract class MachineModel<T extends TileEntity & IMachineTile> extends Model {
    public MachineModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
    }

    public abstract void renderAsBlock(T te);

    public abstract String getTexture(T te);
}
