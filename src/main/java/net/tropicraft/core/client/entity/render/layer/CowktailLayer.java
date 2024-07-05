package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

public class CowktailLayer<T extends CowktailEntity> extends RenderLayer<T, CowModel<T>> {
    public CowktailLayer(RenderLayerParent<T, CowModel<T>> rendererIn) {
        super(rendererIn);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isBaby() && !entitylivingbaseIn.isInvisible()) {
            BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
            BlockState blockState = entitylivingbaseIn.getCowktailType().getRenderState();
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0f);
            stack.pushPose();
            stack.translate(0.2f, -0.35f, 0.5);
            stack.mulPose(Axis.YP.rotationDegrees(-48.0f));
            stack.scale(-1.0f, -1.0f, 1.0f);
            stack.translate(-0.5, -0.5, -0.5);
            renderer.renderSingleBlock(blockState, stack, bufferIn, packedLightIn, overlayCoords);
            stack.popPose();
            stack.pushPose();
            stack.translate(0.2f, -0.35f, 0.5);
            stack.mulPose(Axis.YP.rotationDegrees(42.0f));
            stack.translate(0.1f, 0.0, -0.6f);
            stack.mulPose(Axis.YP.rotationDegrees(-48.0f));
            stack.scale(-1.0f, -1.0f, 1.0f);
            stack.translate(-0.5, -0.5, -0.5);
            renderer.renderSingleBlock(blockState, stack, bufferIn, packedLightIn, overlayCoords);
            stack.popPose();
            stack.pushPose();
            getParentModel().getHead().translateAndRotate(stack);
            stack.translate(0.0, -0.7f, -0.2f);
            stack.mulPose(Axis.YP.rotationDegrees(-78.0f));
            stack.scale(-1.0f, -1.0f, 1.0f);
            stack.translate(-0.5, -0.5, -0.5);
            renderer.renderSingleBlock(blockState, stack, bufferIn, packedLightIn, overlayCoords);
            stack.popPose();
        }
    }
}
