package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.ShoebillStorkModel;
import net.tropicraft.core.common.entity.passive.ShoebillStorkEntity;

public class ShoebillStorkRenderer extends MobRenderer<ShoebillStorkEntity, ShoebillStorkModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/shoebill_stork.png");

    public ShoebillStorkRenderer(EntityRendererProvider.Context context) {
        super(context, new ShoebillStorkModel(context.bakeLayer(TropicraftRenderLayers.SHOEBILL_STORK_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(ShoebillStorkEntity entity) {
        return TEXTURE;
    }
}
