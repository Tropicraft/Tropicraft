package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.client.entity.render.state.LavaBallRenderState;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;

import java.util.List;

public class LavaBallRenderer extends EntityRenderer<LavaBallEntity, LavaBallRenderState> {
    private final BlockRenderDispatcher dispatcher;

    public LavaBallRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.shadowRadius = 0.5F;
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    public boolean shouldRender(LavaBallEntity p_362415_, Frustum p_364047_, double p_362218_, double p_363427_, double p_361722_) {
        return super.shouldRender(p_362415_, p_364047_, p_362218_, p_363427_, p_361722_);
    }

    public void render(LavaBallRenderState state, PoseStack poseStack, MultiBufferSource p_114638_, int p_114639_) {
        BlockState blockstate = Blocks.MAGMA_BLOCK.defaultBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            poseStack.pushPose();
            poseStack.translate(0, 0.5, -0);
            List<BlockModelPart> list = this.dispatcher
                    .getBlockModel(blockstate)
                    .collectParts(state.level, state.blockPos, blockstate, RandomSource.create(0L));
            poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
            poseStack.translate(-0.5, -0.5, -0.5);

            this.dispatcher
                    .getModelRenderer()
                    .tesselateBlock(
                            state,
                            list,
                            blockstate,
                            state.blockPos,
                            poseStack,
                            renderType -> p_114638_.getBuffer(net.neoforged.neoforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType)),
                            false,
                            OverlayTexture.NO_OVERLAY
                    );
            poseStack.popPose();
            super.render(state, poseStack, p_114638_, p_114639_);
        }
    }


    public LavaBallRenderState createRenderState() {
        return new LavaBallRenderState();
    }

    public void extractRenderState(LavaBallEntity p_364559_, LavaBallRenderState p_360509_, float p_361019_) {
        super.extractRenderState(p_364559_, p_360509_, p_361019_);
        BlockPos blockpos = BlockPos.containing(p_364559_.getX(), p_364559_.getBoundingBox().maxY, p_364559_.getZ());
        //p_360509_.startBlockPos = p_364559_.getStartPos();
        p_360509_.blockPos = blockpos;
        //p_360509_.blockState = p_364559_.getBlockState();
        p_360509_.biome = p_364559_.level().getBiome(blockpos);
        p_360509_.level = p_364559_.level();
        p_360509_.xRot = p_364559_.getXRot(p_361019_);
        p_360509_.yRot = p_364559_.getYRot(p_361019_);
    }
}