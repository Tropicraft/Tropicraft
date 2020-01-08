package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.EagleRayModel;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

import javax.annotation.Nullable;

public class EagleRayRenderer extends MobRenderer<EagleRayEntity, EagleRayModel> {

    private static final ResourceLocation RAY_TEXTURE_LOC = TropicraftRenderUtils.bindTextureEntity("ray/eagleray");

    public EagleRayRenderer(EntityRendererManager manager) {
        super(manager, new EagleRayModel(), 0.8f);
    }

    @Override
    public void doRender(EagleRayEntity eagleRay, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(eagleRay, x, y - 1.25, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EagleRayEntity eagleRayEntity) {
        return RAY_TEXTURE_LOC;
    }
}
