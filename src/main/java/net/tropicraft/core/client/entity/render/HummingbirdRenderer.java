package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.HummingbirdModel;
import net.tropicraft.core.common.entity.passive.HummingbirdEntity;

public class HummingbirdRenderer extends MobRenderer<HummingbirdEntity, LivingEntityRenderState, HummingbirdModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/hummingbird.png");

    public HummingbirdRenderer(EntityRendererProvider.Context context) {
        super(context, new HummingbirdModel(context.bakeLayer(TropicraftRenderLayers.HUMMINGBIRD_LAYER)), 0.2f);
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
