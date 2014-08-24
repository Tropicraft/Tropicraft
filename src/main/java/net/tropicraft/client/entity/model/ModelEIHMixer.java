package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEIHMixer extends ModelBase
{
    //fields
    ModelRenderer Base;
    ModelRenderer Back;
    ModelRenderer Nose;
    ModelRenderer Forehead;
    ModelRenderer LeftEye;
    ModelRenderer RightEye;
    ModelRenderer BasinNearBack;
    ModelRenderer BasinSide;
    ModelRenderer BasinSide2;
    ModelRenderer BasinNearFront;
    ModelRenderer BasinCorner1;
    ModelRenderer BasinCorner2;
    ModelRenderer BasinCorner3;
    ModelRenderer BasinCorner4;
    ModelRenderer LidBase;
    ModelRenderer LidTop;
    ModelRenderer Mouth;

    public ModelEIHMixer()
    {
        textureWidth = 64;
        textureHeight = 64;

        Base = new ModelRenderer(this, 0, 44);
        Base.addBox(-8F, -1F, -8F, 16, 3, 16);
        Base.setRotationPoint(0F, 22F, 0F);
        Base.setTextureSize(64, 64);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        Back = new ModelRenderer(this, 0, 0);
        Back.addBox(-3F, -15F, -8F, 6, 25, 16);
        Back.setRotationPoint(5F, 11F, 0F);
        Back.setTextureSize(64, 64);
        Back.mirror = true;
        setRotation(Back, 0F, 0F, 0F);
        Nose = new ModelRenderer(this, 0, 0);
        Nose.addBox(-1F, -7F, -2F, 2, 14, 4);
        Nose.setRotationPoint(1F, 8F, 0F);
        Nose.setTextureSize(64, 64);
        Nose.mirror = true;
        setRotation(Nose, 0F, 0F, 0F);
        Forehead = new ModelRenderer(this, 0, 0);
        Forehead.addBox(-1F, -1F, -8F, 3, 5, 16);
        Forehead.setRotationPoint(0F, -3F, 0F);
        Forehead.setTextureSize(64, 64);
        Forehead.mirror = true;
        setRotation(Forehead, 0F, 0F, 0F);
        LeftEye = new ModelRenderer(this, 1, 35);
        LeftEye.addBox(0F, -1F, -3F, 1, 4, 6);
        LeftEye.setRotationPoint(1F, 2F, 5F);
        LeftEye.setTextureSize(64, 64);
        LeftEye.mirror = true;
        setRotation(LeftEye, 0F, 0F, 0F);
        RightEye = new ModelRenderer(this, 1, 35);
        RightEye.addBox(0F, -1F, -3F, 1, 4, 6);
        RightEye.setRotationPoint(1F, 2F, -5F);
        RightEye.setTextureSize(64, 64);
        RightEye.mirror = true;
        setRotation(RightEye, 0F, 0F, 0F);
        BasinNearBack = new ModelRenderer(this, 0, 0);
        BasinNearBack.addBox(-1F, 0F, -4F, 1, 1, 8);
        BasinNearBack.setRotationPoint(2F, 20F, 0F);
        BasinNearBack.setTextureSize(64, 64);
        BasinNearBack.mirror = true;
        setRotation(BasinNearBack, 0F, 0F, 0F);
        BasinSide = new ModelRenderer(this, 0, 0);
        BasinSide.addBox(-5F, 0F, -2F, 10, 1, 4);
        BasinSide.setRotationPoint(-3F, 20F, 6F);
        BasinSide.setTextureSize(64, 64);
        BasinSide.mirror = true;
        setRotation(BasinSide, 0F, 0F, 0F);
        BasinSide2 = new ModelRenderer(this, 0, 0);
        BasinSide2.addBox(-5F, 0F, -2F, 10, 1, 4);
        BasinSide2.setRotationPoint(-3F, 20F, -6F);
        BasinSide2.setTextureSize(64, 64);
        BasinSide2.mirror = true;
        setRotation(BasinSide2, 0F, 0F, 0F);
        BasinNearFront = new ModelRenderer(this, 0, 0);
        BasinNearFront.addBox(-1F, 0F, -4F, 2, 1, 8);
        BasinNearFront.setRotationPoint(-7F, 20F, 0F);
        BasinNearFront.setTextureSize(64, 64);
        BasinNearFront.mirror = true;
        setRotation(BasinNearFront, 0F, 0F, 0F);
        BasinCorner1 = new ModelRenderer(this, 0, 0);
        BasinCorner1.addBox(0F, 0F, 0F, 1, 1, 1);
        BasinCorner1.setRotationPoint(0F, 20F, 3F);
        BasinCorner1.setTextureSize(64, 64);
        BasinCorner1.mirror = true;
        setRotation(BasinCorner1, 0F, 0F, 0F);
        BasinCorner2 = new ModelRenderer(this, 0, 0);
        BasinCorner2.addBox(0F, 0F, 0F, 1, 1, 1);
        BasinCorner2.setRotationPoint(0F, 20F, -4F);
        BasinCorner2.setTextureSize(64, 64);
        BasinCorner2.mirror = true;
        setRotation(BasinCorner2, 0F, 0F, 0F);
        BasinCorner3 = new ModelRenderer(this, 0, 0);
        BasinCorner3.addBox(0F, 0F, 0F, 1, 1, 1);
        BasinCorner3.setRotationPoint(-6F, 20F, 3F);
        BasinCorner3.setTextureSize(64, 64);
        BasinCorner3.mirror = true;
        setRotation(BasinCorner3, 0F, 0F, 0F);
        BasinCorner4 = new ModelRenderer(this, 0, 0);
        BasinCorner4.addBox(0F, 0F, 0F, 1, 1, 1);
        BasinCorner4.setRotationPoint(-6F, 20F, -4F);
        BasinCorner4.setTextureSize(64, 64);
        BasinCorner4.mirror = true;
        setRotation(BasinCorner4, 0F, 0F, 0F);
        LidBase = new ModelRenderer(this, 0, 0);
        LidBase.addBox(-4F, 0F, -8F, 9, 1, 16);
        LidBase.setRotationPoint(3F, -5F, 0F);
        LidBase.setTextureSize(64, 64);
        LidBase.mirror = true;
        setRotation(LidBase, 0F, 0F, 0F);
        LidTop = new ModelRenderer(this, 0, 0);
        LidTop.addBox(-2F, 0F, -2F, 4, 1, 4);
        LidTop.setRotationPoint(3F, -6F, 0F);
        LidTop.setTextureSize(64, 64);
        LidTop.mirror = true;
        setRotation(LidTop, 0F, 0F, 0F);
        Mouth = new ModelRenderer(this, 54, 0);
        Mouth.addBox(0F, -1F, -2F, 1, 3, 4);
        Mouth.setRotationPoint(1F, 16F, 0F);
        Mouth.setTextureSize(64, 64);
        Mouth.mirror = true;
        setRotation(Mouth, 0F, 0F, 0F);
    }

    public void renderEIHMixer()
    {
        float f5 = 0.0625F;
        Base.render(f5);
        Back.render(f5);
        Nose.render(f5);
        Forehead.render(f5);
        LeftEye.render(f5);
        RightEye.render(f5);
        BasinNearBack.render(f5);
        BasinSide.render(f5);
        BasinSide2.render(f5);
        BasinNearFront.render(f5);
        BasinCorner1.render(f5);
        BasinCorner2.render(f5);
        BasinCorner3.render(f5);
        BasinCorner4.render(f5);
        LidBase.render(f5);
        LidTop.render(f5);
        Mouth.render(f5);

    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
