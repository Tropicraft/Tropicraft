package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenModel extends ListModel<AshenEntity> implements ArmedModel {
    public ModelPart rightLeg;
    public ModelPart leftLeg;
    public ModelPart body;
    public ModelPart head;
    public ModelPart mask;
    public ModelPart rightArm;
    public ModelPart leftArm;
    public ModelPart rightArmSub;
    public ModelPart leftArmSub;
    public float headAngle;
    public boolean swinging;
    public AshenEntity.AshenState actionState;

    public AshenModel() {
        swinging = false;
        actionState = AshenEntity.AshenState.PEACEFUL;
        headAngle = 0;
        texWidth = 64;
        texHeight = 32;

        rightLeg = new ModelPart(this, 25, 0);
        rightLeg.addBox(0F, 0F, 0F, 1, 7, 1);
        rightLeg.setPos(1F, 17F, 0F);
        rightLeg.setTexSize(64, 32);
        rightLeg.mirror = true;
        setRotation(rightLeg, 0F, 0F, 0F);
        leftLeg = new ModelPart(this, 25, 0);
        leftLeg.addBox(-1F, 0F, 0F, 1, 7, 1);
        leftLeg.setPos(-1F, 17F, 0F);
        leftLeg.setTexSize(64, 32);
        leftLeg.mirror = true;
        setRotation(leftLeg, 0F, 0F, 0F);
        body = new ModelPart(this, 24, 8);
        body.addBox(-2F, -3F, 0F, 4, 7, 3);
        body.setPos(0F, 13F, 2F);
        body.setTexSize(64, 32);
        body.mirror = true;
        setRotation(body, 0F, 3.141593F, 0F);
        head = new ModelPart(this, 24, 18);
        head.addBox(-2F, -3F, -1F, 4, 3, 4);
        head.setPos(0F, 10F, 1F);
        head.setTexSize(64, 32);
        head.mirror = true;
        setRotation(head, 0F, 3.141593F, 0F);
        
        //mask = new ModelRenderer(this, 0, 0);
        //mask.addBox(-5.5F, -10F, 3F, 11, 22, 1);
        //mask.setRotationPoint(0F, 10F, 1F);
        //mask.setTextureSize(64, 32);
        //mask.mirror = true;
        //setRotation(mask, 0F, 3.141593F, 0F);

        rightArm = new ModelPart(this);
        rightArm.setPos(-2F, 10.5F, 0.5F);
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = true;
        rightArm.texOffs(0, 24).addBox(-6F, -0.5F, -0.5F, 6, 1, 1);
        rightArmSub = new ModelPart(this);
        rightArmSub.setPos(-5.5F, 0F, 0F);
        setRotation(rightArmSub, 0F, 0F, 0F);
        rightArmSub.mirror = true;
        rightArmSub.texOffs(31, 0).addBox(-0.5F, -6F, -0.5F, 1, 6, 1);
        rightArm.addChild(rightArmSub);
        leftArm = new ModelPart(this);
        leftArm.setPos(2F, 10.46667F, 0.5F);
        setRotation(leftArm, 0F, 0F, 0F);
        leftArm.mirror = true;
        leftArm.texOffs(0, 24).addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        leftArmSub = new ModelPart(this);
        leftArmSub.setPos(5.5F, 0F, 0F);
        setRotation(leftArmSub, 0F, 0F, 0F);
        leftArmSub.mirror = true;
        leftArmSub.texOffs(31, 0).addBox(-0.5F, -6F, -0.5F, 1, 6, 1);
        leftArm.addChild(leftArmSub);
    }

    @Override
    public void setupAnim(AshenEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch / 125F + headAngle;
        head.yRot = netHeadYaw / 125F + 3.14159F;

        final float armRotater = 1.247196F;
        final float subStraight = 1.570795F;

        switch (actionState) {
            case LOST_MASK:                                             //Mask off
                headAngle = -0.4F;
                rightArm.zRot = -armRotater;
                rightArmSub.zRot = -5.1F;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = 5.1F;
                leftArm.xRot = subStraight;
                rightArm.xRot = subStraight;
                rightArm.yRot = -.5F;
                leftArm.yRot = .5F;
                break;
            case HOSTILE:
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
                break;
            default:
                headAngle = 0;
                rightArm.zRot = -armRotater;
                rightArmSub.zRot = -subStraight;
                leftArm.zRot = armRotater;
                leftArmSub.zRot = subStraight;
                rightArm.yRot = 0F;
                leftArm.yRot = 0F;
                break;
        }

        leftArm.zRot += Mth.sin(ageInTicks * 0.25F) * 0.020F;
        rightArm.zRot -= Mth.sin(ageInTicks * 0.25F) * 0.020F;
    }

    public Iterable<ModelPart> parts() {
        return ImmutableList.of(body, head, rightArm, leftArm, leftLeg, rightLeg);
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void prepareMobModel(final AshenEntity entity, float f, float f1, float f2) {
        rightLeg.xRot = Mth.cos(f * 0.6662F) * 1.25F * f1;
        leftLeg.xRot = Mth.cos(f * 0.6662F + 3.141593F) * 1.25F * f1;
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        stack.translate(0.09375F, 0.1875F, 0.0F);
    }
}
