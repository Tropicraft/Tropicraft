package net.tropicraft.core.common.dimension.biome;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

public class TropicraftRainforestBiome extends TropicraftBiome {

    protected TropicraftRainforestBiome(final float depth, final float scale) {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                .precipitation(RainType.RAIN)
                .category(Category.JUNGLE)
                .depth(depth)
                .scale(scale)
                .temperature(1.5F)
                .downfall(2F)
                .parent(null));
    }
    
    @Override
    public void addFeatures() {
        super.addFeatures();
        DefaultTropicsFeatures.addCarvers(this);
        DefaultTropicsFeatures.addTropicsGems(this);

        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.UP_TREE.get().withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.2F, 1))));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(TropicraftFeatures.SMALL_TUALUNG.get(), NoFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.3F, 1)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(TropicraftFeatures.LARGE_TUALUNG.get(), NoFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.5F, 1)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(TropicraftFeatures.TALL_TREE.get(), NoFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(0, 0.5F, 1)));

        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(TropicraftFeatures.RAINFOREST_FLOWERS.get(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_32, new FrequencyConfig(4)));

        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(TropicraftFeatures.UNDERGROWTH.get(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_32, new FrequencyConfig(100)));

        addStructure(TropicraftFeatures.HOME_TREE.get(), new VillageConfig(Constants.MODID + ":home_tree/starts", 10));
        addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, createDecoratedFeature(TropicraftFeatures.HOME_TREE.get(), new VillageConfig(Constants.MODID + ":home_tree/starts", 10), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));

        addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.OCELOT, 10, 1, 1));
        addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.PARROT, 10, 1, 2));
//
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrog.class, 25, 2, 5));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiSpider.class, 30, 1, 1));

        DefaultBiomeFeatures.addJungleGrass(this);
        DefaultTropicsFeatures.addRainforestPlants(this);
    }
}
