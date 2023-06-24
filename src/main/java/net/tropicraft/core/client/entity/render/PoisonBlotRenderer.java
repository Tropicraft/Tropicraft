package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class PoisonBlotRenderer extends EntityRenderer<PoisonBlotEntity> {

    public PoisonBlotRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(final PoisonBlotEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        final VertexConsumer buffer = TropicraftRenderUtils.getEntityCutoutBuilder(bufferIn, getTextureLocation(entity));
        final PoseStack.Pose pose = stack.last();
        vertex(pose, buffer, -.5f, -.5f, 0, 1, packedLightIn);
        vertex(pose, buffer, .5f, -.5f, 1, 1, packedLightIn);
        vertex(pose, buffer, .5f, .5f, 1, 0, packedLightIn);
        vertex(pose, buffer, -.5f, .5f, 0, 0, packedLightIn);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float u, float v, int light) {
        buffer.vertex(pose.pose(), x, y, 0f)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(pose.normal(), 0f, 0f, 1f).endVertex();
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(PoisonBlotEntity entity) {
        return TropicraftRenderUtils.getTextureEntity("treefrog/blot");
    }
}
