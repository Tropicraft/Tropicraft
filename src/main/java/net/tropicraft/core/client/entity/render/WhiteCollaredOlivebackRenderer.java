package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.WhiteCollaredOlivebackModel;
import net.tropicraft.core.client.entity.render.state.BirdRenderState;

public class WhiteCollaredOlivebackRenderer extends SmallBirdRenderer<WhiteCollaredOlivebackModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/white_collared_oliveback.png");

    public WhiteCollaredOlivebackRenderer(EntityRendererProvider.Context context) {
        super(context, new WhiteCollaredOlivebackModel(context.bakeLayer(TropicraftRenderLayers.WHITE_COLLARED_OLIVEBACK_LAYER)), 0.15f);
    }

    @Override
    public Identifier getTextureLocation(BirdRenderState entity) {
        return TEXTURE;
    }
}
