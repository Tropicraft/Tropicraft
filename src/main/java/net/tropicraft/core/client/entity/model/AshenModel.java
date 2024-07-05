package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

import java.util.List;

public class AshenModel extends ListModel<AshenEntity> implements ArmedModel {

    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    public final ModelPart body;
    public final ModelPart head;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightArmSub;
    public final ModelPart leftArmSub;
    public boolean swinging;

    public AshenModel(ModelPart root) {
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
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 8).mirror().addBox(-2.0f, -3.0f, 0.0f, 4, 7, 3), PartPose.offsetAndRotation(0.0f, 13.0f, 2.0f, 0.0f, 3.141593f, 0.0f));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 18).mirror().addBox(-2.0f, -3.0f, -1.0f, 4, 3, 4), PartPose.offsetAndRotation(0.0f, 10.0f, 1.0f, 0.0f, 3.141593f, 0.0f));

        PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-6.0f, -0.5f, -0.5f, 6, 1, 1), PartPose.offsetAndRotation(-2.0f, 10.5f, 0.5f, 0.0f, 0.0f, 0.0f));
        rightArm.addOrReplaceChild("right_arm_sub", CubeListBuilder.create().texOffs(31, 0).mirror().addBox(-0.5f, -6.0f, -0.5f, 1, 6, 1), PartPose.offsetAndRotation(-5.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(0.0f, -0.5f, -0.5f, 6, 1, 1), PartPose.offsetAndRotation(2.0f, 10.46667f, 0.5f, 0.0f, 0.0f, 0.0f));
        leftArm.addOrReplaceChild("left_arm_sub", CubeListBuilder.create().mirror(true).texOffs(31, 0).addBox(-0.5f, -6.0f, -0.5f, 1, 6, 1), PartPose.offsetAndRotation(5.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(AshenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        final float armRotater = 71.46f * Mth.DEG_TO_RAD;
        final float subStraight = 90.0f * Mth.DEG_TO_RAD;
        float headAngle;

        switch (entity.getActionState()) {
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
                leftArm.xRot = 1.65f + limbSwing / 125.0f;
                leftArm.yRot = 0.9f + limbSwingAmount / 125.0f;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = 6.2f;
                rightArm.zRot = 0.0f - Mth.sin(limbSwingAmount * 0.75f) * 0.0220f;
                rightArm.yRot = 0.0f;
                rightArmSub.zRot = 0.0f;
                if (swinging) {
                    rightArm.xRot += Mth.sin(limbSwingAmount * 0.75f) * 0.0520f;
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

        head.xRot = headPitch / 125.0f + headAngle;
        head.yRot = netHeadYaw / 125.0f + Mth.PI;

        leftArm.zRot += Mth.sin(ageInTicks * 0.25f) * 0.020f;
        rightArm.zRot -= Mth.sin(ageInTicks * 0.25f) * 0.020f;
    }

    @Override
    public Iterable<ModelPart> parts() {
        return List.of(body, head, rightArm, leftArm, leftLeg, rightLeg);
    }

    @Override
    public void prepareMobModel(AshenEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662f) * 1.25f * limbSwingAmount;
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662f + 3.141593f) * 1.25f * limbSwingAmount;
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        stack.translate(0.09375f, 0.1875f, 0.0f);
    }
}
