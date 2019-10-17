package net.tropicraft.core.common.dimension.biome;

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
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class TropicsOceanBiome extends TropicraftBiome {
    public TropicsOceanBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG.get())
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
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.SIMPLE_RANDOM_SELECTOR, new SingleRandomFeature(new Feature[]{Feature.CORAL_TREE, Feature.CORAL_CLAW, Feature.CORAL_MUSHROOM}, new IFeatureConfig[]{IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG}), Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED, new TopSolidWithNoiseConfig(20, 400.0D, 0.0D, Heightmap.Type.OCEAN_FLOOR_WG)));
        // Sea floor seagrass
        DefaultBiomeFeatures.func_222309_aj(this);
        // Seagrass underground
        DefaultTropicsFeatures.addUndergroundSeagrass(this);
        // Ocean floor sea pickles
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.SEA_PICKLE, new CountConfig(20), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig(16)));
        // Cave pickles
        DefaultTropicsFeatures.addUndergroundPickles(this);

//        this.addSpawn(new SpawnListEntry(EntitySeahorse.class, 6, 6, 12));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.MARLIN.get(), 10, 1, 4));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityManOWar.class, 2, 1, 1));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityStarfish.class, 4, 1, 4));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaUrchin.class, 4, 1, 4));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.DOLPHIN.get(), 3, 4, 7));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaTurtle.class, 6, 3, 8));
        addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(TropicraftEntities.SEA_TURTLE.get(), 6, 3, 8));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityFailgull.class, 30, 5, 15));
        addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TropicraftEntities.FAILGULL.get(), 30, 5, 15));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityTropicalFish.class, 20, 1, 1));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityEagleRay.class, 6, 2, 4));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityShark.class, 3, 1, 3));
    }
}
