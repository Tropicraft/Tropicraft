package net.tropicraft.core.common.biome;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsBeach;
import net.tropicraft.core.common.dimension.WorldProviderTropicraft;
import net.tropicraft.core.common.worldgen.village.TownKoaVillageGenHelper;
import net.tropicraft.core.registry.BlockRegistry;

import java.util.Random;

public class BiomeGenTropicsBeach extends BiomeGenTropicraft {

	private static final int VILLAGE_CHANCE = 15;

	public BiomeGenTropicsBeach(BiomeProperties props) {
		super(props);
		this.theBiomeDecorator = new BiomeDecoratorTropicsBeach();
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}

	public static void decorateForVillage(World world, Random rand, BlockPos pos) {
		if(rand.nextInt(VILLAGE_CHANCE) == 0) {
			boolean success = false;
			for (int ii = 0; ii < 20 && !success; ii++) {
				int i = randCoord(rand, pos.getX(), 16);
				int k = randCoord(rand, pos.getZ(), 16);
				int y = world.getTopSolidOrLiquidBlock(new BlockPos(i, 0, k)).getY();
				if (y < WorldProviderTropicraft.MID_HEIGHT) y = WorldProviderTropicraft.MID_HEIGHT+1;
				success = TownKoaVillageGenHelper.hookTryGenVillage(new BlockPos(i, y/*getTerrainHeightAt(world, i, k)*/, k), world);
			}
		}
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {

		decorateForVillage(world, rand, pos);

		super.decorate(world, rand, pos);


	}

	public static final int randCoord(Random rand, int base, int variance) {
		return base + rand.nextInt(variance);
	}

	public int getTerrainHeightAt(World world, int x, int z)
	{
		for(int y = world.getHeight(new BlockPos(x, 0, z)).getY() + 1; y > 0; y--)
		{
			IBlockState state = world.getBlockState(new BlockPos(x, y, z));
			Material mat = state.getMaterial();
			Block id = state.getBlock();
			if(id == Blocks.GRASS || id == Blocks.DIRT || id == Blocks.SAND || mat == Material.SAND)
			{
				return y + 1;
			}
		}
		return 0;
	}
}
