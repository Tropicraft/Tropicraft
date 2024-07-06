package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.layer.AshenHeldItemLayer;
import net.tropicraft.core.client.entity.render.layer.AshenMaskLayer;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenRenderer extends MobRenderer<AshenEntity, AshenModel> {

    private static final ResourceLocation ASHEN_TEXTURE_LOCATION = Tropicraft.location("textures/entity/ashen/ashen.png");

    public AshenRenderer(EntityRendererProvider.Context context) {
        super(context, new AshenModel(context.bakeLayer(TropicraftRenderLayers.ASHEN_LAYER)), 0.5f);

        addLayer(new AshenMaskLayer(this, new AshenModel(context.bakeLayer(TropicraftRenderLayers.ASHEN_LAYER))));
        addLayer(new AshenHeldItemLayer<>(this, context.getItemInHandRenderer(), model));
        shadowStrength = 0.5f;
        shadowRadius = 0.3f;
    }

    @Override
    public void render(AshenEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.getTarget() != null && entity.distanceTo(entity.getTarget()) < 5.0f && !entity.swinging) {
            model.swinging = true;
        } else {
            if (entity.swinging && entity.swingTime > 6) {
                model.swinging = false;
            }
        }
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(AshenEntity entity) {
        return ASHEN_TEXTURE_LOCATION;
    }
}
