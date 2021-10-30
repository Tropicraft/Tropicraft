package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.TropiSkellyModel;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyRenderer extends HumanoidMobRenderer<TropiSkellyEntity, TropiSkellyModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/tropiskeleton.png");

    public TropiSkellyRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager, new TropiSkellyModel(), 0.5F);

        layers.clear();

        addLayer(new CustomHeadLayer<>(this));
        addLayer(new ElytraLayer<>(this));
        addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TropiSkellyEntity entity) {
        return TEXTURE;
    }
}
