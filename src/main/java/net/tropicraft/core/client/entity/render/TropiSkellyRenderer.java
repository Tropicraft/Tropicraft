package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.TropiSkellyModel;
import net.tropicraft.core.client.entity.render.layer.TropiSkellyHeldItemLayer;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyRenderer extends BipedRenderer<TropiSkellyEntity, TropiSkellyModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/tropiskeleton.png");

    public TropiSkellyRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new TropiSkellyModel(), 0.5F);

        layerRenderers.clear();

        addLayer(new HeadLayer<>(this));
        addLayer(new ElytraLayer<>(this));
        addLayer(new TropiSkellyHeldItemLayer<>(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(TropiSkellyEntity entity) {
        return TEXTURE;
    }
}
