package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.FiddlerCrabModel;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;

public class FiddlerCrabRenderer extends MobRenderer<FiddlerCrabEntity, LivingEntityRenderState, FiddlerCrabModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/fiddler_crab.png");

    public FiddlerCrabRenderer(EntityRendererProvider.Context context) {
        super(context, new FiddlerCrabModel(context.bakeLayer(TropicraftRenderLayers.FIDDLER_CRAB_LAYER)), 0.3f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
