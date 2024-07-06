package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenHeldItemLayer<T extends AshenEntity, M extends EntityModel<T> & ArmedModel> extends ItemInHandLayer<T, M> {
    private final AshenModel model;

    public AshenHeldItemLayer(RenderLayerParent<T, M> renderer, ItemInHandRenderer itemInHandRenderer, AshenModel model) {
        super(renderer, itemInHandRenderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, T ashen, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack blowGunHand = ashen.getMainHandItem();
        ItemStack daggerHand = ashen.getOffhandItem();

        if (!blowGunHand.isEmpty() || !daggerHand.isEmpty()) {
            stack.pushPose();

            if (model.young) {
                stack.translate(0.0f, 0.625f, 0.0f);
                stack.mulPose(Axis.XN.rotationDegrees(-20));
                stack.scale(0.5f, 0.5f, 0.5f);
            }

            HumanoidArm side = ashen.getMainArm();
            renderHeldItem(ashen, blowGunHand, side, stack, buffer, packedLightIn);
            side = side.getOpposite();
            renderHeldItem(ashen, daggerHand, side, stack, buffer, packedLightIn);

            stack.popPose();
        }
    }

    private void renderHeldItem(AshenEntity entity, ItemStack itemstack, HumanoidArm handSide, PoseStack stack, MultiBufferSource buffer, int combinedLightIn) {
        if (itemstack.isEmpty()) {
            return;
        }

        if (entity.getActionState() == AshenEntity.AshenState.HOSTILE) {
            float scale = 0.5f;
            if (handSide == HumanoidArm.LEFT) {
                stack.pushPose();
                model.leftArm.translateAndRotate(stack);

                stack.translate(0.3f, -0.3f, -0.045f);
                stack.mulPose(Axis.XP.rotationDegrees(180.0f));
                stack.mulPose(Axis.YP.rotationDegrees(180.0f));
                stack.mulPose(Axis.ZP.rotationDegrees(10.0f));

                stack.scale(scale, scale, scale);
                Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, stack, buffer, entity.level(), combinedLightIn, OverlayTexture.NO_OVERLAY, entity.getId());
                stack.popPose();
            } else {
                stack.pushPose();
                model.rightArm.translateAndRotate(stack);

                stack.translate(-0.375f, -0.35f, -0.125f);
                stack.mulPose(Axis.YP.rotationDegrees(90.0f));
                stack.scale(scale, scale, scale);

                Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, false, stack, buffer, entity.level(), combinedLightIn, OverlayTexture.NO_OVERLAY, entity.getId());
                stack.popPose();
            }
        }
    }
}
