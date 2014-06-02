package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTikiTorch extends BlockTropicraft {

	private IIcon lowerTorch;
	private IIcon upperTorch;
	
	public BlockTikiTorch() {
		super(Material.circuits);
		this.setTickRandomly(true);
		this.setCreativeTab(null);

		float w = 0.0625F;
		setBlockBounds(0.5F - w, 0.0F, 0.5F - w, 0.5F + w, 0.9F, 0.5F + w);
	}
	
	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int id, int metadata) {
		if (metadata != 0) {
			return lowerTorch;
		}
		
		return upperTorch;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float w = 0.0625F;
		float top = world.getBlockMetadata(x, y, z) == 0 ? 0.625F : 1.0F;
		setBlockBounds(0.5F - w, 0.0F, 0.5F - w, 0.5F + w, top, 0.5F + w);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		if (!world.isRemote)
			return canPlaceTikiTorchOn(world, x, y - 1, z);
		else
			return false;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int unused) {
		if (meta == 0) {
			return TCItemRegistry.tikiTorch;
		}

		return null;
	}
	
    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return TCItemRegistry.tikiTorch;
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
	public int getRenderType() {
		return TCRenderIDs.tikiTorch;
	}

	private boolean canPlaceTikiTorchOn(World world, int x, int y, int z) {
		if (world.isBlockNormalCubeDefault(x, y, z, false)) {
			return true;
		} else {
			Block b = world.getBlock(x, y, z);
			if (world.isAirBlock(x, y, z)) {
				return false;
			}

			if (b == this && world.getBlockMetadata(x, y, z) == 1) {
				return true;
			}

			if (b != Blocks.glass && !(b instanceof BlockFence)) {
				if (b != null && b instanceof BlockStairs) {
					int meta = world.getBlockMetadata(x, y, z);

					if ((4 & meta) != 0) {
						return true;
					}
				}
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block oldBlock) {
		if (!world.isRemote && !canPlaceTikiTorchOn(world, x, y - 1, z)) {
			dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
		super.onNeighborBlockChange(world, x, y, z, oldBlock);
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		if (!world.isRemote) {
			while (world.getBlock(x, --y, z) == this) {
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			}
		}
	}
	
	 /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
        onBlockDestroyedByPlayer(world, x, y, z, world.getBlockMetadata(x, y, z));
    }
    
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int l = world.getBlockMetadata(x, y, z);
        if (l == 0) {
            double d = (float) x + 0.5F;
            double d1 = (float) y + 0.7F;
            double d2 = (float) z + 0.5F;

            world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        if (l == 0) {
            return super.getLightValue(world, x, y, z);
        } else {
            return 0;
        }
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));        
    }
	
	/**
     * Register all Icons used in this block
     */
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry) {
        this.lowerTorch = iconRegistry.registerIcon(TCInfo.ICON_LOCATION + TCNames.tikiLower);
        this.upperTorch = iconRegistry.registerIcon(TCInfo.ICON_LOCATION + TCNames.tikiUpper);
    }
    
}
