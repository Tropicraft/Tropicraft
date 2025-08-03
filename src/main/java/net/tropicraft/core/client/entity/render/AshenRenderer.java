package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.layer.AshenHeldItemLayer;
import net.tropicraft.core.client.entity.render.layer.AshenMaskLayer;
import net.tropicraft.core.client.entity.render.state.AshenRenderState;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenRenderer extends MobRenderer<AshenEntity, AshenRenderState, AshenModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/ashen/ashen.png");

    private final ItemModelResolver itemModelResolver;

    public AshenRenderer(EntityRendererProvider.Context context) {
        super(context, new AshenModel(context.bakeLayer(TropicraftRenderLayers.ASHEN_LAYER)), 0.5f);
        itemModelResolver = context.getItemModelResolver();

        addLayer(new AshenMaskLayer(this, new AshenModel(context.bakeLayer(TropicraftRenderLayers.ASHEN_LAYER))));
        addLayer(new AshenHeldItemLayer<>(this, model));
        shadowStrength = 0.5f;
        shadowRadius = 0.3f;
    }

    @Override
    public AshenRenderState createRenderState() {
        return new AshenRenderState();
    }

    @Override
    public void extractRenderState(AshenEntity entity, AshenRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, itemModelResolver);
        state.actionState = entity.getActionState();
        // TODO: Shared state ;-; How is this intended to work?
        if (entity.getTarget() != null && entity.closerThan(entity.getTarget(), 5.0) && !entity.swinging) {
            state.swinging = true;
        } else if (entity.swinging && entity.swingTime > 6) {
            state.swinging = false;
        }
        state.hasMask = entity.hasMask();
        state.maskType = entity.getMaskType();
    }

    @Override
    public ResourceLocation getTextureLocation(AshenRenderState state) {
        return TEXTURE;
    }
}
