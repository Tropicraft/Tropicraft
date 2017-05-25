package net.tropicraft.core.client.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.tropicraft.core.common.block.tileentity.TileEntitySifter;

public class TileEntitySifterRenderer extends TileEntitySpecialRenderer<TileEntitySifter> {

	private Entity item;
	private RenderManager renderManager;

	public TileEntitySifterRenderer() {
		renderManager = Minecraft.getMinecraft().getRenderManager();
	}

	@Override
	public void renderTileEntityAt(TileEntitySifter sifter, double x, double y,
			double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x + 0.5F, (float)y, (float)z + 0.5F);

		if (item == null && sifter.isSifting()) {
			item = (EntityItem)(new EntityItem(sifter.getWorld()));
			((EntityItem)item).setEntityItemStack(sifter.siftItem.copy());
		}

		if (item != null)  {
			item.setWorld(sifter.getWorld());    //allows entity to be placed into world
			//f1=size of object inside spawner
			float f1 = 0.4375F;                     
			GlStateManager.translate(0.0F, 0.6F, 0.0F);        //height of thing inside spawner
			GlStateManager.scale(f1 * 3, f1 * 3, f1 * 3);            //size of thing inside spawner, scaled    
			GlStateManager.rotate((float)(sifter.yaw2 + (sifter.yaw - sifter.yaw2) * (double)partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-20F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.0F, -0.4F, 0.0F);       //other height of thing inside spawner

			item.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
			if (sifter.isSifting()) {
				renderManager.doRenderEntity(item, 0, 0, 0, 0.0F, partialTicks, false);
			} else {
				item = null;
			}
		}
		GlStateManager.popMatrix();
	}
}
