package net.tropicraft.registry;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;

public class BlockTropicraftSapling extends BlockSapling implements IGrowable {

	/** Names of the sub-blocks */
	protected String[] names;
	
	/** Array of icons associated with this item */
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;
	
	public BlockTropicraftSapling(String[] names) {
		super();
		this.names = names;
		setTickRandomly(true);
		disableStats();
	}
	
    /**
     * Ticks the block if it's been scheduled
     */
	@Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            super.updateTick(world, x, y, z, rand);

            if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(7) == 0) {
                this.markOrGrowMarked(world, x, y, z, rand);
            }
        }
    }
	
	@Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * "Can Fertilize?"
     */
    public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
        return true;
    }

    /**
     * "Should Fertilize?"
     */
    public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_) {
        return (double)p_149852_1_.rand.nextFloat() < 0.45D;
    }

    /**
     * "Fertilize"
     */
    public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_, int p_149853_5_) {
        this.markOrGrowMarked(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_, p_149853_2_);
    }
    
    /**
     * "growTree"
     * @param world World instance
     * @param x x coord
     * @param y y coord
     * @param z z coord
     * @param rand Random instance
     */
    public void markOrGrowMarked(World world, int x, int y, int z, Random rand) {
        int l = world.getBlockMetadata(x, y, z);

        if ((l & 8) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
        } else {
            this.func_149878_d(world, x, y, z, rand);
        }
    }
    
    /**
     * "Grow Tree"
     */
    public void func_149878_d(World world, int x, int y, int z, Random rand) {
    	
    }
    
    /**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int id, int metadata) {
		if (metadata < 0 || metadata > names.length - 1)
			metadata = names.length - 1;

		return icons[metadata];
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < names.length; i++)
			list.add(new ItemStack(item, 1, i));        
    }
	
	/**
	 * @return The unlocalized block name
	 */
	@Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }
	
	/**
	 * Get the true name of the block
	 * @param unlocalizedName tile.%truename%
	 * @return The actual name of the block, rather than tile.%truename%
	 */
	protected String getActualName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
	}

	/**
	 * Register all icons here
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[names.length];

		for (int i = 0 ; i < names.length ; i++) {
			icons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[i]);
		}
	}
}
