package net.tropicraft.core.common.block;

import java.util.List;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftBundles;

public class BlockBundle extends BlockTropicraft implements ITropicraftBlock {

	public static final PropertyEnum<BlockLog.EnumAxis> BUNDLE_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);
	public static final PropertyEnum<TropicraftBundles> VARIANT = PropertyEnum.create("variant", TropicraftBundles.class);
	public String[] names;

	public BlockBundle(Material mat, String[] names) {
		super(mat);
		this.names = names;
		this.setSoundType(SoundType.PLANT);
        this.setHardness(0.2F);
		//TODO: Figure out harvesting of bundles: this.setHarvestLevel("axe", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftBundles.THATCH).withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.Y));
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getStateFromMeta(meta).withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		switch (rot) {
		case COUNTERCLOCKWISE_90:
		case CLOCKWISE_90:

			switch ((BlockLog.EnumAxis)state.getValue(BUNDLE_AXIS)) {
			case X:
				return state.withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.Z);
			case Z:
				return state.withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.X);
			default:
				return state;
			}

		default:
			return state;
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BUNDLE_AXIS, VARIANT);
	}

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftBundles) state.getValue(VARIANT)).getName();
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

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, TropicraftBundles.byMetadata((meta & 3)));

		switch (meta & 12) {
		case 0:
			iblockstate = iblockstate.withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.Y);
			break;
		case 4:
			iblockstate = iblockstate.withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.X);
			break;
		case 8:
			iblockstate = iblockstate.withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.Z);
			break;
		default:
			iblockstate = iblockstate.withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.NONE);
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((TropicraftBundles)state.getValue(VARIANT)).getMetadata();

		switch ((BlockLog.EnumAxis)state.getValue(BUNDLE_AXIS)) {
		case X:
			i |= 4;
			break;
		case Z:
			i |= 8;
			break;
		case NONE:
			i |= 12;
		default:
			break;
		}

		return i;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return ((TropicraftBundles)state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this), 1, ((TropicraftBundles)state.getValue(VARIANT)).getMetadata());
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
