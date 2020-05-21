package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class AshenHeldItemLayer<T extends AshenEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {

    private AshenModel model;

    public AshenHeldItemLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    public void setAshenModel(final AshenModel model) {
        this.model = model;
    }

    @Override
    protected ResourceLocation getEntityTexture(AshenEntity entityIn) {
        return TropicraftRenderUtils.getTextureEntity("ashen");
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, T ashen, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        final ItemStack blowGunHand = ashen.getHeldItemMainhand();
        final ItemStack daggerHand = ashen.getHeldItemOffhand();

        if (!blowGunHand.isEmpty() || !daggerHand.isEmpty()) {
            stack.push();

            if (model.isChild) {
                stack.translate(0.0F, 0.625F, 0.0F);
                stack.rotate(Vector3f.XN.rotationDegrees(-20));
                stack.scale(0.5f, 0.5f, 0.5f);
            }

            renderHeldItem(ashen, daggerHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, stack, buffer, packedLightIn);
            renderHeldItem(ashen, blowGunHand, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, stack, buffer, packedLightIn);

            stack.pop();
        }
    }

    private void renderHeldItem(AshenEntity entity, ItemStack itemstack, ItemCameraTransforms.TransformType transformType, HandSide handSide, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn) {
        if (itemstack.isEmpty()) {
            return;
        }

        if (entity.getActionState() == AshenEntity.AshenState.HOSTILE) {
            float scale = 0.5F;
            if (handSide == HandSide.LEFT) {
                stack.push();
                model.leftArm.render(stack, TropicraftRenderUtils.getEntityCutoutBuilder(buffer, getEntityTexture(entity)), combinedLightIn, OverlayTexture.NO_OVERLAY);
                stack.translate(-0.375F, -0.35F, -0.125F);
                stack.rotate(Vector3f.YP.rotationDegrees(90F));
                stack.scale(scale, scale, scale);
                Minecraft.getInstance().getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false, stack, buffer, entity.world, combinedLightIn, OverlayTexture.NO_OVERLAY);
                stack.pop();
            } else {
                stack.push();
                model.rightArm.render(stack, TropicraftRenderUtils.getEntityCutoutBuilder(buffer, getEntityTexture(entity)), combinedLightIn, OverlayTexture.NO_OVERLAY);

                GL11.glTranslatef(0.3F, -0.30F, -0.045F);
                stack.rotate(Vector3f.XP.rotationDegrees(180F));
                stack.rotate(Vector3f.YP.rotationDegrees(180F));
                stack.rotate(Vector3f.ZP.rotationDegrees(10F));

                stack.scale(scale, scale, scale);
                Minecraft.getInstance().getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, false, stack, buffer, entity.world, combinedLightIn, OverlayTexture.NO_OVERLAY);
                stack.pop();
            }
        }
    }
}
