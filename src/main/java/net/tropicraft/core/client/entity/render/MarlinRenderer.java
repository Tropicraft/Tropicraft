package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.MarlinModel;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

@OnlyIn(Dist.CLIENT)
public class MarlinRenderer extends MobRenderer<MarlinEntity, MarlinModel> {
    public MarlinRenderer(EntityRendererProvider.Context context) {
        super(context, new MarlinModel(context.bakeLayer(TropicraftRenderLayers.MARLIN_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(MarlinEntity marlin) {
        return Tropicraft.location("textures/entity/" + marlin.getTexture() + ".png");
    }
}
