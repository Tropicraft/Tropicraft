package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;

public class PoisonBlotRenderer extends EntityRenderer<PoisonBlotEntity, EntityRenderState> {
    private static final ResourceLocation TEXTURE_LOCATION = Tropicraft.location("textures/entity/treefrog/blot.png");

    public PoisonBlotRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void render(EntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE_LOCATION));
        PoseStack.Pose pose = poseStack.last();
        vertex(pose, buffer, -0.5f, -0.5f, 0, 1, packedLight);
        vertex(pose, buffer, 0.5f, -0.5f, 1, 1, packedLight);
        vertex(pose, buffer, 0.5f, 0.5f, 1, 0, packedLight);
        vertex(pose, buffer, -0.5f, 0.5f, 0, 0, packedLight);
        poseStack.popPose();
        super.render(state, poseStack, bufferSource, packedLight);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float u, float v, int light) {
        buffer.addVertex(pose, x, y, 0.0f)
                .setColor(CommonColors.WHITE)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0f, 0.0f, 1.0f);
    }
}
