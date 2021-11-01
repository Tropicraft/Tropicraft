package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.entity.model.PiranhaModel;
import net.tropicraft.core.common.entity.underdasea.PiranhaEntity;

public class PiranhaRenderer extends TropicraftFishRenderer<PiranhaEntity, PiranhaModel> {

    public PiranhaRenderer(EntityRendererProvider.Context context) {
        super(context, new PiranhaModel(context.bakeLayer(ClientSetup.PIRANHA_LAYER)), 0.2f);
    }
}
