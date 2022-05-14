package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;

public final class TropicraftConfiguredStructures {
    public final Holder<ConfiguredStructureFeature<?, ?>> homeTree;
    public final Holder<ConfiguredStructureFeature<?, ?>> koaVillage;

    public TropicraftConfiguredStructures(WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> worldgen, TropicraftTemplatePools templatePools) {
        Register structures = new Register(worldgen);

        this.homeTree = structures.register("home_tree", TropicraftFeatures.HOME_TREE, templatePools.homeTreeStarts, 7);
        this.koaVillage = structures.register("koa_village", TropicraftFeatures.KOA_VILLAGE, templatePools.koaTownCenters, 6);
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> worldgen;

        Register(WorldgenDataConsumer<ConfiguredStructureFeature<?, ?>> worldgen) {
            this.worldgen = worldgen;
        }

        public <C extends FeatureConfiguration, S extends StructureFeature<?>> ConfiguredStructureFeature<?, ?> register(String id, RegistryObject<S> structure, C config) {
            return this.worldgen.register(new ConfiguredStructureFeature<>(structure.get(), config));
        }

        public <S extends StructureFeature<JigsawConfiguration>> ConfiguredStructureFeature<?, ?> register(String id, RegistryObject<S> structure, Holder<StructureTemplatePool> templatePool, int maxDepth) {
            return this.register(id, structure, new JigsawConfiguration(templatePool, maxDepth));
        }
    }
}
