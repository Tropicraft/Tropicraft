package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

public abstract class TropicraftBiome extends Biome {
    public static final int TROPICS_WATER_COLOR = 0x4eecdf;
    public static final int TROPICS_WATER_FOG_COLOR = 0x041f33;

    protected TropicraftBiome(final Builder builder) {
        super(builder
            .waterColor(TROPICS_WATER_COLOR)
            .waterFogColor(TROPICS_WATER_FOG_COLOR));    }
    
    public void addFeatures() {
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(TropicraftFeatures.VILLAGE.get(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
        
        // Add dummy volcano structure for /locate, this only adds a structure start that places nothing
        addStructure(TropicraftFeatures.VOLCANO.get(), IFeatureConfig.NO_FEATURE_CONFIG);
        // Volcano feature to add tile entity to the volcano generation. Checks in each chunk if a volcano is nearby.
        addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, createDecoratedFeature(TropicraftFeatures.VOLCANO.get(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));

//        this.spawnableCreatureList.add(new SpawnListEntry(EntityParrot.class, 20, 1, 2));
//        this.spawnableCreatureList.add(new SpawnListEntry(EntityVMonkey.class, 20, 1, 3));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityIguana.class, 15, 1, 1));
//
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiCreeper.class, 4, 1, 2));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityEIH.class, 5, 1, 1));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityTropiSkeleton.class, 8, 2, 8));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityAshenHunter.class, 6, 3, 10));
    }
}
