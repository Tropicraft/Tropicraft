package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.tropicraft.core.client.entity.model.EagleRayModel;
import net.tropicraft.core.client.entity.render.EagleRayRenderer;

import static java.lang.Math.PI;

public class EagleRayWingsLayer extends RenderLayer<LivingEntityRenderState, EagleRayModel> {
    /**
     * Number of joints the wings have. End points included.
     */
    private static final int WING_JOINTS = 10;
    /**
     * Number of ticks that one wing animation cycle takes.
     */
    private static final int WING_CYCLE_TICKS = 3 * 20; // 3 seconds
    /**
     * How many sine function phases to go through. Higher = more wave crests.
     */
    private static final float PHASES = 0.33f;

    public EagleRayWingsLayer(RenderLayerParent<LivingEntityRenderState, EagleRayModel> renderer) {
        super(renderer);
    }

    private float decayFunc(float n) {
        return n / (WING_JOINTS - 1.0f);
    }

    private float amplitudeFunc(LivingEntityRenderState state, float n) {
        double angle = 2 * PI * -n / (WING_JOINTS - 1.0f);
        return decayFunc(n) * Mth.sin((float) ((state.ageInTicks / WING_CYCLE_TICKS) * 2 * PI + PHASES * angle));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, LivingEntityRenderState state, float yRot, float xRot) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(EagleRayRenderer.TEXTURE));
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
        renderWings(state, poseStack, buffer, lightCoords, overlayCoords, CommonColors.WHITE);
        renderTailSimple(poseStack, buffer, lightCoords, overlayCoords, CommonColors.WHITE);
    }

    private void renderTailSimple(PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, int color) {
        float minU = 0.75f;
        float maxU = 1.0f;
        float minV = 0.0f;
        float maxV = 0.5f;

        stack.pushPose();
        stack.translate(0.55f, 0.0f, 1.5f);
        stack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        stack.scale(1.5f, 1.0f, 1.0f);
        vertex(buffer, stack.last(), 0, 0, 0, color, minU, minV, packedLightIn, packedOverlayIn);
        vertex(buffer, stack.last(), 0, 0, 1, color, minU, maxV, packedLightIn, packedOverlayIn);
        vertex(buffer, stack.last(), 1, 0, 1, color, maxU, maxV, packedLightIn, packedOverlayIn);
        vertex(buffer, stack.last(), 1, 0, 0, color, maxU, minV, packedLightIn, packedOverlayIn);
        stack.popPose();
    }

    private static void vertex(VertexConsumer bufferIn, PoseStack.Pose pose, float x, float y, float z, int color, float texU, float texV, int packedLight, int packedOverlay) {
        bufferIn.addVertex(pose, x, y, z).setColor(color).setUv(texU, texV).setOverlay(packedOverlay).setLight(packedLight).setNormal(pose, 0.0f, -1.0f, 0.0f);
    }

    private void renderWings(LivingEntityRenderState state, PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5f / 16.0f, 0, -0.5f); // Center on body
        matrixStackIn.scale(2.0f, 0.5f, 2.0f); // Scale to correct size

        renderWing(state, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color, false);

        // Rotate around center
        matrixStackIn.translate(0, 0, 0.5f);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0f));
        matrixStackIn.translate(0, 0, -0.5f);

        renderWing(state, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color, true);

        matrixStackIn.popPose();
    }

    private void renderWing(LivingEntityRenderState state, PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, int color, boolean reverse) {
        float minUFront = 0.0f;
        float maxUFront = 0.25f;
        float minVFront = 0.0f;
        float maxVFront = 0.5f;

        float minUBack = 0.0f;
        float maxUBack = 0.25f;
        float minVBack = 0.5f;
        float maxVBack = 1.0f;

        stack.pushPose();
        stack.translate(1.25f / 16.0f, 0, 0); // Translate out to body edge

        float prevAmplitude = 0.0f;
        for (int i = 1; i < WING_JOINTS; i++) {
            float amplitude = amplitudeFunc(state, i);

            float prevX = (i - 1) / (WING_JOINTS - 1.0f);
            float x = i / (WING_JOINTS - 1.0f);

            float prevUFront = minUFront + (maxUFront - minUFront) * prevX;
            float uFront = minUFront + (maxUFront - minUFront) * x;
            float prevUBack = minUBack + (maxUBack - minUBack) * prevX;
            float uBack = minUBack + (maxUBack - minUBack) * x;

            float offset = -0.001f;
            // Bottom
            PoseStack.Pose pose = stack.last();

            vertex(buffer, pose, x, amplitude - offset, 0, color, uBack, reverse ? maxVBack : minVBack, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, x, amplitude - offset, 1, color, uBack, reverse ? minVBack : maxVBack, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, prevX, prevAmplitude - offset, 1, color, prevUBack, reverse ? minVBack : maxVBack, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, prevX, prevAmplitude - offset, 0, color, prevUBack, reverse ? maxVBack : minVBack, packedLightIn, packedOverlayIn);

            // Top
            vertex(buffer, pose, prevX, prevAmplitude, 0, color, prevUFront, reverse ? maxVFront : minVFront, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, prevX, prevAmplitude, 1, color, prevUFront, reverse ? minVFront : maxVFront, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, x, amplitude, 1, color, uFront, reverse ? minVFront : maxVFront, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, x, amplitude, 0, color, uFront, reverse ? maxVFront : minVFront, packedLightIn, packedOverlayIn);

            prevAmplitude = amplitude;
        }

        stack.popPose();
    }
}
