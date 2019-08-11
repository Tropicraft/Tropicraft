package net.tropicraft.core.common.dimension.feature;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class FruitTreeFeature extends AbstractTreeFeature<NoFeatureConfig> {

	private final BlockState WOOD_BLOCK = Blocks.OAK_LOG.getDefaultState();
	private final BlockState REGULAR_LEAF_BLOCK = TropicraftBlocks.FRUIT_LEAVES.getDefaultState();
	private final BlockState FRUIT_LEAF_BLOCK;

	public FruitTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49920_1_, boolean doBlockNofityOnPlace, BlockState fruitLeaf) {
		super(p_i49920_1_, doBlockNofityOnPlace);
		FRUIT_LEAF_BLOCK = fruitLeaf;
	}

	@Override
	protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldObj, Random rand, BlockPos pos, MutableBoundingBox p_208519_5_) {
		int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
		int height = rand.nextInt(3) + 4;
		boolean canGenerate = true;

		if (j < 1 || j + height + 1 > worldObj.getMaxHeight()) {
			return false;
		}

		for (int y = j; y <= j + 1 + height; y++) {
			int size = 1;
			if (y == j) {
				size = 0;
			}

			if (y >= (j + 1 + height) - 2) {
				size = 2;
			}

			for (int x = i - size; x <= i + size && canGenerate; x++) {
				for (int z = k - size; z <= k + size && canGenerate; z++) {
					if (y >= 0 && y < worldObj.getMaxHeight()) {
						if (!isAirOrLeaves(worldObj, new BlockPos(x, y, z))) {
							canGenerate = false;
						}
					} else {
						canGenerate = false;
					}
				}
			}
		}

		if (!canGenerate) {
			return false;
		}

		if (!isSoil(worldObj, new BlockPos(i, j - 1, k), getSapling())) {
			return false;
		}

		worldObj.setBlockState(new BlockPos(i, j - 1, k), Blocks.DIRT.getDefaultState(), Constants.BlockFlags.DEFAULT);

		for (int y = (j - 3) + height; y <= j + height; y++) {
			int presizeMod = y - (j + height);
			int size = 1 - presizeMod / 2;
			for (int x = i - size; x <= i + size; x++) {
				int localX = x - i;
				for (int z = k - size; z <= k + size; z++) {
					int localZ = z - k;
					if ((Math.abs(localX) != size || Math.abs(localZ) != size || rand.nextInt(2) != 0 && presizeMod != 0) && isAirOrLeaves(worldObj, new BlockPos(x, y, z))) {
						BlockPos leafPos = new BlockPos(x, y, z);
						if (rand.nextBoolean()) {
							// Set fruit-bearing leaves here
							worldObj.setBlockState(leafPos, FRUIT_LEAF_BLOCK, Constants.BlockFlags.DEFAULT);
						} else {
							// Set plain fruit tree leaves here
						    worldObj.setBlockState(leafPos, REGULAR_LEAF_BLOCK, Constants.BlockFlags.DEFAULT);
						}
					}
				}
			}
		}

		// Tree stem
		for (int y = 0; y < height; y++) {
			if (isAirOrLeaves(worldObj, new BlockPos(i, j + y, k))) {
				worldObj.setBlockState(new BlockPos(i, j + y, k), WOOD_BLOCK, Constants.BlockFlags.DEFAULT);
			}
		}

		return true;
	}
}