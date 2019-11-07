package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.SeaTurtleEntity;

public class SeaTurtleModel extends EntityModel<SeaTurtleEntity> {
    public RendererModel body;
    public RendererModel frFlipper;
    public RendererModel flFlipper;
    public RendererModel head;
    public RendererModel rlFlipper;
    public RendererModel rrFlipper;
    public boolean inWater;

    public SeaTurtleModel() {
        inWater = false;
        textureWidth = 64;
        textureHeight = 64;

        body = new RendererModel(this, "body");
        body.setRotationPoint(0F, 19F, 0F);
        setRotation(body, 0F, 0F, 0F);
        body.mirror = true;
        frFlipper = new RendererModel(this, "frFlipper");
        frFlipper.setRotationPoint(-7F, 2F, -6F);
        setRotation(frFlipper, 0F, 0F, 0F);
        frFlipper.mirror = true;
        frFlipper.setTextureOffset(0, 20).addBox(-10F, 0F, -3F, 10, 1, 4);
        body.addChild(frFlipper);
        flFlipper = new RendererModel(this, "flFlipper");
        flFlipper.setRotationPoint(7F, 2F, -6F);
        setRotation(flFlipper, 0F, 0F, 0F);
        flFlipper.mirror = true;
        flFlipper.setTextureOffset(0, 20).addBox(0F, 0F, -3F, 10, 1, 4);
        body.addChild(flFlipper);
        body.setTextureOffset(0, 29).addBox(-4.5F, -1F, -9F, 9, 2, 1);
        body.setTextureOffset(43, 40).addBox(-3F, -2F, 1F, 6, 1, 4);
        body.setTextureOffset(0, 52).addBox(-7F, -2F, -8F, 14, 4, 8);
        body.setTextureOffset(0, 41).addBox(-5F, -1F, 0F, 10, 3, 8);
        body.setTextureOffset(0, 32).addBox(-4F, -2.5F, -6F, 8, 2, 7);
        body.setTextureOffset(44, 55).addBox(-6F, -0.5F, 0F, 1, 2, 7);
        body.setTextureOffset(44, 55).addBox(5F, -0.5F, 0F, 1, 2, 7);
        body.setTextureOffset(0, 25).addBox(-4F, -0.5F, 8F, 8, 2, 2);
        head = new RendererModel(this, "head");
        head.setRotationPoint(0F, 1F, -8F);
        setRotation(head, 0F, 0F, 0F);
        head.mirror = true;
        head.setTextureOffset(0, 0).addBox(-1.5F, -1.5F, -6F, 3, 3, 6);
        body.addChild(head);
        rlFlipper = new RendererModel(this, "rlFlipper");
        rlFlipper.setRotationPoint(-4F, 2F, 7F);
        setRotation(rlFlipper, 0F, 0F, 0F);
        rlFlipper.mirror = true;
        rlFlipper.setTextureOffset(0, 16).addBox(-7F, 0F, -1F, 7, 1, 3);
        body.addChild(rlFlipper);
        rrFlipper = new RendererModel(this, "rrFlipper");
        rrFlipper.setRotationPoint(4F, 2F, 7F);
        setRotation(rrFlipper, 0F, 0F, 0F);
        rrFlipper.mirror = true;
        rrFlipper.setTextureOffset(0, 16).addBox(-1F, 0F, -1F, 7, 1, 3);
        body.addChild(rrFlipper);
    }

    @Override
    public void render(SeaTurtleEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        body.render(f5);
    }
    
    @Override
    public void setRotationAngles(SeaTurtleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        float defFront = 0.3927F;
        float defFront2 = 0.3F;
        float defRear = .5F;

        if (!entity.isInWater() && !entity.isBeingRidden()) {

            limbSwingAmount *= 3f;
            limbSwing *= 2f;

            body.rotateAngleX = -Math.abs(MathHelper.sin(limbSwing * 0.25F) * 1.25F * limbSwingAmount) - .10F;
            frFlipper.rotateAngleX = defFront2;
            frFlipper.rotateAngleY = swimRotate(limbSwing, limbSwingAmount, 0.5F, 5F, 0, defFront);
            frFlipper.rotateAngleZ = swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            flFlipper.rotateAngleX = defFront2;
            flFlipper.rotateAngleY = swimRotate(limbSwing, limbSwingAmount, 0.5f, 5f, (float) Math.PI, -defFront2);
            flFlipper.rotateAngleZ = -swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            rrFlipper.rotateAngleX = 0F;
            rrFlipper.rotateAngleY = -swimRotate(limbSwing, limbSwingAmount, 3f, 2f, 0, defRear);
            rrFlipper.rotateAngleZ = 0F;
            rlFlipper.rotateAngleX = 0F;
            rlFlipper.rotateAngleY = -swimRotate(limbSwing, limbSwingAmount, 3f, 2f, 0, -defRear);
            rlFlipper.rotateAngleZ = 0F;
        } else {
            limbSwingAmount *= 0.75f;
            limbSwing *= 0.1f;
            body.rotateAngleX = (float) Math.toRadians(headPitch);
            frFlipper.rotateAngleY = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, 0, defFront);
            frFlipper.rotateAngleX = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, (float) Math.PI / 4, defFront2 + 0.25f);
            frFlipper.rotateAngleZ = 0;
            flFlipper.rotateAngleY = -swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, 0, defFront);
            flFlipper.rotateAngleZ = 0;
            flFlipper.rotateAngleX = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, (float) Math.PI / 4, defFront2 + 0.25f);
            rlFlipper.rotateAngleX = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, (float) Math.PI / 4, 0);
            rrFlipper.rotateAngleX = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, (float) Math.PI / 4, 0);
            rrFlipper.rotateAngleY = -0.5f;
            rlFlipper.rotateAngleY = 0.5f;
            rrFlipper.rotateAngleZ = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, 0, 0.5f);
            rlFlipper.rotateAngleZ = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, (float) Math.PI, -0.5f);;
        }
    }
    
    private float swimRotate(float swing, float amount, float rot, float intensity, float rotOffset, float offset) {
        return MathHelper.cos(swing * rot + rotOffset) * amount * intensity + offset;
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
