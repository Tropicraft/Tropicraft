package net.tropicraft.core.common.dimension.feature.pools;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.HomeTreeBranchPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;

import java.util.Arrays;
import java.util.function.Function;

public final class TropicraftTemplatePools {
    public final Holder<StructureTemplatePool> koaTownCenters;
    public final Holder<StructureTemplatePool> koaHuts;
    public final Holder<StructureTemplatePool> koaStreets;
    public final Holder<StructureTemplatePool> koaTerminators;
    public final Holder<StructureTemplatePool> koaVillagers;
    public final Holder<StructureTemplatePool> koaFish;
    public final Holder<StructureTemplatePool> homeTreeStarts;
    public final Holder<StructureTemplatePool> homeTreeRoofs;
    public final Holder<StructureTemplatePool> homeTreeDummy;
    public final Holder<StructureTemplatePool> homeTreeTrunkMiddle;
    public final Holder<StructureTemplatePool> homeTreeTrunkTop;
    public final Holder<StructureTemplatePool> homeTreeBranchesSouth;
    public final Holder<StructureTemplatePool> homeTreeBranchesSouthEast;
    public final Holder<StructureTemplatePool> homeTreeBranchesEast;
    public final Holder<StructureTemplatePool> homeTreeBranchesNorthEast;
    public final Holder<StructureTemplatePool> homeTreeBranchesNorth;
    public final Holder<StructureTemplatePool> homeTreeBranchesNorthWest;
    public final Holder<StructureTemplatePool> homeTreeBranchesWest;
    public final Holder<StructureTemplatePool> homeTreeBranchesSouthWest;

