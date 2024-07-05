package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.TropiBeeModel;
import net.tropicraft.core.client.entity.render.layer.SunglassesLayer;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class TropiBeeRenderer extends MobRenderer<TropiBeeEntity, TropiBeeModel> {

    public TropiBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new TropiBeeModel(context.bakeLayer(TropicraftRenderLayers.TROPI_BEE_LAYER)), 0.4f);

        addLayer(new SunglassesLayer<>(this, entity -> true, (poseStack, entity, model) -> {
            model.body().translateAndRotate(poseStack);
            if (!entity.isBaby()) {
                poseStack.translate(0.03125f, 1.350f, -0.313f);
            } else {
                poseStack.translate(0.025f, 1.450f, -0.163f);
                poseStack.scale(0.55f, 0.55f, 0.55f);
            }
        }));
    }

    @Override
    public ResourceLocation getTextureLocation(TropiBeeEntity bee) {
        if (bee.hasNectar()) {
            return TropicraftRenderUtils.getTextureEntity("tropibee_nectar");
        }
        return TropicraftRenderUtils.getTextureEntity("tropibee");
    }
}
