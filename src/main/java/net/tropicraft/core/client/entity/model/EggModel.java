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
        texWidth = 64;
        texHeight = 32;

        body = new ModelRenderer(this);
        body.setPos(0F, 24F, 0F);
        body.mirror = true;
        body.texOffs(0, 16).addBox(-3F, -10F, -3F, 6, 10, 6);
        body.texOffs(0, 0).addBox(-1.5F, -11F, -1.5F, 3, 1, 3);
        body.texOffs(0, 7).addBox(3F, -7F, -1.5F, 1, 6, 3);
        body.texOffs(24, 9).addBox(-1.5F, -7F, 3F, 3, 6, 1);
        body.texOffs(16, 7).addBox(-4F, -7F, -1.5F, 1, 6, 3);
        body.texOffs(8, 9).addBox(-1.5F, -7F, -4F, 3, 6, 1);
    }

    @Override
    public void setupAnim(EggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(body);
    }

    @Override
    public void prepareMobModel(EggEntity entityliving, float limbSwing, float limbSwingAmount, float partialTick) {
        boolean hatching = entityliving.isNearHatching();
        double randRotator = entityliving.rotationRand;
        body.yRot = 0F;
        if (hatching) {
            body.yRot = (float) (Math.sin((entityliving.tickCount + partialTick) * .6)) * .6f;
            body.xRot = (float) ((Math.sin(randRotator * 4))) * .6f;
            body.zRot = (float) ((Math.cos(randRotator * 4))) * .6f;
        } else {
            body.xRot = 0F;
            body.zRot = 0F;
        }
    }
    
    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
            float red, float green, float blue, float alpha) {
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}