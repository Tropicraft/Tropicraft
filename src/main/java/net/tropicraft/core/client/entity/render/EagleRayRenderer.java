package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.EagleRayModel;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

import javax.annotation.Nullable;

public class EagleRayRenderer extends MobRenderer<EagleRayEntity, EagleRayModel> {

    public static final ResourceLocation RAY_TEXTURE_LOC = TropicraftRenderUtils.bindTextureEntity("ray/eagleray");

    public EagleRayRenderer(final EntityRendererProvider.Context context) {
        super(context, new EagleRayModel(context.bakeLayer(TropicraftRenderLayers.EAGLE_RAY_LAYER)), 0.8f);
    }

    @Override
    public void render(EagleRayEntity eagleRay, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, -1.25, 0);
        super.render(eagleRay, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(EagleRayEntity eagleRayEntity) {
        return RAY_TEXTURE_LOC;
    }
}
