package net.tropicraft.core.common.dimension.config;

import net.minecraft.world.gen.GenerationSettings;

public class TropicraftGeneratorSettings extends GenerationSettings {
    public int getBiomeSize() {
        return 4;
    }

    public int getRiverSize() {
        return 4;
    }

    public int getBiomeId() {
        return -1;
    }

    public int getBedrockFloorHeight() {
        return 0;
    }
}
