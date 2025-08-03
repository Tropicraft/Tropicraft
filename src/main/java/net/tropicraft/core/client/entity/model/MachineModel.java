package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

public abstract class MachineModel extends Model {
    public MachineModel(ModelPart root) {
        super(root, RenderType::entitySolid);
    }
}
