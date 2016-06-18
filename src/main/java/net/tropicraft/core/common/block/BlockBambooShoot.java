package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockReed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockBambooShoot extends BlockReed implements IPlantable {

	protected static final AxisAlignedBB BAMBOO_SHOOT_AABB = new AxisAlignedBB(0.275D, 0.0D, 0.275D, 0.725D, 1.0D, 0.725);

	public BlockBambooShoot() {
		super();
		setHardness(1.0F);
		setResistance(4.0F);
		setHarvestLevel("axe", 0);
		setCreativeTab(null);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BAMBOO_SHOOT_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
		return BAMBOO_SHOOT_AABB;
	}

	/**
	 * Called every random tick of this block, only on the server (YEAH OK THANKS NEWT)
	 * 
	 * Very closely based on BlockReed.updateTick
	 * 
	 * @param world World instance
	 * @param x xCoord in the world
	 * @param y yCoord in the world
	 * @param z zCoord in the world
	 * @param random java.util.Random instance
	 */
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.getBlockState(pos.down()).getBlock() == BlockRegistry.bambooShoot || this.checkForDrop(worldIn, pos, state)) {
			if (worldIn.isAirBlock(pos.up())) {
				int i;

				for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i) {
					;
				}

				if (i < 3) {
					int j = ((Integer)state.getValue(AGE)).intValue();

					if (j == 15) {
						worldIn.setBlockState(pos.up(), this.getDefaultState());
						worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
					} else {
						worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 4);
					}
				}
			}
		}
	}

	@Override
	public net.minecraftforge.common.EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return net.minecraftforge.common.EnumPlantType.Plains;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ItemRegistry.bambooShoot);
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemRegistry.bambooShoot;
	}
}
