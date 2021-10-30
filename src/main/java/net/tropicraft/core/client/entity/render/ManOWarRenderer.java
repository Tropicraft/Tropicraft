package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.layer.ManOWarGelLayer;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

public class ManOWarRenderer extends MobRenderer<ManOWarEntity, ManOWarModel> {

    public ManOWarRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ManOWarModel(32, 20, true), 0.35f);
        addLayer(new ManOWarGelLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ManOWarEntity entity) {
        return TropicraftRenderUtils.getTextureEntity("manowar");
    }
}
