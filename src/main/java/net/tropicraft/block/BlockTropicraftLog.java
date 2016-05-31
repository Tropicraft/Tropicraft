package net.tropicraft.block;

import java.util.Random;

import java.util.List;

import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicraftLog extends BlockTropicraftMulti {

	protected IIcon palmSide;
	protected IIcon palmEnd;
	protected IIcon mahoganySide;
	protected IIcon mahoganyEnd;

	public BlockTropicraftLog(String[] names) {
		super(names, Material.wood);
		this.setBlockTextureName(TCNames.log);
		this.disableStats();
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
		
		this.setTickRandomly(true);
	}
	
	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		/*
		 * The following code makes existing palm trees spawn coconuts.
		 * Not just newly grown ones.
		 */
		
		int meta = world.getBlockMetadata(i, j, k);
		if (meta % 4 == 0) // palm log
		{
			spawnCoconuts(world, i, j, k, random, 5);
		}
	}

	public static void spawnCoconuts(World world, int i, int j, int k, Random random, int chance) {
		if (world.getBlock(i, j + 1, k) == TCBlockRegistry.palmLeaves || world.getBlock(i, j + 2, k) == TCBlockRegistry.palmLeaves) {
			if (world.isAirBlock(i + 1, j, k) && random.nextInt(chance) == 0) {
				world.setBlock(i + 1, j, k, TCBlockRegistry.coconut);
				world.setBlockMetadataWithNotify(i, j, k, 0, 3);

			}
			if (world.isAirBlock(i - 1, j, k) && random.nextInt(chance) == 0) {
				world.setBlock(i - 1, j, k, TCBlockRegistry.coconut);
				world.setBlockMetadataWithNotify(i, j, k, 0, 3);


			}
			if (world.isAirBlock(i, j, k - 1) && random.nextInt(chance) == 0) {
				world.setBlock(i, j, k - 1, TCBlockRegistry.coconut);
				world.setBlockMetadataWithNotify(i, j, k, 0, 3);


			}
			if (world.isAirBlock(i, j, k + 1) && random.nextInt(chance) == 0) {
				world.setBlock(i, j, k + 1, TCBlockRegistry.coconut);
				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
			}

			if (world.isAirBlock(i, j - 1, k) && random.nextInt(chance) == 0) {
				world.setBlock(i, j - 1, k, TCBlockRegistry.coconut);
				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
			}
		} 

	}

	/**
	 * Currently only called by fire when it is on top of this block.
	 * Returning true will prevent the fire from naturally dying during updating.
	 * Also prevents firing from dying from rain.
	 *
	 * @param world The current world
	 * @param x The blocks X position
	 * @param y The blocks Y position
	 * @param z The blocks Z position
	 * @param metadata The blocks current metadata
	 * @param side The face that the fire is coming from
	 * @return True if this block sustains fire, meaning it will never go out.
	 */
	public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 31;
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
	 */
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		int j1 = metadata & 1;
		byte b0 = 0;

		switch (side) {
		case 0:
		case 1:
			b0 = 0;
			break;
		case 2:
		case 3:
			b0 = 8;
			break;
		case 4:
		case 5:
			b0 = 4;
		}

		return j1 | b0;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int i, int j) {

		if (j == 0) {
			if (i == 0 || i == 1) {
				return palmEnd;    //palm top
			} else {
				return palmSide;    //palm side
			}
		}

		if (j == 8) {
			if (i == 2 || i == 3) {
				return palmEnd;    //palm top
			} else {
				return palmSide;    //palm side
			}
		}

		if (j == 4) {
			if (i == 4 || i == 5) {
				return palmEnd;    //palm top
			} else {
				return palmSide;    //palm side
			}
		}

		if (j == 1) {
			if (i == 0 || i == 1) {
				return mahoganyEnd;
			} else {
				return mahoganySide;
			}
		}

		if (j == 5) {
			if (i == 4 || i == 5) {
				return mahoganyEnd;
			} else {
				return mahoganySide;
			}
		}

		if (j == 9) {
			if (i == 3 || i == 2) {
				return mahoganyEnd;
			} else {
				return mahoganySide;
			}
		}

		return palmEnd;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random rand) {
		return 1;
	}

	public Item getItemDropped(int p_149650_1_, Random rand, int p_149650_3_) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	/**
	 * Register all icons here
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		palmSide = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[0] + "_Side");
		palmEnd = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[0] + "_End");

		mahoganySide = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[1] + "_Side");
		mahoganyEnd = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[1] + "_End");
	}
}
