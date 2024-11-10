package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.WhiteWingedWarblerModel;
import net.tropicraft.core.common.entity.passive.SmallBirdEntity;

public class WhiteWingedWarblerRenderer extends MobRenderer<SmallBirdEntity, WhiteWingedWarblerModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/white_winged_warbler.png");

    public WhiteWingedWarblerRenderer(EntityRendererProvider.Context context) {
        super(context, new WhiteWingedWarblerModel(context.bakeLayer(TropicraftRenderLayers.WHITE_WINGED_WARBLER_LAYER)), 0.15f);
    }

    @Override
    public ResourceLocation getTextureLocation(SmallBirdEntity entity) {
        return TEXTURE;
    }
}
