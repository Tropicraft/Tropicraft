package net.tropicraft.core.client.entity.render;

import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.render.layer.CowktailLayer;
import net.tropicraft.core.client.entity.render.state.CowktailRenderState;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

public class CowktailRenderer extends AgeableMobRenderer<CowktailEntity, CowktailRenderState, CowModel> {
    private static final Identifier IRIS_TEXTURE = Tropicraft.id("textures/entity/cowktail/iris_cowktail.png");
    private static final Identifier ANEMONE_TEXTURE = Tropicraft.id("textures/entity/cowktail/anemone_cowktail.png");

    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelResolver blockModelResolver;

    public CowktailRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel(context.bakeLayer(TropicraftRenderLayers.COWKTAIL_LAYER)), new CowModel(context.bakeLayer(TropicraftRenderLayers.COWKTAIL_BABY_LAYER)), 0.7f);
        addLayer(new CowktailLayer<>(this));
        blockModelResolver = context.getBlockModelResolver();
    }

    @Override
    public CowktailRenderState createRenderState() {
        return new CowktailRenderState();
    }

    @Override
    public void extractRenderState(CowktailEntity entity, CowktailRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.type = entity.getCowktailType();
        blockModelResolver.update(state.flowerModel, entity.getCowktailType().getRenderState(), BLOCK_DISPLAY_CONTEXT);
    }

    @Override
    public Identifier getTextureLocation(CowktailRenderState state) {
        return switch (state.type) {
            case IRIS -> IRIS_TEXTURE;
            case ANEMONE -> ANEMONE_TEXTURE;
        };
    }
}
