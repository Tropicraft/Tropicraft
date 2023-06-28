package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Arrays;
import java.util.List;

public final class TropicraftProcessorLists {
    public static final ResourceKey<StructureProcessorList> KOA_TOWN_CENTERS = createKey("koa_village/town_centers");
    public static final ResourceKey<StructureProcessorList> KOA_BUILDINGS = createKey("koa_village/buildings");
    public static final ResourceKey<StructureProcessorList> KOA_PATH = createKey("koa_village/koa_path");

    public static final ResourceKey<StructureProcessorList> HOME_TREE_BASE = createKey("home_tree/base");
    public static final ResourceKey<StructureProcessorList> HOME_TREE_START = createKey("home_tree/start");

    public static void bootstrap(final BootstapContext<StructureProcessorList> context) {
        StructureSupportsProcessor fenceExtender = new StructureSupportsProcessor(false, holderSet(TropicraftBlocks.BAMBOO_FENCE));

        context.register(KOA_TOWN_CENTERS, new StructureProcessorList(List.of(
                fenceExtender,
                new StructureVoidProcessor()
        )));

        context.register(KOA_BUILDINGS, new StructureProcessorList(List.of(
                new AdjustBuildingHeightProcessor(126),
                fenceExtender,
                new StructureVoidProcessor()
        )));

        context.register(KOA_PATH, new StructureProcessorList(List.of(
                new SmoothingGravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1),
                new SinkInGroundProcessor(),
                new SteepPathProcessor(),
                fenceExtender
        )));

        // TODO add SpawnerProcessor
        context.register(HOME_TREE_BASE, new StructureProcessorList(List.of(new AirToCaveAirProcessor())));
        context.register(HOME_TREE_START, new StructureProcessorList(List.of(
                new AirToCaveAirProcessor(),
                new StructureSupportsProcessor(true, holderSet(TropicraftBlocks.MAHOGANY_LOG)))
        ));
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    private static HolderSet.Direct<Block> holderSet(BlockEntry<? extends Block>... blocks) {
        return HolderSet.direct(Arrays.stream(blocks).map(object -> object.getHolder().orElseThrow()).toArray(Holder[]::new));
    }

    private static ResourceKey<StructureProcessorList> createKey(final String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation(Constants.MODID, name));
    }
}
