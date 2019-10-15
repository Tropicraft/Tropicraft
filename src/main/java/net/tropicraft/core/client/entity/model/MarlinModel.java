package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

public class MarlinModel extends EntityModel<MarlinEntity> {

    RendererModel body;
    RendererModel dorsalFin1;
    RendererModel leftFin;
    RendererModel rightFin;
    RendererModel bottomFin;
    RendererModel head;
    RendererModel tail;
    RendererModel tail1;
    RendererModel sword;
    RendererModel tail3;
    RendererModel tailEndB;
    RendererModel tailEndT;
    public boolean inWater;

    public MarlinModel() {
        textureWidth = 64;
        textureHeight = 32;

        body = new RendererModel(this, 0, 22);
        body.addBox(-5F, -3F, -2F, 7, 6, 4);
        body.setRotationPoint(0F, 19F, 0F);
        body.mirror = true;
        setRotation(body, 0F, -1.570796F, 0F);
        dorsalFin1 = new RendererModel(this, 24, 20);
        dorsalFin1.addBox(-0.5F, -0.5F, -0.5F, 1, 2, 10);
        dorsalFin1.setRotationPoint(0F, 15.5F, -5F);
        dorsalFin1.mirror = true;
        leftFin = new RendererModel(this, 12, 10);
        leftFin.addBox(0F, -0.5F, -2F, 4, 1, 2);
        leftFin.setRotationPoint(2F, 21F, -3F);
        leftFin.mirror = true;
        rightFin = new RendererModel(this, 12, 7);
        rightFin.addBox(-4F, -0.5F, -2F, 4, 1, 2);
        rightFin.setRotationPoint(-2F, 21F, -3F);
        rightFin.mirror = true;
        bottomFin = new RendererModel(this, 52, 0);
        bottomFin.addBox(-0.5F, 2F, -2.5F, 1, 3, 2);
        bottomFin.setRotationPoint(0F, 19F, 0F);
        bottomFin.mirror = true;
        setRotation(bottomFin, 0.6981317F, 0F, 0F);
        head = new RendererModel(this, 46, 24);
        head.setRotationPoint(0F, 20F, -5F);
        head.mirror = true;
        head.addBox(-1.5F, -3F, -3F, 3, 5, 3);
        head.setTextureOffset(28, 0).addBox(-1F, -1.5F, -4F, 2, 3, 1);
        head.setTextureOffset(22, 0).addBox(-0.5F, -0.5F, -6F, 1, 2, 2);
        head.setTextureOffset(23, 24).addBox(-0.5F, -6F, -2.5F, 1, 3, 2);
        sword = new RendererModel(this, "sword");
        sword.setRotationPoint(0F, 0F, 0F);
        setRotation(sword, 0F, 1.5707F, 0F);
        sword.mirror = true;
        sword.setTextureOffset(0, 0).addBox(4F, -1.5F, -0.5F, 10, 1, 1);
        head.addChild(sword);
        tail = new RendererModel(this, "tail");
        tail.setRotationPoint(0F, 19F, 2F);
        tail.mirror = true;
        tail.setTextureOffset(0, 13).addBox(-1.5F, -2F, 0F, 3, 5, 4);
        tail1 = new RendererModel(this, "tail1");
        tail1.setRotationPoint(0F, 0F, 4F);
        tail1.mirror = true;
        tail1.setTextureOffset(0, 5).addBox(-1F, -1.5F, 0F, 2, 4, 4);
        tail3 = new RendererModel(this, "tail3");
        tail3.setRotationPoint(0F, 1F, 4F);
        tail3.mirror = true;
        tail3.setTextureOffset(46, 0).addBox(-0.5F, -1.5F, 0F, 1, 3, 2);
        tailEndB = new RendererModel(this, "tailEndB");
        tailEndB.setRotationPoint(0F, 0F, 0F);
        setRotation(tailEndB, 0.593411F, 0F, 0F);
        tailEndB.mirror = true;
        tailEndB.setTextureOffset(40, 0).addBox(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndB);
        tailEndT = new RendererModel(this, "tailEndT");
        tailEndT.setRotationPoint(0F, 0F, 0F);
        setRotation(tailEndT, 2.548179F, 0F, 0F);
        tailEndT.mirror = true;
        tailEndT.setTextureOffset(34, 0).addBox(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndT);
        tail1.addChild(tail3);
        tail.addChild(tail1);
    }

    @Override
    public void render(MarlinEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        body.render(f5);
        dorsalFin1.render(f5);
        leftFin.render(f5);
        rightFin.render(f5);
        bottomFin.render(f5);
        head.render(f5);
        tail.render(f5);
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(MarlinEntity marlin, float f, float f1, float f2, float f3, float f4, float f5) {
        if (!inWater) {
            head.rotateAngleY = (float) (Math.sin(f2 * .55F)) * .260F;
            tail.rotateAngleY = (float) (Math.sin(f2 * .55F)) * .260F;
            tail1.rotateAngleY = (float) Math.sin(f2 * .55F) * .260F;
            tail3.rotateAngleY = (float) Math.sin(f2 * .55F) * .260F;
            leftFin.rotateAngleZ = (float) (Math.sin(f2 * .25F)) * .165F + 0.523598F;
            rightFin.rotateAngleZ = -(float) (Math.sin(f2 * .25F)) * .165F - 0.523598F;
            leftFin.rotateAngleY = -1.5F;
            rightFin.rotateAngleY = 1.5F - (float) (Math.sin(f2 * .25F)) * .165F - 0.523598F;
        } else {
            head.rotateAngleY = (float) (Math.sin(f2 * .25F)) * .135F;
            tail.rotateAngleY = (float) (Math.sin(f2 * .25F)) * .135F;
            tail1.rotateAngleY = (float) Math.sin(f2 * .35F) * .150F;
            tail3.rotateAngleY = (float) Math.sin(f2 * .45F) * .160F;
            leftFin.rotateAngleZ = (float) (Math.sin(f2 * .25F)) * .165F + 0.523598F;
            rightFin.rotateAngleZ = -(float) (Math.sin(f2 * .25F)) * .165F - 0.523598F;
            leftFin.rotateAngleY = -0.392699F;
            rightFin.rotateAngleY = 0.392699F;
        }
    }
}