    public TropicraftTemplatePools(WorldgenDataConsumer<StructureTemplatePool> worldgen, TropicraftConfiguredFeatures features, TropicraftProcessorLists processors) {
        Register pools = new Register(worldgen);

        this.koaTownCenters = pools.register(
                "koa_village/town_centers",
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/town_centers/firepit_01", processors.koaTownCenters, 1)
        );

        this.koaHuts = pools.register(
                "koa_village/huts",
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/huts/hut_01", processors.koaBuildings, 5),
                noAirSingle("koa_village/huts/hut_02", processors.koaBuildings, 2),
                noAirSingle("koa_village/huts/hut_03", processors.koaBuildings, 3),
                noAirSingle("koa_village/huts/hut_04", processors.koaBuildings, 4),
                noAirSingle("koa_village/huts/hut_05", processors.koaBuildings, 10),
                noAirSingle("koa_village/huts/bongo_hut_01", processors.koaBuildings, 2),
                noAirSingle("koa_village/huts/trade_hut_01", processors.koaBuildings, 2)
        );

        this.koaStreets = pools.register(
                "koa_village/streets",
                new ResourceLocation(Constants.MODID, "koa_village/terminators"),
                StructureTemplatePool.Projection.TERRAIN_MATCHING,
                koaPath(processors, "koa_village/streets/straight_01", 3),
                koaPath(processors, "koa_village/streets/straight_02", 4),
                koaPath(processors, "koa_village/streets/straight_03", 10),
                koaPath(processors, "koa_village/streets/straight_04", 2),
                koaPath(processors, "koa_village/streets/straight_05", 3),
                koaPath(processors, "koa_village/streets/straight_06", 2),
                koaPath(processors, "koa_village/streets/corner_01", 2),
                koaPath(processors, "koa_village/streets/corner_02", 4),
                koaPath(processors, "koa_village/streets/corner_03", 6),
                koaPath(processors, "koa_village/streets/corner_04", 2),
                koaPath(processors, "koa_village/streets/crossroad_01", 5),
                koaPath(processors, "koa_village/streets/crossroad_02", 2),
                koaPath(processors, "koa_village/streets/crossroad_03", 1),
                koaPath(processors, "koa_village/streets/crossroad_04", 2)
        );

        this.koaTerminators = pools.register(
                "koa_village/terminators",
                StructureTemplatePool.Projection.TERRAIN_MATCHING,
                koaPath(processors, "koa_village/terminators/terminator_01", 1)
        );

        this.koaVillagers = pools.register(
                "koa_village/villagers",
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/villagers/unemployed", 1)
        );

        this.koaFish = pools.register(
                "koa_village/fish",
                StructureTemplatePool.Projection.RIGID,
                noAirSingle("koa_village/fish/fish_01", 1)
        );

        this.homeTreeStarts = pools.register(
                "home_tree/starts",
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/trunks/bottom/trunk_0", processors.homeTreeStart, 1),
                singlePiece("home_tree/trunks/bottom/trunk_1", processors.homeTreeStart, 1)
        );

        this.homeTreeRoofs = pools.register(
                "home_tree/roofs",
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/roofs/roof_0", processors.homeTreeBase, 1)
        );

        this.homeTreeDummy = pools.register(
                "home_tree/dummy",
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/dummy", processors.homeTreeBase, 1),
                singlePiece("home_tree/outer_dummy", processors.homeTreeBase, 1)
        );

        this.homeTreeTrunkMiddle = pools.register(
                "home_tree/trunks/middle",
                StructureTemplatePool.Projection.RIGID,
                singlePiece("home_tree/trunks/middle/trunk_0", processors.homeTreeBase, 1),
                singlePiece("home_tree/trunks/middle/trunk_1", processors.homeTreeBase, 1),
                singlePiece("home_tree/trunks/middle/trunk_1_iguanas", processors.homeTreeBase, 1),
                singlePiece("home_tree/trunks/middle/trunk_1_ashen", processors.homeTreeBase, 1)
        );

        this.homeTreeTrunkTop = pools.register(
                "home_tree/trunks/top",
                StructureTemplatePool.Projection.RIGID,
                noRotateSingle("home_tree/trunks/top/trunk_0", processors.homeTreeBase, 1),
                noRotateSingle("home_tree/trunks/top/trunk_1", processors.homeTreeBase, 1),
                noRotateSingle("home_tree/trunks/top/trunk_2", processors.homeTreeBase, 1),
                noRotateSingle("home_tree/trunks/top/trunk_3", processors.homeTreeBase, 1)
        );

        this.homeTreeBranchesSouth = pools.register(
                "home_tree/branches/south",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(-30, 30, 4),
                homeTreeBranch(0, 0, 1)
        );
        this.homeTreeBranchesSouthEast = pools.register(
                "home_tree/branches/southeast",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(30, 60, 4),
                homeTreeBranch(45, 45, 1)
        );
        this.homeTreeBranchesEast = pools.register(
                "home_tree/branches/east",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(60, 120, 4),
                homeTreeBranch(90, 90, 1)
        );
        this.homeTreeBranchesNorthEast = pools.register(
                "home_tree/branches/northeast",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(120, 150, 4),
                homeTreeBranch(135, 135, 1)
        );
        this.homeTreeBranchesNorth = pools.register(
                "home_tree/branches/north",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(150, 210, 4),
                homeTreeBranch(180, 180, 1)
        );
        this.homeTreeBranchesNorthWest = pools.register(
                "home_tree/branches/northwest",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(210, 240, 4),
                homeTreeBranch(225, 225, 1)
        );
        this.homeTreeBranchesWest = pools.register(
                "home_tree/branches/west",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(240, 300, 4),
                homeTreeBranch(270, 270, 1)
        );
        this.homeTreeBranchesSouthWest = pools.register(
                "home_tree/branches/southwest",
                StructureTemplatePool.Projection.RIGID,
                homeTreeBranch(300, 330, 4),
                homeTreeBranch(315, 315, 1)
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> singlePiece(String path, Holder<StructureProcessorList> processors, int weight) {
        return new Pair<>(
                StructurePoolElement.single(Constants.MODID + ":" + path, processors),
                weight
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> singlePiece(String path, int weight) {
        return new Pair<>(
                StructurePoolElement.single(Constants.MODID + ":" + path),
                weight
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> noAirSingle(String path, Holder<StructureProcessorList> processors, int weight) {
        return new Pair<>(
                SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path, processors, false),
                weight
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> noAirSingle(String path, int weight) {
        return new Pair<>(
                SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path),
                weight
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> noRotateSingle(String path, Holder<StructureProcessorList> processors, int weight) {
        return new Pair<>(
                NoRotateSingleJigsawPiece.createNoRotate(Constants.MODID + ":" + path, processors),
                weight
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> feature(Holder<PlacedFeature> feature, int weight) {
        return new Pair<>(
                StructurePoolElement.feature(feature),
                weight
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> homeTreeBranch(float minAngle, float maxAngle, int weight) {
        return new Pair<>(
                HomeTreeBranchPiece.create(minAngle, maxAngle),
                weight
        );
    }

    private static Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> koaPath(TropicraftProcessorLists processors, String path, int weight) {
        return new Pair<>(
                SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path, processors.koaPath, true),
                weight
        );
    }

    static final class Register {
        private final WorldgenDataConsumer<StructureTemplatePool> worldgen;

        Register(WorldgenDataConsumer<StructureTemplatePool> worldgen) {
            this.worldgen = worldgen;
        }

        @SafeVarargs
        public final Holder<StructureTemplatePool> register(String id, StructureTemplatePool.Projection placementBehaviour, Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>... pieces) {
            return this.register(id, new ResourceLocation("empty"), placementBehaviour, pieces);
        }

        @SafeVarargs
        public final Holder<StructureTemplatePool> register(String id, ResourceLocation fallback, StructureTemplatePool.Projection placementBehaviour, Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>... pieces) {
            return this.register(new StructureTemplatePool(new ResourceLocation(Constants.MODID, id), fallback, Arrays.asList(pieces), placementBehaviour));
        }

        public Holder<StructureTemplatePool> register(StructureTemplatePool templatePool) {
            return this.worldgen.register(templatePool.getName(), templatePool);
        }
    }
}
