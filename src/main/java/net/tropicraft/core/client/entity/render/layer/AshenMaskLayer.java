package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.AshenRenderer;
import net.tropicraft.core.client.entity.render.state.AshenRenderState;

public class AshenMaskLayer extends RenderLayer<AshenRenderState, AshenModel> {
    private static final Identifier TEXTURE_LOCATION = Tropicraft.id("textures/entity/ashen/mask.png");

    private final TropicraftSpecialRenderHelper mask;
    private final AshenModel modelAshen;

    public AshenMaskLayer(AshenRenderer renderer, AshenModel model) {
        super(renderer);
        modelAshen = model;
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AshenRenderState state, float yRot, float xRot) {
        if (state.hasMask) {
            poseStack.pushPose();
            modelAshen.setupAnim(state);
            modelAshen.head.translateAndRotate(poseStack);

            poseStack.translate(-0.03125f, 0.0625f * 3, 0.18f);
            poseStack.scale(0.75f, 0.75f, 0.75f);
            submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(TEXTURE_LOCATION), (pose, buffer) -> {
                PoseStack stack = new PoseStack();
                stack.mulPose(pose.pose());
                mask.renderMask(stack, buffer, state.maskType, lightCoords, OverlayTexture.NO_OVERLAY);
            });
            poseStack.popPose();
        }
    }
}
