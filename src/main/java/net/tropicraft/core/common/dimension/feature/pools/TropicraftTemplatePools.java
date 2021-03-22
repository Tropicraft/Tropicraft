package net.tropicraft.core.common.dimension.feature.pools;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;

import java.util.Arrays;
import java.util.function.Function;

public final class TropicraftTemplatePools {
    public final JigsawPattern koaTownCenters;
    public final JigsawPattern koaHuts;
    public final JigsawPattern koaStreets;
    public final JigsawPattern koaTerminators;
    public final JigsawPattern koaVillagers;
    public final JigsawPattern koaFish;
    public final JigsawPattern homeTreeStarts;
    public final JigsawPattern homeTreeRoofs;
    public final JigsawPattern homeTreeDummy;
    public final JigsawPattern homeTreeTrunkMiddle;
    public final JigsawPattern homeTreeTrunkTop;
    public final JigsawPattern homeTreeBranchesSouth;
    public final JigsawPattern homeTreeBranchesSouthEast;
    public final JigsawPattern homeTreeBranchesEast;
    public final JigsawPattern homeTreeBranchesNorthEast;
    public final JigsawPattern homeTreeBranchesNorth;
    public final JigsawPattern homeTreeBranchesNorthWest;
    public final JigsawPattern homeTreeBranchesWest;
    public final JigsawPattern homeTreeBranchesSouthWest;

