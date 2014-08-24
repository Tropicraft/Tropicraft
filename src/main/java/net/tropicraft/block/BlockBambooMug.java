package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityBambooMug;
import net.tropicraft.factory.TileEntityFactory;
import net.tropicraft.registry.TCItemRegistry;

public class BlockBambooMug extends BlockContainer {
    // edited classes: BlockBambooMug, TileEntityBambooMug, TileEntityBambooMugRenderer, ModelBabooMug
    // TropicraftMod, Drink*, ItemDrink, /tropicalmod/bamboomug.png /tropicalmod/tropiitems.png
    
    public BlockBambooMug() {
        super(Material.plants);
        this.setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 0.45f, 0.7f);
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && world.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }
    
    @Override
    public int getRenderType() {
        return -1;
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        TileEntityBambooMug mug = (TileEntityBambooMug) world.getTileEntity(x, y, z);
        
        if (mug.isEmpty()) {
            return new ItemStack(TCItemRegistry.bambooMug);
        }
        
        return mug.cocktail.copy();
    }
    

    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        if (!world.isRemote) {
            TileEntityBambooMug mug = (TileEntityBambooMug) world.getTileEntity(x, y, z);
            if (!mug.isEmpty()) {
                dropBlockAsItem(world, x, y, z, mug.cocktail.copy());
            } else {
                dropBlockAsItem(world, x, y, z, new ItemStack(TCItemRegistry.bambooMug));
            }
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegistry) {
        //this.blockIcon = iconRegistry.registerIcon(ModInfo.MODID + ":" + getImageName());
    }
    
    /**
     * @return Get the image name for this block
     */
    public String getImageName() {
        return "bamboomug";
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return TileEntityFactory.getBambooMugTE();
    }
}
