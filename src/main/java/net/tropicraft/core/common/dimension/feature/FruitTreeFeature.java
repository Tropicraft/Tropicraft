package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractSmallTreeFeature;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class FruitTreeFeature extends AbstractSmallTreeFeature<TreeFeatureConfig> {

	private final Supplier<BlockState> WOOD_BLOCK = () -> Blocks.OAK_LOG.getDefaultState();
	private final Supplier<BlockState> REGULAR_LEAF_BLOCK = () -> TropicraftBlocks.FRUIT_LEAVES.get().getDefaultState();
	private final Supplier<BlockState> FRUIT_LEAF_BLOCK;
	private final Supplier<? extends IPlantable> sapling;

	public <T extends Block & IPlantable> FruitTreeFeature(Function<Dynamic<?>, ? extends TreeFeatureConfig> placer, Supplier<T> sapling, Supplier<BlockState> fruitLeaf) {
		super(placer);
		this.sapling = sapling;
		FRUIT_LEAF_BLOCK = fruitLeaf;
	}

	@Override
	protected boolean place(IWorldGenerationReader worldObj, Random rand, BlockPos pos, Set<BlockPos> p_225557_4_, Set<BlockPos> p_225557_5_, MutableBoundingBox bb, TreeFeatureConfig config) {
		pos = pos.toImmutable();
		int height = rand.nextInt(3) + 4;

		if (goesBeyondWorldSize(worldObj, pos.getY(), height)) {
			return false;
		}

		if (!isBBAvailable(worldObj, pos, height)) {
			return false;
		}

		if (!isSoil(worldObj, pos.down(), getSapling())) {
			return false;
		}

		setDirtAt(worldObj, pos, pos.down());

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
							setBlockState(worldObj, leafPos, FRUIT_LEAF_BLOCK.get());
						} else {
							// Set plain fruit tree leaves here
							setBlockState(worldObj, leafPos, REGULAR_LEAF_BLOCK.get());
						}
					}
				}
			}
		}

		// Tree stem
		for (int y = 0; y < height; y++) {
			BlockPos logPos = pos.up(y);
			if (isAirOrLeaves(worldObj, logPos) || isTallPlants(worldObj, logPos)) {
				setBlockState(worldObj, logPos, WOOD_BLOCK.get());
			}
		}

		return true;
	}

	protected IPlantable getSapling() {
	    return sapling.get();
	}

}