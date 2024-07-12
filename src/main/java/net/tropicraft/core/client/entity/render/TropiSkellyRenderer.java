package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropiSkellyModel;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyRenderer extends HumanoidMobRenderer<TropiSkellyEntity, TropiSkellyModel> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/tropiskeleton.png");

    public TropiSkellyRenderer(EntityRendererProvider.Context context) {
        super(context, new TropiSkellyModel(context.bakeLayer(TropicraftRenderLayers.TROPI_SKELLY_LAYER)), 0.5F);

        layers.clear();

        addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        addLayer(new ElytraLayer<>(this, context.getModelSet()));           // wtf?
        addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(TropiSkellyEntity entity) {
        return TEXTURE;
    }
}
