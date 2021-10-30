package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.EagleRayModel;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

import javax.annotation.Nullable;

public class EagleRayRenderer extends MobRenderer<EagleRayEntity, EagleRayModel> {

    public static final ResourceLocation RAY_TEXTURE_LOC = TropicraftRenderUtils.bindTextureEntity("ray/eagleray");

    public EagleRayRenderer(EntityRendererManager manager) {
        super(manager, new EagleRayModel(), 0.8f);
    }

    @Override
    public void render(EagleRayEntity eagleRay, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        // TODO still needed?
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
