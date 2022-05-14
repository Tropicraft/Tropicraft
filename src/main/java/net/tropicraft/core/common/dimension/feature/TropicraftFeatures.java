package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Constants.MODID);

    public static final RegistryObject<FruitTreeFeature> FRUIT_TREE = register("fruit_tree", () -> new FruitTreeFeature(FruitTreeConfig.CODEC));
    public static final RegistryObject<PalmTreeFeature> NORMAL_PALM_TREE = register("normal_palm_tree", () -> new NormalPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<PalmTreeFeature> CURVED_PALM_TREE = register("curved_palm_tree", () -> new CurvedPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<PalmTreeFeature> LARGE_PALM_TREE = register("large_palm_tree", () -> new LargePalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestTreeFeature> UP_TREE = register("up_tree", () -> new UpTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestTreeFeature> SMALL_TUALUNG = register("small_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 16, 9));
    public static final RegistryObject<RainforestTreeFeature> LARGE_TUALUNG = register("large_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 25, 11));
    public static final RegistryObject<RainforestTreeFeature> TALL_TREE = register("tall_tree", () -> new TallRainforestTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<EIHFeature> EIH = register("eih", () -> new EIHFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<UndergrowthFeature> UNDERGROWTH = register("undergrowth", () -> new UndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<SingleUndergrowthFeature> SINGLE_UNDERGROWTH = register("single_undergrowth", () -> new SingleUndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestVinesFeature> VINES = register("rainforest_vines", () -> new RainforestVinesFeature(RainforestVinesConfig.CODEC));
    public static final RegistryObject<UndergroundSeaPickleFeature> UNDERGROUND_SEA_PICKLE = register("underground_sea_pickle", () -> new UndergroundSeaPickleFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> KOA_VILLAGE = registerStructure("koa_village", new KoaVillageStructure(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> HOME_TREE = registerStructure("home_tree", new HomeTreeStructure(JigsawConfiguration.CODEC, HomeTreeStructure::checkLocation), GenerationStep.Decoration.SURFACE_STRUCTURES);
    public static final RegistryObject<CoffeePlantFeature> COFFEE_BUSH = register("coffee_bush", () -> new CoffeePlantFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<ReedsFeature> REEDS = register("reeds", () -> new ReedsFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<MangroveTreeFeature> MANGROVE_TREE = register("mangrove_tree", () -> new MangroveTreeFeature((TreeFeature) Feature.TREE, TreeConfiguration.CODEC));

    public static final StructureTemplatePool.Projection KOA_PATH = StructureTemplatePool.Projection.create("KOA_PATH", Constants.MODID + ":koa_path",
            ImmutableList.of(new SmoothingGravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1), new SinkInGroundProcessor(), new SteepPathProcessor(), new StructureSupportsProcessor(false, ImmutableList.of(TropicraftBlocks.BAMBOO_FENCE.getId()))));

    private static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }

    private static <T extends StructureFeature<?>> RegistryObject<T> registerStructure(final String name, T structure, GenerationStep.Decoration step) {
        StructureFeature.STEP.put(structure, step);
        return STRUCTURES.register(name, () -> structure);
    }
}
