package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropicraftFishModel;
import net.tropicraft.core.common.entity.underdasea.TropicraftTropicalFishEntity;

public class TropicraftTropicalFishRenderer extends TropicraftFishRenderer<TropicraftTropicalFishEntity> {
    public TropicraftTropicalFishRenderer(final EntityRendererProvider.Context context) {
        super(context, new TropicraftFishModel<>(context.bakeLayer(TropicraftRenderLayers.TROPICAL_FISH_LAYER)), 0.2f);
    }
}
