package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class EagleRayModel extends EntityModel<LivingEntityRenderState> {
    public EagleRayModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2.0f, 0.0f, 0.0f, 5, 3, 32), PartPose.offset(0.0f, 0.0f, -8.0f));

        return LayerDefinition.create(mesh, 128, 64);
    }
}
