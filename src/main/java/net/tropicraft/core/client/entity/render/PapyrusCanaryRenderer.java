package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.PapyrusCanaryModel;
import net.tropicraft.core.client.entity.render.state.BirdRenderState;

public class PapyrusCanaryRenderer extends SmallBirdRenderer<PapyrusCanaryModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/papyrus_canary.png");

    public PapyrusCanaryRenderer(EntityRendererProvider.Context context) {
        super(context, new PapyrusCanaryModel(context.bakeLayer(TropicraftRenderLayers.PAPYRUS_CANARY_LAYER)), 0.15f);
    }

    @Override
    public Identifier getTextureLocation(BirdRenderState state) {
        return TEXTURE;
    }
}
