package net.tropicraft.world.genlayer;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTropicraftZoom extends GenLayerTropicraft {

    public GenLayerTropicraftZoom(long par1, GenLayerTropicraft parent)
    {
        super(par1);
        super.parent = parent;
        this.setZoom(1);
    }
    
    public int[] getInts(int x, int y, int width, int length)
    {
		int x2 = x >> 1;
		int y2 = y >> 1;
		int width2 = (width >> 1) + 2;
		int length2 = (length >> 1) + 2;
		int[] parentMap = this.parent.getInts(x2, y2, width2, length2);
		int reshiftedWidth = width2 - 1 << 1;
		int reshiftedLength = length2 - 1 << 1;
		int[] resultMap = IntCache
				.getIntCache(reshiftedWidth * reshiftedLength);
		int index;

		for(int j = 0; j < length2 - 1; ++j) {
			index = (j << 1) * reshiftedWidth;
			int i = 0;
			int backXY = parentMap[i + 0 + (j + 0) * width2];

			for(int backX = parentMap[i + 0 + (j + 1) * width2]; i < width2 - 1; ++i) {
				this.initChunkSeed((long) (i + x2 << 1), (long) (j + y2 << 1));
				int backY = parentMap[i + 1 + (j + 0) * width2];
				int cur = parentMap[i + 1 + (j + 1) * width2];
				resultMap[index] = backXY;
				resultMap[index++ + reshiftedWidth] = this.selectRandom(new int[] { backXY, backX });
				resultMap[index] = this.selectRandom(new int[] { backXY, backY });
				resultMap[index++ + reshiftedWidth] = this.selectModeOrRandom(backXY, backY, backX, cur);
				backXY = backY;
				backX = cur;
			}
		}

		int[] aint2 = IntCache.getIntCache(width * length);

		for(index = 0; index < length; ++index) {
			System.arraycopy(resultMap, (index + (y & 1)) * reshiftedWidth + (x & 1), aint2, index * width, width);
		}

		return aint2;
    }
    
    public static GenLayerTropicraft magnify(long seed, GenLayerTropicraft parent, int zoomAmount)
    {
        GenLayerTropicraft zoomLayers = parent;

        for (int zoom = 0; zoom < zoomAmount; ++zoom) {
            zoomLayers = new GenLayerTropicraftZoom(seed + zoom, zoomLayers);
        }

        return zoomLayers;
    }
    
    @Override
    public void setZoom(int zoom) {
    	this.zoom = zoom;
    	this.parent.setZoom(zoom * 2);
    }
    
}