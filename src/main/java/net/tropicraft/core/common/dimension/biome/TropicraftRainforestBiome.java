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
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.entity.TropicraftEntities;

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
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.SMALL_TUALUNG.get().withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.3F, 1))));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.LARGE_TUALUNG.get().withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.5F, 1))));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.TALL_TREE.get().withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.5F, 1))));

        // TODO used a dummy config here for 1.15 - fix later
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.RAINFOREST_FLOWERS.get().withConfiguration(DefaultBiomeFeatures.ROSE_BUSH_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(4))));

        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.COFFEE_BUSH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(5))));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.UNDERGROWTH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(100))));

        addStructure(TropicraftFeatures.HOME_TREE.get().withConfiguration(new VillageConfig(Constants.MODID + ":home_tree/starts", 10)));

        addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.OCELOT, 10, 1, 1));
        addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.PARROT, 10, 1, 2));

        addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TropicraftEntities.TREE_FROG.get(), 25, 2, 5));
        addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TropicraftEntities.TROPI_SPIDER.get(), 30, 1, 1));

        DefaultBiomeFeatures.addJungleGrass(this);
        DefaultTropicsFeatures.addRainforestPlants(this);
    }
}
