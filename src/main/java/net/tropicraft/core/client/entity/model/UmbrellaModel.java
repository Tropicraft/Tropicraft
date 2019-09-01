package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

public class UmbrellaModel extends EntityModel<UmbrellaEntity> {

    public RendererModel base;
    public RendererModel shape2;
    public RendererModel shape3;
    public RendererModel shape31;
    public RendererModel shape32;
    public RendererModel shape33;
    public RendererModel shape4;
    public RendererModel shape11;
    public RendererModel shape12;
    public RendererModel shape111;
    public RendererModel shape112;

    public UmbrellaModel() {
        base = new RendererModel(this, 0, 0);
        base.addBox(-0.5F, 0F, -0.5F, 1, 14, 1, 0F);
        base.setRotationPoint(0F, -13F, 0F);

        shape2 = new RendererModel(this, 0, 0);
        shape2.addBox(-7.5F, -2F, -7.5F, 15, 1, 15, 0F);
        shape2.setRotationPoint(0F, -12F, 0F);

        shape3 = new RendererModel(this, 0, 20);
        shape3.addBox(-4F, -1F, 0F, 9, 1, 3, 0F);
        shape3.setRotationPoint(-0.5F, -13F, 7.5F);

        shape3.rotateAngleX = -0.2443461F;
        shape3.rotateAngleY = 0F;
        shape3.rotateAngleZ = 0F;
        shape3.mirror = false;

        shape31 = new RendererModel(this, 0, 24);
        shape31.addBox(-4.5F, -1F, 0F, 9, 1, 3, 0F);
        shape31.setRotationPoint(7.5F, -13F, 0F);

        shape31.rotateAngleX = -0.2443461F;
        shape31.rotateAngleY = 1.570796F;
        shape31.rotateAngleZ = 0F;
        shape31.mirror = false;

        shape32 = new RendererModel(this, 0, 28);
        shape32.addBox(-4.5F, -1F, -1F, 9, 1, 3, 0F);
        shape32.setRotationPoint(0F, -12.75F, -8.45F);

        shape32.rotateAngleX = -0.2443461F;
        shape32.rotateAngleY = 3.141593F;
        shape32.rotateAngleZ = 0F;
        shape32.mirror = false;

        shape33 = new RendererModel(this, 24, 28);
        shape33.addBox(-4.5F, -1F, 1F, 9, 1, 3, 0F);
        shape33.setRotationPoint(-6.5F, -13.25F, 0F);

        shape33.rotateAngleX = -0.2443461F;
        shape33.rotateAngleY = -1.570796F;
        shape33.rotateAngleZ = 0F;
        shape33.mirror = false;

        shape4 = new RendererModel(this, 25, 25);
        shape4.addBox(-1F, -1F, -1F, 2, 1, 2, 0F);
        shape4.setRotationPoint(0F, -14F, 0F);

        shape11 = new RendererModel(this, 0, 0);
        shape11.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape11.setRotationPoint(0F, -10F, 0F);

        shape11.rotateAngleX = 1.902409F;
        shape11.rotateAngleY = 0F;
        shape11.rotateAngleZ = 0F;
        shape11.mirror = false;

        shape12 = new RendererModel(this, 0, 0);
        shape12.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape12.setRotationPoint(0F, -10F, 0F);

        shape12.rotateAngleX = -1.902409F;
        shape12.rotateAngleY = 0F;
        shape12.rotateAngleZ = 0F;
        shape12.mirror = false;

        shape111 = new RendererModel(this, 0, 0);
        shape111.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape111.setRotationPoint(0F, -10F, 0F);

        shape111.rotateAngleX = 1.902409F;
        shape111.rotateAngleY = 1.570796F;
        shape111.rotateAngleZ = 0F;
        shape111.mirror = false;

        shape112 = new RendererModel(this, 0, 0);
        shape112.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape112.setRotationPoint(0F, -10F, 0F);

        shape112.rotateAngleX = 1.902409F;
        shape112.rotateAngleY = -1.570796F;
        shape112.rotateAngleZ = 0F;
        shape112.mirror = false;

    }

    @Override
    public void render(UmbrellaEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        base.render(f5);
        shape2.render(f5);
        shape3.render(f5);
        shape31.render(f5);
        shape32.render(f5);
        shape33.render(f5);
        shape4.render(f5);
        shape11.render(f5);
        shape12.render(f5);
        shape111.render(f5);
        shape112.render(f5);
    }

    @Override
    public void setRotationAngles(final UmbrellaEntity ent, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(ent, f, f1, f2, f3, f4, f5);
    }
}