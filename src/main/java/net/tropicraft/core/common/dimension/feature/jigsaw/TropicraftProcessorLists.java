package net.tropicraft.core.common.dimension.feature.jigsaw;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.List;
import java.util.function.Supplier;

public final class TropicraftProcessorLists {
    public static final DeferredRegister<StructureProcessorList> REGISTER = DeferredRegister.create(Registry.PROCESSOR_LIST_REGISTRY, Constants.MODID);

    private static StructureSupportsProcessor fenceExtender() {
        return new StructureSupportsProcessor(false, HolderSet.direct(Holder.hackyErase(TropicraftBlocks.BAMBOO_FENCE.getHolder().orElseThrow())));
    }

    public static final RegistryObject<StructureProcessorList> KOA_TOWN_CENTERS = register(
            "koa_village/town_centers",
            () -> List.of(
                    fenceExtender(),
                    new StructureVoidProcessor()
            )
    );

    public static final RegistryObject<StructureProcessorList> KOA_BUILDINGS = register(
            "koa_village/buildings",
            () -> List.of(
                    new AdjustBuildingHeightProcessor(126),
                    fenceExtender(),
                    new StructureVoidProcessor()
            )
    );

    public static final RegistryObject<StructureProcessorList> KOA_PATH = register(
            "koa_village/koa_path",
            () -> List.of(
                    new SmoothingGravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1),
                    new SinkInGroundProcessor(),
                    new SteepPathProcessor(),
                    fenceExtender()
            )
    );

    // TODO add SpawnerProcessor
    public static final RegistryObject<StructureProcessorList> HOME_TREE_BASE = register("home_tree/base", () -> List.of(new AirToCaveAirProcessor()));
    public static final RegistryObject<StructureProcessorList> HOME_TREE_START = register(
            "home_tree/start",
            () -> List.of(
                    new AirToCaveAirProcessor(),
                    new StructureSupportsProcessor(true, HolderSet.direct(Holder.hackyErase(TropicraftBlocks.MAHOGANY_LOG.getHolder().orElseThrow())))
            )
    );

    private static RegistryObject<StructureProcessorList> register(String id, Supplier<List<StructureProcessor>> processors) {
        return REGISTER.register(id, () -> new StructureProcessorList(processors.get()));
    }
}
