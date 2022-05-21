package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Arrays;
import java.util.List;

public final class TropicraftProcessorLists {
    public static final DeferredRegister<StructureProcessorList> REGISTER = DeferredRegister.create(Registry.PROCESSOR_LIST_REGISTRY, Constants.MODID);

    private static final StructureSupportsProcessor FENCE_EXTENDER = new StructureSupportsProcessor(false, List.of(TropicraftBlocks.BAMBOO_FENCE.getId()));

    public static final RegistryObject<StructureProcessorList> KOA_TOWN_CENTERS = register(
            "koa_village/town_centers",
            FENCE_EXTENDER,
            new StructureVoidProcessor()
    );

    public static final RegistryObject<StructureProcessorList> KOA_BUILDINGS = register(
            "koa_village/buildings",
            new AdjustBuildingHeightProcessor(126),
            FENCE_EXTENDER,
            new StructureVoidProcessor()
    );

    public static final RegistryObject<StructureProcessorList> KOA_PATH = register(
            "koa_village/koa_path",
            new SmoothingGravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1),
            new SinkInGroundProcessor(),
            new SteepPathProcessor(),
            new StructureSupportsProcessor(false, List.of(TropicraftBlocks.BAMBOO_FENCE.getId()))
    );

    // TODO add SpawnerProcessor
    public static final RegistryObject<StructureProcessorList> HOME_TREE_BASE = register("home_tree/base", new AirToCaveAirProcessor());
    public static final RegistryObject<StructureProcessorList> HOME_TREE_START = register(
            "home_tree/start",
            new AirToCaveAirProcessor(),
            new StructureSupportsProcessor(true, ImmutableList.of(TropicraftBlocks.MAHOGANY_LOG.getId()))
    );

    private static RegistryObject<StructureProcessorList> register(String id, StructureProcessor... processors) {
        return REGISTER.register(id, () -> new StructureProcessorList(Arrays.asList(processors)));
    }
}
