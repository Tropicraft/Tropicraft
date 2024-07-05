package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ManateeModel;
import net.tropicraft.core.common.entity.underdasea.ManateeEntity;

public class ManateeRenderer extends MobRenderer<ManateeEntity, ManateeModel> {

    public static final ResourceLocation TEXTURE = TropicraftRenderUtils.getTextureEntity("manatee");

    public ManateeRenderer(EntityRendererProvider.Context context) {
        super(context, new ManateeModel(context.bakeLayer(TropicraftRenderLayers.MANATEE_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ManateeEntity pEntity) {
        return TEXTURE;
    }
}
