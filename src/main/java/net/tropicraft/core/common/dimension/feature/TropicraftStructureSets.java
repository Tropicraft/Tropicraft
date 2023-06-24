package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public final class TropicraftStructureSets {
    public static final DeferredRegister<StructureSet> REGISTER = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, Constants.MODID);

    public static final RegistryObject<StructureSet> HOME_TREES = register("home_trees", TropicraftStructures.HOME_TREE, new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 1010101010));
    public static final RegistryObject<StructureSet> KOA_VILLAGES = register("koa_village", TropicraftStructures.KOA_VILLAGE, new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 1010101011));

    public static final RegistryObject<StructureSet> VOLCANOES = register("volcanoes",
            () -> List.of(entry(TropicraftStructures.OCEAN_VOLCANO), entry(TropicraftStructures.LAND_VOLCANO)),
            new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.LINEAR, 916865926)
    );

    private static RegistryObject<StructureSet> register(String id, RegistryObject<Structure> structure, StructurePlacement placement) {
        return register(id, () -> List.of(entry(structure)), placement);
    }

    private static RegistryObject<StructureSet> register(String id, Supplier<List<StructureSet.StructureSelectionEntry>> entries, StructurePlacement placement) {
        return REGISTER.register(id, () -> new StructureSet(entries.get(), placement));
    }

    private static StructureSet.StructureSelectionEntry entry(RegistryObject<Structure> oceanVolcano) {
        return StructureSet.entry(oceanVolcano.getHolder().orElseThrow(), 1);
    }
}
