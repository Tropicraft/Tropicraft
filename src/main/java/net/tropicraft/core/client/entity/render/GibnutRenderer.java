package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.GibnutModel;
import net.tropicraft.core.common.entity.passive.GibnutEntity;

public class GibnutRenderer extends MobRenderer<GibnutEntity, GibnutModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/gibnut.png");

    public GibnutRenderer(final EntityRendererProvider.Context context) {
        super(context, new GibnutModel(context.bakeLayer(TropicraftRenderLayers.GIBNUT_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(GibnutEntity entity) {
        return TEXTURE;
    }
}
