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

public class TreeFrogModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart rearRightLeg;
    private final ModelPart rearLeftLeg;

    public TreeFrogModel(ModelPart root) {
        super(root);
        frontLeftLeg = root.getChild("frontLeftLeg");
        frontRightLeg = root.getChild("frontRightLeg");
        rearRightLeg = root.getChild("rearRightLeg");
        rearLeftLeg = root.getChild("rearLeftLeg");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("frontLeftLeg",
                CubeListBuilder.create().texOffs(12, 19)
                        .addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4),
                PartPose.offset(2.0f, 23.0f, -3.0f));

        root.addOrReplaceChild("frontRightLeg",
                CubeListBuilder.create().texOffs(12, 14)
                        .addBox(-2.0f, 0.0f, -2.0f, 4, 1, 4),
                PartPose.offset(-2.0f, 23.0f, -3.0f));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(28, 8)
                        .addBox(-2.0f, -5.0f, -2.0f, 4, 9, 4),
                PartPose.offsetAndRotation(0.0f, 21.0f, 1.0f, Mth.HALF_PI, 0.0f, 0.0f));

        root.addOrReplaceChild("rearRightLeg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-3.0f, 0.0f, -2.0f, 3, 5, 3),
                PartPose.offset(-2.0f, 19.0f, 4.0f));

        root.addOrReplaceChild("rearLeftLeg",
                CubeListBuilder.create().texOffs(0, 8)
                        .addBox(0.0f, 0.0f, -2.0f, 3, 5, 3),
                PartPose.offset(2.0f, 19.0f, 4.0f));

        root.addOrReplaceChild("rightEye",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offset(-1.0f, 19.0f, -1.0f));

        root.addOrReplaceChild("leftEye",
                CubeListBuilder.create().texOffs(0, 4)
                        .addBox(0.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offset(1.0f, 19.0f, -1.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        frontLeftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f) * 1.4f * state.walkAnimationSpeed;
        rearLeftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f + Mth.PI) * 1.4f * state.walkAnimationSpeed;
        rearRightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f + Mth.PI) * 1.4f * state.walkAnimationSpeed;
        frontRightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f) * 1.4f * state.walkAnimationSpeed;
    }
}
