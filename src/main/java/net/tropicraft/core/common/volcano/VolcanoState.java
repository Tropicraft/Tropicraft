package net.tropicraft.core.common.volcano;

public enum VolcanoState {
	DORMANT(600), SMOKING(600), RISING(600), ERUPTING(600), RETREATING(600);
	
	private final int duration;
	
	VolcanoState(int duration) {
		this.duration = duration;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public static int getTimeBefore(VolcanoState state) {
		switch(state) {
		case DORMANT:
			return RETREATING.duration;
		case SMOKING:
			return DORMANT.duration;
		case RISING:
			return SMOKING.duration;
		case ERUPTING:
			return RISING.duration;
		case RETREATING:
			return ERUPTING.duration;
		default:
			return 2000;
		}
	}
}
