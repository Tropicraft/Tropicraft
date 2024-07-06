package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SeahorseModel;
import net.tropicraft.core.common.entity.underdasea.SeahorseEntity;

public class SeahorseRenderer extends MobRenderer<SeahorseEntity, SeahorseModel> {
    public SeahorseRenderer(EntityRendererProvider.Context context) {
        super(context, new SeahorseModel(context.bakeLayer(TropicraftRenderLayers.SEAHORSE_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public void render(SeahorseEntity seahorse, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();

        matrixStackIn.translate(0, -1.0f, 0);
        matrixStackIn.scale(0.5f, 0.5f, 0.5f);

        super.render(seahorse, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SeahorseEntity seahorseEntity) {
        return Tropicraft.location("textures/entity/seahorse/" + seahorseEntity.getTexture() + ".png");
    }
}
