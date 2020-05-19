package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

public class UmbrellaModel extends SegmentedModel<UmbrellaEntity> {

    public ModelRenderer base;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape31;
    public ModelRenderer shape32;
    public ModelRenderer shape33;
    public ModelRenderer shape4;
    public ModelRenderer shape11;
    public ModelRenderer shape12;
    public ModelRenderer shape111;
    public ModelRenderer shape112;

    public UmbrellaModel() {
        base = new ModelRenderer(this, 0, 0);
        base.addBox(-0.5F, 0F, -0.5F, 1, 14, 1, 0F);
        base.setRotationPoint(0F, -13F, 0F);

        shape2 = new ModelRenderer(this, 0, 0);
        shape2.addBox(-7.5F, -2F, -7.5F, 15, 1, 15, 0F);
        shape2.setRotationPoint(0F, -12F, 0F);

        shape3 = new ModelRenderer(this, 0, 20);
        shape3.addBox(-4F, -1F, 0F, 9, 1, 3, 0F);
        shape3.setRotationPoint(-0.5F, -13F, 7.5F);

        shape3.rotateAngleX = -0.2443461F;
        shape3.rotateAngleY = 0F;
        shape3.rotateAngleZ = 0F;
        shape3.mirror = false;

        shape31 = new ModelRenderer(this, 0, 24);
        shape31.addBox(-4.5F, -1F, 0F, 9, 1, 3, 0F);
        shape31.setRotationPoint(7.5F, -13F, 0F);

        shape31.rotateAngleX = -0.2443461F;
        shape31.rotateAngleY = 1.570796F;
        shape31.rotateAngleZ = 0F;
        shape31.mirror = false;

        shape32 = new ModelRenderer(this, 0, 28);
        shape32.addBox(-4.5F, -1F, -1F, 9, 1, 3, 0F);
        shape32.setRotationPoint(0F, -12.75F, -8.45F);

        shape32.rotateAngleX = -0.2443461F;
        shape32.rotateAngleY = 3.141593F;
        shape32.rotateAngleZ = 0F;
        shape32.mirror = false;

        shape33 = new ModelRenderer(this, 24, 28);
        shape33.addBox(-4.5F, -1F, 1F, 9, 1, 3, 0F);
        shape33.setRotationPoint(-6.5F, -13.25F, 0F);

        shape33.rotateAngleX = -0.2443461F;
        shape33.rotateAngleY = -1.570796F;
        shape33.rotateAngleZ = 0F;
        shape33.mirror = false;

        shape4 = new ModelRenderer(this, 25, 25);
        shape4.addBox(-1F, -1F, -1F, 2, 1, 2, 0F);
        shape4.setRotationPoint(0F, -14F, 0F);

        shape11 = new ModelRenderer(this, 0, 0);
        shape11.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape11.setRotationPoint(0F, -10F, 0F);

        shape11.rotateAngleX = 1.902409F;
        shape11.rotateAngleY = 0F;
        shape11.rotateAngleZ = 0F;
        shape11.mirror = false;

        shape12 = new ModelRenderer(this, 0, 0);
        shape12.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape12.setRotationPoint(0F, -10F, 0F);

        shape12.rotateAngleX = -1.902409F;
        shape12.rotateAngleY = 0F;
        shape12.rotateAngleZ = 0F;
        shape12.mirror = false;

        shape111 = new ModelRenderer(this, 0, 0);
        shape111.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape111.setRotationPoint(0F, -10F, 0F);

        shape111.rotateAngleX = 1.902409F;
        shape111.rotateAngleY = 1.570796F;
        shape111.rotateAngleZ = 0F;
        shape111.mirror = false;

        shape112 = new ModelRenderer(this, 0, 0);
        shape112.addBox(-0.5F, 0F, -0.5F, 1, 9, 1, 0F);
        shape112.setRotationPoint(0F, -10F, 0F);

        shape112.rotateAngleX = 1.902409F;
        shape112.rotateAngleY = -1.570796F;
        shape112.rotateAngleZ = 0F;
        shape112.mirror = false;

    }

    @Override
    public void setRotationAngles(UmbrellaEntity umbrella, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
            base, shape2, shape3, shape31, shape32, shape33,
            shape4, shape11, shape12, shape111, shape112
        );
    }
}
