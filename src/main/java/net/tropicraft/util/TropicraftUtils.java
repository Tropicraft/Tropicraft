package net.tropicraft.util;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;

public class TropicraftUtils {
	public static ResourceLocation bindTexture(String path) {
		ResourceLocation derp = new ResourceLocation(TCInfo.MODID, path);
		return derp;
	}

	public static ResourceLocation bindTextureEntity(String path) {
		return bindTexture(String.format("textures/entity/%s.png", path));
	}

	public static ResourceLocation bindTextureGui(String path) {
		return bindTexture(String.format("textures/gui/%s.png", path));
	}

	public static int getTopWaterBlockY(World world, int xCoord, int zCoord) {
		int y = world.getHeightValue(xCoord, zCoord);

		while (world.getBlock(xCoord, y, zCoord).getMaterial() != Material.water) {
			y--;
		}

		return y;
	}
	
	public static String translateGUI(String word) {
		return StatCollector.translateToLocal(String.format("gui.tropicraft:%s", word));
	}

}
