package net.tropicraft.core.common.dimension.biome;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class TropicsBiome extends TropicraftBiome {
    protected TropicsBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(TropicraftSurfaceBuilders._TROPICS, TropicsBuilderConfigs.TROPICS_CONFIG.get())
                .precipitation(RainType.RAIN)
                .category(Category.PLAINS)
                .depth(0.1F)
                .scale(0.1F)
                .temperature(2.0F)
                .downfall(1.5F)
                .parent(null));
    }

    @Override
    public void addFeatures() {
        super.addFeatures();

        DefaultTropicsFeatures.addCarvers(this);

        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.GRAPEFRUIT_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.2F, 1))));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.ORANGE_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.2F, 1))));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.LEMON_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.2F, 1))));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.LIME_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.2F, 1))));

        DefaultTropicsFeatures.addPalmTrees(this);
        
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TropicraftFeatures.EIH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.01F, 1))));

        DefaultTropicsFeatures.addTropicsFlowers(this);
        DefaultTropicsFeatures.addPineapples(this);
        
        addSpawn(EntityClassification.AMBIENT, new SpawnListEntry(TropicraftEntities.FAILGULL.get(), 10, 5, 15));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.TROPI_BEE.get(), 10, 4, 4));
        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.COWKTAIL.get(), 10, 4, 4));

        addSpawn(EntityClassification.MONSTER, new SpawnListEntry(TropicraftEntities.TREE_FROG.get(), 4, 4, 4));

        DefaultBiomeFeatures.addGrass(this);
        DefaultBiomeFeatures.addTallGrass(this);
    }
}
