package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SlenderHarvestMouseModel;
import net.tropicraft.core.common.entity.passive.SlenderHarvestMouseEntity;

public class SlenderHarvestMouseRenderer extends MobRenderer<SlenderHarvestMouseEntity, SlenderHarvestMouseModel<SlenderHarvestMouseEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/slender_harvest_mouse.png");

    public SlenderHarvestMouseRenderer(EntityRendererProvider.Context context) {
        super(context, new SlenderHarvestMouseModel<>(context.bakeLayer(TropicraftRenderLayers.SLENDER_HARVEST_MOUSE_LAYER)), 0.15f);
    }

    @Override
    public ResourceLocation getTextureLocation(SlenderHarvestMouseEntity entity) {
        return TEXTURE;
    }
}
