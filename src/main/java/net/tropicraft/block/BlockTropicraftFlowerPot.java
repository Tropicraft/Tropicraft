package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.info.TCNames;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftFlowerPot extends BlockTropicraft implements ITileEntityProvider {

	public BlockTropicraftFlowerPot() {
		super(Material.circuits);
		this.setBlockTextureName(TCNames.flowerPot);
		this.setBlockBoundsForItemRender();
	}
	
    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
        float f = 0.375F;
        float f1 = f / 2.0F;
        this.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, f, 0.5F + f1);
    }
    
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return TCRenderIDs.flowerPot;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack var10 = par5EntityPlayer.inventory.getCurrentItem();

        if (var10 == null)
        {
            return false;
        }
        else if (par1World.getBlockMetadata(par2, par3, par4) != 0)
        {
            return false;
        }
        else
        {
            int var11 = getMetaForPlant(var10);

            if (var11 > 0)
            {
                TileEntityTropicraftFlowerPot pot = (TileEntityTropicraftFlowerPot) par1World.getTileEntity(par2, par3, par4);

                if (pot == null)
                	System.out.println("Flower pot was null!");
                
                pot.setFlowerID((short) var11);

                par1World.setBlockMetadataWithNotify(par2, par3, par4, var11, 2);

                par1World.markBlockForUpdate(par2, par3, par4);

                if (!par5EntityPlayer.capabilities.isCreativeMode && --var10.stackSize <= 0)
                {
                    par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
    	ItemStack var5 = getPlantForMeta(world.getBlockMetadata(x, y, z));
        return var5 == null ? TCItemRegistry.flowerPot : var5.getItem();
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block block) {
        if (!World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4)) {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

        if (par5 > 0) {
            ItemStack var8 = getPlantForMeta(par5);
            if (var8 != null) {
                this.dropBlockAsItem(par1World, par2, par3, par4, var8);
            }
        }
    }
    
    /**
     * Get the block's damage value (for use with pick block).
     */
    @Override
    public int getDamageValue(World par1World, int par2, int par3, int par4) {
        ItemStack var5 = getPlantForMeta(par1World.getBlockMetadata(par2, par3, par4));
        return var5 == null ? 0 : var5.getItemDamage();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot() {
        return true;
    }
    
    /**
     * Return the item associated with the specified flower pot metadata value.
     */
    public static ItemStack getPlantForMeta(int meta) {
        switch (meta) {
        case 1:
            return new ItemStack(TCBlockRegistry.flowers, 1, 0);
        case 2:
            return new ItemStack(TCBlockRegistry.flowers, 1, 1);
        case 3:
            return new ItemStack(TCBlockRegistry.flowers, 1, 2);
        case 4:
            return new ItemStack(TCBlockRegistry.flowers, 1, 3);
        case 5:
            return new ItemStack(TCBlockRegistry.flowers, 1, 4);
        case 6:
            return new ItemStack(TCBlockRegistry.flowers, 1, 5);
        case 7:
            return new ItemStack(TCBlockRegistry.flowers, 1, 6);
        case 8:
            return new ItemStack(TCBlockRegistry.flowers, 1, 7);
        case 9:
            return new ItemStack(TCBlockRegistry.flowers, 1, 8);
        case 10:
            return new ItemStack(TCBlockRegistry.flowers, 1, 9);
        case 11:
            return new ItemStack(TCBlockRegistry.flowers, 1, 10);
        case 12:
            return new ItemStack(TCBlockRegistry.flowers, 1, 11);
        case 13:
            return new ItemStack(TCBlockRegistry.flowers, 1, 12);
        case 14:
            return new ItemStack(TCBlockRegistry.flowers, 1, 13);
        case 15:
            return new ItemStack(TCBlockRegistry.flowers, 1, 14);
        case 16:
            return new ItemStack(TCBlockRegistry.flowers, 1, 15);
        case 17:
            return new ItemStack(TCBlockRegistry.tallFlowers, 1, 8);
        case 18:
            return new ItemStack(TCBlockRegistry.pineapple, 1, 8);
        case 19:
            return new ItemStack(TCBlockRegistry.saplings, 1, 0);
        case 20:
            return new ItemStack(TCBlockRegistry.saplings, 1, 1);
        case 21:
            return new ItemStack(TCBlockRegistry.saplings, 1, 2);
        case 22:
            return new ItemStack(TCBlockRegistry.saplings, 1, 3);
        case 23:
            return new ItemStack(TCBlockRegistry.saplings, 1, 4);
        case 24:
        	return new ItemStack(Blocks.red_flower, 1, 0);
        default:
            return null;
        }
    }

    /**
     * Return the flower pot metadata value associated with the specified item.
     */
    public static int getMetaForPlant(ItemStack itemstack) {
        Item item = itemstack.getItem();
        int damage = itemstack.getItemDamage() + 1;

        if (item == Item.getItemFromBlock(TCBlockRegistry.flowers)) {
            return damage;
        } else
            if (item == Item.getItemFromBlock(TCBlockRegistry.tallFlowers)) {
                return 17;
            } else
            	if (item == Item.getItemFromBlock(TCBlockRegistry.pineapple)) {
            		return 18;
            	} else
	                if (item == Item.getItemFromBlock(TCBlockRegistry.saplings)) {
	                    return 19 + damage - 1;
	                } else
	                	if (item == Item.getItemFromBlock(Blocks.red_flower)) {
	                		return 25 + damage - 1;
	                	} else
	                		if (item == Item.getItemFromBlock(Blocks.yellow_flower)) {
	                			return 34 + damage - 1;
	                		}
        
        return 0;
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityTropicraftFlowerPot();
	}

}
