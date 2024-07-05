package net.tropicraft.core.common.dimension.feature.pools;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.HomeTreeBranchPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TropicraftTemplatePools {
    public static final ResourceKey<StructureTemplatePool> KOA_TOWN_CENTERS = createKey("koa_village/town_centers");

    public static final ResourceKey<StructureTemplatePool> KOA_HUTS = createKey("koa_village/huts");

    public static final ResourceKey<StructureTemplatePool> KOA_STREETS = createKey("koa_village/streets");
    public static final ResourceKey<StructureTemplatePool> KOA_TERMINATORS = createKey("koa_village/terminators");

    public static final ResourceKey<StructureTemplatePool> KOA_VILLAGERS = createKey("koa_village/villagers");

    public static final ResourceKey<StructureTemplatePool> KOA_FISH = createKey("koa_village/fish");

    public static final ResourceKey<StructureTemplatePool> HOME_TREE_STARTS = createKey("home_tree/starts");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_ROOFS = createKey("home_tree/roofs");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_DUMMY = createKey("home_tree/dummy");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_TRUNK_MIDDLE = createKey("home_tree/trunks/middle");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_TRUNK_TOP = createKey("home_tree/trunks/top");

    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_SOUTH = createKey("home_tree/branches/south");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_SOUTH_EAST = createKey("home_tree/branches/southeast");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_EAST = createKey("home_tree/branches/east");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_NORTH_EAST = createKey("home_tree/branches/northeast");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_NORTH = createKey("home_tree/branches/north");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_NORTH_WEST = createKey("home_tree/branches/northwest");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_WEST = createKey("home_tree/branches/west");
    public static final ResourceKey<StructureTemplatePool> HOME_TREE_BRANCHES_SOUTH_WEST = createKey("home_tree/branches/southwest");

    public static void bootstrap(final BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);

        Holder.Reference<StructureProcessorList> koaTownCenterProcessors = processorLists.getOrThrow(TropicraftProcessorLists.KOA_TOWN_CENTERS);
        Holder.Reference<StructureProcessorList> koaBuildingProcessors = processorLists.getOrThrow(TropicraftProcessorLists.KOA_BUILDINGS);
        Holder.Reference<StructureProcessorList> koaPathProcessors = processorLists.getOrThrow(TropicraftProcessorLists.KOA_PATH);

        register(context, KOA_TOWN_CENTERS,
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/town_centers/firepit_01", koaTownCenterProcessors, 1)
        );

        register(context, KOA_HUTS,
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/huts/hut_01", koaBuildingProcessors, 5),
                noAirSingle("koa_village/huts/hut_02", koaBuildingProcessors, 2),
                noAirSingle("koa_village/huts/hut_03", koaBuildingProcessors, 3),
                noAirSingle("koa_village/huts/hut_04", koaBuildingProcessors, 4),
                noAirSingle("koa_village/huts/hut_05", koaBuildingProcessors, 10),
                noAirSingle("koa_village/huts/bongo_hut_01", koaBuildingProcessors, 2),
                noAirSingle("koa_village/huts/trade_hut_01", koaBuildingProcessors, 2)
        );

        Holder<StructureTemplatePool> koaTerminators = register(context, KOA_TERMINATORS,
                StructureTemplatePool.Projection.TERRAIN_MATCHING,
                koaPath("koa_village/terminators/terminator_01", 1, koaPathProcessors)
        );

        register(context, KOA_STREETS,
                koaTerminators,
                StructureTemplatePool.Projection.TERRAIN_MATCHING,
                koaPath("koa_village/streets/straight_01", 3, koaPathProcessors),
                koaPath("koa_village/streets/straight_02", 4, koaPathProcessors),
                koaPath("koa_village/streets/straight_03", 10, koaPathProcessors),
                koaPath("koa_village/streets/straight_04", 2, koaPathProcessors),
                koaPath("koa_village/streets/straight_05", 3, koaPathProcessors),
                koaPath("koa_village/streets/straight_06", 2, koaPathProcessors),
                koaPath("koa_village/streets/corner_01", 2, koaPathProcessors),
                koaPath("koa_village/streets/corner_02", 4, koaPathProcessors),
                koaPath("koa_village/streets/corner_03", 6, koaPathProcessors),
                koaPath("koa_village/streets/corner_04", 2, koaPathProcessors),
                koaPath("koa_village/streets/crossroad_01", 5, koaPathProcessors),
                koaPath("koa_village/streets/crossroad_02", 2, koaPathProcessors),
                koaPath("koa_village/streets/crossroad_03", 1, koaPathProcessors),
                koaPath("koa_village/streets/crossroad_04", 2, koaPathProcessors)
        );

        register(context, KOA_VILLAGERS,
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/villagers/unemployed", 1)
        );

        register(context, KOA_FISH,
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/fish/fish_01", 1)
        );

        Holder<StructureProcessorList> homeTreeStartProcessors = processorLists.getOrThrow(TropicraftProcessorLists.HOME_TREE_START);
        Holder<StructureProcessorList> homeTreeBaseProcessors = processorLists.getOrThrow(TropicraftProcessorLists.HOME_TREE_BASE);

        register(context, HOME_TREE_STARTS,
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/trunks/bottom/trunk_0", homeTreeStartProcessors, 1),
                singlePiece("home_tree/trunks/bottom/trunk_1", homeTreeStartProcessors, 1)
        );

        register(context, HOME_TREE_ROOFS,
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/roofs/roof_0", homeTreeBaseProcessors, 1)
        );

        register(context, HOME_TREE_DUMMY,
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/dummy", homeTreeBaseProcessors, 1),
                singlePiece("home_tree/outer_dummy", homeTreeBaseProcessors, 1)
        );

        register(context, HOME_TREE_TRUNK_MIDDLE,
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/trunks/middle/trunk_0", homeTreeBaseProcessors, 1),
                singlePiece("home_tree/trunks/middle/trunk_1", homeTreeBaseProcessors, 1),
                singlePiece("home_tree/trunks/middle/trunk_1_iguanas", homeTreeBaseProcessors, 1),
                singlePiece("home_tree/trunks/middle/trunk_1_ashen", homeTreeBaseProcessors, 1)
        );

        register(context, HOME_TREE_TRUNK_TOP,
                StructureTemplatePool.Projection.RIGID,
                noRotateSingle("home_tree/trunks/top/trunk_0", homeTreeBaseProcessors, 1),
                noRotateSingle("home_tree/trunks/top/trunk_1", homeTreeBaseProcessors, 1),
                noRotateSingle("home_tree/trunks/top/trunk_2", homeTreeBaseProcessors, 1),
                noRotateSingle("home_tree/trunks/top/trunk_3", homeTreeBaseProcessors, 1)
        );

        register(context, HOME_TREE_BRANCHES_SOUTH,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(-30, 30, 4),
                homeTreeBranch(0, 0, 1)
        );
        register(context, HOME_TREE_BRANCHES_SOUTH_EAST,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(30, 60, 4),
                homeTreeBranch(45, 45, 1)
        );
        register(context, HOME_TREE_BRANCHES_EAST,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(60, 120, 4),
                homeTreeBranch(90, 90, 1)
        );
        register(context, HOME_TREE_BRANCHES_NORTH_EAST,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(120, 150, 4),
                homeTreeBranch(135, 135, 1)
        );
        register(context, HOME_TREE_BRANCHES_NORTH,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(150, 210, 4),
                homeTreeBranch(180, 180, 1)
        );
        register(context, HOME_TREE_BRANCHES_NORTH_WEST,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(210, 240, 4),
                homeTreeBranch(225, 225, 1)
        );
        register(context, HOME_TREE_BRANCHES_WEST,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(240, 300, 4),
                homeTreeBranch(270, 270, 1)
        );
        register(context, HOME_TREE_BRANCHES_SOUTH_WEST,
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(300, 330, 4),
                homeTreeBranch(315, 315, 1)
        );
    }

    private static WeightedPiece singlePiece(String path, Holder<StructureProcessorList> processors, int weight) {
        return new WeightedPiece(
                () -> StructurePoolElement.single(Constants.MODID + ":" + path, processors),
                weight
        );
    }

    private static WeightedPiece noAirSingle(String path, Holder<StructureProcessorList> processors, int weight) {
        return new WeightedPiece(
                () -> SingleNoAirJigsawPiece.create(ResourceLocation.fromNamespaceAndPath(Constants.MODID, path), processors, false),
                weight
        );
    }

    private static WeightedPiece noAirSingle(String path, int weight) {
        return new WeightedPiece(
                () -> SingleNoAirJigsawPiece.create(ResourceLocation.fromNamespaceAndPath(Constants.MODID, path)),
                weight
        );
    }

    private static WeightedPiece noRotateSingle(String path, Holder<StructureProcessorList> processors, int weight) {
        return new WeightedPiece(
                () -> NoRotateSingleJigsawPiece.createNoRotate(ResourceLocation.fromNamespaceAndPath(Constants.MODID, path), processors),
                weight
        );
    }

    private static WeightedPiece homeTreeBranch(float minAngle, float maxAngle, int weight) {
        return new WeightedPiece(
                () -> HomeTreeBranchPiece.create(minAngle, maxAngle),
                weight
        );
    }

    private static WeightedPiece koaPath(String path, int weight, Holder<StructureProcessorList> processorList) {
        return new WeightedPiece(
                () -> SingleNoAirJigsawPiece.create(ResourceLocation.fromNamespaceAndPath(Constants.MODID, path), processorList, true),
                weight
        );
    }

    private static Holder.Reference<StructureTemplatePool> register(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, StructureTemplatePool.Projection placementBehaviour, WeightedPiece... pieces) {
        Holder<StructureTemplatePool> empty = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);
        return register(context, key, empty, placementBehaviour, pieces);
    }

    private static Holder.Reference<StructureTemplatePool> register(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, Holder<StructureTemplatePool> fallback, StructureTemplatePool.Projection placementBehaviour, WeightedPiece... pieces) {
        return context.register(key, new StructureTemplatePool(fallback, Arrays.stream(pieces).map(WeightedPiece::resolve).toList(), placementBehaviour));
    }

    private static ResourceKey<StructureTemplatePool> createKey(final String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(Constants.MODID, name));
    }

    private record WeightedPiece(
            Supplier<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>> factory, int weight) {
        public Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> resolve() {
            return new Pair<>(factory.get(), weight);
        }
    }
}
