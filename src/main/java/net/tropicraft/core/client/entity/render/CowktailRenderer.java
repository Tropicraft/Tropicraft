package net.tropicraft.core.client.entity.render;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.render.layer.CowktailLayer;
import net.tropicraft.core.client.entity.render.state.CowktailRenderState;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

public class CowktailRenderer extends MobRenderer<CowktailEntity, CowktailRenderState, CowModel> {
    private static final ResourceLocation IRIS_TEXTURE = Tropicraft.location("textures/entity/cowktail/iris_cowktail.png");
    private static final ResourceLocation ANEMONE_TEXTURE = Tropicraft.location("textures/entity/cowktail/anemone_cowktail.png");

    public CowktailRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel(context.bakeLayer(TropicraftRenderLayers.COWKTAIL_LAYER)), 0.7f);
        addLayer(new CowktailLayer<>(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public CowktailRenderState createRenderState() {
        return new CowktailRenderState();
    }

    @Override
    public void extractRenderState(CowktailEntity entity, CowktailRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.type = entity.getCowktailType();
    }

    @Override
    public ResourceLocation getTextureLocation(CowktailRenderState state) {
        return switch (state.type) {
            case IRIS -> IRIS_TEXTURE;
            case ANEMONE -> ANEMONE_TEXTURE;
        };
    }
}
