package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntitySifter;
import net.tropicraft.factory.TileEntityFactory;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class BlockSifter extends BlockTropicraft implements ITileEntityProvider {

    public BlockSifter() {
        super(Material.wood);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return TileEntityFactory.getSifterTE();
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public Item getItemDropped(int id, Random random, int j) {
        return Item.getItemFromBlock(TCBlockRegistry.sifter);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int d, float f1, float f2, float f3) {
        if (world.isRemote) return true;

        ItemStack stack = entityplayer.inventory.getCurrentItem();

        TileEntitySifter tileentitysifta = (TileEntitySifter) world.getTileEntity(i, j, k);

        if (tileentitysifta != null && stack != null && !tileentitysifta.isSifting()) {
            Item helditem = stack.getItem();
            if (helditem == Item.getItemFromBlock(Blocks.sand) ||  /*unrefined raftous ore */(helditem == TCItemRegistry.ore && stack.getItemDamage() == 5)
                    || /* mineral sands */(helditem == Item.getItemFromBlock(TCBlockRegistry.mineralSands) && stack.getItemDamage() == 3)) {
                entityplayer.getCurrentEquippedItem().stackSize--;

                if (helditem == TCItemRegistry.ore) {
                    float percent = getTagCompound(stack).getFloat("AmtRefined");
                    tileentitysifta.setSifting(true, helditem == Item.getItemFromBlock(Blocks.sand) ? 1 : 
                        helditem == Item.getItemFromBlock(TCBlockRegistry.mineralSands) ? 2 : 3, percent);
                } else {
                    tileentitysifta.setSifting(true, helditem == Item.getItemFromBlock(Blocks.sand) ? 1 : 
                        helditem == Item.getItemFromBlock(TCBlockRegistry.mineralSands) ? 2 : 3, -1);
                }
            }
        }
        return true;
    } // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D

    /**
     * Retrives an existing nbt tag compound or creates a new one if it is null
     * @param stack
     */
    public NBTTagCompound getTagCompound(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        return stack.getTagCompound();
    }
}
