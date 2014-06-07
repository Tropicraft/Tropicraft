package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelUmbrella extends ModelBase {

    public ModelRenderer New_Shape1;
    public ModelRenderer New_Shape2;
    public ModelRenderer New_Shape3;
    public ModelRenderer New_Shape31;
    public ModelRenderer New_Shape32;
    public ModelRenderer New_Shape33;
    public ModelRenderer New_Shape4;
    public ModelRenderer New_Shape11;
    public ModelRenderer New_Shape12;
    public ModelRenderer New_Shape111;
    public ModelRenderer New_Shape112;

    public ModelUmbrella() {
        New_Shape1 = new ModelRenderer(this, 0, 0);
        New_Shape1.addBox(-0.5F, 0F, -0.5F, 1, 14, 1, 0F);
        New_Shape1.setRotationPoint(0F, -13F, 0F);

        New_Shape1.rotateAngleX = 0F;
        New_Shape1.rotateAngleY = 0F;
        New_Shape1.rotateAngleZ = 0F;
        New_Shape1.mirror = false;

        New_Shape2 = new ModelRenderer(this, 0, 0);
        New_Shape2.addBox(-7.5F, -2F, -7.5F, 15, 1, 15, 0F);
        New_Shape2.setRotationPoint(0F, -12F, 0F);

        New_Shape2.rotateAngleX = 0F;
        New_Shape2.rotateAngleY = 0F;
        New_Shape2.rotateAngleZ = 0F;
        New_Shape2.mirror = false;

        New_Shape3 = new ModelRenderer(this, 0, 20);
        New_Shape3.addBox(-4F, -1F, 0F, 9, 1, 3, 0F);
        New_Shape3.setRotationPoint(-0.5F, -13F, 7.5F);

        New_Shape3.rotateAngleX = -0.2443461F;
        New_Shape3.rotateAngleY = 0F;
        New_Shape3.rotateAngleZ = 0F;
        New_Shape3.mirror = false;

        New_Shape31 = new ModelRenderer(this, 0, 24);
        New_Shape31.addBox(-4.5F, -1F, 0F, 9, 1, 3, 0F);
        New_Shape31.setRotationPoint(7.5F, -13F, 0F);

        New_Shape31.rotateAngleX = -0.2443461F;
        New_Shape31.rotateAngleY = 1.570796F;
        New_Shape31.rotateAngleZ = 0F;
        New_Shape31.mirror = false;

        New_Shape32 = new ModelRenderer(this, 0, 28);
        New_Shape32.addBox(-4.5F, -1F, -1F, 9, 1, 3, 0F);
        New_Shape32.setRotationPoint(0F, -12.75F, -8.5F);

        New_Shape32.rotateAngleX = -0.2443461F;
        New_Shape32.rotateAngleY = 3.141593F;
        New_Shape32.rotateAngleZ = 0F;
        New_Shape32.mirror = false;

        New_Shape33 = new ModelRenderer(this, 24, 28);
        New_Shape33.addBox(-4.5F, -1F, 1F, 9, 1, 3, 0F);
        New_Shape33.setRotationPoint(-6.5F, -13.25F, 0F);

        New_Shape33.rotateAngleX = -0.2443461F;
        New_Shape33.rotateAngleY = -1.570796F;
        New_Shape33.rotateAngleZ = 0F;
        New_Shape33.mirror = false;

        New_Shape4 = new ModelRenderer(this, 25, 25);
        New_Shape4.addBox(-1F, -1F, -1F, 2, 1, 2, 0F);
        New_Shape4.setRotationPoint(0F, -14F, 0F);

        New_Shape4.rotateAngleX = 0F;
        New_Shape4.rotateAngleY = 0F;
        New_Shape4.rotateAngleZ = 0F;
        New_Shape4.mirror = false;

        New_Shape11 = new ModelRenderer(this, 0, 0);
        New_Shape11.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        New_Shape11.setRotationPoint(0F, -10F, 0F);

        New_Shape11.rotateAngleX = 1.902409F;
        New_Shape11.rotateAngleY = 0F;
        New_Shape11.rotateAngleZ = 0F;
        New_Shape11.mirror = false;

        New_Shape12 = new ModelRenderer(this, 0, 0);
        New_Shape12.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        New_Shape12.setRotationPoint(0F, -10F, 0F);

        New_Shape12.rotateAngleX = -1.902409F;
        New_Shape12.rotateAngleY = 0F;
        New_Shape12.rotateAngleZ = 0F;
        New_Shape12.mirror = false;

        New_Shape111 = new ModelRenderer(this, 0, 0);
        New_Shape111.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        New_Shape111.setRotationPoint(0F, -10F, 0F);

        New_Shape111.rotateAngleX = 1.902409F;
        New_Shape111.rotateAngleY = 1.570796F;
        New_Shape111.rotateAngleZ = 0F;
        New_Shape111.mirror = false;

        New_Shape112 = new ModelRenderer(this, 0, 0);
        New_Shape112.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        New_Shape112.setRotationPoint(0F, -10F, 0F);

        New_Shape112.rotateAngleX = 1.902409F;
        New_Shape112.rotateAngleY = -1.570796F;
        New_Shape112.rotateAngleZ = 0F;
        New_Shape112.mirror = false;

    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        New_Shape1.render(f5);
        New_Shape2.render(f5);
        New_Shape3.render(f5);
        New_Shape31.render(f5);
        New_Shape32.render(f5);
        New_Shape33.render(f5);
        New_Shape4.render(f5);
        New_Shape11.render(f5);
        New_Shape12.render(f5);
        New_Shape111.render(f5);
        New_Shape112.render(f5);

    }

    public void renderUmbrella() {
        float f5 = 0.0625F;
        New_Shape1.render(f5);
        New_Shape2.render(f5);
        New_Shape3.render(f5);
        New_Shape31.render(f5);
        New_Shape32.render(f5);
        New_Shape33.render(f5);
        New_Shape4.render(f5);
        New_Shape11.render(f5);
        New_Shape12.render(f5);
        New_Shape111.render(f5);
        New_Shape112.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}