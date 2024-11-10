package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.PapyrusCanaryModel;
import net.tropicraft.core.client.entity.model.PapyrusGonolekModel;
import net.tropicraft.core.common.entity.passive.SmallBirdEntity;

public class PapyrusGonolekRenderer extends MobRenderer<SmallBirdEntity, PapyrusGonolekModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/papyrus_gonolek.png");

    public PapyrusGonolekRenderer(EntityRendererProvider.Context context) {
        super(context, new PapyrusGonolekModel(context.bakeLayer(TropicraftRenderLayers.PAPYRUS_GONOLEK_LAYER)), 0.15f);
    }

    @Override
    public ResourceLocation getTextureLocation(SmallBirdEntity entity) {
        return TEXTURE;
    }
}
