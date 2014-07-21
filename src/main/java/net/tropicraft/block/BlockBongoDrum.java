package net.tropicraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBongoDrum extends BlockTropicraftMulti {

    public static final float SMALL_DRUM_SIZE = 0.5f;
    public static final float MEDIUM_DRUM_SIZE = 0.6f;
    public static final float BIG_DRUM_SIZE = 0.7f;
    
    public static final float SMALL_DRUM_OFFSET = (1.0f - SMALL_DRUM_SIZE)/2.0f;
    public static final float MEDIUM_DRUM_OFFSET = (1.0f - MEDIUM_DRUM_SIZE)/2.0f;
    public static final float BIG_DRUM_OFFSET = (1.0f - BIG_DRUM_SIZE)/2.0f;
    public static final float DRUM_HEIGHT = 1.0f;
    
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;
    
    public BlockBongoDrum(String[] names) {
        super(names, Material.circuits);
        setBlockBounds(SMALL_DRUM_OFFSET, 0.0f, SMALL_DRUM_OFFSET, 1-SMALL_DRUM_OFFSET, DRUM_HEIGHT, 1-SMALL_DRUM_OFFSET);
        this.setLightOpacity(255);
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry) {
        topIcon = iconRegistry.registerIcon(TCInfo.MODID + ":" + TCNames.bongoDrum + "Top");
        blockIcon = sideIcon = iconRegistry.registerIcon(TCInfo.MODID + ":" + TCNames.bongoDrum + "Side");
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ) {
        if (side != 1) {
            return true;
        }

        int meta = world.getBlockMetadata(x, y, z)  &3;
        
        switch (meta) {
        case 2:
            playLowBongo(world, x, y, z);
            break;
        case 1:
            playMediumBongo(world, x, y, z);
            break;
        case 0:
        default:
            playHighBongo(world, x, y, z);
            break;
        }

        return true;
    }
    
    private void playHighBongo(World world, int x, int y, int z) {
        world.playSoundEffect(x, y, z, "bongohigh", 1.0f, 1.0f);
    }
    
    private void playMediumBongo(World world, int x, int y, int z) {
        world.playSoundEffect(x, y, z, "bongomedium", 1.0f, 1.0f);
        
    }

    private void playLowBongo(World world, int x, int y, int z) {
        world.playSoundEffect(x, y, z, "bongolow", 1.0f, 1.0f);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z) & 3;
        
        switch (meta) {
        case 2:
            setBlockBounds(BIG_DRUM_OFFSET, 0.0f, BIG_DRUM_OFFSET, 1-BIG_DRUM_OFFSET, DRUM_HEIGHT, 1-BIG_DRUM_OFFSET);
            break;
        case 1:
            setBlockBounds(MEDIUM_DRUM_OFFSET, 0.0f, MEDIUM_DRUM_OFFSET, 1-MEDIUM_DRUM_OFFSET, DRUM_HEIGHT, 1-MEDIUM_DRUM_OFFSET);
            break;
        case 0:
        default:
            setBlockBounds(SMALL_DRUM_OFFSET, 0.0f, SMALL_DRUM_OFFSET, 1-SMALL_DRUM_OFFSET, DRUM_HEIGHT, 1-SMALL_DRUM_OFFSET);
            break;
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(BIG_DRUM_OFFSET, 0.0f, BIG_DRUM_OFFSET, 1-BIG_DRUM_OFFSET, DRUM_HEIGHT, 1-BIG_DRUM_OFFSET);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return topIcon;
        } else {
            return sideIcon;
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (side == 0) {
            return false;
        }
        
        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }
    
    /**
     * Location aware and overrideable version of the lightOpacity array,
     * return the number to subtract from the light value when it passes through this block.
     *
     * This is not guaranteed to have the tile entity in place before this is called, so it is
     * Recommended that you have your tile entity call relight after being placed if you
     * rely on it for light info.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return The amount of light to block, 0 for air, 255 for fully opaque.
     */
    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return 255;
    }
    
    /**
     * Get the block's damage value (for use with pick block).
     */
    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        int dmg = super.getDamageValue(world, x, y, z);
        
        if (dmg > 2) {
            dmg = 0;
        }
        
        return dmg;
    }
}
