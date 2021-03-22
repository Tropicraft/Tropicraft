package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

import java.util.Arrays;

public final class TropicraftProcessorLists {
    public final StructureProcessorList koaTownCenters;
    public final StructureProcessorList koaBuildings;

    public final StructureProcessorList homeTreeBase;
    public final StructureProcessorList homeTreeStart;

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

        public StructureProcessorList register(String id, StructureProcessor... processors) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), new StructureProcessorList(Arrays.asList(processors)));
        }
    }
}
