package net.tropicraft.core.common;

import net.minecraft.util.Mth;

public class BinaryAnimation {
	private final int totalSteps;
	private final int stepIn;
	private final int stepOut;
	private final Easing easing;

	private int value;
	private int lastValue;

	public BinaryAnimation(int lengthIn, int lengthOut, Easing easing) {
		// Some common multiple
		totalSteps = lengthIn * lengthOut;
		stepIn = totalSteps / lengthIn;
		stepOut = totalSteps / lengthOut;
		this.easing = easing;
	}

	public BinaryAnimation(int length, Easing easing) {
		totalSteps = length;
		stepIn = 1;
		stepOut = 1;
		this.easing = easing;
	}

	public void tick(boolean active) {
		lastValue = value;
		if (active) {
			if (value < totalSteps) {
				value = Math.min(value + stepIn, totalSteps);
			}
		} else if (value > 0) {
			value = Math.max(value - stepOut, 0);
		}
	}

	public void setImmediate(boolean active) {
		value = active ? totalSteps : 0;
	}

	public float get(float partialTicks) {
		return easing.apply(Mth.lerp(partialTicks, lastValue, value) / totalSteps);
	}

	public interface Easing {
		float apply(float x);
	}
}
