package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.AshenRenderer;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenMaskLayer extends RenderLayer<AshenEntity, AshenModel> {

    private TropicraftSpecialRenderHelper mask;
    private AshenModel modelAshen;

    public AshenMaskLayer(RenderLayerParent<AshenEntity, AshenModel> featureRendererContext) {
        super(featureRendererContext);
        modelAshen = this.getParentModel();
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, AshenEntity ashen, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (ashen.hasMask()) {
            stack.pushPose();
            modelAshen.setupAnim(ashen, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            modelAshen.head.translateAndRotate(stack);

            stack.translate(-0.03125F, 0.0625f * 3, .18F);
            stack.scale(0.75f, 0.75f, 0.75f);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TropicraftRenderUtils.getTextureEntity("ashen/mask")));
            mask.renderMask(stack, ivertexbuilder, ashen.getMaskType(), packedLightIn, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }
    }
}
