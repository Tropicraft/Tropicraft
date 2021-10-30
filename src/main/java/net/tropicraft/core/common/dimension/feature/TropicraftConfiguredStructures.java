package net.tropicraft.core.common.dimension.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;

import java.util.function.Function;

public final class TropicraftConfiguredStructures {
    public final StructureFeature<?, ?> homeTree;
    public final StructureFeature<?, ?> koaVillage;

    public TropicraftConfiguredStructures(WorldgenDataConsumer<StructureFeature<?, ?>> worldgen, TropicraftTemplatePools templatePools) {
        Register structures = new Register(worldgen);

        this.homeTree = structures.register("home_tree", TropicraftFeatures.HOME_TREE, templatePools.homeTreeStarts, 7);
        this.koaVillage = structures.register("koa_village", TropicraftFeatures.KOA_VILLAGE, templatePools.koaTownCenters, 6);
    }

    static final class Register {
        private final WorldgenDataConsumer<StructureFeature<?, ?>> worldgen;

        Register(WorldgenDataConsumer<StructureFeature<?, ?>> worldgen) {
            this.worldgen = worldgen;
        }

        public <S extends Structure<?>> StructureFeature<?, ?> register(String id, RegistryObject<S> structure, Function<S, StructureFeature<?, ?>> configure) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), configure.apply(structure.get()));
        }

        public <S extends Structure<VillageConfig>> StructureFeature<?, ?> register(String id, RegistryObject<S> structure, JigsawPattern templatePool, int maxDepth) {
            return this.register(id, structure, s -> s.configured(new VillageConfig(() -> templatePool, maxDepth)));
        }
    }
}
