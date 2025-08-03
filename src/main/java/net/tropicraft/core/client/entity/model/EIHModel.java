package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class EIHModel extends EntityModel<LivingEntityRenderState> {
    public EIHModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 8).addBox(-4.0f, 1.0f, -1.0f, 8, 17, 7), PartPose.offset(0.0f, -2.0f, 0.0f));
        root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, 11.0f, -3.0f, 8, 8, 11), PartPose.offset(0.0f, 5.0f, -2.0f));
        root.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(27, 2).addBox(13.5f, -1.0f, -3.0f, 13, 2, 3), PartPose.offsetAndRotation(0.0f, -14.8f, -1.0f, 0, 0, Mth.HALF_PI));
        root.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(56, 11).addBox(-1.5f, 4.0f, -1.0f, 3, 3, 1), PartPose.offset(0.0f, 7.5f, -0.5f));
        root.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0f, -1.0f, -10.0f, 8, 5, 10), PartPose.offset(0.0f, -5.0f, 6.0f));
        root.addOrReplaceChild("leye", CubeListBuilder.create().texOffs(56, 7).mirror().addBox(0.0f, 0.0f, 0.0f, 3, 3, 1), PartPose.offset(1.0f, -1.0f, -2.0f));
        root.addOrReplaceChild("reye", CubeListBuilder.create().texOffs(56, 7).addBox(-1.5f, -1.0f, -1.0f, 3, 3, 1), PartPose.offset(-2.5f, 0.0f, -1.0f));

        return LayerDefinition.create(mesh, 64, 32).apply(ModelAnimator.scaling(2.0f, 1.75f, 2.0f));
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        // it's a statue, what do you want from me
    }
}
