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

        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(25, 0).mirror().addBox(0F, 0F, 0F, 1, 7, 1), PartPose.offsetAndRotation(1F, 17F, 0F, 0F, 0F, 0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(25, 0).mirror().addBox(-1F, 0F, 0F, 1, 7, 1), PartPose.offsetAndRotation(-1F, 17F, 0F, 0F, 0F, 0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 8).mirror().addBox(-2F, -3F, 0F, 4, 7, 3), PartPose.offsetAndRotation(0F, 13F, 2F, 0F, 3.141593F, 0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 18).mirror().addBox(-2F, -3F, -1F, 4, 3, 4), PartPose.offsetAndRotation(0F, 10F, 1F, 0F, 3.141593F, 0F));

        PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-6F, -0.5F, -0.5F, 6, 1, 1), PartPose.offsetAndRotation(-2F, 10.5F, 0.5F, 0F, 0F, 0F));
        rightArm.addOrReplaceChild("right_arm_sub", CubeListBuilder.create().texOffs(31, 0).mirror().addBox(-0.5F, -6F, -0.5F, 1, 6, 1), PartPose.offsetAndRotation(-5.5F, 0F, 0F, 0F, 0F, 0F));

        PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(0F, -0.5F, -0.5F, 6, 1, 1), PartPose.offsetAndRotation(2F, 10.46667F, 0.5F, 0F, 0F, 0F));
        leftArm.addOrReplaceChild("left_arm_sub", CubeListBuilder.create().mirror(true).texOffs(31, 0).addBox(-0.5F, -6F, -0.5F, 1, 6, 1), PartPose.offsetAndRotation(5.5F, 0F, 0F, 0F, 0F, 0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(AshenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        final float armRotater = 71.46f * Mth.DEG_TO_RAD;
        final float subStraight = 90.0f * Mth.DEG_TO_RAD;
        float headAngle;

        switch (entity.getActionState()) {
            case LOST_MASK -> {
                headAngle = -0.4F;
                rightArm.zRot = -armRotater;
                rightArmSub.zRot = -5.1F;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = 5.1F;
                leftArm.xRot = subStraight;
                rightArm.xRot = subStraight;
                rightArm.yRot = -.5F;
                leftArm.yRot = .5F;
            }
            case HOSTILE -> {
                headAngle = 0.0F;
                leftArm.xRot = 1.65F + limbSwing / 125F;
                leftArm.yRot = .9F + limbSwingAmount / 125F;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = 6.2F;
                rightArm.zRot = 0.0F - Mth.sin(limbSwingAmount * 0.75F) * 0.0220F;
                rightArm.yRot = 0.0F;
                rightArmSub.zRot = 0.0F;
                if (swinging) {
                    rightArm.xRot += Mth.sin(limbSwingAmount * 0.75F) * 0.0520F;
                } else {
                    rightArm.xRot = 0.0F;
                }
            }
            default -> {
                headAngle = 0;
                rightArm.zRot = -armRotater;
                rightArmSub.zRot = -subStraight;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = subStraight;
                rightArm.yRot = 0F;
                leftArm.yRot = 0F;
            }
        }

        head.xRot = headPitch / 125F + headAngle;
        head.yRot = netHeadYaw / 125F + Mth.PI;

        leftArm.zRot += Mth.sin(ageInTicks * 0.25F) * 0.020F;
        rightArm.zRot -= Mth.sin(ageInTicks * 0.25F) * 0.020F;
    }

    @Override
    public Iterable<ModelPart> parts() {
        return List.of(body, head, rightArm, leftArm, leftLeg, rightLeg);
    }

    @Override
    public void prepareMobModel(final AshenEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.25F * limbSwingAmount;
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.25F * limbSwingAmount;
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        stack.translate(0.09375F, 0.1875F, 0.0F);
    }
}
