package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SeahorseModel;
import net.tropicraft.core.client.entity.render.state.SeahorseRenderState;
import net.tropicraft.core.common.entity.underdasea.SeahorseEntity;

public class SeahorseRenderer extends MobRenderer<SeahorseEntity, SeahorseRenderState, SeahorseModel> {
    public SeahorseRenderer(EntityRendererProvider.Context context) {
        super(context, new SeahorseModel(context.bakeLayer(TropicraftRenderLayers.SEAHORSE_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public SeahorseRenderState createRenderState() {
        return new SeahorseRenderState();
    }

    @Override
    public void extractRenderState(SeahorseEntity entity, SeahorseRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.texture = entity.getTexture();
    }

    @Override
    public ResourceLocation getTextureLocation(SeahorseRenderState state) {
        return Tropicraft.location("textures/entity/seahorse/" + state.texture + ".png");
    }
}
