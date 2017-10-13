package net.tropicraft.core.common.town;

import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class SpawnLocationData {

	public BlockPos coords;
	public String type;
	public UUID entityUUID;
	
	public SpawnLocationData(BlockPos parCoords, String parType) {
		coords = parCoords;
		type = parType;
	}
}
