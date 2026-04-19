package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropiSkellyModel;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyRenderer extends HumanoidMobRenderer<TropiSkellyEntity, ZombieRenderState, TropiSkellyModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/tropiskeleton.png");

    public TropiSkellyRenderer(EntityRendererProvider.Context context) {
        super(context, new TropiSkellyModel(context.bakeLayer(TropicraftRenderLayers.TROPI_SKELLY_LAYER)), 0.5f);
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public void extractRenderState(TropiSkellyEntity entity, ZombieRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isAggressive = entity.isAggressive();
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return TEXTURE;
    }
}
