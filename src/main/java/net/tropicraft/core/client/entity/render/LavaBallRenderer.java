package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.LavaBallModel;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;

public class LavaBallRenderer extends EntityRenderer<LavaBallEntity> {
    public static final ResourceLocation LAVA_BALL_TEXTURE = ResourceLocation.withDefaultNamespace("textures/block" +
            "/lava_flow.png");
    private final EntityModel<LavaBallEntity> model;

    public LavaBallRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new LavaBallModel(context.bakeLayer(TropicraftRenderLayers.LAVA_BALL_LAYER));
    }

    public void render(LavaBallEntity lavaBall, float entityYaw, float partialTicks, PoseStack stack,
                       MultiBufferSource buffer, int packedLightIn) {
        stack.pushPose();
        stack.scale(1.5f, 1.5f, 1.5f);
        stack.translate(0, -0.875, 0);
        stack.mulPose(Axis.YP.rotationDegrees(180 - entityYaw));
        VertexConsumer builder = buffer.getBuffer(model.renderType(LAVA_BALL_TEXTURE));
        model.renderToBuffer(stack, builder, getPackedLightCoords(lavaBall, partialTicks), OverlayTexture.NO_OVERLAY);
        super.render(lavaBall, entityYaw, partialTicks, stack, buffer, packedLightIn);
        stack.popPose();
    }

    //Light level is always max, cuz it's lava!
    @Override
    protected int getBlockLightLevel(LavaBallEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(LavaBallEntity entity) {
        return LAVA_BALL_TEXTURE;
    }
}
