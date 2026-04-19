package net.tropicraft.core.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;
import org.jspecify.annotations.Nullable;

public class SifterBlockEntityRenderer implements BlockEntityRenderer<SifterBlockEntity, SifterBlockEntityRenderer.RenderState> {
    public static final float ITEM_SCALE = 1.3125f;

    private final ItemModelResolver itemModelResolver;
    private final RandomSource random = RandomSource.create();

    public SifterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(SifterBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        ItemStack item = blockEntity.getSiftItem();
        if (blockEntity.isSifting() && !item.isEmpty()) {
            itemModelResolver.updateForTopItem(state.clusterState.item, item, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
            state.clusterState.count = ItemClusterRenderState.getRenderedAmount(item.getCount());
            state.clusterState.seed = ItemClusterRenderState.getSeedForItemStack(item);
            state.yRot = (float) Mth.rotLerp(partialTicks, blockEntity.yaw2, blockEntity.yaw);
        } else {
            state.clusterState.item.clear();
            state.yRot = 0.0f;
        }
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.clusterState.item.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.2, 0.5);

        poseStack.rotateAround(Axis.YP.rotationDegrees(state.yRot), 0.0f, -0.4f, 0.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-20.0f));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

        ItemEntityRenderer.renderMultipleFromCount(poseStack, submitNodeCollector, state.lightCoords, state.clusterState, random);

        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public final ItemClusterRenderState clusterState = new ItemClusterRenderState();
        public float yRot;
    }
}
