package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.ToucanModel;
import net.tropicraft.core.client.entity.render.state.BirdRenderState;
import net.tropicraft.core.common.entity.passive.ToucanEntity;

public class ToucanRenderer extends MobRenderer<ToucanEntity, BirdRenderState, ToucanModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/toucan.png");

    public ToucanRenderer(EntityRendererProvider.Context context) {
        super(context, new ToucanModel(context.bakeLayer(TropicraftRenderLayers.TOUCAN_LAYER)), 0.2f);
    }

    @Override
    public BirdRenderState createRenderState() {
        return new BirdRenderState();
    }

    @Override
    public void extractRenderState(ToucanEntity entity, BirdRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.flightAnimation = entity.getFlightAnimation(partialTicks);
    }

    @Override
    public Identifier getTextureLocation(BirdRenderState entity) {
        return TEXTURE;
    }
}
