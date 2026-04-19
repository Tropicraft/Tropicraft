package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.VMonkeyModel;
import net.tropicraft.core.client.entity.render.layer.VMonkeyHeldItemLayer;
import net.tropicraft.core.client.entity.render.state.VMonkeyRenderState;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

public class VMonkeyRenderer extends MobRenderer<VMonkeyEntity, VMonkeyRenderState, VMonkeyModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/monkeytext.png");
    private static final Identifier ANGRY_TEXTURE = Tropicraft.id("textures/entity/monkey_angrytext.png");

    private final ItemModelResolver itemModelResolver;

    public VMonkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new VMonkeyModel(context.bakeLayer(TropicraftRenderLayers.V_MONKEY_LAYER)), 0.5f);
        shadowRadius = 0.3f;
        shadowStrength = 0.5f;
        itemModelResolver = context.getItemModelResolver();
        addLayer(new VMonkeyHeldItemLayer<>(this));
    }

    @Override
    public VMonkeyRenderState createRenderState() {
        return new VMonkeyRenderState();
    }

    @Override
    public void extractRenderState(VMonkeyEntity entity, VMonkeyRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, itemModelResolver, partialTicks);
        state.isOrderedToSit = entity.isOrderedToSit();
        state.isClimbing = entity.isClimbing();
        state.isAggressive = entity.isAggressive();
    }

    @Override
    public Identifier getTextureLocation(VMonkeyRenderState state) {
        return state.isAggressive ? ANGRY_TEXTURE : TEXTURE;
    }
}
