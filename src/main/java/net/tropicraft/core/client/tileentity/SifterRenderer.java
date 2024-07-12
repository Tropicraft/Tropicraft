package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

public class SifterRenderer implements BlockEntityRenderer<SifterBlockEntity> {
    public static final float ITEM_SCALE = 1.3125f;

    private final ItemRenderer itemRenderer;

    public SifterRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SifterBlockEntity sifter, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
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

        ItemEntityRenderer.renderMultipleFromCount(itemRenderer, poseStack, bufferSource, packedLight, item, level.random, level);

        poseStack.popPose();
    }
}
