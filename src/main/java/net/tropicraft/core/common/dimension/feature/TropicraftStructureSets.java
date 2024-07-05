package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.tropicraft.Constants;

import java.util.List;

public final class TropicraftStructureSets {
    public static final ResourceKey<StructureSet> HOME_TREES = createKey("home_trees");
    public static final ResourceKey<StructureSet> KOA_VILLAGES = createKey("koa_village");
    public static final ResourceKey<StructureSet> VOLCANOES = createKey("volcanoes");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        context.register(HOME_TREES, new StructureSet(
                structures.getOrThrow(TropicraftStructures.HOME_TREE),
                new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 1010101010)
        ));
        context.register(KOA_VILLAGES, new StructureSet(
                structures.getOrThrow(TropicraftStructures.KOA_VILLAGE),
                new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 1010101011)
        ));

        context.register(VOLCANOES, new StructureSet(
                List.of(
                        StructureSet.entry(structures.getOrThrow(TropicraftStructures.OCEAN_VOLCANO)),
                        StructureSet.entry(structures.getOrThrow(TropicraftStructures.LAND_VOLCANO))
                ),
                new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.LINEAR, 916865926)
        ));
    }

    private static ResourceKey<StructureSet> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(Constants.MODID, name));
    }
}
