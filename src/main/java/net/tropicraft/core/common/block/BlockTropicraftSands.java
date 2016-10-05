		package net.tropicraft.core.common.block;

import java.util.List;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.enums.TropicraftSands;

public class BlockTropicraftSands extends BlockFalling implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftSands> VARIANT = PropertyEnum.create("variant", TropicraftSands.class);
	public String[] names;
	
	public BlockTropicraftSands(String[] names) {
		super(Material.SAND);
		this.names = names;
	}
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {        
		for (int i = 0; i < names.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftSands) state.getValue(VARIANT)).getName();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, TropicraftSands.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((TropicraftSands) state.getValue(VARIANT)).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {VARIANT};
	}

	@Override
	public IBlockColor getBlockColor() {
		return TropicraftRenderUtils.SAND_COLORING;
	}

	@Override
	public IItemColor getItemColor() {
		return TropicraftRenderUtils.BLOCK_ITEM_COLORING;
	}
}
