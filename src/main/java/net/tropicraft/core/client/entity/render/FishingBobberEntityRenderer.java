package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.passive.FishingBobberEntity;

public class FishingBobberEntityRenderer extends EntityRenderer<FishingBobberEntity, FishingHookRenderState> {
    private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/fishing/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(TEXTURE_LOCATION);

    public FishingBobberEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public FishingHookRenderState createRenderState() {
        return new FishingHookRenderState();
    }

    @Override
    public void extractRenderState(FishingBobberEntity entity, FishingHookRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        EntityKoaBase koa = entity.getAngler();
        if (koa == null) {
            state.lineOriginOffset = Vec3.ZERO;
        } else {
            Vec3 handPos = getHandPos(koa, partialTicks);
            Vec3 targetPos = entity.getPosition(partialTicks).add(0.0, 0.25, 0.0);
            state.lineOriginOffset = handPos.subtract(targetPos);
        }
    }

    public static HumanoidArm getHoldingArm(EntityKoaBase koa) {
        return koa.getMainHandItem().canPerformAction(ItemAbilities.FISHING_ROD_CAST) ? koa.getMainArm() : koa.getMainArm().getOpposite();
    }

    private static Vec3 getHandPos(EntityKoaBase koa, float partialTick) {
        int side = getHoldingArm(koa) == HumanoidArm.RIGHT ? 1 : -1;
        float yRot = Mth.lerp(partialTick, koa.yBodyRotO, koa.yBodyRot) * Mth.DEG_TO_RAD;
        double sin = Mth.sin(yRot);
        double cos = Mth.cos(yRot);
        float scale = koa.getScale();
        double right = side * 0.35 * scale;
        double forward = 0.8 * scale;
        float yOffset = koa.isCrouching() ? -3.0f / 16.0f : 0.0f;
        return koa.getEyePosition(partialTick).add(-cos * right - sin * forward, yOffset - 0.45 * scale, -sin * right + cos * forward);
    }

    @Override
    public boolean shouldRender(FishingBobberEntity entity, Frustum frustum, double cameraX, double cameraY, double cameraZ) {
        return super.shouldRender(entity, frustum, cameraX, cameraY, cameraZ) && entity.getAngler() != null;
    }

    @Override
    protected boolean affectedByCulling(FishingBobberEntity display) {
        return false;
    }

    @Override
    public void submit(FishingHookRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(camera.orientation);
        submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
            vertex(buffer, pose, state.lightCoords, 0.0f, 0, 0, 1);
            vertex(buffer, pose, state.lightCoords, 1.0f, 0, 1, 1);
            vertex(buffer, pose, state.lightCoords, 1.0f, 1, 1, 0);
            vertex(buffer, pose, state.lightCoords, 0.0f, 1, 0, 0);
        });
        poseStack.popPose();

        float originX = (float) state.lineOriginOffset.x;
        float originY = (float) state.lineOriginOffset.y;
        float originZ = (float) state.lineOriginOffset.z;

        float width = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
            int steps = 16;
            for (int i = 0; i < steps; i++) {
                float a0 = fraction(i, steps);
                float a1 = fraction(i + 1, steps);
                stringVertex(originX, originY, originZ, buffer, pose, a0, a1, width);
                stringVertex(originX, originY, originZ, buffer, pose, a1, a0, width);
            }
        });

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static float fraction(int numerator, int denominator) {
        return (float) numerator / denominator;
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v) {
        consumer.addVertex(pose, x - 0.5f, y - 0.5f, 0.0f)
                .setColor(CommonColors.WHITE)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }

    private static void stringVertex(float originX, float originY, float originZ, VertexConsumer consumer, PoseStack.Pose pose, float stringFraction, float nextStringFraction, float width) {
        float x = originX * stringFraction;
        float y = originY * (stringFraction * stringFraction + stringFraction) * 0.5f + 0.25f;
        float z = originZ * stringFraction;
        float normalX = originX * nextStringFraction - x;
        float normalY = originY * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5f + 0.25f - y;
        float normalZ = originZ * nextStringFraction - z;
        float normalLength = Mth.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX /= normalLength;
        normalY /= normalLength;
        normalZ /= normalLength;
        consumer.addVertex(pose, x, y, z).setColor(CommonColors.BLACK).setNormal(pose, normalX, normalY, normalZ).setLineWidth(width);
    }
}
