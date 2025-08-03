package net.tropicraft.core.client.entity.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.tropicraft.core.client.entity.render.state.BirdRenderState;
import net.tropicraft.core.common.entity.passive.SmallBirdEntity;

public abstract class SmallBirdRenderer<M extends EntityModel<BirdRenderState>> extends MobRenderer<SmallBirdEntity, BirdRenderState, M> {
    public SmallBirdRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public BirdRenderState createRenderState() {
        return new BirdRenderState();
    }

    @Override
    public void extractRenderState(SmallBirdEntity entity, BirdRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.flightAnimation = entity.getFlightAnimation(partialTicks);
    }
}
