package net.tropicraft.core.common.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropics;

import java.util.Random;

public class BiomeGenTropics extends BiomeGenTropicraft {

	public BiomeGenTropics(BiomeProperties props) {
		super(props);
		this.theBiomeDecorator = new BiomeDecoratorTropics();
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {

		BiomeGenTropicsBeach.decorateForVillage(world, rand, pos);

		super.decorate(world, rand, pos);


	}

}
