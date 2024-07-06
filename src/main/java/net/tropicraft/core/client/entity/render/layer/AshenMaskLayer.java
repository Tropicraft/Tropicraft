package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.AshenRenderer;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenMaskLayer extends RenderLayer<AshenEntity, AshenModel> {
    private static final ResourceLocation TEXTURE_LOCATION = Tropicraft.location("textures/entity/ashen/mask.png");

    private final TropicraftSpecialRenderHelper mask;
    private final AshenModel modelAshen;

    public AshenMaskLayer(AshenRenderer renderer, AshenModel model) {
        super(renderer);
        modelAshen = model;
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLight, AshenEntity ashen, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (ashen.hasMask()) {
            stack.pushPose();
            modelAshen.setupAnim(ashen, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            modelAshen.head.translateAndRotate(stack);

            stack.translate(-0.03125f, 0.0625f * 3, 0.18f);
            stack.scale(0.75f, 0.75f, 0.75f);
            VertexConsumer builder = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_LOCATION));
            mask.renderMask(stack, builder, ashen.getMaskType(), packedLight, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }
    }
}
