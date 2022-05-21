package net.tropicraft.core.common.dimension.feature.pools;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.HomeTreeBranchPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TropicraftTemplatePools {
    public static final DeferredRegister<StructureTemplatePool> REGISTER = DeferredRegister.create(Registry.TEMPLATE_POOL_REGISTRY, Constants.MODID);

    public static final RegistryObject<StructureTemplatePool> KOA_TOWN_CENTERS = register(
            "koa_village/town_centers",
            StructureTemplatePool.Projection.RIGID,
            noAirSingle("koa_village/town_centers/firepit_01", TropicraftProcessorLists.KOA_TOWN_CENTERS, 1)
    );

    public static final RegistryObject<StructureTemplatePool> KOA_HUTS = register(
            "koa_village/huts",
            StructureTemplatePool.Projection.RIGID,
            noAirSingle("koa_village/huts/hut_01", TropicraftProcessorLists.KOA_BUILDINGS, 5),
            noAirSingle("koa_village/huts/hut_02", TropicraftProcessorLists.KOA_BUILDINGS, 2),
            noAirSingle("koa_village/huts/hut_03", TropicraftProcessorLists.KOA_BUILDINGS, 3),
            noAirSingle("koa_village/huts/hut_04", TropicraftProcessorLists.KOA_BUILDINGS, 4),
            noAirSingle("koa_village/huts/hut_05", TropicraftProcessorLists.KOA_BUILDINGS, 10),
            noAirSingle("koa_village/huts/bongo_hut_01", TropicraftProcessorLists.KOA_BUILDINGS, 2),
            noAirSingle("koa_village/huts/trade_hut_01", TropicraftProcessorLists.KOA_BUILDINGS, 2)
    );

    public static final RegistryObject<StructureTemplatePool> KOA_STREETS = register(
            "koa_village/streets",
            new ResourceLocation(Constants.MODID, "koa_village/terminators"),
            StructureTemplatePool.Projection.TERRAIN_MATCHING,
            koaPath("koa_village/streets/straight_01", 3),
            koaPath("koa_village/streets/straight_02", 4),
            koaPath("koa_village/streets/straight_03", 10),
            koaPath("koa_village/streets/straight_04", 2),
            koaPath("koa_village/streets/straight_05", 3),
            koaPath("koa_village/streets/straight_06", 2),
            koaPath("koa_village/streets/corner_01", 2),
            koaPath("koa_village/streets/corner_02", 4),
            koaPath("koa_village/streets/corner_03", 6),
            koaPath("koa_village/streets/corner_04", 2),
            koaPath("koa_village/streets/crossroad_01", 5),
            koaPath("koa_village/streets/crossroad_02", 2),
            koaPath("koa_village/streets/crossroad_03", 1),
            koaPath("koa_village/streets/crossroad_04", 2)
    );

    public static final RegistryObject<StructureTemplatePool> KOA_TERMINATORS = register(
            "koa_village/terminators",
            StructureTemplatePool.Projection.TERRAIN_MATCHING,
            koaPath("koa_village/terminators/terminator_01", 1)
    );

    public static final RegistryObject<StructureTemplatePool> KOA_VILLAGERS = register(
            "koa_village/villagers",
            StructureTemplatePool.Projection.RIGID,
            noAirSingle("koa_village/villagers/unemployed", 1)
    );

    public static final RegistryObject<StructureTemplatePool> KOA_FISH = register(
            "koa_village/fish",
            StructureTemplatePool.Projection.RIGID,
            noAirSingle("koa_village/fish/fish_01", 1)
    );

    public static final RegistryObject<StructureTemplatePool> HOME_TREE_STARTS = register(
            "home_tree/starts",
            StructureTemplatePool.Projection.RIGID,
            singlePiece("home_tree/trunks/bottom/trunk_0", TropicraftProcessorLists.HOME_TREE_START, 1),
            singlePiece("home_tree/trunks/bottom/trunk_1", TropicraftProcessorLists.HOME_TREE_START, 1)
    );

    public static final RegistryObject<StructureTemplatePool> HOME_TREE_ROOFS = register(
            "home_tree/roofs",
            StructureTemplatePool.Projection.RIGID,
            singlePiece("home_tree/roofs/roof_0", TropicraftProcessorLists.HOME_TREE_BASE, 1)
    );

    public static final RegistryObject<StructureTemplatePool> HOME_TREE_DUMMY = register(
            "home_tree/dummy",
            StructureTemplatePool.Projection.RIGID,
            singlePiece("home_tree/dummy", TropicraftProcessorLists.HOME_TREE_BASE, 1),
            singlePiece("home_tree/outer_dummy", TropicraftProcessorLists.HOME_TREE_BASE, 1)
    );

    public static final RegistryObject<StructureTemplatePool> HOME_TREE_TRUNK_MIDDLE = register(
            "home_tree/trunks/middle",
            StructureTemplatePool.Projection.RIGID,
            singlePiece("home_tree/trunks/middle/trunk_0", TropicraftProcessorLists.HOME_TREE_BASE, 1),
            singlePiece("home_tree/trunks/middle/trunk_1", TropicraftProcessorLists.HOME_TREE_BASE, 1),
            singlePiece("home_tree/trunks/middle/trunk_1_iguanas", TropicraftProcessorLists.HOME_TREE_BASE, 1),
            singlePiece("home_tree/trunks/middle/trunk_1_ashen", TropicraftProcessorLists.HOME_TREE_BASE, 1)
    );

    public static final RegistryObject<StructureTemplatePool> HOME_TREE_TRUNK_TOP = register(
            "home_tree/trunks/top",
            StructureTemplatePool.Projection.RIGID,
            noRotateSingle("home_tree/trunks/top/trunk_0", TropicraftProcessorLists.HOME_TREE_BASE, 1),
            noRotateSingle("home_tree/trunks/top/trunk_1", TropicraftProcessorLists.HOME_TREE_BASE, 1),
            noRotateSingle("home_tree/trunks/top/trunk_2", TropicraftProcessorLists.HOME_TREE_BASE, 1),
            noRotateSingle("home_tree/trunks/top/trunk_3", TropicraftProcessorLists.HOME_TREE_BASE, 1)
    );

    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_SOUTH = register(
            "home_tree/branches/south",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(-30, 30, 4),
            homeTreeBranch(0, 0, 1)
    );
    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_SOUTH_EAST = register(
            "home_tree/branches/southeast",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(30, 60, 4),
            homeTreeBranch(45, 45, 1)
    );
    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_EAST = register(
            "home_tree/branches/east",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(60, 120, 4),
            homeTreeBranch(90, 90, 1)
    );
    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_NORTH_EAST = register(
            "home_tree/branches/northeast",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(120, 150, 4),
            homeTreeBranch(135, 135, 1)
    );
    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_NORTH = register(
            "home_tree/branches/north",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(150, 210, 4),
            homeTreeBranch(180, 180, 1)
    );
    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_NORTH_WEST = register(
            "home_tree/branches/northwest",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(210, 240, 4),
            homeTreeBranch(225, 225, 1)
    );
    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_WEST = register(
            "home_tree/branches/west",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(240, 300, 4),
            homeTreeBranch(270, 270, 1)
    );
    public static final RegistryObject<StructureTemplatePool> HOME_TREE_BRANCHES_SOUTH_WEST = register(
            "home_tree/branches/southwest",
            StructureTemplatePool.Projection.RIGID,
            homeTreeBranch(300, 330, 4),
            homeTreeBranch(315, 315, 1)
    );

    private static WeightedPiece singlePiece(String path, RegistryObject<StructureProcessorList> processors, int weight) {
        return new WeightedPiece(
                () -> StructurePoolElement.single(Constants.MODID + ":" + path, processors.getHolder().orElseThrow()),
                weight
        );
    }

    private static WeightedPiece singlePiece(String path, int weight) {
        return new WeightedPiece(
                () -> StructurePoolElement.single(Constants.MODID + ":" + path),
                weight
        );
    }

    private static WeightedPiece noAirSingle(String path, RegistryObject<StructureProcessorList> processors, int weight) {
        return new WeightedPiece(
                () -> SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path, processors.getHolder().orElseThrow(), false),
                weight
        );
    }

    private static WeightedPiece noAirSingle(String path, int weight) {
        return new WeightedPiece(
                () -> SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path),
                weight
        );
    }

    private static WeightedPiece noRotateSingle(String path, RegistryObject<StructureProcessorList> processors, int weight) {
        return new WeightedPiece(
                () -> NoRotateSingleJigsawPiece.createNoRotate(Constants.MODID + ":" + path, processors.getHolder().orElseThrow()),
                weight
        );
    }

    private static WeightedPiece feature(Holder<PlacedFeature> feature, int weight) {
        return new WeightedPiece(
                () -> StructurePoolElement.feature(feature),
                weight
        );
    }

    private static WeightedPiece homeTreeBranch(float minAngle, float maxAngle, int weight) {
        return new WeightedPiece(
                () -> HomeTreeBranchPiece.create(minAngle, maxAngle),
                weight
        );
    }

    private static WeightedPiece koaPath(String path, int weight) {
        return new WeightedPiece(
                () -> SingleNoAirJigsawPiece.create(Constants.MODID + ":" + path, TropicraftProcessorLists.KOA_PATH.getHolder().orElseThrow(), true),
                weight
        );
    }

    private static RegistryObject<StructureTemplatePool> register(String name, StructureTemplatePool.Projection placementBehaviour, WeightedPiece... pieces) {
        return register(name, new ResourceLocation("empty"), placementBehaviour, pieces);
    }

    private static RegistryObject<StructureTemplatePool> register(String name, ResourceLocation fallback, StructureTemplatePool.Projection placementBehaviour, WeightedPiece... pieces) {
        final ResourceLocation id = new ResourceLocation(Constants.MODID, name);
        return REGISTER.register(name, () -> new StructureTemplatePool(id, fallback, Arrays.stream(pieces).map(WeightedPiece::resolve).toList(), placementBehaviour));
    }

    private record WeightedPiece(
            Supplier<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>> factory, int weight) {
        public Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> resolve() {
            return new Pair<>(factory.get(), weight);
        }
    }
}
