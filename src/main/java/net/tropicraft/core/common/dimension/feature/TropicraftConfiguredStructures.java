package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;

import java.util.Map;

public final class TropicraftConfiguredStructures {
    public final Holder<ConfiguredStructureFeature<?, ?>> homeTree;
    public final Holder<ConfiguredStructureFeature<?, ?>> koaVillage;

    public TropicraftConfiguredStructures(WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> worldgen, TropicraftBiomes biomes, TropicraftTemplatePools templatePools) {
        Register structures = new Register(worldgen);

        // TODO: these should really be tags! but we don't have the infrastructure to generate tags for dynamic content
        final HolderSet<Biome> hasHomeTree = HolderSet.direct(biomes.bambooRainforest, biomes.osaRainforest, biomes.rainforestHills, biomes.rainforestPlains, biomes.rainforestIslandMountains, biomes.rainforestMountains);
        final HolderSet<Biome> hasKoaVillage = HolderSet.direct(biomes.tropicsBeach);

        this.homeTree = structures.register("home_tree", TropicraftFeatures.HOME_TREE, templatePools.homeTreeStarts, 7, hasHomeTree);
        this.koaVillage = structures.register("koa_village", TropicraftFeatures.KOA_VILLAGE, templatePools.koaTownCenters, 6, hasKoaVillage);
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> worldgen;

        Register(WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> worldgen) {
            this.worldgen = worldgen;
        }

        public <C extends FeatureConfiguration, S extends StructureFeature<C>> Holder<ConfiguredStructureFeature<?, ?>> register(String id, RegistryObject<S> structure, C config, HolderSet<Biome> biomes, boolean adaptNoise) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), new ConfiguredStructureFeature<>(structure.get(), config, biomes, adaptNoise, Map.of()));
        }

        public <S extends StructureFeature<JigsawConfiguration>> Holder<ConfiguredStructureFeature<?, ?>> register(String id, RegistryObject<S> structure, Holder<StructureTemplatePool> templatePool, int maxDepth, HolderSet<Biome> biomes) {
            return this.register(id, structure, new JigsawConfiguration(templatePool, maxDepth), biomes, true);
        }
    }
}
