package net.tropicraft.core.common.dimension.feature;

import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, Info.MODID);

	public static final RegistryObject<FruitTreeFeature> GRAPEFRUIT_TREE = register(
	        "grapefruit_tree", () -> new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.GRAPEFRUIT_SAPLING, TropicraftBlocks.GRAPEFRUIT_LEAVES.lazyMap(Block::getDefaultState)));
	public static final RegistryObject<FruitTreeFeature> ORANGE_TREE = register(
	        "orange_tree", () -> new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.ORANGE_SAPLING, () -> TropicraftBlocks.ORANGE_LEAVES.get().getDefaultState()));
	public static final RegistryObject<FruitTreeFeature> LEMON_TREE = register(
	        "lemon_tree", () -> new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.LEMON_SAPLING, () -> TropicraftBlocks.LEMON_LEAVES.get().getDefaultState()));
	public static final RegistryObject<FruitTreeFeature> LIME_TREE = register(
	        "lime_tree", () -> new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.LIME_SAPLING, () -> TropicraftBlocks.LIME_LEAVES.get().getDefaultState()));
	public static final RegistryObject<PalmTreeFeature> NORMAL_PALM_TREE = register(
	        "normal_palm_tree", () -> new NormalPalmTreeFeature(NoFeatureConfig::deserialize, true));
	public static final RegistryObject<PalmTreeFeature> CURVED_PALM_TREE = register(
	        "curved_palm_tree", () -> new CurvedPalmTreeFeature(NoFeatureConfig::deserialize, true));
	public static final RegistryObject<PalmTreeFeature> LARGE_PALM_TREE = register(
	        "large_palm_tree", () -> new LargePalmTreeFeature(NoFeatureConfig::deserialize, true));
	public static final RegistryObject<RainforestTreeFeature> UP_TREE = register(
	        "up_tree", () -> new UpTreeFeature(NoFeatureConfig::deserialize, true));
	public static final RegistryObject<RainforestTreeFeature> SMALL_TUALUNG = register(
	        "small_tualung", () -> new TualungFeature(NoFeatureConfig::deserialize, true, 16, 9));
	public static final RegistryObject<RainforestTreeFeature> LARGE_TUALUNG = register(
	        "large_tualung", () -> new TualungFeature(NoFeatureConfig::deserialize, true, 25, 11));
	public static final RegistryObject<RainforestTreeFeature> TALL_TREE = register(
	        "tall_tree", () -> new TallRainforestTreeFeature(NoFeatureConfig::deserialize, true));
	public static final RegistryObject<EIHFeature> EIH = register(
	        "eih", () -> new EIHFeature(NoFeatureConfig::deserialize));
	public static final RegistryObject<TropicsFlowersFeature> TROPICS_FLOWERS = register(
	        "tropics_flowers", () -> new TropicsFlowersFeature(NoFeatureConfig::deserialize, TropicraftBlocks.TROPICS_FLOWERS));
	public static final RegistryObject<TropicsFlowersFeature> RAINFOREST_FLOWERS = register(
	        "rainforest_flowers", () -> new TropicsFlowersFeature(NoFeatureConfig::deserialize, TropicraftBlocks.MAGIC_MUSHROOM));
	public static final RegistryObject<UndergrowthFeature> UNDERGROWTH = register(
	        "undergrowth", () -> new UndergrowthFeature(NoFeatureConfig::deserialize));
	public static final RegistryObject<RainforestVinesFeature> VINES = register(
	        "rainforest_vines", () -> new RainforestVinesFeature(NoFeatureConfig::deserialize));
	public static final RegistryObject<Structure<NoFeatureConfig>> VILLAGE = register(
	        "koa_village", () -> new KoaVillageStructure(NoFeatureConfig::deserialize));
	public static final RegistryObject<VolcanoFeature> VOLCANO = register(
	        "volcano", () -> new VolcanoFeature(NoFeatureConfig::deserialize));

	public static final PlacementBehaviour KOA_PATH = PlacementBehaviour.create("KOA_PATH", Info.MODID + ":koa_path",
            ImmutableList.of(new SmoothingGravityProcessor(Heightmap.Type.WORLD_SURFACE_WG, -1), new SinkInGroundProcessor(), new SteepPathProcessor(), new StructureSupportsProcessor()));

    private static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }
}
