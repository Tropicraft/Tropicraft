package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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

    public void render(final PoisonBlotEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        final VertexConsumer buffer = TropicraftRenderUtils.getEntityCutoutBuilder(bufferIn, getTextureLocation(entity));
        buffer.vertex(-.5, -.5, 0).uv(0, 1).endVertex();
        buffer.vertex( .5, -.5, 0).uv(1, 1).endVertex();
        buffer.vertex( .5,  .5, 0).uv(1, 0).endVertex();
        buffer.vertex(-.5,  .5, 0).uv(0, 0).endVertex();
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(PoisonBlotEntity entity) {
        return TropicraftRenderUtils.getTextureEntity("treefrog/blot");
    }
}
