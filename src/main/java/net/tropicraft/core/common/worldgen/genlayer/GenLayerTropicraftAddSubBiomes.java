package net.tropicraft.core.common.worldgen.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftAddSubBiomes extends GenLayerTropicraft {
	
	private int baseID;
	private int[] subBiomeIDs;
	
	public GenLayerTropicraftAddSubBiomes(long seed, GenLayerTropicraft parent, int baseID, int[] subBiomeIDs) {
		super(seed);
		this.parent = parent;
		this.baseID = baseID;
		this.subBiomeIDs = subBiomeIDs;
	}

	@Override
	public int[] getInts(int x, int y, int width, int length) {
		int[] parentMap = this.parent.getInts(x, y, width, length);
        int[] resultMap = IntCache.getIntCache(width * length);

		for(int j = 0; j < length; ++j) {
			for(int i = 0; i < width; ++i) {
				this.initChunkSeed((long) (i + x), (long) (j + y));
				int cur = parentMap[i + j * width];

				if(cur == baseID) {
					resultMap[i + j * width] = subBiomeIDs[this.nextInt(subBiomeIDs.length)];
				} else {
					resultMap[i + j * width] = cur;
				}
			}
		}

        return resultMap;
	}

	@Override
	public void setZoom(int zoom) {
		this.zoom = zoom;
		this.parent.setZoom(zoom);
	}
	
}
