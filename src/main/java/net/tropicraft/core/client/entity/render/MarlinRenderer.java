package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.MarlinModel;
import net.tropicraft.core.client.entity.render.state.MarlinRenderState;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

public class MarlinRenderer extends MobRenderer<MarlinEntity, MarlinRenderState, MarlinModel> {
    public MarlinRenderer(EntityRendererProvider.Context context) {
        super(context, new MarlinModel(context.bakeLayer(TropicraftRenderLayers.MARLIN_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public MarlinRenderState createRenderState() {
        return new MarlinRenderState();
    }

    @Override
    public void extractRenderState(MarlinEntity entity, MarlinRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.texture = entity.getTexture();
    }

    @Override
    public ResourceLocation getTextureLocation(MarlinRenderState state) {
        return Tropicraft.location("textures/entity/" + state.texture + ".png");
    }
}
