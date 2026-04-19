package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SpiderMonkeyModel;
import net.tropicraft.core.client.entity.render.state.SpiderMonkeyRenderState;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;

public class SpiderMonkeyRenderer extends MobRenderer<SpiderMonkeyEntity, SpiderMonkeyRenderState, SpiderMonkeyModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/spider_monkey.png");

    public SpiderMonkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderMonkeyModel(context.bakeLayer(TropicraftRenderLayers.SPIDER_MONKEY_LAYER)), 0.4f);
    }

    @Override
    public SpiderMonkeyRenderState createRenderState() {
        return new SpiderMonkeyRenderState();
    }

    @Override
    public void extractRenderState(SpiderMonkeyEntity entity, SpiderMonkeyRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.standAnimation = entity.getStandAnimation(partialTicks);
    }

    @Override
    public Identifier getTextureLocation(SpiderMonkeyRenderState state) {
        return TEXTURE;
    }
}
