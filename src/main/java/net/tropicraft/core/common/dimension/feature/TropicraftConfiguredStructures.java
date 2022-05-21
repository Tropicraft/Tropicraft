package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;

import java.util.List;
import java.util.Map;

public final class TropicraftConfiguredStructures {
    public static final DeferredRegister<ConfiguredStructureFeature<?, ?>> REGISTER = DeferredRegister.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, Constants.MODID);

    // TODO: these should really be tags! but we don't have the infrastructure to generate tags for dynamic content
    private static final List<RegistryObject<Biome>> HAS_HOME_TREE = List.of(TropicraftBiomes.RAINFOREST, TropicraftBiomes.BAMBOO_RAINFOREST, TropicraftBiomes.OSA_RAINFOREST);
    private static final List<RegistryObject<Biome>> HAS_KOA_VILLAGE = List.of(TropicraftBiomes.BEACH);

    public static final RegistryObject<ConfiguredStructureFeature<?, ?>> HOME_TREE = register("home_tree", TropicraftFeatures.HOME_TREE, TropicraftTemplatePools.HOME_TREE_STARTS, 7, HAS_HOME_TREE);
    public static final RegistryObject<ConfiguredStructureFeature<?, ?>> KOA_VILLAGE = register("koa_village", TropicraftFeatures.KOA_VILLAGE, TropicraftTemplatePools.KOA_TOWN_CENTERS, 6, HAS_KOA_VILLAGE);

    private static <C extends FeatureConfiguration, S extends StructureFeature<C>> RegistryObject<ConfiguredStructureFeature<?, ?>> register(String id, RegistryObject<S> structure, C config, List<RegistryObject<Biome>> biomes, boolean adaptNoise) {
        return REGISTER.register(id, () -> {
            final HolderSet<Biome> biomeSet = HolderSet.direct(biomes.stream().map(object -> object.getHolder().orElseThrow()).toList());
            return new ConfiguredStructureFeature<>(structure.get(), config, biomeSet, adaptNoise, Map.of());
        });
    }

    private static <S extends StructureFeature<JigsawConfiguration>> RegistryObject<ConfiguredStructureFeature<?, ?>> register(String id, RegistryObject<S> structure, RegistryObject<StructureTemplatePool> templatePool, int maxDepth, List<RegistryObject<Biome>> biomes) {
        return register(id, structure, new JigsawConfiguration(templatePool.getHolder().orElseThrow(), maxDepth), biomes, false);
    }
}
