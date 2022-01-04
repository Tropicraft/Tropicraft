package net.tropicraft.core.common.dimension.feature;

import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

import java.util.function.Function;

public class TropicraftPlacements {
    // VEGETATION
//    public final PlacedFeature BAMBOO;
//    public final PlacedFeature TROPICS_FLOWERS;

    // STRUCTURES


    public TropicraftPlacements(WorldgenDataConsumer<? extends PlacedFeature> worldgen) {
        Register features = new Register(worldgen);

//        BAMBOO = features.register("bamboo",
//                Feature.BAMBOO.configured(new ProbabilityFeatureConfiguration(0.15F)).placed(
//                        NoiseBasedCountPlacement.of(70, 140.0D, 0.3D),
//                        InSquarePlacement.spread(),
//                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
//                        BiomeFilter.biome()
//                )
//        );
//
//        TROPICS_FLOWERS = features.register("tropics_flowers",
//            TropicraftConfiguredFeatures.tropicsFlowers.placed(RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
//        );

    }

    static final class Register {
        private final WorldgenDataConsumer<PlacedFeature> worldgen;

        @SuppressWarnings("unchecked")
        Register(WorldgenDataConsumer<? extends PlacedFeature> worldgen) {
            this.worldgen = (WorldgenDataConsumer<PlacedFeature>) worldgen;
        }

        public <F extends Feature<?>> PlacedFeature register(String id, PlacedFeature feature) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), feature);
        }

        public <F extends Feature<?>> PlacedFeature register(String id, F feature, Function<F, PlacedFeature> configure) {
            return this.register(id, configure.apply(feature));
        }

        public <F extends Feature<?>> PlacedFeature register(String id, RegistryObject<F> feature, Function<F, PlacedFeature> configure) {
            return this.register(id, feature.get(), configure);
        }
    }
}
