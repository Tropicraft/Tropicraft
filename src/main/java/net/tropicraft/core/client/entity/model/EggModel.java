package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggModel extends EntityModel<EggEntity> {

    public RendererModel body;

    public EggModel() {
        textureWidth = 64;
        textureHeight = 32;

        body = new RendererModel(this);
        body.setRotationPoint(0F, 24F, 0F);
        setRotation(body, 0F, 0F, 0F);
        body.mirror = true;
        body.setTextureOffset(0, 16).addBox(-3F, -10F, -3F, 6, 10, 6);
        body.setTextureOffset(0, 0).addBox(-1.5F, -11F, -1.5F, 3, 1, 3);
        body.setTextureOffset(0, 7).addBox(3F, -7F, -1.5F, 1, 6, 3);
        body.setTextureOffset(24, 9).addBox(-1.5F, -7F, 3F, 3, 6, 1);
        body.setTextureOffset(16, 7).addBox(-4F, -7F, -1.5F, 1, 6, 3);
        body.setTextureOffset(8, 9).addBox(-1.5F, -7F, -4F, 3, 6, 1);
    }

    @Override
    public void render(EggEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        body.render(f5);
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(EggEntity entityliving, float limbSwing, float limbSwingAmount, float partialTick) {
        boolean hatching = entityliving.isNearHatching();
        double randRotator = entityliving.rotationRand;
        if (hatching) {
            body.rotateAngleY = 0F;
            body.rotateAngleY = (float) (Math.sin(entityliving.ticksExisted * .4)) * .2f;
            body.rotateAngleX = (float) ((Math.sin(randRotator * 2))) * .2f;
            body.rotateAngleZ = (float) ((Math.cos(randRotator * 2))) * .2f;
        } else {
            body.rotateAngleY = 0F;
            body.rotateAngleX = 0F;
            body.rotateAngleZ = 0F;
        }
    }
}