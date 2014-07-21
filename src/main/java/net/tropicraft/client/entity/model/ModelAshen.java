package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelAshen extends ModelBase {

    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer mask;
    public ModelRenderer leftArm;
    public ModelRenderer rightArm;
    public ModelRenderer leftArmSub;
    public ModelRenderer rightArmSub;
    public float headAngle;
    public boolean swinging;
    public int actionState;

    public ModelAshen() {
        swinging = false;
        actionState = 0;
        headAngle = 0;
        textureWidth = 64;
        textureHeight = 32;
        setTextureOffset("leftArm.leftArmBase", 0, 24);
        setTextureOffset("leftArmSub.leftArmTop", 31, 0);
        setTextureOffset("rightArm.rightArmBase", 0, 24);
        setTextureOffset("rightArmSub.rightArmTop", 31, 0);

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

        leftArm = new ModelRenderer(this, "leftArm");
        leftArm.setRotationPoint(-2F, 10.5F, 0.5F);
        setRotation(leftArm, 0F, 0F, 0F);
        leftArm.mirror = true;
        leftArm.addBox("leftArmBase", -6F, -0.5F, -0.5F, 6, 1, 1);
        leftArmSub = new ModelRenderer(this, "leftArmSub");
        leftArmSub.setRotationPoint(-5.5F, 0F, 0F);
        setRotation(leftArmSub, 0F, 0F, 0F);
        leftArmSub.mirror = true;
        leftArmSub.addBox("leftArmTop", -0.5F, -6F, -0.5F, 1, 6, 1);
        leftArm.addChild(leftArmSub);
        rightArm = new ModelRenderer(this, "rightArm");
        rightArm.setRotationPoint(2F, 10.46667F, 0.5F);
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = true;
        rightArm.addBox("rightArmBase", 0F, -0.5F, -0.5F, 6, 1, 1);
        rightArmSub = new ModelRenderer(this, "rightArmSub");
        rightArmSub.setRotationPoint(5.5F, 0F, 0F);
        setRotation(rightArmSub, 0F, 0F, 0F);
        rightArmSub.mirror = true;
        rightArmSub.addBox("rightArmTop", -0.5F, -6F, -0.5F, 1, 6, 1);
        rightArm.addChild(rightArmSub);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        //super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        rightLeg.render(f5);
        leftLeg.render(f5);
        body.render(f5);
        head.render(f5);
        leftArm.render(f5);
        rightArm.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2) {
        rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.25F * f1;
        leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.25F * f1;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {

        head.rotateAngleX = f4 / 125F + headAngle;
        head.rotateAngleY = f3 / 125F + 3.14159F;

        float ArmRotater = 1.247196F;
        float subStraight = 1.570795F;


        switch (actionState) {
            case 1: 											//Mask off
                headAngle = -0.4F;
                leftArm.rotateAngleZ = -ArmRotater;
                leftArmSub.rotateAngleZ = -5.1F;
                rightArm.rotateAngleZ = ArmRotater;
                rightArmSub.rotateAngleZ = 5.1F;
                rightArm.rotateAngleX = subStraight;
                leftArm.rotateAngleX = subStraight;
                leftArm.rotateAngleY = -.5F;
                rightArm.rotateAngleY = .5F;
                break;
            case 2:
                headAngle = 0.0F;
                rightArm.rotateAngleX = 1.65F + f3 / 125F;
                rightArm.rotateAngleY = .9F + f4 / 125F;
                rightArm.rotateAngleZ = ArmRotater;
                rightArmSub.rotateAngleZ = 6.2F;
                leftArm.rotateAngleZ = 0.0F - MathHelper.sin(f2 * 0.75F) * 0.0220F;
                leftArm.rotateAngleY = 0.0F;
                leftArmSub.rotateAngleZ = 0.0F;

                if (swinging) {
                    leftArm.rotateAngleX += MathHelper.sin(f2 * 0.75F) * 0.0520F;
                } else {
                    leftArm.rotateAngleX = 0.0F;
                }
                break;
            default:
                headAngle = 0;
                leftArm.rotateAngleZ = -ArmRotater;
                leftArmSub.rotateAngleZ = -subStraight;
                rightArm.rotateAngleZ = ArmRotater;
                rightArmSub.rotateAngleZ = subStraight;
                leftArm.rotateAngleY = 0F;
                rightArm.rotateAngleY = 0F;

                break;
        }

        rightArm.rotateAngleY += MathHelper.sin(f2 * 0.25F) * 0.0020F;
        leftArm.rotateAngleY -= MathHelper.sin(f2 * 0.25F) * 0.0020F;

        //leftArmSub.rotateAngleY += MathHelper.sin(f2 * 0.25F) * 0.0020F;
        //rightArmSub.rotateAngleY += MathHelper.sin(f2 * 0.25F) * 0.0020F;
    }
}