    public TropicraftTemplatePools(WorldgenDataConsumer<JigsawPattern> worldgen, TropicraftConfiguredFeatures features, TropicraftProcessorLists processors) {
        Register pools = new Register(worldgen);

        this.koaTownCenters = pools.register(
                "koa_village/town_centers",
                JigsawPattern.PlacementBehaviour.RIGID,
                noAirSingle("koa_village/town_centers/firepit_01", processors.koaTownCenters, 1)
        );

        this.koaHuts = pools.register(
                "koa_village/huts",
                JigsawPattern.PlacementBehaviour.RIGID,
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
                TropicraftFeatures.KOA_PATH,
                noAirSingle("koa_village/streets/straight_01", 3),
                noAirSingle("koa_village/streets/straight_02", 4),
                noAirSingle("koa_village/streets/straight_03", 10),
                noAirSingle("koa_village/streets/straight_04", 2),
                noAirSingle("koa_village/streets/straight_05", 3),
                noAirSingle("koa_village/streets/straight_06", 2),
                noAirSingle("koa_village/streets/corner_01", 2),
                noAirSingle("koa_village/streets/corner_02", 4),
                noAirSingle("koa_village/streets/corner_03", 6),
                noAirSingle("koa_village/streets/corner_04", 2),
                noAirSingle("koa_village/streets/crossroad_01", 5),
                noAirSingle("koa_village/streets/crossroad_02", 2),
                noAirSingle("koa_village/streets/crossroad_03", 1),
                noAirSingle("koa_village/streets/crossroad_04", 2)
        );

        this.koaTerminators = pools.register(
                "koa_village/terminators",
                TropicraftFeatures.KOA_PATH,
                noAirSingle("koa_village/terminators/terminator_01", 1)
        );

        this.koaVillagers = pools.register(
                "koa_village/villagers",
                JigsawPattern.PlacementBehaviour.RIGID,
                noAirSingle("koa_village/villagers/unemployed", 1)
        );

        this.koaFish = pools.register(
                "koa_village/fish",
                JigsawPattern.PlacementBehaviour.RIGID,
                noAirSingle("koa_village/fish/fish_01", 1)
        );

        this.homeTreeStarts = pools.register(
                "home_tree/starts",
                JigsawPattern.PlacementBehaviour.RIGID,
                singlePiece("home_tree/trunks/bottom/trunk_0", processors.homeTreeStart, 1),
                singlePiece("home_tree/trunks/bottom/trunk_1", processors.homeTreeStart, 1)
        );

        this.homeTreeRoofs = pools.register(
                "home_tree/roofs",
                JigsawPattern.PlacementBehaviour.RIGID,
                singlePiece("home_tree/roofs/roof_0", processors.homeTreeBase, 1)
        );

        this.homeTreeDummy = pools.register(
                "home_tree/dummy",
                JigsawPattern.PlacementBehaviour.RIGID,
                singlePiece("home_tree/dummy", processors.homeTreeBase, 1),
                singlePiece("home_tree/outer_dummy", processors.homeTreeBase, 1)
        );

        this.homeTreeTrunkMiddle = pools.register(
                "home_tree/trunks/middle",
                JigsawPattern.PlacementBehaviour.RIGID,
                singlePiece("home_tree/trunks/middle/trunk_0", processors.homeTreeBase, 1),
                singlePiece("home_tree/trunks/middle/trunk_1", processors.homeTreeBase, 1),
                singlePiece("home_tree/trunks/middle/trunk_1_iguanas", processors.homeTreeBase, 1),
                singlePiece("home_tree/trunks/middle/trunk_1_ashen", processors.homeTreeBase, 1)
        );

        this.homeTreeTrunkTop = pools.register(
                "home_tree/trunks/top",
                JigsawPattern.PlacementBehaviour.RIGID,
                noRotateSingle("home_tree/trunks/top/trunk_0", processors.homeTreeBase, 1),
                noRotateSingle("home_tree/trunks/top/trunk_1", processors.homeTreeBase, 1),
                noRotateSingle("home_tree/trunks/top/trunk_2", processors.homeTreeBase, 1),
                noRotateSingle("home_tree/trunks/top/trunk_3", processors.homeTreeBase, 1)
        );

        this.homeTreeBranchesSouth = pools.register(
                "home_tree/branches/south",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchSouth, 4),
                feature(features.homeTreeBranchSouthExact, 1)
        );
        this.homeTreeBranchesSouthEast = pools.register(
                "home_tree/branches/southeast",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchSouthEast, 4),
                feature(features.homeTreeBranchSouthEastExact, 1)
        );
        this.homeTreeBranchesEast = pools.register(
                "home_tree/branches/east",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchEast, 4),
                feature(features.homeTreeBranchEastExact, 1)
        );
        this.homeTreeBranchesNorthEast = pools.register(
                "home_tree/branches/northeast",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchNorthEast, 4),
                feature(features.homeTreeBranchNorthEastExact, 1)
        );
        this.homeTreeBranchesNorth = pools.register(
                "home_tree/branches/north",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchNorth, 4),
                feature(features.homeTreeBranchNorthExact, 1)
        );
        this.homeTreeBranchesNorthWest = pools.register(
                "home_tree/branches/northwest",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchNorthWest, 4),
                feature(features.homeTreeBranchNorthWestExact, 1)
        );
        this.homeTreeBranchesWest = pools.register(
                "home_tree/branches/west",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchWest, 4),
                feature(features.homeTreeBranchWestExact, 1)
        );
        this.homeTreeBranchesSouthWest = pools.register(
                "home_tree/branches/southwest",
                JigsawPattern.PlacementBehaviour.RIGID,
                feature(features.homeTreeBranchSouthWest, 4),
                feature(features.homeTreeBranchSouthWestExact, 1)
        );
    }

    private static Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> singlePiece(String path, StructureProcessorList processors, int weight) {
        return new Pair<>(
                JigsawPiece.func_242861_b(Constants.MODID + ":" + path, processors),
                weight
        );
    }

    private static Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> singlePiece(String path, int weight) {
        return new Pair<>(
                JigsawPiece.func_242859_b(Constants.MODID + ":" + path),
                weight
        );
    }

    private static Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> noAirSingle(String path, StructureProcessorList processors, int weight) {
        return new Pair<>(
                SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path, processors),
                weight
        );
    }

    private static Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> noAirSingle(String path, int weight) {
        return new Pair<>(
                SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path),
                weight
        );
    }

    private static Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> noRotateSingle(String path, StructureProcessorList processors, int weight) {
        return new Pair<>(
                NoRotateSingleJigsawPiece.createNoRotate(Constants.MODID + ":" + path, processors),
                weight
        );
    }

    private static Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> feature(ConfiguredFeature<?, ?> feature, int weight) {
        return new Pair<>(
                JigsawPiece.func_242845_a(feature),
                weight
        );
    }

    static final class Register {
        private final WorldgenDataConsumer<JigsawPattern> worldgen;

        Register(WorldgenDataConsumer<JigsawPattern> worldgen) {
            this.worldgen = worldgen;
        }

        @SafeVarargs
        public final JigsawPattern register(String id, JigsawPattern.PlacementBehaviour placementBehaviour, Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer>... pieces) {
            return this.register(id, new ResourceLocation("empty"), placementBehaviour, pieces);
        }

        @SafeVarargs
        public final JigsawPattern register(String id, ResourceLocation fallback, JigsawPattern.PlacementBehaviour placementBehaviour, Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer>... pieces) {
            return this.register(new JigsawPattern(new ResourceLocation(Constants.MODID, id), fallback, Arrays.asList(pieces), placementBehaviour));
        }

        public JigsawPattern register(JigsawPattern templatePool) {
            return this.worldgen.register(templatePool.getName(), templatePool);
        }
    }
}
