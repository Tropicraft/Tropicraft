package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

import java.util.Arrays;
import java.util.List;

public final class TropicraftProcessorLists {
    public final Holder<StructureProcessorList> koaTownCenters;
    public final Holder<StructureProcessorList> koaBuildings;
    public final Holder<StructureProcessorList> koaPath;

    public final Holder<StructureProcessorList> homeTreeBase;
    public final Holder<StructureProcessorList> homeTreeStart;

    public TropicraftProcessorLists(WorldgenDataConsumer<StructureProcessorList> worldgen) {
        Register processors = new Register(worldgen);

        StructureSupportsProcessor fenceExtender = new StructureSupportsProcessor(false, ImmutableList.of(TropicraftBlocks.BAMBOO_FENCE.getId()));

        this.koaTownCenters = processors.register(
                "koa_village/town_centers",
                fenceExtender,
                new StructureVoidProcessor()
        );

        this.koaBuildings = processors.register(
                "koa_village/buildings",
                new AdjustBuildingHeightProcessor(126),
                fenceExtender,
                new StructureVoidProcessor()
        );

        this.koaPath = processors.register(
                "koa_village/koa_path",
                new SmoothingGravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1),
                new SinkInGroundProcessor(),
                new SteepPathProcessor(),
                new StructureSupportsProcessor(false, List.of(TropicraftBlocks.BAMBOO_FENCE.getId()))
        );

        // TODO add SpawnerProcessor
        this.homeTreeBase = processors.register("home_tree/base", new AirToCaveAirProcessor());
        this.homeTreeStart = processors.register(
                "home_tree/start",
                new AirToCaveAirProcessor(),
                new StructureSupportsProcessor(true, ImmutableList.of(TropicraftBlocks.MAHOGANY_LOG.getId()))
        );
    }

    static final class Register {
        private final WorldgenDataConsumer<StructureProcessorList> worldgen;

        Register(WorldgenDataConsumer<StructureProcessorList> worldgen) {
            this.worldgen = worldgen;
        }

        public Holder<StructureProcessorList> register(String id, StructureProcessor... processors) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), new StructureProcessorList(Arrays.asList(processors)));
        }
    }
}
