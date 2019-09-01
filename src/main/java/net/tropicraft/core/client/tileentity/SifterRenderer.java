package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

public class SifterRenderer extends TileEntityRenderer<SifterTileEntity> {
	private ItemEntity item;

	public SifterRenderer() {
	}

	@Override
	public void render(SifterTileEntity sifter, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

		final World world = sifter.getWorld();
		if (item == null && !sifter.getSiftItem().isEmpty() && sifter.isSifting()) {
			item = new ItemEntity(EntityType.ITEM, world);
			item.setItem(sifter.getSiftItem().copy());
		}

		if (item != null)  {
			item.setWorld(world);
			float itemRenderSize = 0.4375F;
			GlStateManager.translatef(0.0F, 0.6F, 0.0F);        // height of thing inside spawner
			GlStateManager.scalef(itemRenderSize * 3, itemRenderSize * 3, itemRenderSize * 3);            //size of thing inside spawner, scaled
			GlStateManager.rotatef((float)(sifter.yaw2 + (sifter.yaw - sifter.yaw2) * (double)partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(-20F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translatef(0.0F, -0.4F, 0.0F);       // other height of thing inside spawner

			item.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
			if (sifter.isSifting()) {
				Minecraft.getInstance().getRenderManager().renderEntity(item, 0, 0, 0, 0.0F, partialTicks, false);
			} else {
				item = null;
			}
		}
		GlStateManager.popMatrix();
	}
}
