package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.IguanaModel;
import net.tropicraft.core.client.entity.render.state.IguanaRenderState;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;

public class IguanaRenderer extends MobRenderer<IguanaEntity, IguanaRenderState, IguanaModel> {
    private static final String IGOR = "igor";

    private static final ResourceLocation DEFAULT_TEXTURE = Tropicraft.location("textures/entity/iggy.png");
    private static final ResourceLocation IGOR_TEXTURE = Tropicraft.location("textures/entity/iggy_igor.png");

    public IguanaRenderer(EntityRendererProvider.Context context) {
        super(context, new IguanaModel(context.bakeLayer(TropicraftRenderLayers.IGUANA_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public IguanaRenderState createRenderState() {
        return new IguanaRenderState();
    }

    @Override
    public void extractRenderState(IguanaEntity entity, IguanaRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.igor = entity.getName().getString().equalsIgnoreCase(IGOR);
    }

    @Override
    public ResourceLocation getTextureLocation(IguanaRenderState state) {
        return state.igor ? IGOR_TEXTURE : DEFAULT_TEXTURE;
    }
}
