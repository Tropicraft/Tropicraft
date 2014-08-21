package net.tropicraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.TropicraftWorldUtils;
import net.tropicraft.world.TeleporterTropics;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPortalEnchanter extends ItemTropicraft {

    public ItemPortalEnchanter() {
        super();
        maxStackSize = 1;
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer ent, List list, boolean wat) {
        list.add(StatCollector.translateToLocal("portalenchanter.type_" + itemstack.getItemDamage()));
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isRemote && (itemstack.getItemDamage() == 1 || entityplayer.capabilities.isCreativeMode)) {
            int destination = entityplayer.dimension == 0 ? TropicraftWorldUtils.TROPICS_DIMENSION_ID : 0;
            TropicraftWorldUtils.teleportPlayer((EntityPlayerMP) entityplayer);
            return itemstack;
        }

        // Portal starter only usable in overworld & tropics
        // TODO NOTE: Commented out for now because pickles     
        /*if (entityplayer.dimension != 0 && entityplayer.dimension != TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
            return itemstack;
        }*/

        double playerX = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX);
        double playerY = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) + 1.62D - (double) entityplayer.yOffset;
        double playerZ = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ);

        MovingObjectPosition target = this.getMovingObjectPositionFromPlayer(world, entityplayer, true);

        if (target == null || world.isRemote) {
            return itemstack;
        } else {
            if (target.typeOfHit == MovingObjectType.BLOCK) {
                int x = target.blockX;
                int y = target.blockY;
                int z = target.blockZ;
                int l = world.getBlockMetadata(x, y, z);

                if (!world.canMineBlock(entityplayer, x, y, z)) {
                    return itemstack;
                }

                if (!entityplayer.canPlayerEdit(x, y, z, l, itemstack)) {
                    return itemstack;
                }

                boolean found = false;
                for (int searchZ = -4; searchZ <= 4 && !found; searchZ++) {
                    for (int searchX = -4; searchX <= 4 && !found; searchX++) {
                        if (canGen(world, x + searchX, y, z + searchZ)) {
                            found = true;
                            entityplayer.swingItem();
                            (new TeleporterTropics(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(entityplayer.dimension))).buildTeleporterAt(x + searchX, y, z + searchZ, entityplayer);
                            //ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntitySplashFX(ModLoader.getMinecraftInstance().theWorld, playerX, playerY, playerZ, 0D, 0D, 0D));
                            itemstack.damageItem(1, entityplayer);
                        }
                    }
                }
            }
        }

        return itemstack;
    }

    public boolean canGen(World world, int x, int y, int z) {
        if (y < 9)
            return false;

        for (int offsetZ = -2; offsetZ < 3; offsetZ++) {
            for (int offsetX = -2; offsetX < 3; offsetX++) {
                if (offsetX == -2 || offsetX == 2 || offsetZ == -2 || offsetZ == 2) {
                    Block block = world.getBlock(x + offsetX, y, z + offsetZ);
                    if (block != Blocks.sandstone && block != TCBlockRegistry.tropicsPortalWall) {
                        return false;
                    }
                } else {
                    if (world.getBlock(x + offsetX, y, z + offsetZ).getMaterial() != Material.water) {
                        return false;
                    }
                    if (!world.isAirBlock(x + offsetX, y + 1, z + offsetZ)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }
}
