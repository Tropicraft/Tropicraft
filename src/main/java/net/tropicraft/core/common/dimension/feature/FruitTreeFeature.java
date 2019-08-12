package net.tropicraft.core.common.dimension.feature;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
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
	protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldObj, Random rand, BlockPos pos, MutableBoundingBox bb) {
		pos = pos.toImmutable();
		int height = rand.nextInt(3) + 4;

		if (pos.getY() < 1 || pos.getY() + height + 1 > worldObj.getMaxHeight()) {
			return false;
		}

		for (int y = 0; y <= 1 + height; y++) {
			BlockPos checkPos = pos.up(y);
			int size = 1;
			if (checkPos.getY() < 0 || checkPos.getY() >= worldObj.getMaxHeight()) {
				return false;
			}
			
			if (y == 0) {
				size = 0;
			}

			if (y >= (1 + height) - 2) {
				size = 2;
			}
			
			if (BlockPos.getAllInBox(checkPos.add(-size, 0, -size), checkPos.add(size, 0, size))
						.filter(p -> !isAirOrLeaves(worldObj, p))
						.count() > 0) {
				return false;
			}
		}

		if (!isSoil(worldObj, pos.down(), getSapling())) {
			return false;
		}

		setLogState(changedBlocks, worldObj, pos.down(), Blocks.DIRT.getDefaultState(), bb);

		for (int y = (pos.getY() - 3) + height; y <= pos.getY() + height; y++) {
			int presizeMod = y - (pos.getY() + height);
			int size = 1 - presizeMod / 2;
			for (int x = pos.getX() - size; x <= pos.getX() + size; x++) {
				int localX = x - pos.getX();
				for (int z = pos.getZ() - size; z <= pos.getZ() + size; z++) {
					int localZ = z - pos.getZ();
					if ((Math.abs(localX) != size || Math.abs(localZ) != size || rand.nextInt(2) != 0 && presizeMod != 0) && isAirOrLeaves(worldObj, new BlockPos(x, y, z))) {
						BlockPos leafPos = new BlockPos(x, y, z);
						if (rand.nextBoolean()) {
							// Set fruit-bearing leaves here
							setLogState(changedBlocks, worldObj, leafPos, FRUIT_LEAF_BLOCK, bb);
						} else {
							// Set plain fruit tree leaves here
						    setLogState(changedBlocks, worldObj, leafPos, REGULAR_LEAF_BLOCK, bb);
						}
					}
				}
			}
		}

		// Tree stem
		for (int y = 0; y < height; y++) {
			BlockPos logPos = pos.up(y);
			if (isAirOrLeaves(worldObj, logPos) || func_214576_j(worldObj, logPos)) {
				setLogState(changedBlocks, worldObj, logPos, WOOD_BLOCK, bb);
			}
		}

		return true;
	}
}