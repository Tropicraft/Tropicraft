package net.tropicraft.core.common.dimension.layer;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

public final class TropicraftBiomeIds {
    public final int ocean;
    public final int kelpForest;
    public final int land;
    public final int river;
    public final int beach;
    public final int islandMountains;
    public final int rainforestPlains;
    public final int rainforestHills;
    public final int rainforestMountains;
    public final int bambooRainforest;
    public final int mangroves;
    public final int overgrownMangroves;

    public TropicraftBiomeIds(Registry<Biome> biomes) {
        this.ocean = getId(biomes, TropicraftBiomes.TROPICS_OCEAN);
        this.kelpForest = getId(biomes, TropicraftBiomes.KELP_FOREST);
        this.land = getId(biomes, TropicraftBiomes.TROPICS);
        this.river = getId(biomes, TropicraftBiomes.TROPICS_RIVER);
        this.beach = getId(biomes, TropicraftBiomes.TROPICS_BEACH);
        this.islandMountains = getId(biomes, TropicraftBiomes.RAINFOREST_ISLAND_MOUNTAINS);
        this.rainforestPlains = getId(biomes, TropicraftBiomes.RAINFOREST_PLAINS);
        this.rainforestHills = getId(biomes, TropicraftBiomes.RAINFOREST_HILLS);
        this.rainforestMountains = getId(biomes, TropicraftBiomes.RAINFOREST_MOUNTAINS);
        this.bambooRainforest = getId(biomes, TropicraftBiomes.BAMBOO_RAINFOREST);
        this.mangroves = getId(biomes, TropicraftBiomes.MANGROVES);
        this.overgrownMangroves = getId(biomes, TropicraftBiomes.OVERGROWN_MANGROVES);
    }

    private static int getId(Registry<Biome> biomes, ResourceKey<Biome> key) {
        return biomes.getId(biomes.get(key));
    }

    public boolean isOcean(final int biome) {
        return biome == this.ocean || biome == this.kelpForest;
    }

    public boolean isRiver(final int biome) {
        return biome == this.river;
    }

    public boolean isLand(final int biome) {
        return biome == this.land;
    }
}
