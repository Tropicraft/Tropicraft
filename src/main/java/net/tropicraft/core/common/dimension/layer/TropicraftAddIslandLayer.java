package net.tropicraft.core.common.dimension.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.BishopTransformer;

public final class TropicraftAddIslandLayer implements BishopTransformer {
    private final TropicraftBiomeIds biomeIds;
    private final int chance;
    private final int landId;

    TropicraftAddIslandLayer(TropicraftBiomeIds biomeIds, int chance, int landId) {
        this.biomeIds = biomeIds;
        this.chance = chance;
        this.landId = landId;
    }

    public static TropicraftAddIslandLayer basic2(TropicraftBiomeIds biomeIds) {
        return new TropicraftAddIslandLayer(biomeIds, 2, biomeIds.land);
    }

    public static TropicraftAddIslandLayer basic3(TropicraftBiomeIds biomeIds) {
        return new TropicraftAddIslandLayer(biomeIds, 3, biomeIds.land);
    }

    public static TropicraftAddIslandLayer basic8(TropicraftBiomeIds biomeIds) {
        return new TropicraftAddIslandLayer(biomeIds, 8, biomeIds.land);
    }

    public static TropicraftAddIslandLayer rainforest13(TropicraftBiomeIds biomeIds) {
        return new TropicraftAddIslandLayer(biomeIds, 13, biomeIds.islandMountains);
    }

    @Override
    public int apply(Context random, int ne, int se, int sw, int nw, int center) {
        if (!biomeIds.isLand(nw) && !biomeIds.isLand(sw) && !biomeIds.isLand(ne) && !biomeIds.isLand(se) && !biomeIds.isLand(center) && random.nextRandom(chance) == 0) {
            return landId;
            // TODO - maybe this is incorrect, but in old tropicode we actually didn't return the variable, it was unused return landID;
        }

        return center;
    }
}
