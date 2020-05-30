package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggModel extends SegmentedModel<EggEntity> {
    public ModelRenderer body;

    public EggModel() {
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelRenderer(this);
        body.setRotationPoint(0F, 24F, 0F);
        body.mirror = true;
        body.setTextureOffset(0, 16).addBox(-3F, -10F, -3F, 6, 10, 6);
        body.setTextureOffset(0, 0).addBox(-1.5F, -11F, -1.5F, 3, 1, 3);
        body.setTextureOffset(0, 7).addBox(3F, -7F, -1.5F, 1, 6, 3);
        body.setTextureOffset(24, 9).addBox(-1.5F, -7F, 3F, 3, 6, 1);
        body.setTextureOffset(16, 7).addBox(-4F, -7F, -1.5F, 1, 6, 3);
        body.setTextureOffset(8, 9).addBox(-1.5F, -7F, -4F, 3, 6, 1);
    }

    @Override
    public void setRotationAngles(EggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body);
    }

    @Override
    public void setLivingAnimations(EggEntity entityliving, float limbSwing, float limbSwingAmount, float partialTick) {
        boolean hatching = entityliving.isNearHatching();
        double randRotator = entityliving.rotationRand;
        body.rotateAngleY = 0F;
        if (hatching) {
            body.rotateAngleY = (float) (Math.sin(entityliving.ticksExisted * .4)) * .2f;
            body.rotateAngleX = (float) ((Math.sin(randRotator * 2))) * .2f;
            body.rotateAngleZ = (float) ((Math.cos(randRotator * 2))) * .2f;
        } else {
            body.rotateAngleX = 0F;
            body.rotateAngleZ = 0F;
        }
    }
    
    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
    		float red, float green, float blue, float alpha) {
    	super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}