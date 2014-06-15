package net.tropicraft.util;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;

public class TropicraftUtils {
    public static ResourceLocation getTexture(String path) {
        ResourceLocation derp = new ResourceLocation(TCInfo.MODID, path);
        return derp;
    }

    public static ResourceLocation getTextureBlock(String path) {
        return getTexture(String.format("textures/blocks/%s.png", path));
    }

    public static ResourceLocation getTextureEntity(String path) {
        return getTexture(String.format("textures/entity/%s.png", path));
    }

    public static ResourceLocation getTextureGui(String path) {
        return getTexture(String.format("textures/gui/%s.png", path));
    }
    
    public static ResourceLocation bindTextureBlock(String path) {
        ResourceLocation resource = getTextureBlock(path);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        return resource;
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
