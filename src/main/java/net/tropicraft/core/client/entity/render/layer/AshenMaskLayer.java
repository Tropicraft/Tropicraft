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
import net.tropicraft.core.client.entity.render.state.AshenRenderState;

public class AshenMaskLayer extends RenderLayer<AshenRenderState, AshenModel> {
    private static final ResourceLocation TEXTURE_LOCATION = Tropicraft.location("textures/entity/ashen/mask.png");

    private final TropicraftSpecialRenderHelper mask;
    private final AshenModel modelAshen;

    public AshenMaskLayer(AshenRenderer renderer, AshenModel model) {
        super(renderer);
        modelAshen = model;
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLight, AshenRenderState state, float yRot, float xRot) {
        if (state.hasMask) {
            stack.pushPose();
            modelAshen.setupAnim(state);
            modelAshen.head.translateAndRotate(stack);

            stack.translate(-0.03125f, 0.0625f * 3, 0.18f);
            stack.scale(0.75f, 0.75f, 0.75f);
            VertexConsumer builder = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_LOCATION));
            mask.renderMask(stack, builder, state.maskType, packedLight, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }
    }
}
