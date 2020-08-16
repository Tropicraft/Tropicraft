package net.tropicraft.core.common.dimension.biome;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.SingleRandomFeature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class TropicsOceanBiome extends TropicraftBiome {
    public TropicsOceanBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(TropicraftSurfaceBuilders._UNDERWATER, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG.get())
                .precipitation(RainType.RAIN)
                .category(Category.OCEAN)
                .depth(-1.6F)
                .scale(0.4F)
                .temperature(1.5F)
                .downfall(1.25F)
                .parent(null)
        );
    }

    @Override
    public void addFeatures() {
        super.addFeatures();
        DefaultTropicsFeatures.addTropicsMetals(this);
        DefaultTropicsFeatures.addUnderwaterCarvers(this);
        // Various coral features
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SIMPLE_RANDOM_SELECTOR
                .withConfiguration(new SingleRandomFeature(ImmutableList.of(
                    Feature.CORAL_TREE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG),
                    Feature.CORAL_CLAW.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG),
                    Feature.CORAL_MUSHROOM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))))
                .withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED
                    .configure(new TopSolidWithNoiseConfig(20, 400.0D, 0.0D, Heightmap.Type.OCEAN_FLOOR_WG))));
        // Sea floor seagrass
        DefaultBiomeFeatures.addTallSeagrassSparse(this);
        // Seagrass underground
        DefaultTropicsFeatures.addUndergroundSeagrass(this);
        // Ocean floor sea pickles
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SEA_PICKLE.withConfiguration(new CountConfig(20)).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(16))));
        // Cave pickles
        DefaultTropicsFeatures.addUndergroundPickles(this);

        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.MARLIN.get(), 10, 1, 4));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.MAN_O_WAR.get(), 2, 1, 1));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.STARFISH.get(), 4, 1, 4));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.SEA_URCHIN.get(), 4, 1, 4));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.DOLPHIN.get(), 3, 4, 7));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.SEAHORSE.get(), 6, 6, 12));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.SEA_TURTLE.get(), 6, 3, 8));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.TROPICAL_FISH.get(), 20, 4, 8));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.EAGLE_RAY.get(), 6, 1, 1));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.HAMMERHEAD.get(), 2, 1, 1));
        addSpawn(EntityClassification.AMBIENT, new SpawnListEntry(TropicraftEntities.FAILGULL.get(), 15, 5, 10));
    }
}
