package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.passive.FishingBobberEntity;

@OnlyIn(Dist.CLIENT)
public class FishingBobberEntityRenderer extends EntityRenderer<FishingBobberEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);

    public FishingBobberEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FishingBobberEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        EntityKoaBase koa = entity.getAngler();
        if (koa == null) {
            return;
        }

        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = bufferSource.getBuffer(RENDER_TYPE);
        vertex(consumer, pose, packedLight, 0.0F, 0, 0, 1);
        vertex(consumer, pose, packedLight, 1.0F, 0, 1, 1);
        vertex(consumer, pose, packedLight, 1.0F, 1, 1, 0);
        vertex(consumer, pose, packedLight, 0.0F, 1, 0, 0);
        poseStack.popPose();

        float handOffset = koa.getMainArm() == HumanoidArm.RIGHT ? 0.35f : -0.35f;
        if (!koa.getMainHandItem().is(Items.FISHING_ROD)) {
            handOffset = -handOffset;
        }

        float yBodyRot = Mth.lerp(partialTicks, koa.yBodyRotO, koa.yBodyRot) * Mth.DEG_TO_RAD;
        float sin = Mth.sin(yBodyRot);
        float cos = Mth.cos(yBodyRot);
        double rodX = Mth.lerp(partialTicks, koa.xo, koa.getX()) - cos * handOffset - sin * 0.8;
        double rodY = koa.yo + koa.getEyeHeight() + (koa.getY() - koa.yo) * partialTicks - 0.45;
        double rodZ = Mth.lerp(partialTicks, koa.zo, koa.getZ()) - sin * handOffset + cos * 0.8;
        float offset = koa.isCrouching() ? -0.1875F : 0.0F;

        double bobberX = Mth.lerp(partialTicks, entity.xo, entity.getX());
        double bobberY = Mth.lerp(partialTicks, entity.yo, entity.getY()) + 0.25;
        double bobberZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
        float deltaX = (float) (rodX - bobberX);
        float deltaY = (float) (rodY - bobberY) + offset;
        float deltaZ = (float) (rodZ - bobberZ);
        VertexConsumer line = bufferSource.getBuffer(RenderType.lineStrip());
        for (int i = 0; i <= 16; i++) {
            stringVertex(deltaX, deltaY, deltaZ, line, poseStack.last(), i / 16.0f, (i + 1) / 16.0f);
        }

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int p_114715_, float p_114716_, int p_114717_, int p_114718_, int p_114719_) {
        consumer.addVertex(pose, p_114716_ - 0.5F, p_114717_ - 0.5F, 0.0F).setColor(CommonColors.WHITE).setUv((float) p_114718_, (float) p_114719_).setOverlay(OverlayTexture.NO_OVERLAY).setLight(p_114715_).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    private static void stringVertex(float deltaX, float deltaY, float deltaZ, VertexConsumer consumer, PoseStack.Pose pose, float start, float end) {
        float x = deltaX * start;
        float y = deltaY * (start * start + start) * 0.5F + 0.25F;
        float z = deltaZ * start;
        float normalX = deltaX * end - x;
        float normalY = deltaY * (end * end + end) * 0.5F + 0.25F - y;
        float normalZ = deltaZ * end - z;
        float length = Mth.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX = normalX / length;
        normalY = normalY / length;
        normalZ = normalZ / length;
        consumer.addVertex(pose, x, y, z).setColor(CommonColors.BLACK).setNormal(pose, normalX, normalY, normalZ);
    }

    @Override
    public ResourceLocation getTextureLocation(FishingBobberEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
