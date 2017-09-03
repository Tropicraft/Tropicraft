package net.tropicraft.core.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTropicraftLeaves extends BlockLeaves implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftLeaves> VARIANT = PropertyEnum.create("variant", TropicraftLeaves.class);

	public String[] names;

	public BlockTropicraftLeaves(String[] names) {
		this.names = names;
		setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true).withProperty(VARIANT, TropicraftLeaves.MAHOGANY));
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(BlockRegistry.saplings);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		int chance = this.getSaplingDropChance(state);

		if (fortune > 0) {
			chance -= 2 << fortune;
			if (chance < 10) chance = 10;
		}

		chance = 200;
		if (fortune > 0) {
			chance -= 10 << fortune;
			if (chance < 40) chance = 40;
		}

		this.captureDrops(true);

		ret.addAll(this.captureDrops(false));
		return ret;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, TropicraftLeaves.byMetadata(meta)).withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = ((TropicraftLeaves)state.getValue(VARIANT)).ordinal();
		if (!state.getValue(DECAYABLE)) {
			i |= 4;
		}

		if (state.getValue(CHECK_DECAY)) {
			i |= 8;
		}

		return i;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return Blocks.LEAVES.getBlockLayer();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return state.getValue(VARIANT).isSolid();
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Deprecated
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return state.getValue(VARIANT).isSolid() ? 
				// bit of a hack, break out of encapsulation, assumes stone hasn't been messed with by mods
				Blocks.STONE.shouldSideBeRendered(state, world, pos, side) : 
				super.shouldSideBeRendered(state, world, pos, side);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {	
		for (TropicraftLeaves leaf : TropicraftLeaves.VALUES) {
			list.add(new ItemStack(item, 1, leaf.getMetadata()));
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CHECK_DECAY, DECAYABLE, VARIANT });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftLeaves) state.getValue(VARIANT)).getName();
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		IBlockState state = world.getBlockState(pos);

		if (state.getBlock() == this) {
			int meta = this.getMetaFromState(state);
			items.add(new ItemStack(this, 1, meta));
		}

		return items;
	}

	@Override
	public EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {CHECK_DECAY, DECAYABLE, VARIANT};
	}

	@Override
	public IBlockColor getBlockColor() {
		return null;
	}

	@Override
	public IItemColor getItemColor() {
		return null;
	}

}
