package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooDoor extends BlockDoor {

	@SideOnly(Side.CLIENT)
	private IIcon[] images;

	public BlockBambooDoor() {
		super(Material.plants);
		this.setBlockTextureName(TCNames.bambooDoor);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return images[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (par5 != 1 && par5 != 0) {
			int i1 = this.func_150012_g(par1IBlockAccess, par2, par3, par4);
			int j1 = i1 & 3;
			boolean flag = (i1 & 4) != 0;
			boolean flag1 = false;
			boolean flag2 = (i1 & 8) != 0;

			if (flag) {
				if (j1 == 0 && par5 == 2) {
					flag1 = !flag1;
				} else if (j1 == 1 && par5 == 5) {
					flag1 = !flag1;
				} else if (j1 == 2 && par5 == 3) {
					flag1 = !flag1;
				} else if (j1 == 3 && par5 == 4) {
					flag1 = !flag1;
				}
			} else {
				if (j1 == 0 && par5 == 5) {
					flag1 = !flag1;
				}
				else if (j1 == 1 && par5 == 3) {
					flag1 = !flag1;
				}
				else if (j1 == 2 && par5 == 4) {
					flag1 = !flag1;
				}
				else if (j1 == 3 && par5 == 2) {
					flag1 = !flag1;
				}

				if ((i1 & 16) != 0) {
					flag1 = !flag1;
				}
			}

			return this.images[0 + (flag1 ? 2 : 0) + (flag2 ? 1 : 0)];
		}
		else {
			return this.images[0];
		}
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return TCItemRegistry.bambooDoor;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return (p_149650_1_ & 8) != 0 ? null : (TCItemRegistry.bambooDoor);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.images = new IIcon[4];
		this.images[0] = register.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_Bottom");
		this.images[1] = register.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_Top");
		this.images[2] = new IconFlipped(this.images[0], true, false);
		this.images[3] = new IconFlipped(this.images[1], true, false);
	}
}
