package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

public class SifterRenderer extends TileEntityRenderer<SifterTileEntity> {
	private ItemEntity item;

	public SifterRenderer(final TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(SifterTileEntity sifter, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0.5D, 0.0D, 0.5D);

		if (!sifter.isSifting()) {
			item = null;
		} else if (!sifter.getSiftItem().isEmpty()) {
			final World world = sifter.getLevel();
			final float itemRenderSize = 0.4375F;

			if (item == null) {
				item = new ItemEntity(EntityType.ITEM, world);
				item.setItem(sifter.getSiftItem().copy());
				item.setLevel(world);
			}

			matrixStackIn.translate(0.0D, 0.4F, 0.0D);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees((float)(sifter.yaw2 + (sifter.yaw - sifter.yaw2) * (double)partialTicks) * 10.0F));
			matrixStackIn.translate(0.0D, -0.4F, 0.0D);
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-20.0F));
			matrixStackIn.scale(itemRenderSize * 3, itemRenderSize * 3, itemRenderSize * 3);
			final int light = WorldRenderer.getLightColor(world, sifter.getBlockPos().above());
			Minecraft.getInstance().getEntityRenderDispatcher().render(item, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, light);
		}

		matrixStackIn.popPose();
	}
}
