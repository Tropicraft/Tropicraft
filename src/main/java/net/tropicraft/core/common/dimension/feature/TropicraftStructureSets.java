package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

import java.util.List;
import java.util.function.Supplier;

public final class TropicraftStructureSets {
    public static final DeferredRegister<StructureSet> REGISTER = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, Constants.MODID);

    public static final RegistryObject<StructureSet> HOME_TREES = register("home_trees", TropicraftConfiguredStructures.HOME_TREE, new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR,1010101010 ));
    public static final RegistryObject<StructureSet> KOA_VILLAGES = register("koa_village", TropicraftConfiguredStructures.KOA_VILLAGE, new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 1010101011));

    private static RegistryObject<StructureSet> register(String id, RegistryObject<ConfiguredStructureFeature<?, ?>> structure, StructurePlacement placement) {
        return register(id, () -> List.of(new StructureSet.StructureSelectionEntry(structure.getHolder().orElseThrow(), 1)), placement);
    }

    private static RegistryObject<StructureSet> register(String id, Supplier<List<StructureSet.StructureSelectionEntry>> entries, StructurePlacement placement) {
        return REGISTER.register(id, () -> new StructureSet(entries.get(), placement));
    }
}
