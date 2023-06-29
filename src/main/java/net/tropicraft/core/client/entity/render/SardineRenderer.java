package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropicraftFishModel;
import net.tropicraft.core.common.entity.underdasea.SardineEntity;

public class SardineRenderer extends TropicraftFishRenderer<SardineEntity> {
    public SardineRenderer(EntityRendererProvider.Context context) {
        super(context, new TropicraftFishModel<>(context.bakeLayer(TropicraftRenderLayers.RIVER_SARDINE_LAYER)), 0.2f);
    }
}
