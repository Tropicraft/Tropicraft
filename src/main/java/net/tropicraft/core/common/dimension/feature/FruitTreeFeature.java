package net.tropicraft.core.common.dimension.feature;

import static net.minecraft.world.gen.feature.AbstractTreeFeature.isAirOrLeaves;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class FruitTreeFeature extends Feature<NoFeatureConfig> {

	private final Supplier<BlockState> WOOD_BLOCK = () -> Blocks.OAK_LOG.getDefaultState();
	private final Supplier<BlockState> REGULAR_LEAF_BLOCK = () -> TropicraftBlocks.FRUIT_LEAVES.get().getDefaultState();
	private final Supplier<BlockState> FRUIT_LEAF_BLOCK;
	private final Supplier<? extends SaplingBlock> sapling;

	public FruitTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> placer, Supplier<? extends SaplingBlock> sapling, Supplier<BlockState> fruitLeaf) {
		super(placer);
		this.sapling = sapling;
		FRUIT_LEAF_BLOCK = fruitLeaf;
	}

	protected SaplingBlock getSapling() {
	    return sapling.get();
	}

	@Override
	public boolean place(IWorld worldObj, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		pos = pos.toImmutable();
		int height = rand.nextInt(3) + 4;

		if (goesBeyondWorldSize(worldObj, pos.getY(), height)) {
			return false;
		}

		if (!isBBAvailable(worldObj, pos, height)) {
			return false;
		}

        if (!getSapling().isValidPosition(getSapling().getDefaultState(), worldObj, pos.down())) {
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
			if (isAirOrLeaves(worldObj, logPos) || AbstractTreeFeature.isTallPlants(worldObj, logPos)) {
				setBlockState(worldObj, logPos, WOOD_BLOCK.get());
			}
		}

		return true;
	}

	protected static boolean isDirt(IWorldGenerationBaseReader worldIn, BlockPos pos) {
		return worldIn.hasBlockState(pos, (p_214590_0_) -> {
			Block block = p_214590_0_.getBlock();
			return isDirt(block) && block != Blocks.GRASS_BLOCK && block != Blocks.MYCELIUM;
		});
	}

	protected void setDirt(IWorldGenerationReader world, BlockPos pos) {
		if (!isDirt(world, pos)) {
			setBlockState(world, pos, Blocks.DIRT.getDefaultState());
		}
	}

	protected void setDirtAt(IWorldGenerationReader reader, BlockPos pos, BlockPos origin) {
		if (!(reader instanceof IWorld)) {
			setDirt(reader, pos);
			return;
		}
		((IWorld)reader).getBlockState(pos).onPlantGrow((IWorld)reader, pos, origin);
	}
}