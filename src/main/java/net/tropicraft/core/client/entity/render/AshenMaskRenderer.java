package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.common.entity.placeable.AshenMaskEntity;

public class AshenMaskRenderer extends EntityRenderer<AshenMaskEntity> {
    private static final ResourceLocation TEXTURE = TropicraftRenderUtils.getTextureEntity("ashen/mask");

    private final TropicraftSpecialRenderHelper mask;

    public AshenMaskRenderer(final EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.5F;
        shadowStrength = 0.5f;
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public ResourceLocation getTextureLocation(final AshenMaskEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(AshenMaskEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLightIn) {
        stack.pushPose();
        stack.mulPose(Axis.XN.rotationDegrees(90));
        mask.renderMask(stack, buffer.getBuffer(RenderType.entityCutout(getTextureLocation(entity))), entity.getMaskType(), packedLightIn, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }
}
