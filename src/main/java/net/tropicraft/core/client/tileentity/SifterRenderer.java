package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

public class SifterRenderer implements BlockEntityRenderer<SifterBlockEntity> {
    private ItemEntity item;

    public SifterRenderer(BlockEntityRendererProvider.Context pContext) {

    }

    @Override
    public void render(SifterBlockEntity sifter, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 0.0, 0.5);

        if (!sifter.isSifting()) {
            item = null;
        } else if (!sifter.getSiftItem().isEmpty()) {
            Level level = sifter.getLevel();
            final float itemRenderSize = 0.4375f;

            if (item == null) {
                item = new ItemEntity(EntityType.ITEM, level);
                item.setItem(sifter.getSiftItem().copy());
            }

            matrixStackIn.translate(0.0, 0.4f, 0.0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees((float) (sifter.yaw2 + (sifter.yaw - sifter.yaw2) * (double) partialTicks) * 10.0f));
            matrixStackIn.translate(0.0, -0.4f, 0.0);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-20.0f));
            matrixStackIn.scale(itemRenderSize * 3, itemRenderSize * 3, itemRenderSize * 3);
            int light = LevelRenderer.getLightColor(level, sifter.getBlockPos().above());
            Minecraft.getInstance().getEntityRenderDispatcher().render(item, 0.0, 0.0, 0.0, 0.0f, partialTicks, matrixStackIn, bufferIn, light);
        }

        matrixStackIn.popPose();
    }
}
