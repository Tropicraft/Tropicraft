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
        frFlipper.addBox(-10F, 0F, -3F, 10, 1, 4).setTextureOffset(0, 20);
        body.addChild(frFlipper);
        flFlipper = new RendererModel(this, "flFlipper");
        flFlipper.setRotationPoint(7F, 2F, -6F);
        setRotation(flFlipper, 0F, 0F, 0F);
        flFlipper.mirror = true;
        flFlipper.addBox(0F, 0F, -3F, 10, 1, 4).setTextureOffset(0, 20);
        body.addChild(flFlipper);
        body.addBox(-4.5F, -1F, -9F, 9, 2, 1).setTextureOffset(0, 29);
        body.addBox(-3F, -2F, 1F, 6, 1, 4).setTextureOffset(43, 40);
        body.addBox(-7F, -2F, -8F, 14, 4, 8).setTextureOffset(0, 52);
        body.addBox(-5F, -1F, 0F, 10, 3, 8).setTextureOffset(0, 41);
        body.addBox(-4F, -2.5F, -6F, 8, 2, 7).setTextureOffset(0, 32);
        body.addBox(-6F, -0.5F, 0F, 1, 2, 7).setTextureOffset(44, 55);
        body.addBox(5F, -0.5F, 0F, 1, 2, 7).setTextureOffset(44, 55);
        body.addBox(-4F, -0.5F, 8F, 8, 2, 2).setTextureOffset(0, 25);
        head = new RendererModel(this, "head");
        head.setRotationPoint(0F, 1F, -8F);
        setRotation(head, 0F, 0F, 0F);
        head.mirror = true;
        head.addBox(-1.5F, -1.5F, -6F, 3, 3, 6).setTextureOffset(0, 0);
        body.addChild(head);
        rlFlipper = new RendererModel(this, "rlFlipper");
        rlFlipper.setRotationPoint(-4F, 2F, 7F);
        setRotation(rlFlipper, 0F, 0F, 0F);
        rlFlipper.mirror = true;
        rlFlipper.addBox(-7F, 0F, -1F, 7, 1, 3).setTextureOffset(0, 16);
        body.addChild(rlFlipper);
        rrFlipper = new RendererModel(this, "rrFlipper");
        rrFlipper.setRotationPoint(4F, 2F, 7F);
        setRotation(rrFlipper, 0F, 0F, 0F);
        rrFlipper.mirror = true;
        rrFlipper.addBox(-1F, 0F, -1F, 7, 1, 3).setTextureOffset(0, 16);
        body.addChild(rrFlipper);
    }

    @Override
    public void render(SeaTurtleEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);

        float defFront = 0.3927F;
        float defFront2 = 0.3F;
        float defRear = .5F;

        f1 *=2f;
        f *= 1.5f;

        if (!entity.isInWater()) {
            body.rotateAngleX = -Math.abs(MathHelper.sin(f * 0.25F) * 1.25F * f1) - .10F;
            frFlipper.rotateAngleY = MathHelper.cos(f * 0.50F) * 2.5F * f1 + defFront;
            frFlipper.rotateAngleX = -defFront2;
            frFlipper.rotateAngleZ = MathHelper.cos(f * 0.50F) * 1.25F * f1 - defFront2;
            flFlipper.rotateAngleY = MathHelper.cos(f * 0.50F) * 2.5F * f1 - defFront;
            flFlipper.rotateAngleZ = -MathHelper.cos(f * 0.50F) * 1.25F * f1 + defFront2;
            frFlipper.rotateAngleX = defFront2;
            rrFlipper.rotateAngleY = -MathHelper.cos(f * 0.50F) * 1.25F * f1 - defRear;
            rlFlipper.rotateAngleY = -MathHelper.cos(f * 0.50F) * 1.25F * f1 + defRear;
            rrFlipper.rotateAngleZ = 0F;
            rlFlipper.rotateAngleZ = 0F;
        } else {
            body.rotateAngleX = 0F; // Y forward backward
            frFlipper.rotateAngleY = MathHelper.cos(f * 0.25F) * 1.5F * f1 + defFront;
            frFlipper.rotateAngleX = -defFront2;
            frFlipper.rotateAngleZ = -MathHelper.cos(f * 1.25F) * 1.75F * f1 - defFront2;
            flFlipper.rotateAngleY = MathHelper.cos(f * 0.25F) * 1.5F * f1 - defFront;
            flFlipper.rotateAngleZ = MathHelper.cos(f * 1.25F) * 1.75F * f1 + defFront2;
            frFlipper.rotateAngleX = defFront2;
            rrFlipper.rotateAngleY = -MathHelper.cos(f * 0.25F) * .25F * f1 - defRear;
            rlFlipper.rotateAngleY = MathHelper.cos(f * 0.25F) * .25F * f1 + defRear;
            rrFlipper.rotateAngleZ = -MathHelper.cos(f * 1.25F) * 1.25F * f1;
            rlFlipper.rotateAngleZ = -MathHelper.cos(f * 1.25F) * 1.25F * f1;
        }

        body.render(f5);
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
