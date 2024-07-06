package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.FiddlerCrabModel;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;

public class FiddlerCrabRenderer extends MobRenderer<FiddlerCrabEntity, FiddlerCrabModel<FiddlerCrabEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "textures/entity/fiddler_crab.png");

    public FiddlerCrabRenderer(EntityRendererProvider.Context context) {
        super(context, new FiddlerCrabModel<>(context.bakeLayer(TropicraftRenderLayers.FIDDLER_CRAB_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(FiddlerCrabEntity entity) {
        return TEXTURE;
    }
}
