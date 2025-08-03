package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.JaguarModel;
import net.tropicraft.core.common.entity.neutral.JaguarEntity;

public class JaguarRenderer extends AgeableMobRenderer<JaguarEntity, LivingEntityRenderState, JaguarModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/jaguar.png");

    public JaguarRenderer(EntityRendererProvider.Context context) {
        super(context, new JaguarModel(context.bakeLayer(TropicraftRenderLayers.JAGUAR_LAYER)), new JaguarModel(context.bakeLayer(TropicraftRenderLayers.JAGUAR_BABY_LAYER)), 0.7f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
