package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.GibnutModel;
import net.tropicraft.core.client.entity.render.state.GibnutRenderState;
import net.tropicraft.core.common.entity.passive.GibnutEntity;

public class GibnutRenderer extends AgeableMobRenderer<GibnutEntity, GibnutRenderState, GibnutModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/gibnut.png");

    public GibnutRenderer(EntityRendererProvider.Context context) {
        super(context, new GibnutModel(context.bakeLayer(TropicraftRenderLayers.GIBNUT_LAYER)), new GibnutModel(context.bakeLayer(TropicraftRenderLayers.GIBNUT_BABY_LAYER)), 0.3f);
    }

    @Override
    public GibnutRenderState createRenderState() {
        return new GibnutRenderState();
    }

    @Override
    public void extractRenderState(GibnutEntity entity, GibnutRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.vibing = entity.isVibing();
    }

    @Override
    public Identifier getTextureLocation(GibnutRenderState state) {
        return TEXTURE;
    }
}
