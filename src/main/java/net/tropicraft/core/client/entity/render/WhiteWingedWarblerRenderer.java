package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.WhiteWingedWarblerModel;
import net.tropicraft.core.client.entity.render.state.BirdRenderState;

public class WhiteWingedWarblerRenderer extends SmallBirdRenderer<WhiteWingedWarblerModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/white_winged_warbler.png");

    public WhiteWingedWarblerRenderer(EntityRendererProvider.Context context) {
        super(context, new WhiteWingedWarblerModel(context.bakeLayer(TropicraftRenderLayers.WHITE_WINGED_WARBLER_LAYER)), 0.15f);
    }

    @Override
    public Identifier getTextureLocation(BirdRenderState entity) {
        return TEXTURE;
    }
}
