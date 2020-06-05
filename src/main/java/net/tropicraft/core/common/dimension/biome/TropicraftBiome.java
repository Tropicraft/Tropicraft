package net.tropicraft.core.common.dimension.biome;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.entity.TropicraftEntities;

public abstract class TropicraftBiome extends Biome {
    public static final int TROPICS_WATER_COLOR = 0x4eecdf;
    public static final int TROPICS_WATER_FOG_COLOR = 0x041f33;

    protected TropicraftBiome(final Builder builder) {
        super(builder
            .waterColor(TROPICS_WATER_COLOR)
            .waterFogColor(TROPICS_WATER_FOG_COLOR));
    }
    
    public void addFeatures() {
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TropicraftFeatures.VILLAGE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TropicraftFeatures.HOME_TREE.get().withConfiguration(new VillageConfig(Constants.MODID + ":home_tree/starts", 10)).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));

        // Add dummy volcano structure for /locate, this only adds a structure start that places nothing
        addStructure(TropicraftFeatures.VOLCANO.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        // Volcano feature to add tile entity to the volcano generation. Checks in each chunk if a volcano is nearby.
        addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TropicraftFeatures.VOLCANO.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));

        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.PARROT, 20, 1, 2));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.V_MONKEY.get(), 20, 1, 3));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.IGUANA.get(), 15, 4, 4));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.TROPI_CREEPER.get(), 4, 1, 2));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.EIH.get(), 5, 1, 1));

        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.TROPI_SKELLY.get(), 8, 2, 4));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.TROPI_SPIDER.get(), 8, 2, 2));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.ASHEN.get(), 6, 2, 4));
    }
}
