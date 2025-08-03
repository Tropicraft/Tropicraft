package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.CuberaModel;
import net.tropicraft.core.common.entity.underdasea.CuberaEntity;

public class CuberaRenderer extends MobRenderer<CuberaEntity, LivingEntityRenderState, CuberaModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/cubera.png");

    public CuberaRenderer(EntityRendererProvider.Context context) {
        super(context, new CuberaModel(context.bakeLayer(TropicraftRenderLayers.CUBERA_LAYER)), 0.6f);
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
