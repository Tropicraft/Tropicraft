package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.tropicraft.core.client.entity.render.state.VMonkeyRenderState;

public class VMonkeyHeldItemLayer<S extends VMonkeyRenderState, M extends EntityModel<S> & ArmedModel> extends RenderLayer<S, M> {
    public VMonkeyHeldItemLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        ItemStackRenderState item = state.getMainHandItemState();
        if (state.isOrderedToSit && !item.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0f, 1.30f, -0.425f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, EntityRenderState.NO_OUTLINE);
            poseStack.popPose();
        }
    }
}
