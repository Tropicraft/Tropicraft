package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.List;

public final class TropicraftTreePlacements {
    public static final TropicraftFeatures.Register REGISTER = TropicraftFeatures.Register.create();

    public static final RegistryObject<PlacedFeature> GRAPEFRUIT_TREE_CHECKED = REGISTER.placed("grapefruit_tree_checked", TropicraftTreeFeatures.GRAPEFRUIT_TREE, () -> checkTree(TropicraftBlocks.GRAPEFRUIT_SAPLING));
    public static final RegistryObject<PlacedFeature> ORANGE_TREE_CHECKED = REGISTER.placed("orange_tree_checked", TropicraftTreeFeatures.ORANGE_TREE, () -> checkTree(TropicraftBlocks.ORANGE_SAPLING));
    public static final RegistryObject<PlacedFeature> LEMON_TREE_CHECKED = REGISTER.placed("lemon_tree_checked", TropicraftTreeFeatures.LEMON_TREE, () -> checkTree(TropicraftBlocks.LEMON_SAPLING));
    public static final RegistryObject<PlacedFeature> LIME_TREE_CHECKED = REGISTER.placed("lime_tree_checked", TropicraftTreeFeatures.LIME_TREE, () -> checkTree(TropicraftBlocks.LIME_SAPLING));

    public static final RegistryObject<PlacedFeature> PALM_TREE_CHECKED = REGISTER.placed("palm_tree_checked", TropicraftTreeFeatures.PALM_TREE, () -> checkTree(TropicraftBlocks.PALM_SAPLING));
    public static final RegistryObject<PlacedFeature> RAINFOREST_TALL_TREE_CHECKED = REGISTER.placed("rainforest_tall_tree_checked", TropicraftTreeFeatures.RAINFOREST_TALL_TREE, () -> checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
    public static final RegistryObject<PlacedFeature> RAINFOREST_UP_TREE_CHECKED = REGISTER.placed("rainforest_up_tree_checked", TropicraftTreeFeatures.RAINFOREST_UP_TREE, () -> checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
    public static final RegistryObject<PlacedFeature> RAINFOREST_SMALL_TUALUNG_CHECKED = REGISTER.placed("rainforest_small_tualung_checked", TropicraftTreeFeatures.RAINFOREST_SMALL_TUALUNG, () -> checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
    public static final RegistryObject<PlacedFeature> RAINFOREST_LARGE_TUALUNG_CHECKED = REGISTER.placed("rainforest_large_tualung_checked", TropicraftTreeFeatures.RAINFOREST_LARGE_TUALUNG, () -> checkTree(TropicraftBlocks.MAHOGANY_SAPLING));

    public static final RegistryObject<PlacedFeature> PLEODENDRON_CHECKED = REGISTER.placed("pleodendron_checked", TropicraftTreeFeatures.PLEODENDRON, () -> checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
    public static final RegistryObject<PlacedFeature> PAPAYA_CHECKED = REGISTER.placed("papaya_checked", TropicraftTreeFeatures.PAPAYA, () -> checkTree(TropicraftBlocks.PAPAYA_SAPLING));

    public static final RegistryObject<PlacedFeature> RED_MANGROVE_CHECKED = REGISTER.placed("red_mangrove_checked", TropicraftTreeFeatures.RED_MANGROVE, () -> checkMangrove(TropicraftBlocks.RED_MANGROVE_PROPAGULE, 2));
    public static final RegistryObject<PlacedFeature> TALL_MANGROVE_CHECKED = REGISTER.placed("tall_mangrove_checked", TropicraftTreeFeatures.TALL_MANGROVE, () -> checkMangrove(TropicraftBlocks.TALL_MANGROVE_PROPAGULE, 2));
    public static final RegistryObject<PlacedFeature> TEA_MANGROVE_CHECKED = REGISTER.placed("tea_mangrove_checked", TropicraftTreeFeatures.TEA_MANGROVE, () -> checkMangrove(TropicraftBlocks.TEA_MANGROVE_PROPAGULE, 1));
    public static final RegistryObject<PlacedFeature> BLACK_MANGROVE_CHECKED = REGISTER.placed("black_mangrove_checked", TropicraftTreeFeatures.BLACK_MANGROVE, () -> checkMangrove(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE, 1));

    public static final RegistryObject<PlacedFeature> LIGHT_MANGROVES_CHECKED = REGISTER.randomChecked("light_mangroves", TALL_MANGROVE_CHECKED, TEA_MANGROVE_CHECKED, BLACK_MANGROVE_CHECKED);

    private static List<PlacementModifier> checkTree(RegistryObject<? extends Block> sapling) {
        return List.of(saplingFilter(sapling));
    }

    private static List<PlacementModifier> checkMangrove(RegistryObject<? extends Block> sapling, int maxWaterDepth) {
        return List.of(SurfaceWaterDepthFilter.forMaxDepth(maxWaterDepth), saplingFilter(sapling));
    }

    private static BlockPredicateFilter saplingFilter(RegistryObject<? extends Block> sapling) {
        return BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.get().defaultBlockState(), BlockPos.ZERO));
    }
}
