package net.tropicraft.core.common.dimension;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderTropicraft extends WorldProvider {

	public static final int MID_HEIGHT = 63;
	public static final int MAX_HEIGHT = 256;
	public static final int INTER_HEIGHT = MAX_HEIGHT - MID_HEIGHT;
	
	@Override
    protected void init() {
		super.init();
    	this.biomeProvider = new BiomeProviderTropicraft(world.getWorldInfo());
    }

	@Override
	public IChunkGenerator createChunkGenerator() {
		super.createChunkGenerator();
		return new ChunkProviderTropicraft(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
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
