package net.tropicraft.core.common.block;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.enums.TropicraftBundles;

public class BlockBundle extends BlockTropicraftEnumVariants<TropicraftBundles> {

	public static final PropertyEnum<BlockLog.EnumAxis> BUNDLE_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);

	public BlockBundle(Material mat) {
		super(mat, TropicraftBundles.class);
		this.setSoundType(SoundType.PLANT);
		//TODO: Figure out harvesting of bundles: this.setHarvestLevel("axe", 0);
		this.setDefaultState(this.getDefaultState().withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.Y));
	}

	@Override
	@Deprecated
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
	    return this.getVariant(blockState).getHardness();
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getStateFromMeta(meta).withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getVariant(state).getMeta();
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
	protected IProperty<?>[] getAdditionalProperties() {
	    return new IProperty[] { BUNDLE_AXIS };
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = super.getStateFromMeta(meta & 3);
		return iblockstate.withProperty(BUNDLE_AXIS, BlockLog.EnumAxis.values()[(meta >>> 2) & 3]);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return (super.getMetaFromState(state) & 3) | state.getValue(BUNDLE_AXIS).ordinal() << 2;
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(state));
	}
}
