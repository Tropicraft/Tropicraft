package net.tropicraft.core.common.block;

import java.util.List;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftLeaves;

public class BlockTropicraftLeaves extends BlockLeaves implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftLeaves> VARIANT = PropertyEnum.create("variant", TropicraftLeaves.class);

	public String[] names;

	public BlockTropicraftLeaves(String[] names) {
		this.names = names;
		setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true));
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
		return Blocks.LEAVES.isOpaqueCube(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side);
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

	//   TODO: 
	//	 @Override
	//    public int quantityDropped(Random random) {
	//        return random.nextInt(20) != 0 ? 0 : 1;
	//    }
	//
	//    @Override
	//    public Item getItemDropped(int metadata, Random random, int j) {
	//        return Item.getItemFromBlock(BlockRegistry.saplings);
	//    }

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CHECK_DECAY, DECAYABLE, VARIANT });
	}

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftLeaves) state.getValue(VARIANT)).getName();
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world,
			BlockPos pos, int fortune) {
		return null;
	}

	@Override
	public EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {CHECK_DECAY, DECAYABLE, VARIANT};
	}

}
