package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.config.FruitTreeConfig;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.jigsaw.SinkInGroundProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SmoothingGravityProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SteepPathProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureSupportsProcessor;
import net.tropicraft.core.common.dimension.feature.tree.*;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveTreeFeature;

import java.util.function.Supplier;

public class TropicraftFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MODID);
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Constants.MODID);

    public static final RegistryObject<FruitTreeFeature> FRUIT_TREE = register("fruit_tree", () -> new FruitTreeFeature(FruitTreeConfig.CODEC));
    public static final RegistryObject<PalmTreeFeature> NORMAL_PALM_TREE = register("normal_palm_tree", () -> new NormalPalmTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<PalmTreeFeature> CURVED_PALM_TREE = register("curved_palm_tree", () -> new CurvedPalmTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<PalmTreeFeature> LARGE_PALM_TREE = register("large_palm_tree", () -> new LargePalmTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<RainforestTreeFeature> UP_TREE = register("up_tree", () -> new UpTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<RainforestTreeFeature> SMALL_TUALUNG = register("small_tualung", () -> new TualungFeature(NoFeatureConfig.CODEC, 16, 9));
    public static final RegistryObject<RainforestTreeFeature> LARGE_TUALUNG = register("large_tualung", () -> new TualungFeature(NoFeatureConfig.CODEC, 25, 11));
    public static final RegistryObject<RainforestTreeFeature> TALL_TREE = register("tall_tree", () -> new TallRainforestTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<EIHFeature> EIH = register("eih", () -> new EIHFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<UndergrowthFeature> UNDERGROWTH = register("undergrowth", () -> new UndergrowthFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<SingleUndergrowthFeature> SINGLE_UNDERGROWTH = register("single_undergrowth", () -> new SingleUndergrowthFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<RainforestVinesFeature> VINES = register("rainforest_vines", () -> new RainforestVinesFeature(RainforestVinesConfig.CODEC));
    public static final RegistryObject<UndergroundSeaPickleFeature> UNDERGROUND_SEA_PICKLE = register("underground_sea_pickle", () -> new UndergroundSeaPickleFeature(NoFeatureConfig.CODEC));

    public static final RegistryObject<Structure<VillageConfig>> KOA_VILLAGE = registerStructure("koa_village", new KoaVillageStructure(VillageConfig.field_236533_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final RegistryObject<Structure<VillageConfig>> HOME_TREE = registerStructure("home_tree", new HomeTreeStructure(VillageConfig.field_236533_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
    public static final RegistryObject<CoffeePlantFeature> COFFEE_BUSH = register("coffee_bush", () -> new CoffeePlantFeature(NoFeatureConfig.CODEC));

    public static final RegistryObject<ReedsFeature> REEDS = register("reeds", () -> new ReedsFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<MangroveTreeFeature> MANGROVE_TREE = register("mangrove_tree", () -> new MangroveTreeFeature((TreeFeature) Feature.TREE, BaseTreeFeatureConfig.CODEC));

    public static final PlacementBehaviour KOA_PATH = PlacementBehaviour.create("KOA_PATH", Constants.MODID + ":koa_path",
            ImmutableList.of(new SmoothingGravityProcessor(Heightmap.Type.WORLD_SURFACE_WG, -1), new SinkInGroundProcessor(), new SteepPathProcessor(), new StructureSupportsProcessor(false, ImmutableList.of(TropicraftBlocks.BAMBOO_FENCE.getId()))));

    private static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }

    private static <T extends Structure<?>> RegistryObject<T> registerStructure(final String name, T structure, GenerationStage.Decoration step) {
        Structure.NAME_STRUCTURE_BIMAP.put("tropicraft:" + name, structure);
        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(structure, step);
        return STRUCTURES.register(name, () -> structure);
    }
}
