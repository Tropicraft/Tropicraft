package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.state.AshenRenderState;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenHeldItemLayer<T extends AshenRenderState, M extends EntityModel<T> & ArmedModel<T>> extends ItemInHandLayer<T, M> {
    private final AshenModel model;

    public AshenHeldItemLayer(RenderLayerParent<T, M> renderer, AshenModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void submit(PoseStack stack, SubmitNodeCollector submitNodeCollector, int lightCoords, T state, float yRot, float xRot) {
        ItemStackRenderState blowGunHand = state.getMainHandItemState();
        ItemStackRenderState daggerHand = state.mainArm == HumanoidArm.RIGHT ? state.leftHandItemState : state.rightHandItemState;

        if (!blowGunHand.isEmpty() || !daggerHand.isEmpty()) {
            stack.pushPose();

            if (state.isBaby) {
                stack.translate(0.0f, 0.625f, 0.0f);
                stack.mulPose(Axis.XN.rotationDegrees(-20));
                stack.scale(0.5f, 0.5f, 0.5f);
            }

            HumanoidArm side = state.mainArm;
            submitHeldItem(state, blowGunHand, side, stack, submitNodeCollector, lightCoords);
            side = side.getOpposite();
            submitHeldItem(state, daggerHand, side, stack, submitNodeCollector, lightCoords);

            stack.popPose();
        }
    }

    private void submitHeldItem(AshenRenderState state, ItemStackRenderState item, HumanoidArm handSide, PoseStack stack, SubmitNodeCollector submitNodeCollector, int combinedLightIn) {
        if (item.isEmpty()) {
            return;
        }

        if (state.actionState == AshenEntity.AshenState.HOSTILE) {
            float scale = 0.5f;
            if (handSide == HumanoidArm.LEFT) {
                stack.pushPose();
                model.leftArm.translateAndRotate(stack);

                stack.translate(0.3f, -0.3f, -0.045f);
                stack.mulPose(Axis.XP.rotationDegrees(180.0f));
                stack.mulPose(Axis.YP.rotationDegrees(180.0f));
                stack.mulPose(Axis.ZP.rotationDegrees(10.0f));

                stack.scale(scale, scale, scale);
                item.submit(stack, submitNodeCollector, combinedLightIn, OverlayTexture.NO_OVERLAY, EntityRenderState.NO_OUTLINE);
                stack.popPose();
            } else {
                stack.pushPose();
                model.rightArm.translateAndRotate(stack);

                stack.translate(-0.375f, -0.35f, -0.125f);
                stack.mulPose(Axis.YP.rotationDegrees(90.0f));
                stack.scale(scale, scale, scale);

                item.submit(stack, submitNodeCollector, combinedLightIn, OverlayTexture.NO_OVERLAY, EntityRenderState.NO_OUTLINE);
                stack.popPose();
            }
        }
    }
}
