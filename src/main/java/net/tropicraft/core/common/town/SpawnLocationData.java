package net.tropicraft.core.common.town;

import net.minecraft.util.ChunkCoordinates;

import java.util.UUID;

public class SpawnLocationData {

	public ChunkCoordinates coords;
	public String type;
	public UUID entityUUID;
	
	public SpawnLocationData(ChunkCoordinates parCoords, String parType) {
		coords = parCoords;
		type = parType;
	}
}
