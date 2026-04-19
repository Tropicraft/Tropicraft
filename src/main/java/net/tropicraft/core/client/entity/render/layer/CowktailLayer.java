package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.tropicraft.core.client.entity.render.state.CowktailRenderState;

public class CowktailLayer<S extends CowktailRenderState> extends RenderLayer<S, CowModel> {
    public CowktailLayer(RenderLayerParent<S, CowModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        if (state.isBaby || state.isInvisible || state.flowerModel.isEmpty()) {
            return;
        }

        CowModel model = getParentModel();
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-48.0f));
        renderBlock(state, poseStack, submitNodeCollector, lightCoords, overlayCoords);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(42.0f));
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-48.0f));
        renderBlock(state, poseStack, submitNodeCollector, lightCoords, overlayCoords);
        poseStack.popPose();

        poseStack.pushPose();
        model.getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-78.0f));
        renderBlock(state, poseStack, submitNodeCollector, lightCoords, overlayCoords);
        poseStack.popPose();
    }

    private void renderBlock(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int overlayCoords) {
        final float scale = 0.6f;
        poseStack.scale(-scale, -scale, scale);
        poseStack.translate(-0.5, -0.8, -0.5);
        state.flowerModel.submit(poseStack, submitNodeCollector, packedLight, overlayCoords, state.outlineColor);
    }
}
