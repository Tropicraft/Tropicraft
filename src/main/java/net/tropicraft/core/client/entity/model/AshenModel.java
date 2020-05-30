package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenModel extends SegmentedModel<AshenEntity> implements IHasArm {
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer mask;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;
    public ModelRenderer rightArmSub;
    public ModelRenderer leftArmSub;
    public float headAngle;
    public boolean swinging;
    public AshenEntity.AshenState actionState;

    public AshenModel() {
        swinging = false;
        actionState = AshenEntity.AshenState.PEACEFUL;
        headAngle = 0;
        textureWidth = 64;
        textureHeight = 32;

        rightLeg = new ModelRenderer(this, 25, 0);
        rightLeg.addBox(0F, 0F, 0F, 1, 7, 1);
        rightLeg.setRotationPoint(1F, 17F, 0F);
        rightLeg.setTextureSize(64, 32);
        rightLeg.mirror = true;
        setRotation(rightLeg, 0F, 0F, 0F);
        leftLeg = new ModelRenderer(this, 25, 0);
        leftLeg.addBox(-1F, 0F, 0F, 1, 7, 1);
        leftLeg.setRotationPoint(-1F, 17F, 0F);
        leftLeg.setTextureSize(64, 32);
        leftLeg.mirror = true;
        setRotation(leftLeg, 0F, 0F, 0F);
        body = new ModelRenderer(this, 24, 8);
        body.addBox(-2F, -3F, 0F, 4, 7, 3);
        body.setRotationPoint(0F, 13F, 2F);
        body.setTextureSize(64, 32);
        body.mirror = true;
        setRotation(body, 0F, 3.141593F, 0F);
        head = new ModelRenderer(this, 24, 18);
        head.addBox(-2F, -3F, -1F, 4, 3, 4);
        head.setRotationPoint(0F, 10F, 1F);
        head.setTextureSize(64, 32);
        head.mirror = true;
        setRotation(head, 0F, 3.141593F, 0F);
        
        //mask = new ModelRenderer(this, 0, 0);
        //mask.addBox(-5.5F, -10F, 3F, 11, 22, 1);
        //mask.setRotationPoint(0F, 10F, 1F);
        //mask.setTextureSize(64, 32);
        //mask.mirror = true;
        //setRotation(mask, 0F, 3.141593F, 0F);

        rightArm = new ModelRenderer(this);
        rightArm.setRotationPoint(-2F, 10.5F, 0.5F);
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = true;
        rightArm.setTextureOffset(0, 24).addBox(-6F, -0.5F, -0.5F, 6, 1, 1);
        rightArmSub = new ModelRenderer(this);
        rightArmSub.setRotationPoint(-5.5F, 0F, 0F);
        setRotation(rightArmSub, 0F, 0F, 0F);
        rightArmSub.mirror = true;
        rightArmSub.setTextureOffset(31, 0).addBox(-0.5F, -6F, -0.5F, 1, 6, 1);
        rightArm.addChild(rightArmSub);
        leftArm = new ModelRenderer(this);
        leftArm.setRotationPoint(2F, 10.46667F, 0.5F);
        setRotation(leftArm, 0F, 0F, 0F);
        leftArm.mirror = true;
        leftArm.setTextureOffset(0, 24).addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        leftArmSub = new ModelRenderer(this);
        leftArmSub.setRotationPoint(5.5F, 0F, 0F);
        setRotation(leftArmSub, 0F, 0F, 0F);
        leftArmSub.mirror = true;
        leftArmSub.setTextureOffset(31, 0).addBox(-0.5F, -6F, -0.5F, 1, 6, 1);
        leftArm.addChild(leftArmSub);
    }

    @Override
    public void setRotationAngles(AshenEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.rotateAngleX = headPitch / 125F + headAngle;
        head.rotateAngleY = netHeadYaw / 125F + 3.14159F;

        final float armRotater = 1.247196F;
        final float subStraight = 1.570795F;

        switch (actionState) {
            case LOST_MASK: 											//Mask off
                headAngle = -0.4F;
                rightArm.rotateAngleZ = -armRotater;
                rightArmSub.rotateAngleZ = -5.1F;
                leftArm.rotateAngleZ = armRotater;
                leftArmSub.rotateAngleZ = 5.1F;
                leftArm.rotateAngleX = subStraight;
                rightArm.rotateAngleX = subStraight;
                rightArm.rotateAngleY = -.5F;
                leftArm.rotateAngleY = .5F;
                break;
            case HOSTILE:
                headAngle = 0.0F;
                leftArm.rotateAngleX = 1.65F + limbSwing / 125F;
                leftArm.rotateAngleY = .9F + limbSwingAmount / 125F;
                leftArm.rotateAngleZ = armRotater;
                leftArmSub.rotateAngleZ = 6.2F;
                rightArm.rotateAngleZ = 0.0F - MathHelper.sin(limbSwingAmount * 0.75F) * 0.0220F;
                rightArm.rotateAngleY = 0.0F;
                rightArmSub.rotateAngleZ = 0.0F;

                if (swinging) {
                    rightArm.rotateAngleX += MathHelper.sin(limbSwingAmount * 0.75F) * 0.0520F;
                } else {
                    rightArm.rotateAngleX = 0.0F;
                }
                break;
            default:
                headAngle = 0;
                rightArm.rotateAngleZ = -armRotater;
                rightArmSub.rotateAngleZ = -subStraight;
                leftArm.rotateAngleZ = armRotater;
                leftArmSub.rotateAngleZ = subStraight;
                rightArm.rotateAngleY = 0F;
                leftArm.rotateAngleY = 0F;
                break;
        }

        leftArm.rotateAngleZ += MathHelper.sin(ageInTicks * 0.25F) * 0.020F;
        rightArm.rotateAngleZ -= MathHelper.sin(ageInTicks * 0.25F) * 0.020F;
    }

    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body, head, rightArm, leftArm, leftLeg, rightLeg);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(final AshenEntity entity, float f, float f1, float f2) {
        rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.25F * f1;
        leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.25F * f1;
    }

    @Override
    public void translateHand(HandSide side, MatrixStack stack) {
        stack.translate(0.09375F, 0.1875F, 0.0F);
    }
}
