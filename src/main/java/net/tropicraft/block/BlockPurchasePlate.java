package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityPurchasePlate;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockPurchasePlate extends BlockContainer
{

    public BlockPurchasePlate()
    {
        super(Material.circuits);
        this.setTickRandomly(true);
        float var5 = 0.0625F;
        this.setBlockBounds(var5, 0.0F, var5, 1.0F - var5, 0.03125F, 1.0F - var5);
        setCreativeTab(TCCreativeTabRegistry.tabBlock);
        setHardness(20F);
    }
    
    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(ModInfo.ICONLOCATION + "bambooBundle_side");
    }*/
    
    /*@Override
    public IIcon getIcon(int i, int j) {
        return TropicraftBlocks.bambooBundle.getIcon(i, j);
    }*/

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_) || BlockFence.func_149825_a(p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_));
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    //public void onBlockAdded(World par1World, int par2, int par3, int par4) {}

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        boolean flag = false;

        if (!World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_) && !BlockFence.func_149825_a(p_149695_1_.getBlock(p_149695_2_, p_149695_3_ - 1, p_149695_4_)))
        {
            flag = true;
        }

        if (flag)
        {
            this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
            p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    	if (par1World.isRemote) return true;
    	//if (!ZCGame.instance().canEdit(par5EntityPlayer)) return true;
    	
    	TileEntityPurchasePlate pp = (TileEntityPurchasePlate)par1World.getTileEntity(par2, par3, par4);
    	
    	if (pp != null) {
    		
    		pp.onClicked(true);
    		
    	}
    	
    	return true;
    }
    
    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
    	
    	
    	
    	if (world.isRemote) return;
    	//if (!ZCGame.instance().canEdit(entityplayer)) return;
    	
    	TileEntityPurchasePlate pp = (TileEntityPurchasePlate)world.getTileEntity(i, j, k);
    	
    	if (pp != null) {
    		
    		pp.onClicked(false);
    		
    	}
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPurchasePlate();
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        boolean var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1;
        float var6 = 0.0625F;

        if (var5)
        {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.03125F, 1.0F - var6);
        }
        else
        {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.0625F, 1.0F - var6);
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.5F;
        float var2 = 0.125F;
        float var3 = 0.5F;
        this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    @Override
    public int getMobilityFlag()
    {
        return 1;
    }
}
