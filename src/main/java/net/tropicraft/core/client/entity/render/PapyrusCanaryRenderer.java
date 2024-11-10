package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.PapyrusCanaryModel;
import net.tropicraft.core.common.entity.passive.SmallBirdEntity;

public class PapyrusCanaryRenderer extends MobRenderer<SmallBirdEntity, PapyrusCanaryModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/papyrus_canary.png");

    public PapyrusCanaryRenderer(EntityRendererProvider.Context context) {
        super(context, new PapyrusCanaryModel(context.bakeLayer(TropicraftRenderLayers.PAPYRUS_CANARY_LAYER)), 0.15f);
    }

    @Override
    public ResourceLocation getTextureLocation(SmallBirdEntity entity) {
        return TEXTURE;
    }
}
