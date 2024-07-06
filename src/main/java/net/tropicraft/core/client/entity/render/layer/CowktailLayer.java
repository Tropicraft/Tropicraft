package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

public class CowktailLayer<T extends CowktailEntity> extends RenderLayer<T, CowModel<T>> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CowktailLayer(RenderLayerParent<T, CowModel<T>> renderer, BlockRenderDispatcher blockRenderDispatcher) {
        super(renderer);
        this.blockRenderDispatcher = blockRenderDispatcher;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isBaby() || entity.isInvisible()) {
            return;
        }

        CowModel<T> model = getParentModel();
        BlockState blockState = entity.getCowktailType().getRenderState();
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(entity, 0.0f);

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-48.0f));
        renderBlock(poseStack, bufferSource, packedLight, blockState, overlayCoords);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(42.0f));
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-48.0f));
        renderBlock(poseStack, bufferSource, packedLight, blockState, overlayCoords);
        poseStack.popPose();

        poseStack.pushPose();
        model.getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-78.0f));
        renderBlock(poseStack, bufferSource, packedLight, blockState, overlayCoords);
        poseStack.popPose();
    }

    private void renderBlock(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, BlockState blockState, int overlayCoords) {
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(-0.5, -0.5, -0.5);
        blockRenderDispatcher.renderSingleBlock(blockState, poseStack, bufferSource, packedLight, overlayCoords);
    }
}
