package net.tropicraft.core.common.dimension;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

public class WorldProviderTropicraft extends WorldProvider {

	public static final int MID_HEIGHT = 63;
	public static final int MAX_HEIGHT = 256;
	public static final int INTER_HEIGHT = MAX_HEIGHT - MID_HEIGHT;

	protected void registerWorldChunkManager() {
		this.biomeProvider = new BiomeProviderTropicraft(world.getWorldInfo());
	}
	
	@Override
    protected void createBiomeProvider() {
    	this.biomeProvider = new BiomeProviderTropicraft(world.getWorldInfo());
    }

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkProviderTropicraft(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
	}

	@Override
	public String getWelcomeMessage() {
		return "Drifting in to the Tropics of " + this.world.getWorldInfo().getWorldName();
	}

	@Override
	public String getDepartMessage() {
		return "Fading out of the Tropics of " + this.world.getWorldInfo().getWorldName();
	}

	@Override
	public String getSaveFolder() {
		return "TROPICS";
	}

	@Override
	public DimensionType getDimensionType() {
		return TropicraftWorldUtils.tropicsDimension;
	}

}
