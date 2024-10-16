package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;

@OnlyIn(Dist.CLIENT)
public class PoisonBlotRenderer extends EntityRenderer<PoisonBlotEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = Tropicraft.location("textures/entity/treefrog/blot.png");

    public PoisonBlotRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PoisonBlotEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.mulPose(entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Axis.YP.rotationDegrees(180.0f));
        ResourceLocation resourceLocation = getTextureLocation(entity);
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityCutout(resourceLocation));
        PoseStack.Pose pose = stack.last();
        vertex(pose, buffer, -0.5f, -0.5f, 0, 1, packedLightIn);
        vertex(pose, buffer, 0.5f, -0.5f, 1, 1, packedLightIn);
        vertex(pose, buffer, 0.5f, 0.5f, 1, 0, packedLightIn);
        vertex(pose, buffer, -0.5f, 0.5f, 0, 0, packedLightIn);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float u, float v, int light) {
        buffer.addVertex(pose, x, y, 0.0f)
                .setColor(CommonColors.WHITE)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(PoisonBlotEntity entity) {
        return TEXTURE_LOCATION;
    }
}
