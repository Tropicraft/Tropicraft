package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.FailgullModel;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

@OnlyIn(Dist.CLIENT)
public class FailgullRenderer extends MobRenderer<FailgullEntity, FailgullModel> {
    private static final ResourceLocation FAILGULL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "textures/entity/failgull.png");

    public FailgullRenderer(EntityRendererProvider.Context context) {
        super(context, new FailgullModel(context.bakeLayer(TropicraftRenderLayers.FAILGULL_LAYER)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(FailgullEntity e) {
        return FAILGULL_TEXTURE;
    }
}
