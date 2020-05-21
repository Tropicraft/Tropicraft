package net.tropicraft.core.common.dimension.config;

import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

public class TropicraftBiomeProviderSettings implements IBiomeProviderSettings {
    private WorldInfo worldInfo;
    private TropicraftGeneratorSettings generatorSettings;

    public TropicraftBiomeProviderSettings(WorldInfo info) {
        this.worldInfo = info;
    }

    public TropicraftBiomeProviderSettings setWorldInfo(WorldInfo worldInfo) {
        this.worldInfo = worldInfo;
        return this;
    }

    public TropicraftBiomeProviderSettings setGeneratorSettings(TropicraftGeneratorSettings settings) {
        this.generatorSettings = settings;
        return this;
    }

    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }

    public TropicraftGeneratorSettings getGeneratorSettings() {
        return this.generatorSettings;
    }
}
