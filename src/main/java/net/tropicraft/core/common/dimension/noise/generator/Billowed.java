package net.tropicraft.core.common.dimension.noise.generator;

import net.minecraft.util.RandomSource;
import net.tropicraft.core.common.dimension.noise.FishyNoise;
import net.tropicraft.core.common.dimension.noise.NoiseModule;

public class Billowed extends NoiseModule {
	private final FishyNoise noiseGen;
	private final double offsetX;
	private final double offsetY;
	private final double offsetZ;
	private final int numOctaves;
	private final double persistance;

	public Billowed(long seed, int nOctaves, double p) {
		this.numOctaves = nOctaves;
		this.persistance = p;
		final RandomSource rand = RandomSource.create(seed);
		this.offsetX = rand.nextDouble() / 2 + 0.01D;
		this.offsetY = rand.nextDouble() / 2 + 0.01D;
		this.offsetZ = rand.nextDouble() / 2 + 0.01D;
		this.noiseGen = new FishyNoise(seed);
	}

	@Override
	public double getNoise(double i) {
		return this.getNoise(i, 0.0D);
	}

	@Override
	public double getNoise(double i, double j) {
		i *= this.frequency;
		j *= this.frequency;
		double val = 0;
		double curAmplitude = this.amplitude;
		for(int n = 0; n < this.numOctaves; n++) {
			val += Math.abs(this.noiseGen.noise2d(i + this.offsetX, j + this.offsetY) * curAmplitude);
			i *= 2;
			j *= 2;
			curAmplitude *= this.persistance;
		}
		return val;
	}

	@Override
	public double getNoise(double i, double j, double k) {
		i *= this.frequency;
		j *= this.frequency;
		k *= this.frequency;
		double val = 0;
		for (int n = 0; n < this.numOctaves; n++) {
			val += Math.abs(this.noiseGen.noise3d(i + this.offsetX, j + this.offsetY, k + this.offsetZ) * this.amplitude);
			i *= 2;
			j *= 2;
			k *= 2;
		}
		return val;
	}

}