package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderItemInFrameEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.tropicraft.core.common.entity.BambooItemFrame;

public class BambooItemFrameRenderer extends ItemFrameRenderer<BambooItemFrame> {
    public static final StateDefinition<Block, BlockState> FAKE_BLOCK_STATE = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR)
            .add(BlockStateProperties.MAP)
            .create(Block::defaultBlockState, BlockState::new);

    private final MapRenderer mapRenderer;
    private final BlockRenderDispatcher blockRenderer;

    public BambooItemFrameRenderer(EntityRendererProvider.Context context) {
        super(context);
        mapRenderer = context.getMapRenderer();
        blockRenderer = context.getBlockRenderDispatcher();
    }

    // Direct copy of super.render() with model replaced
    @Override
    public void render(ItemFrameRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (state.nameTag != null) {
            RenderNameTagEvent.DoRender event = new RenderNameTagEvent.DoRender(state, state.nameTag, this, poseStack, bufferSource, packedLight, state.partialTick);
            if (!NeoForge.EVENT_BUS.post(event).isCanceled()) {
                renderNameTag(state, state.nameTag, poseStack, bufferSource, packedLight);
            }
        }

        poseStack.pushPose();
        Direction direction = state.direction;

        Vec3 offset = getRenderOffset(state);
        poseStack.translate(-offset.x(), -offset.y(), -offset.z());

        double step = 7.5 / 16.0;
        poseStack.translate(direction.getStepX() * step, direction.getStepY() * step, direction.getStepZ() * step);

        float xRot;
        float yRot;
        if (direction.getAxis().isHorizontal()) {
            xRot = 0.0f;
            yRot = 180.0f - direction.toYRot();
        } else {
            xRot = -90.0f * direction.getAxisDirection().getStep();
            yRot = 180.0f;
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        if (!state.isInvisible) {
            BlockState fakeBlockState = FAKE_BLOCK_STATE.any().setValue(BlockStateProperties.MAP, state.mapId != null);
            BlockStateModel model = blockRenderer.getBlockModel(fakeBlockState);
            poseStack.pushPose();
            poseStack.translate(-0.5f, -0.5f, -0.5f);
            ModelBlockRenderer.renderModel(
                    poseStack.last(),
                    bufferSource.getBuffer(RenderType.entitySolidZOffsetForward(TextureAtlas.LOCATION_BLOCKS)),
                    model,
                    1.0f,
                    1.0f,
                    1.0f,
                    packedLight,
                    OverlayTexture.NO_OVERLAY
            );
            poseStack.popPose();
        }

        if (state.isInvisible) {
            poseStack.translate(0.0f, 0.0f, 0.5f);
        } else {
            poseStack.translate(0.0f, 0.0f, 7.0f / 16.0f);
        }

        if (!NeoForge.EVENT_BUS.post(new RenderItemInFrameEvent(state, this, poseStack, bufferSource, packedLight)).isCanceled()) {
            if (state.mapId != null) {
                int rotation = state.rotation % 4 * 2;
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 360.0f / 8.0f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
                float scale = 1.0f / 128.0f;
                poseStack.scale(scale, scale, scale);
                poseStack.translate(-64.0f, -64.0f, 0.0f);
                poseStack.translate(0.0f, 0.0f, -1.0f);
                mapRenderer.render(state.mapRenderState, poseStack, bufferSource, true, packedLight);
            } else if (!state.item.isEmpty()) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(state.rotation * 360.0f / 8.0f));
                poseStack.scale(0.5f, 0.5f, 0.5f);
                state.item.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        poseStack.popPose();
    }
}
