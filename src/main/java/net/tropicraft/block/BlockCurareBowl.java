package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityCurareBowl;
import net.tropicraft.registry.TCBlockRegistry;

public class BlockCurareBowl extends BlockTropicraft implements ITileEntityProvider {

    public BlockCurareBowl() {
        super(Material.rock);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityCurareBowl();
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        TileEntityCurareBowl bowl = (TileEntityCurareBowl)par1World.getTileEntity(par2, par3, par4);
        
        if (bowl != null && !par1World.isRemote) {
            if (!bowl.hasMetMaxNumClicks()) {
                for (int var8 = 0; var8 < bowl.getIngredients().length; ++var8) {
                    ItemStack var9 = bowl.getIngredientList().get(var8);

                    if (var9 != null)
                    {
                        float var10 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                        float var11 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                        EntityItem item = new EntityItem(par1World, par2 + var10, par3 + var10, par4 + var11, var9);
                        
                        par1World.spawnEntityInWorld(item);
                    }
                }
            } else {
                bowl.dropResult();
            }
        }

        par1World.removeTileEntity(par2, par3, par4);
        
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && par1World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int doop, float f1, float f2, float f3) {
        if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() != null) {
            TileEntityCurareBowl bowl = (TileEntityCurareBowl) world.getTileEntity(i, j, k);

            if (entityplayer.getCurrentEquippedItem().getItem() == Items.stick) {
                if (bowl.isBowlFull()) {
                    bowl.incrementNumClicks();

                    if (bowl.hasMetMaxNumClicks()) {
                        bowl.resetClicks();
                        bowl.dropResult();
                    }
                }
            } else
                if (entityplayer.getCurrentEquippedItem().getItem() == Item.getItemFromBlock(TCBlockRegistry.flowers)) {
                    bowl.addIngredient(entityplayer.getCurrentEquippedItem());
                    
                    if (!entityplayer.capabilities.isCreativeMode)
                        entityplayer.getCurrentEquippedItem().stackSize--;
                }
        }

        return true;
    }
}
