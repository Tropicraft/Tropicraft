package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelKoaMan extends ModelBase {

    public ModelRenderer bipedHead;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer headband;
    public ModelRenderer armband1;
    public ModelRenderer leaf;
    public ModelRenderer leaf3;
    public ModelRenderer leaf2;
    public ModelRenderer leaf4;
    public ModelRenderer leaf5;
    public ModelRenderer leaf6;
    public ModelRenderer leaf7;
    public ModelRenderer leaf8;
    public ModelRenderer leaf9;
    public ModelRenderer leaf10;
    public ModelRenderer armband11;

    public ModelKoaMan() {
        
        //textureWidth = 64;
        //textureHeight = 32;
        
        bipedHead = new ModelRenderer(this, 0, 2);
        bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
        bipedHead.setRotationPoint(0F, 0F, 0F);
        bipedHead.setTextureSize(64, 32);
        bipedHead.mirror = true;
        setRotation(bipedHead, 0F, 0F, 0F);
        bipedBody = new ModelRenderer(this, 16, 16);
        bipedBody.addBox(-4F, 0F, -2F, 8, 12, 4);
        bipedBody.setRotationPoint(0F, 0F, 0F);
        bipedBody.setTextureSize(64, 32);
        bipedBody.mirror = true;
        setRotation(bipedBody, 0F, 0F, 0F);
        bipedRightArm = new ModelRenderer(this, 40, 16);
        bipedRightArm.addBox(-2F, -2F, -2F, 3, 12, 4);
        bipedRightArm.setRotationPoint(-4F, 3F, 0F);
        bipedRightArm.setTextureSize(64, 32);
        bipedRightArm.mirror = true;
        setRotation(bipedRightArm, 0F, 0F, 0F);
        bipedLeftArm = new ModelRenderer(this, 40, 16);
        bipedLeftArm.addBox(-1F, -2F, -2F, 3, 12, 4);
        bipedLeftArm.setRotationPoint(5F, 3F, 0F);
        bipedLeftArm.setTextureSize(64, 32);
        bipedLeftArm.mirror = true; //hey baby whats shakin		//bacon :D /me wants bacon mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
        setRotation(bipedLeftArm, 0F, 0F, 0F);
        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
        bipedRightLeg.setRotationPoint(-2F, 12F, 0F);
        bipedRightLeg.setTextureSize(64, 32);
        bipedRightLeg.mirror = true;
        setRotation(bipedRightLeg, 0F, 0F, 0F);
        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
        bipedLeftLeg.setRotationPoint(2F, 12F, 0F);
        bipedLeftLeg.setTextureSize(64, 32);
        bipedLeftLeg.mirror = true;
        setRotation(bipedLeftLeg, 0F, 0F, 0F);
        headband = new ModelRenderer(this, 24, 0);
        headband.addBox(-5F, 0F, -5F, 10, 2, 10);
        headband.setRotationPoint(0F, -7F, 0F);		//0,-7,0 before
        headband.setTextureSize(64, 32);
        headband.mirror = true;
        setRotation(headband, 0F, 0F, 0F);
        armband1 = new ModelRenderer(this, 35, 5);
        armband1.addBox(-2F, 0F, -3F, 4, 1, 6);	//offset, dimensions
        armband1.setRotationPoint(-6F, 3F, 0F);	//position
        armband1.setTextureSize(64, 32);
        armband1.mirror = true;
        setRotation(armband1, 0F, 0F, 0F);
        leaf = new ModelRenderer(this, 0, 0);
        leaf.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf.setRotationPoint(2F, -6F, -6F);
        leaf.setTextureSize(64, 32);
        leaf.mirror = true;
        setRotation(leaf, 0F, 0F, 0F);
        leaf3 = new ModelRenderer(this, 0, 0);
        leaf3.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf3.setRotationPoint(-1F, -6F, -6F);
        leaf3.setTextureSize(64, 32);
        leaf3.mirror = true;
        setRotation(leaf3, 0F, 0F, 0F);
        leaf2 = new ModelRenderer(this, 0, 0);
        leaf2.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf2.setRotationPoint(-4F, -6F, -6F);
        leaf2.setTextureSize(64, 32);
        leaf2.mirror = true;
        setRotation(leaf2, 0F, 0F, 0F);
        leaf4 = new ModelRenderer(this, 0, 0);
        leaf4.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf4.setRotationPoint(0F, -7F, -6F);
        leaf4.setTextureSize(64, 32);
        leaf4.mirror = true;
        setRotation(leaf4, 0F, 0F, 0F);
        leaf5 = new ModelRenderer(this, 0, 0);
        leaf5.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf5.setRotationPoint(5F, -6F, -1F);
        leaf5.setTextureSize(64, 32);
        leaf5.mirror = true;
        setRotation(leaf5, 0F, 0F, 0F);
        leaf6 = new ModelRenderer(this, 0, 0);
        leaf6.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf6.setRotationPoint(5F, -6F, 3F);
        leaf6.setTextureSize(64, 32);
        leaf6.mirror = true;
        setRotation(leaf6, 0F, 0F, 0F);
        leaf7 = new ModelRenderer(this, 0, 0);
        leaf7.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf7.setRotationPoint(-6F, -6F, 0F);
        leaf7.setTextureSize(64, 32);
        leaf7.mirror = true;
        setRotation(leaf7, 0F, 0F, 0F);
        leaf8 = new ModelRenderer(this, 0, 0);
        leaf8.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf8.setRotationPoint(-6F, -6F, -4F);
        leaf8.setTextureSize(64, 32);
        leaf8.mirror = true;
        setRotation(leaf8, 0F, 0F, 0F);
        leaf9 = new ModelRenderer(this, 0, 0);
        leaf9.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf9.setRotationPoint(-2F, -6F, 5F);
        leaf9.setTextureSize(64, 32);
        leaf9.mirror = true;
        setRotation(leaf9, 0F, 0F, 0F);
        leaf10 = new ModelRenderer(this, 0, 0);
        leaf10.addBox(0F, 0F, 0F, 1, 0, 1);
        leaf10.setRotationPoint(2F, -6F, 5F);
        leaf10.setTextureSize(64, 32);
        leaf10.mirror = true;
        setRotation(leaf10, 0F, 0F, 0F);
        armband11 = new ModelRenderer(this, 35, -1);
        armband11.addBox(-2F, 0F, -3F, 4, 1, 6);		//offset, dimensions
        armband11.setRotationPoint(6F, 3F, 0F);		//position
        armband11.setTextureSize(64, 32);
        armband11.mirror = true;
        setRotation(armband11, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        bipedHead.render(f5);
        bipedBody.render(f5);
        bipedRightArm.render(f5);
        bipedLeftArm.render(f5);
        bipedRightLeg.render(f5);
        bipedLeftLeg.render(f5);
        headband.render(f5);
        armband1.render(f5);		//right arm :)
        leaf.render(f5);
        leaf3.render(f5);
        leaf2.render(f5);
        leaf4.render(f5);
        leaf5.render(f5);
        leaf6.render(f5);
        leaf7.render(f5);
        leaf8.render(f5);
        leaf9.render(f5);
        leaf10.render(f5);
        armband11.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    private void a(ModelRenderer a, ModelRenderer b) {
        a.rotateAngleX = b.rotateAngleX;
        a.rotateAngleY = b.rotateAngleY;
        a.rotateAngleZ = b.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        //super.setRotationAngles(f, f1, f2, f3, f4, f5);

        bipedHead.rotateAngleY = f3 / 57.29578F;
        bipedHead.rotateAngleX = f4 / 57.29578F;
        a(headband, bipedHead);
        leaf.rotateAngleX = leaf3.rotateAngleX = leaf2.rotateAngleX = leaf4.rotateAngleX = leaf5.rotateAngleX = leaf6.rotateAngleX = leaf7.rotateAngleX =
                leaf8.rotateAngleX = leaf9.rotateAngleX = leaf10.rotateAngleX = headband.rotateAngleY = bipedHead.rotateAngleY;
        leaf.rotateAngleY = leaf3.rotateAngleY = leaf2.rotateAngleY = leaf4.rotateAngleY = leaf5.rotateAngleY = leaf6.rotateAngleY = leaf7.rotateAngleY =
                leaf8.rotateAngleY = leaf9.rotateAngleY = leaf10.rotateAngleY = headband.rotateAngleY = bipedHead.rotateAngleY;
        leaf.rotateAngleZ = leaf3.rotateAngleZ = leaf2.rotateAngleZ = leaf4.rotateAngleZ = leaf5.rotateAngleZ = leaf6.rotateAngleZ = leaf7.rotateAngleZ =
                leaf8.rotateAngleZ = leaf9.rotateAngleZ = leaf10.rotateAngleZ = headband.rotateAngleZ = bipedHead.rotateAngleZ;
        //a(headband, bipedHead);
        bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
        bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        bipedRightArm.rotateAngleZ = 0.0F;
        bipedLeftArm.rotateAngleZ = 0.0F;
        a(armband1, bipedRightArm);
        bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        bipedRightLeg.rotateAngleY = 0.0F;
        bipedLeftLeg.rotateAngleY = 0.0F;


        a(armband11, bipedLeftArm);

        if (isRiding) {
            bipedRightArm.rotateAngleX += -0.6283185F;
            bipedLeftArm.rotateAngleX += -0.6283185F;
            bipedRightLeg.rotateAngleX = -1.256637F;
            bipedLeftLeg.rotateAngleX = -1.256637F;
            bipedRightLeg.rotateAngleY = 0.3141593F;
            bipedLeftLeg.rotateAngleY = -0.3141593F;
            a(armband1, bipedRightArm);
            a(armband11, bipedLeftArm);
        }

        bipedRightArm.rotateAngleY = 0.0F;
        bipedLeftArm.rotateAngleY = 0.0F;
        a(armband1, bipedRightArm);
        a(armband11, bipedLeftArm);
        if (onGround > -9990F) {
            float f6 = onGround;
            bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
            bipedRightArm.rotationPointZ = MathHelper.sin(bipedBody.rotateAngleY) * 5F;
            bipedRightArm.rotationPointX = -MathHelper.cos(bipedBody.rotateAngleY) * 5F;
            a(armband1, bipedRightArm);
            bipedLeftArm.rotationPointZ = -MathHelper.sin(bipedBody.rotateAngleY) * 5F;
            bipedLeftArm.rotationPointX = MathHelper.cos(bipedBody.rotateAngleY) * 5F;
            a(armband11, bipedLeftArm);
            bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
            a(armband1, bipedRightArm);
            bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
            bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
            a(armband1, bipedRightArm);
            a(armband11, bipedLeftArm);
            f6 = 1.0F - onGround;
            f6 *= f6;
            f6 *= f6;
            f6 = 1.0F - f6;
            float f7 = MathHelper.sin(f6 * 3.141593F);
            float f8 = MathHelper.sin(onGround * 3.141593F) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
            bipedRightArm.rotateAngleX -= (double) f7 * 1.2D + (double) f8;
            bipedRightArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
            bipedRightArm.rotateAngleZ = MathHelper.sin(onGround * 3.141593F) * -0.4F;
            a(armband1, bipedRightArm);
            a(armband11, bipedLeftArm);
        }

        {
            bipedBody.rotateAngleX = 0.0F;
            bipedRightLeg.rotationPointZ = 0.0F;
            bipedLeftLeg.rotationPointZ = 0.0F;
            bipedRightLeg.rotationPointY = 12F;
            bipedLeftLeg.rotationPointY = 12F;
            bipedHead.rotationPointY = 0.0F;
            a(headband, bipedHead);
            float ffff = headband.rotationPointY;
            leaf.rotationPointY = leaf3.rotationPointY = leaf2.rotationPointY = leaf4.rotationPointY = leaf5.rotationPointY = leaf6.rotationPointY = leaf7.rotationPointY =
                    leaf8.rotationPointY = leaf9.rotationPointY = leaf10.rotationPointY = headband.rotationPointY = bipedHead.rotationPointY + ffff;

        }
        bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
        a(armband1, bipedRightArm);
        a(armband11, bipedLeftArm);
        //a(headband, bipedHead);


    }
}
