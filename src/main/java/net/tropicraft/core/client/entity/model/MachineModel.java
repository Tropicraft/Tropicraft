package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;

public abstract class MachineModel extends Model<Unit> {
    public MachineModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }
}
