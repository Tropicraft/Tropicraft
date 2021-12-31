package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

@OnlyIn(Dist.CLIENT)
public class AshenHeldItemLayer<T extends AshenEntity, M extends EntityModel<T> & ArmedModel> extends ItemInHandLayer<T, M> {

    private AshenModel model;

    public AshenHeldItemLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    public void setAshenModel(final AshenModel model) {
        this.model = model;
    }

    @Override
    protected ResourceLocation getTextureLocation(AshenEntity entityIn) {
        return TropicraftRenderUtils.getTextureEntity("ashen/ashen");
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, T ashen, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        final ItemStack blowGunHand = ashen.getMainHandItem();
        final ItemStack daggerHand = ashen.getOffhandItem();

        if (!blowGunHand.isEmpty() || !daggerHand.isEmpty()) {
            stack.pushPose();

            if (model.young) {
                stack.translate(0.0F, 0.625F, 0.0F);
                stack.mulPose(Vector3f.XN.rotationDegrees(-20));
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
            float scale = 0.5F;
            if (handSide == HumanoidArm.LEFT) {
                stack.pushPose();
                model.leftArm.translateAndRotate(stack);

                stack.translate(0.3F, -0.30F, -0.045F);
                stack.mulPose(Vector3f.XP.rotationDegrees(180F));
                stack.mulPose(Vector3f.YP.rotationDegrees(180F));
                stack.mulPose(Vector3f.ZP.rotationDegrees(10F));

                stack.scale(scale, scale, scale);
                Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemstack, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false, stack, buffer, entity.level, combinedLightIn, OverlayTexture.NO_OVERLAY, entity.getId());
                stack.popPose();
            } else {
                stack.pushPose();
                model.rightArm.translateAndRotate(stack);
                
                stack.translate(-0.375F, -0.35F, -0.125F);
                stack.mulPose(Vector3f.YP.rotationDegrees(90F));
                stack.scale(scale, scale, scale);
                
                Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemstack, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, false, stack, buffer, entity.level, combinedLightIn, OverlayTexture.NO_OVERLAY, entity.getId());
                stack.popPose();
            }
        }
    }
}
