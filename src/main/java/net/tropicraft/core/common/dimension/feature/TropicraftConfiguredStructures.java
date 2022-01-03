package net.tropicraft.core.common.dimension.feature;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;

import java.util.function.Function;

public final class TropicraftConfiguredStructures {
    public final ConfiguredStructureFeature<?, ?> homeTree;
    public final ConfiguredStructureFeature<?, ?> koaVillage;

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

        public <S extends StructureFeature<?>> ConfiguredStructureFeature<?, ?> register(String id, RegistryObject<S> structure, Function<S, ConfiguredStructureFeature<?, ?>> configure) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), configure.apply(structure.get()));
        }

        public <S extends StructureFeature<JigsawConfiguration>> ConfiguredStructureFeature<?, ?> register(String id, RegistryObject<S> structure, StructureTemplatePool templatePool, int maxDepth) {
            return this.register(id, structure, s -> s.configured(new JigsawConfiguration(() -> templatePool, maxDepth)));
        }
    }
}
