package net.tropicraft.core.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

public class SifterBlockEntityRenderer implements BlockEntityRenderer<SifterBlockEntity> {
    public static final float ITEM_SCALE = 1.3125f;

    private final ItemModelResolver itemModelResolver;
    private final RandomSource random = RandomSource.create();
    private final ItemClusterRenderState clusterState = new ItemClusterRenderState();

    public SifterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public void render(SifterBlockEntity sifter, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
        Level level = sifter.getLevel();
        ItemStack item = sifter.getSiftItem();

        if (!sifter.isSifting() || item.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.2, 0.5);

        poseStack.rotateAround(Axis.YP.rotationDegrees((float) Mth.rotLerp(partialTicks, sifter.yaw2, sifter.yaw)), 0.0f, -0.4f, 0.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-20.0f));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

        itemModelResolver.updateForTopItem(clusterState.item, item, ItemDisplayContext.GROUND, level, null, 0);
        clusterState.count = ItemClusterRenderState.getRenderedAmount(item.getCount());
        clusterState.seed = ItemClusterRenderState.getSeedForItemStack(item);
        ItemEntityRenderer.renderMultipleFromCount(poseStack, bufferSource, packedLight, clusterState, random);

        poseStack.popPose();
    }
}
