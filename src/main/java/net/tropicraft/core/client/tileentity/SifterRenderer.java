package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

public class SifterRenderer extends TileEntityRenderer<SifterTileEntity> {
	private ItemEntity item;

	public SifterRenderer(final TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(SifterTileEntity sifter, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		final World world = sifter.getWorld();
		matrixStackIn.push();
		matrixStackIn.translate(0.5D, 0.0D, 0.5D);

		if (item == null && !sifter.getSiftItem().isEmpty() && sifter.isSifting()) {
			item = new ItemEntity(EntityType.ITEM, world);
			item.setItem(sifter.getSiftItem().copy());
		}

		if (item != null) {
			final float itemRenderSize = 0.4375F;
			item.setWorld(world);
			matrixStackIn.translate(0.0D, (double)0.4F, 0.0D);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees((float)(sifter.yaw2 + (sifter.yaw - sifter.yaw2) * (double)partialTicks) * 10.0F));
			matrixStackIn.translate(0.0D, (double)-0.4F, 0.0D);
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-20.0F));
			GlStateManager.scalef(itemRenderSize * 3, itemRenderSize * 3, itemRenderSize * 3);
			Minecraft.getInstance().getRenderManager().renderEntityStatic(item, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
		}

		matrixStackIn.pop();
	}
}
