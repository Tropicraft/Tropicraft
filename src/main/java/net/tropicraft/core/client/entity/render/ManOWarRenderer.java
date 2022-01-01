package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.layer.ManOWarGelLayer;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

public class ManOWarRenderer extends MobRenderer<ManOWarEntity, ManOWarModel> {

    public ManOWarRenderer(final EntityRendererProvider.Context context) {
        super(context, new ManOWarModel(context.bakeLayer(TropicraftRenderLayers.MAN_O_WAR_OUTER_LAYER)), 0.35f);
        addLayer(new ManOWarGelLayer(this, new ManOWarModel(context.bakeLayer(TropicraftRenderLayers.MAN_O_WAR_GEL_LAYER))));
    }

    @Override
    public ResourceLocation getTextureLocation(ManOWarEntity entity) {
        return TropicraftRenderUtils.getTextureEntity("manowar");
    }
}
