package net.tropicraft.util;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import cpw.mods.fml.common.FMLCommonHandler;

public class TropicraftUtils {
    
    /**
     * NOTE: REPLACED WITH world.markBlockForUpdate
     * Helper method for syncing a TileEntity's data
     * @param packet
     * @param dimensionID
     */
    public static void sync(Packet packet, int dimensionID) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dimensionID);
    }
    
    public static ResourceLocation getTexture(String path) {
        ResourceLocation derp = new ResourceLocation(TCInfo.MODID, path);
        return derp;
    }

    public static ResourceLocation getTextureArmor(String path) {
        return getTexture(String.format("textures/models/armor/%s.png", path));
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
    
    public static ResourceLocation getTextureTE(String path) {
        return getTexture(String.format("textures/blocks/te/%s.png", path));
    }
    
    public static ResourceLocation bindTextureArmor(String path) {
        return bindTexture(getTextureArmor(path));
    }
    
    public static ResourceLocation bindTextureEntity(String path) {
        return bindTexture(getTextureEntity(path));
    }
    
    public static ResourceLocation bindTextureTE(String path) {
        return bindTexture(getTextureTE(path));
    }
    
    public static ResourceLocation bindTextureBlock(String path) {
        return bindTexture(getTextureBlock(path));
    }
    
    public static ResourceLocation bindTexture(ResourceLocation resource) {
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
    
    public static int getSlotOfItemWithDamage(InventoryPlayer inventory, Item item, int damage) {
        for (int j = 0; j < inventory.mainInventory.length; ++j) {
            if (inventory.mainInventory[j] != null && inventory.mainInventory[j].getItem() == item && inventory.mainInventory[j].getItemDamage() == damage) {
                return j;
            }
        }

        return -1;
    }
}
