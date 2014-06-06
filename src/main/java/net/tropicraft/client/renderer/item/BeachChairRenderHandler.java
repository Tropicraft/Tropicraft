package net.tropicraft.client.renderer.item;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.TropicraftUtils;

public class BeachChairRenderHandler implements IItemRenderer {


	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return item != null && item.getItem() == TCItemRegistry.chair && type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		System.out.println("testing testing 1 2 3");
		
	}

}
