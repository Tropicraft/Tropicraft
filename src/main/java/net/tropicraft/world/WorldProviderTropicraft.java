package net.tropicraft.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.tropicraft.world.biomes.WorldChunkManagerTropicraft;
import net.tropicraft.world.chunk.ChunkProviderTropicraft;

public class WorldProviderTropicraft extends WorldProvider {

	public static final int MID_HEIGHT = 63;
	public static final int MAX_HEIGHT = 256;
	public static final int INTER_HEIGHT = MAX_HEIGHT - MID_HEIGHT;
	
    protected void registerWorldChunkManager()
    {
        this.worldChunkMgr = new WorldChunkManagerTropicraft(worldObj.getSeed(), terrainType);
    }

    public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderTropicraft(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
    }
    
	@Override
	public String getWelcomeMessage() {
		return "Drifting in to the Tropics of " + this.worldObj.getWorldInfo().getWorldName();
	}
	
	@Override
	public String getDepartMessage() {
		return "Fading out of the Tropics of " + this.worldObj.getWorldInfo().getWorldName();
	}
	
	@Override
	public String getDimensionName() {
		return "Tropics";
	}
	
	@Override
	public String getSaveFolder() {
		return "TROPICS";
	}
	
}
