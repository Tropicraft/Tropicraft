package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.FailgullModel;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

public class FailgullRenderer extends MobRenderer<FailgullEntity, LivingEntityRenderState, FailgullModel> {
    private static final Identifier FAILGULL_TEXTURE = Tropicraft.id("textures/entity/failgull.png");

    public FailgullRenderer(EntityRendererProvider.Context context) {
        super(context, new FailgullModel(context.bakeLayer(TropicraftRenderLayers.FAILGULL_LAYER)), 0.25f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return FAILGULL_TEXTURE;
    }
}
