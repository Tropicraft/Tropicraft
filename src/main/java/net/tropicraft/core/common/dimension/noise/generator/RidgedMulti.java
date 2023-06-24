package net.tropicraft.core.common.dimension.noise.generator;

import net.minecraft.util.RandomSource;
import net.tropicraft.core.common.dimension.noise.FishyNoise;
import net.tropicraft.core.common.dimension.noise.NoiseModule;

public class RidgedMulti extends NoiseModule {
	private final FishyNoise noiseGen;
	private final double offsetX;
	private final double offsetY;
	private final double offsetZ;
	private final int numOctaves;
	private final double[] spectralWeights = new double[32];

	public RidgedMulti(long seed, int nOctaves) {
		numOctaves = nOctaves;
		final RandomSource rand = RandomSource.create(seed);
		offsetX = rand.nextDouble() / 2 + 0.01D;
		offsetY = rand.nextDouble() / 2 + 0.01D;
		offsetZ = rand.nextDouble() / 2 + 0.01D;
		noiseGen = new FishyNoise(seed);
		for (int i = 0; i < 32; i++) {
			spectralWeights[i] = Math.pow(frequency, -1.0);
			frequency *= 2;
		}
		frequency = 1.0;
	}

	@Override
	public double getNoise(double i) {
		return getNoise(i, 0.0D);
	}

	@Override
	public double getNoise(double i, double j) {
		i *= frequency;
		j *= frequency;
		double val = 0;
		double weight = 1.0;
		final double offset = 1.0;
		final double gain = 2.0;
		for(int n = 0; n < numOctaves; n++) {
			double noise = absolute(noiseGen.noise2d(i + offsetX, j + offsetY));
			noise = offset - noise;
			noise *= noise;
			noise *= weight;

			weight = noise * gain;

			if(weight > 1D) {
				weight = 1D;
			}

			if(weight < 0D) {
				weight = 0D;
			}

			val += noise * this.spectralWeights[n];

			i *= 2;
			j *= 2;
		}
		return val;
	}

	@Override
	public double getNoise(double i, double j, double k) {
		i *= frequency;
		j *= frequency;
		k *= frequency;
		double val = 0;
		double weight = 1.0;
		final double offset = 1.0;
		final double gain = 2.0;
		for(int n = 0; n < numOctaves; n++) {
			double noise = absolute(noiseGen.noise3d(i + offsetX, j + offsetY, k + offsetZ));
			noise = offset - noise;
			noise *= noise;
			noise *= weight;

			weight = noise * gain;

			if(weight > 1D) {
				weight = 1D;
			}

			if(weight < 0D) {
				weight = 0D;
			}

			val += noise * spectralWeights[n];

			i *= 2;
			j *= 2;
			k *= 2;
		}
		return val;
	}

	private double absolute(double d) {
		if (d < 0) {
			d = -d;
		}
		return d;
	}
}
