package net.tropicraft.core.common.dimension.noise;

public abstract class NoiseModule {

	public double frequency = 1;
	public double amplitude = 1;

	public abstract double getNoise(double i);
	public abstract double getNoise(double i, double j);
	public abstract double getNoise(double i, double j, double k);

}