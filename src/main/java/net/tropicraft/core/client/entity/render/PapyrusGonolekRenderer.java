package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.PapyrusGonolekModel;
import net.tropicraft.core.client.entity.render.state.BirdRenderState;

public class PapyrusGonolekRenderer extends SmallBirdRenderer<PapyrusGonolekModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/papyrus_gonolek.png");

    public PapyrusGonolekRenderer(EntityRendererProvider.Context context) {
        super(context, new PapyrusGonolekModel(context.bakeLayer(TropicraftRenderLayers.PAPYRUS_GONOLEK_LAYER)), 0.15f);
    }

    @Override
    public Identifier getTextureLocation(BirdRenderState state) {
        return TEXTURE;
    }
}
