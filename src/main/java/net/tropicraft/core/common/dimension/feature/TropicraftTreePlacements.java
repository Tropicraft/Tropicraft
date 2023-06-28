package net.tropicraft.core.common.dimension.feature;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.List;

import static net.tropicraft.core.common.dimension.feature.TropicraftPlacementUtil.register;
import static net.tropicraft.core.common.dimension.feature.TropicraftPlacementUtil.registerRandomChecked;

public final class TropicraftTreePlacements {
    public static final ResourceKey<PlacedFeature> GRAPEFRUIT_TREE_CHECKED = createKey("grapefruit_tree_checked");
    public static final ResourceKey<PlacedFeature> ORANGE_TREE_CHECKED = createKey("orange_tree_checked");
    public static final ResourceKey<PlacedFeature> LEMON_TREE_CHECKED = createKey("lemon_tree_checked");
    public static final ResourceKey<PlacedFeature> LIME_TREE_CHECKED = createKey("lime_tree_checked");

    public static final ResourceKey<PlacedFeature> PALM_TREE_CHECKED = createKey("palm_tree_checked");
    public static final ResourceKey<PlacedFeature> RAINFOREST_TALL_TREE_CHECKED = createKey("rainforest_tall_tree_checked");
    public static final ResourceKey<PlacedFeature> RAINFOREST_UP_TREE_CHECKED = createKey("rainforest_up_tree_checked");
    public static final ResourceKey<PlacedFeature> RAINFOREST_SMALL_TUALUNG_CHECKED = createKey("rainforest_small_tualung_checked");
    public static final ResourceKey<PlacedFeature> RAINFOREST_LARGE_TUALUNG_CHECKED = createKey("rainforest_large_tualung_checked");

    public static final ResourceKey<PlacedFeature> PLEODENDRON_CHECKED = createKey("pleodendron_checked");
    public static final ResourceKey<PlacedFeature> PAPAYA_CHECKED = createKey("papaya_checked");

    public static final ResourceKey<PlacedFeature> RED_MANGROVE_CHECKED = createKey("red_mangrove_checked");
    public static final ResourceKey<PlacedFeature> TALL_MANGROVE_CHECKED = createKey("tall_mangrove_checked");
    public static final ResourceKey<PlacedFeature> TEA_MANGROVE_CHECKED = createKey("tea_mangrove_checked");
    public static final ResourceKey<PlacedFeature> BLACK_MANGROVE_CHECKED = createKey("black_mangrove_checked");

    public static final ResourceKey<PlacedFeature> LIGHT_MANGROVES_CHECKED = createKey("light_mangroves");

    public static void boostrap(final BootstapContext<PlacedFeature> context) {
        register(context, GRAPEFRUIT_TREE_CHECKED, TropicraftTreeFeatures.GRAPEFRUIT_TREE, checkTree(TropicraftBlocks.GRAPEFRUIT_SAPLING));
        register(context, ORANGE_TREE_CHECKED, TropicraftTreeFeatures.ORANGE_TREE, checkTree(TropicraftBlocks.ORANGE_SAPLING));
        register(context, LEMON_TREE_CHECKED, TropicraftTreeFeatures.LEMON_TREE, checkTree(TropicraftBlocks.LEMON_SAPLING));
        register(context, LIME_TREE_CHECKED, TropicraftTreeFeatures.LIME_TREE, checkTree(TropicraftBlocks.LIME_SAPLING));

        register(context, PALM_TREE_CHECKED, TropicraftTreeFeatures.PALM_TREE, checkTree(TropicraftBlocks.PALM_SAPLING));
        register(context, RAINFOREST_TALL_TREE_CHECKED, TropicraftTreeFeatures.RAINFOREST_TALL_TREE, checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
        register(context, RAINFOREST_UP_TREE_CHECKED, TropicraftTreeFeatures.RAINFOREST_UP_TREE, checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
        register(context, RAINFOREST_SMALL_TUALUNG_CHECKED, TropicraftTreeFeatures.RAINFOREST_SMALL_TUALUNG, checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
        register(context, RAINFOREST_LARGE_TUALUNG_CHECKED, TropicraftTreeFeatures.RAINFOREST_LARGE_TUALUNG, checkTree(TropicraftBlocks.MAHOGANY_SAPLING));

        register(context, PLEODENDRON_CHECKED, TropicraftTreeFeatures.PLEODENDRON, checkTree(TropicraftBlocks.MAHOGANY_SAPLING));
        register(context, PAPAYA_CHECKED, TropicraftTreeFeatures.PAPAYA, checkTree(TropicraftBlocks.PAPAYA_SAPLING));

        register(context, RED_MANGROVE_CHECKED, TropicraftTreeFeatures.RED_MANGROVE, checkMangrove(TropicraftBlocks.RED_MANGROVE_PROPAGULE, 2));
        Holder.Reference<PlacedFeature> tallMangrove = register(context, TALL_MANGROVE_CHECKED, TropicraftTreeFeatures.TALL_MANGROVE, checkMangrove(TropicraftBlocks.TALL_MANGROVE_PROPAGULE, 2));
        Holder.Reference<PlacedFeature> teaMangrove = register(context, TEA_MANGROVE_CHECKED, TropicraftTreeFeatures.TEA_MANGROVE, checkMangrove(TropicraftBlocks.TEA_MANGROVE_PROPAGULE, 1));
        Holder.Reference<PlacedFeature> blackMangrove = register(context, BLACK_MANGROVE_CHECKED, TropicraftTreeFeatures.BLACK_MANGROVE, checkMangrove(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE, 1));

        registerRandomChecked(context, LIGHT_MANGROVES_CHECKED, tallMangrove, teaMangrove, blackMangrove);
    }

    private static ResourceKey<PlacedFeature> createKey(final String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Constants.MODID, name));
    }

    private static List<PlacementModifier> checkTree(RegistryEntry<? extends Block> sapling) {
        return List.of(saplingFilter(sapling));
    }

    private static List<PlacementModifier> checkMangrove(RegistryEntry<? extends Block> sapling, int maxWaterDepth) {
        return List.of(SurfaceWaterDepthFilter.forMaxDepth(maxWaterDepth), saplingFilter(sapling));
    }

    private static BlockPredicateFilter saplingFilter(RegistryEntry<? extends Block> sapling) {
        return BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.get().defaultBlockState(), BlockPos.ZERO));
    }
}
