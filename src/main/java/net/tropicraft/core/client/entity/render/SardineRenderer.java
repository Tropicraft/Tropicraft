package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.entity.model.SardineModel;
import net.tropicraft.core.common.entity.underdasea.SardineEntity;

public class SardineRenderer extends TropicraftFishRenderer<SardineEntity, SardineModel> {
    public SardineRenderer(EntityRendererProvider.Context context) {
        super(context, new SardineModel(context.bakeLayer(ClientSetup.RIVER_SARDINE_LAYER)), 0.2f);
    }
}
