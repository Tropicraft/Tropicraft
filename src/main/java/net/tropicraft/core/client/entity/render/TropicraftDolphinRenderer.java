package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropicraftDolphinModel;
import net.tropicraft.core.client.entity.render.state.TropicraftDolphinRenderState;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;

public class TropicraftDolphinRenderer extends MobRenderer<TropicraftDolphinEntity, TropicraftDolphinRenderState, TropicraftDolphinModel> {
    public TropicraftDolphinRenderer(EntityRendererProvider.Context context) {
        super(context, new TropicraftDolphinModel(context.bakeLayer(TropicraftRenderLayers.DOLPHIN_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public TropicraftDolphinRenderState createRenderState() {
        return new TropicraftDolphinRenderState();
    }

    @Override
    public void extractRenderState(TropicraftDolphinEntity entity, TropicraftDolphinRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isMouthOpen = entity.getMouthOpen();
        state.onGround = entity.onGround();
        state.hasAirSupply = entity.getAirSupply() > 0;
        state.texture = entity.getTexture();
    }

    @Override
    public ResourceLocation getTextureLocation(TropicraftDolphinRenderState state) {
        return Tropicraft.location("textures/entity/" + state.texture + ".png");
    }
}
