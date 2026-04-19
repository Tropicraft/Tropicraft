package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SlenderHarvestMouseModel;
import net.tropicraft.core.common.entity.passive.SlenderHarvestMouseEntity;

public class SlenderHarvestMouseRenderer extends AgeableMobRenderer<SlenderHarvestMouseEntity, LivingEntityRenderState, SlenderHarvestMouseModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/slender_harvest_mouse.png");

    public SlenderHarvestMouseRenderer(EntityRendererProvider.Context context) {
        super(context, new SlenderHarvestMouseModel(context.bakeLayer(TropicraftRenderLayers.SLENDER_HARVEST_MOUSE_LAYER)), new SlenderHarvestMouseModel(context.bakeLayer(TropicraftRenderLayers.SLENDER_HARVEST_MOUSE_BABY_LAYER)), 0.15f);
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
