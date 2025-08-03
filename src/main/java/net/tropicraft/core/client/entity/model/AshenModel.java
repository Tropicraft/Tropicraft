package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.tropicraft.core.client.entity.render.state.AshenRenderState;

public class AshenModel extends EntityModel<AshenRenderState> implements ArmedModel {
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    public final ModelPart body;
    public final ModelPart head;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightArmSub;
    public final ModelPart leftArmSub;

    public AshenModel(ModelPart root) {
        super(root);
        rightLeg = root.getChild("right_leg");
        leftLeg = root.getChild("left_leg");
        body = root.getChild("body");
        head = root.getChild("head");
        rightArm = root.getChild("right_arm");
        leftArm = root.getChild("left_arm");
        rightArmSub = rightArm.getChild("right_arm_sub");
        leftArmSub = leftArm.getChild("left_arm_sub");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(25, 0).mirror().addBox(0.0f, 0.0f, 0.0f, 1, 7, 1), PartPose.offsetAndRotation(1.0f, 17.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(25, 0).mirror().addBox(-1.0f, 0.0f, 0.0f, 1, 7, 1), PartPose.offsetAndRotation(-1.0f, 17.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 8).mirror().addBox(-2.0f, -3.0f, 0.0f, 4, 7, 3), PartPose.offsetAndRotation(0.0f, 13.0f, 2.0f, 0.0f, Mth.PI, 0.0f));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 18).mirror().addBox(-2.0f, -3.0f, -1.0f, 4, 3, 4), PartPose.offsetAndRotation(0.0f, 10.0f, 1.0f, 0.0f, Mth.PI, 0.0f));

        PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-6.0f, -0.5f, -0.5f, 6, 1, 1), PartPose.offsetAndRotation(-2.0f, 10.5f, 0.5f, 0.0f, 0.0f, 0.0f));
        rightArm.addOrReplaceChild("right_arm_sub", CubeListBuilder.create().texOffs(31, 0).mirror().addBox(-0.5f, -6.0f, -0.5f, 1, 6, 1), PartPose.offsetAndRotation(-5.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(0.0f, -0.5f, -0.5f, 6, 1, 1), PartPose.offsetAndRotation(2.0f, 10.46667f, 0.5f, 0.0f, 0.0f, 0.0f));
        leftArm.addOrReplaceChild("left_arm_sub", CubeListBuilder.create().mirror(true).texOffs(31, 0).addBox(-0.5f, -6.0f, -0.5f, 1, 6, 1), PartPose.offsetAndRotation(5.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(AshenRenderState state) {
        super.setupAnim(state);

        rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f) * 1.25f * state.walkAnimationSpeed;
        leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f + Mth.PI) * 1.25f * state.walkAnimationSpeed;

        final float armRotater = 71.46f * Mth.DEG_TO_RAD;
        final float subStraight = 90.0f * Mth.DEG_TO_RAD;
        float headAngle;

        switch (state.actionState) {
            case LOST_MASK -> {
                headAngle = -0.4f;
                rightArm.zRot = -armRotater;
                rightArmSub.zRot = -5.1f;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = 5.1f;
                leftArm.xRot = subStraight;
                rightArm.xRot = subStraight;
                rightArm.yRot = -0.5f;
                leftArm.yRot = 0.5f;
            }
            case HOSTILE -> {
                headAngle = 0.0f;
                leftArm.xRot = 1.65f + state.walkAnimationPos / 125.0f;
                leftArm.yRot = 0.9f + state.walkAnimationSpeed / 125.0f;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = 6.2f;
                rightArm.zRot = 0.0f - Mth.sin(state.walkAnimationSpeed * 0.75f) * 0.0220f;
                rightArm.yRot = 0.0f;
                rightArmSub.zRot = 0.0f;
                if (state.swinging) {
                    rightArm.xRot += Mth.sin(state.walkAnimationSpeed * 0.75f) * 0.0520f;
                } else {
                    rightArm.xRot = 0.0f;
                }
            }
            default -> {
                headAngle = 0;
                rightArm.zRot = -armRotater;
                rightArmSub.zRot = -subStraight;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = subStraight;
                rightArm.yRot = 0.0f;
                leftArm.yRot = 0.0f;
            }
        }

        head.xRot = state.xRot / 125.0f + headAngle;
        head.yRot = state.yRot / 125.0f + Mth.PI;

        leftArm.zRot += Mth.sin(state.ageInTicks * 0.25f) * 0.020f;
        rightArm.zRot -= Mth.sin(state.ageInTicks * 0.25f) * 0.020f;
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        stack.translate(0.09375f, 0.1875f, 0.0f);
    }
}
