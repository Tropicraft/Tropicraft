package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEIH extends ModelBase {

    public ModelRenderer body;
    public ModelRenderer base;
    public ModelRenderer nose;
    public ModelRenderer mouth;
    public ModelRenderer top;
    public ModelRenderer leye;
    public ModelRenderer reye;

    public ModelEIH() {
        body = new ModelRenderer(this, 34, 8);
        body.addBox(-4F, 1.0F, -1F, 8, 17, 7, 0.0F);
        body.setRotationPoint(0.0F, -2F, 0.0F);
        body.rotateAngleX = 0.0F;
        body.rotateAngleY = 0.0F;
        body.rotateAngleZ = 0.0F;
        body.mirror = false;
        base = new ModelRenderer(this, 0, 0);
        base.addBox(-4F, 11F, -3F, 8, 8, 11, 0.0F);
        base.setRotationPoint(0.0F, 5F, -2F);
        base.rotateAngleX = 0.0F;
        base.rotateAngleY = 0.0F;
        base.rotateAngleZ = 0.0F;
        base.mirror = false;
        nose = new ModelRenderer(this, 27, 2);
        nose.addBox(13.5F, -1F, -3F, 13, 2, 3, 0.0F);
        nose.setRotationPoint(0.0F, -14.8F, -1F);
        nose.rotateAngleX = 0.0F;
        nose.rotateAngleY = 0.0F;
        nose.rotateAngleZ = 1.570796F;
        nose.mirror = false;
        mouth = new ModelRenderer(this, 56, 11);
        mouth.addBox(-1.5F, 4F, -1F, 3, 3, 1, 0.0F);
        mouth.setRotationPoint(0.0F, 7.5F, -0.5F);
        mouth.rotateAngleX = 0.0F;
        mouth.rotateAngleY = 0.0F;
        mouth.rotateAngleZ = 0.0F;
        mouth.mirror = false;
        top = new ModelRenderer(this, 0, 17);
        top.addBox(-4F, -1F, -10F, 8, 5, 10, 0.0F);
        top.setRotationPoint(0.0F, -5F, 6F);
        top.rotateAngleX = 0.0F;
        top.rotateAngleY = 0.0F;
        top.rotateAngleZ = 0.0F;
        top.mirror = false;
        leye = new ModelRenderer(this, 56, 7);
        leye.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        leye.setRotationPoint(1.0F, -1F, -2F);
        leye.rotateAngleX = 0.0F;
        leye.rotateAngleY = 0.0F;
        leye.rotateAngleZ = 0.0F;
        leye.mirror = true;
        reye = new ModelRenderer(this, 56, 7);
        reye.addBox(-1.5F, -1F, -1F, 3, 3, 1, 0.0F);
        reye.setRotationPoint(-2.5F, 0.0F, -1F);
        reye.rotateAngleX = 0.0F;
        reye.rotateAngleY = 0.0F;
        reye.rotateAngleZ = 0.0F;
        reye.mirror = false;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, null);
        body.render(f5);
        base.render(f5);
        nose.render(f5);
        mouth.render(f5);
        top.render(f5);
        leye.render(f5);
        reye.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}
