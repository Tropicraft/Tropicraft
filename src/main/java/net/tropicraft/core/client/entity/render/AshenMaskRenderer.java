package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.render.state.AshenMaskRenderState;
import net.tropicraft.core.common.entity.placeable.AshenMaskEntity;

public class AshenMaskRenderer extends EntityRenderer<AshenMaskEntity, AshenMaskRenderState> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/ashen/mask.png");

    private final TropicraftSpecialRenderHelper mask;

    public AshenMaskRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.5f;
        shadowStrength = 0.5f;
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public AshenMaskRenderState createRenderState() {
        return new AshenMaskRenderState();
    }

    @Override
    public void extractRenderState(AshenMaskEntity entity, AshenMaskRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.maskType = entity.getMaskType();
    }

    @Override
    public void render(AshenMaskRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(90));
        mask.renderMask(poseStack, bufferSource.getBuffer(RenderType.entityCutout(TEXTURE)), state.maskType, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
