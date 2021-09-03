package net.tropicraft.core.common.dimension.biome;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftConfiguredSurfaceBuilders;
import net.tropicraft.core.common.entity.TropicraftEntities;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class TropicraftBiomes {
    public static final int TROPICS_WATER_COLOR = 0x4eecdf;
    public static final int TROPICS_WATER_FOG_COLOR = 0x041f33;
    public static final int TROPICS_FOG_COLOR = 0xC0D8FF;
    public static final int RAINFOREST_FOG_COLOR = 0xbae6c3;
    public static final int TROPICS_SKY_COLOR = getSkyColor(0.8F);

    public static final RegistryKey<Biome> TROPICS_OCEAN = key("tropics_ocean");
    public static final RegistryKey<Biome> TROPICS = key("tropics");
    public static final RegistryKey<Biome> KELP_FOREST = key("kelp_forest");
    public static final RegistryKey<Biome> RAINFOREST_PLAINS = key("rainforest_plains");
    public static final RegistryKey<Biome> RAINFOREST_HILLS = key("rainforest_hills");
    public static final RegistryKey<Biome> RAINFOREST_MOUNTAINS = key("rainforest_mountains");
    public static final RegistryKey<Biome> BAMBOO_RAINFOREST = key("bamboo_rainforest");
    public static final RegistryKey<Biome> RAINFOREST_ISLAND_MOUNTAINS = key("rainforest_island_mountains");
    public static final RegistryKey<Biome> TROPICS_RIVER = key("tropics_river");
    public static final RegistryKey<Biome> TROPICS_BEACH = key("tropics_beach");
    public static final RegistryKey<Biome> MANGROVES = key("mangroves");

    private static RegistryKey<Biome> key(String id) {
        return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, new ResourceLocation(Constants.MODID, id));
    }

    public final Biome tropics;
    public final Biome tropicsBeach;
    public final Biome rainforestPlains;
    public final Biome rainforestHills;
    public final Biome rainforestMountains;
    public final Biome bambooRainforest;
    public final Biome rainforestIslandMountains;

    public final Biome tropicsOcean;
    public final Biome kelpForest;

    public final Biome tropicsRiver;

    public final Biome mangroves;

    private final TropicraftConfiguredFeatures features;
    private final TropicraftConfiguredStructures structures;
    private final TropicraftConfiguredCarvers carvers;
    private final TropicraftConfiguredSurfaceBuilders surfaces;

    public TropicraftBiomes(WorldgenDataConsumer<Biome> worldgen, TropicraftConfiguredFeatures features, TropicraftConfiguredStructures structures, TropicraftConfiguredCarvers carvers, TropicraftConfiguredSurfaceBuilders surfaces) {
        this.features = features;
        this.structures = structures;
        this.carvers = carvers;
        this.surfaces = surfaces;

        this.tropics = worldgen.register(TROPICS, createTropics());
        this.tropicsBeach = worldgen.register(TROPICS_BEACH, createTropicsBeach());
        this.rainforestPlains = worldgen.register(RAINFOREST_PLAINS, createRainforest(0.25F, 0.1F));
        this.rainforestHills = worldgen.register(RAINFOREST_HILLS, createRainforest(0.45F, 0.425F));
        this.rainforestMountains = worldgen.register(RAINFOREST_MOUNTAINS, createRainforest(0.8F, 0.8F));
        this.bambooRainforest = worldgen.register(BAMBOO_RAINFOREST, createRainforest(0.25F, 0.25F, true));
        this.rainforestIslandMountains = worldgen.register(RAINFOREST_ISLAND_MOUNTAINS, createRainforest(0.1F, 1.2F));

        this.tropicsOcean = worldgen.register(TROPICS_OCEAN, createTropicsOcean());
        this.kelpForest = worldgen.register(KELP_FOREST, createKelpForest());

        this.tropicsRiver = worldgen.register(TROPICS_RIVER, createTropicsRiver());

        this.mangroves = worldgen.register(MANGROVES, createMangroves());
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        ResourceLocation name = event.getName();
        if (name != null && name.getNamespace().equals(Constants.MODID)) {
            return;
        }

        Biome.Category category = event.getCategory();

        Biome.RainType precipitation = event.getClimate().precipitation;
        if (precipitation == Biome.RainType.SNOW) {
            return;
        }

        BiomeGenerationSettingsBuilder generation = event.getGeneration();

        if (category == Biome.Category.BEACH) {
            generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    TropicraftFeatures.NORMAL_PALM_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                            .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.08F, 1)))
            );
            generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    TropicraftFeatures.CURVED_PALM_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                            .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.08F, 1)))
            );
            generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    TropicraftFeatures.LARGE_PALM_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                            .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.08F, 1)))
            );
        } else if (category == Biome.Category.JUNGLE) {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.PINEAPPLE.get().getDefaultState());
            generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                    Feature.RANDOM_PATCH.withConfiguration(new BlockClusterFeatureConfig.Builder(state, new DoublePlantBlockPlacer())
                            .tries(6)
                            .replaceable()
                            .build()
                    ).withPlacement(Features.Placements.PATCH_PLACEMENT)
            );
        }
    }

    private Biome createTropics() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .withSurfaceBuilder(surfaces.tropics);

        carvers.addLand(generation);

        features.addFruitTrees(generation);
        features.addPalmTrees(generation);
        features.addEih(generation);
        features.addTropicsFlowers(generation);
        features.addPineapples(generation);
        features.addRegularSeagrass(generation);

        features.addTropicsGrass(generation);
        DefaultBiomeFeatures.withLargeFern(generation);
        DefaultBiomeFeatures.withTallGrass(generation);

        MobSpawnInfo.Builder spawns = defaultSpawns();
        spawns.withSpawner(EntityClassification.AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.FAILGULL.get(), 10, 5, 15));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.TROPI_BEE.get(), 10, 4, 4));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.COWKTAIL.get(), 10, 4, 4));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.TREE_FROG.get(), 4, 4, 4));

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .depth(0.1F).scale(0.1F)
                .temperature(2.0F).downfall(1.5F)
                .category(Biome.Category.PLAINS)
                .withGenerationSettings(generation.build())
                .withMobSpawnSettings(spawns.build())
                .setEffects(defaultAmbience(true).build())
                .build();
    }

    private Biome createTropicsBeach() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .withSurfaceBuilder(surfaces.sandy);

        features.addRegularSeagrass(generation);
        carvers.addUnderwater(generation);

        features.addPalmTrees(generation);
        features.addTropicsFlowers(generation);

        generation.withStructure(structures.koaVillage);

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .depth(-0.1F).scale(0.1F)
                .temperature(1.5F).downfall(1.25F)
                .category(Biome.Category.BEACH)
                .withGenerationSettings(generation.build())
                .withMobSpawnSettings(defaultSpawns().build())
                .setEffects(defaultAmbience(false).build())
                .build();
    }

    private Biome createRainforest(float depth, float scale) {
        return createRainforest(depth, scale, false);
    }

    private Biome createRainforest(float depth, float scale, boolean bamboo) {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .withSurfaceBuilder(ConfiguredSurfaceBuilders.GRASS);

        carvers.addLand(generation);

        features.addTropicsGems(generation);
        features.addPleodendron(generation);
        features.addRainforestTrees(generation);
        features.addRegularSeagrass(generation);

        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, features.rainforestFlowers);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, features.coffeeBush);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, features.undergrowth);

        generation.withStructure(structures.homeTree);

        DefaultBiomeFeatures.withJungleGrass(generation);
        DefaultBiomeFeatures.withLightBambooVegetation(generation);

        features.addRainforestPlants(generation);

        if (bamboo) {
            features.addBamboo(generation);
        }

        MobSpawnInfo.Builder spawns = defaultSpawns();
        spawns.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.OCELOT, 10, 1, 1));
        spawns.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.PARROT, 10, 1, 2));
        spawns.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.TREE_FROG.get(), 25, 2, 5));
        spawns.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.TROPI_SPIDER.get(), 30, 1, 1));

        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.TAPIR.get(), 10, 1, 3));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.JAGUAR.get(), 3, 1, 1));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.HUMMINGBIRD.get(), 10, 1, 4));

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .depth(depth).scale(scale)
                .temperature(1.5F).downfall(2.0F)
                .category(Biome.Category.JUNGLE)
                .withGenerationSettings(generation.build())
                .withMobSpawnSettings(spawns.build())
                .setEffects(defaultAmbience(true).build())
                .build();
    }

    private Biome createTropicsOcean() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .withSurfaceBuilder(surfaces.sandy);

        carvers.addUnderwater(generation);

        features.addTropicsMetals(generation);

        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.WARM_OCEAN_VEGETATION);

        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_WARM);
        features.addUndergroundSeagrass(generation);

        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.SEA_PICKLE);
        features.addUndergroundPickles(generation);

        MobSpawnInfo.Builder spawns = defaultSpawns();
        this.addOceanWaterCreatures(spawns);
        spawns.withSpawner(EntityClassification.AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.FAILGULL.get(), 15, 5, 10));

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .depth(-1.6F).scale(0.4F)
                .temperature(1.5F).downfall(1.25F)
                .category(Biome.Category.OCEAN)
                .withGenerationSettings(generation.build())
                .withMobSpawnSettings(spawns.build())
                .setEffects(defaultAmbience(false).build())
                .build();
    }

    private Biome createKelpForest() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .withSurfaceBuilder(surfaces.sandy);

        carvers.addUnderwater(generation);

        // KELP!
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.KELP_COLD);

        features.addUndergroundSeagrass(generation);
        features.addUndergroundPickles(generation);

        MobSpawnInfo.Builder spawns = defaultSpawns();
        this.addOceanWaterCreatures(spawns);

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .depth(-1.5F).scale(0.3F)
                .temperature(1.5F).downfall(1.25F)
                .category(Biome.Category.OCEAN)
                .withGenerationSettings(generation.build())
                .withMobSpawnSettings(spawns.build())
                .setEffects(defaultAmbience(false).build())
                .build();
    }

    private Biome createTropicsRiver() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .withSurfaceBuilder(surfaces.sandy);

        carvers.addLand(generation);

        features.addTropicsFlowers(generation);

        MobSpawnInfo.Builder spawns = defaultSpawns();
        addRiverWaterCreatures(spawns);

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .depth(-0.7F).scale(0.05F)
                .temperature(1.5F).downfall(1.25F)
                .category(Biome.Category.RIVER)
                .withGenerationSettings(generation.build())
                .withMobSpawnSettings(spawns.build())
                .setEffects(defaultAmbience(false).build())
                .build();
    }

    private Biome createMangroves() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .withSurfaceBuilder(surfaces.mangrove);

        carvers.addLand(generation);

        features.addMudDisks(generation);
        features.addMangroveVegetation(generation);
        features.addTropicsFlowers(generation);

        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_DEEP_WARM);
        features.addMangroveReeds(generation);

        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_WATERLILLY);

        DefaultBiomeFeatures.withTallGrass(generation);

        MobSpawnInfo.Builder spawns = defaultSpawns();
        spawns.withSpawner(EntityClassification.AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.FAILGULL.get(), 10, 5, 15));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.TREE_FROG.get(), 4, 4, 4));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.HUMMINGBIRD.get(), 10, 1, 4));

        addMangroveWaterCreatures(spawns);

        BiomeAmbience.Builder ambience = defaultAmbience(true);
        ambience.setWaterColor(0x66C197).setWaterFogColor(0x0C3522);
        ambience.withGrassColor(0x6FB21C);

        return new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .depth(-0.25F).scale(-0.045F)
                .temperature(2.0F).downfall(1.5F)
                .category(Biome.Category.SWAMP)
                .withGenerationSettings(generation.build())
                .withMobSpawnSettings(spawns.build())
                .setEffects(ambience.build())
                .build();
    }

    private void addOceanWaterCreatures(MobSpawnInfo.Builder spawns) {
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.TROPICAL_FISH.get(), 20, 4, 8));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.STARFISH.get(), 4, 1, 4));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.SEAHORSE.get(), 6, 6, 12));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.SEA_URCHIN.get(), 4, 1, 4));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.MAN_O_WAR.get(), 2, 1, 1));
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.MARLIN.get(), 10, 1, 4));
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.EAGLE_RAY.get(), 6, 1, 1));
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.SEA_TURTLE.get(), 6, 3, 8));
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.DOLPHIN.get(), 3, 4, 7));
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.HAMMERHEAD.get(), 2, 1, 1));
    }

    private void addRiverWaterCreatures(MobSpawnInfo.Builder spawns) {
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.PIRANHA.get(), 20, 1, 12));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.RIVER_SARDINE.get(), 20, 1, 8));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(EntityType.SQUID, 8, 1, 4));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(EntityType.COD, 4, 1, 5));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(EntityType.SALMON, 4, 1, 5));
    }

    private void addMangroveWaterCreatures(MobSpawnInfo.Builder spawns) {
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.PIRANHA.get(), 20, 1, 12));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.RIVER_SARDINE.get(), 20, 1, 8));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.STARFISH.get(), 4, 1, 4));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.SEA_URCHIN.get(), 4, 1, 4));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(TropicraftEntities.SEAHORSE.get(), 6, 6, 12));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(EntityType.COD, 4, 1, 5));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(EntityType.SALMON, 4, 1, 5));
        spawns.withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(EntityType.TROPICAL_FISH, 12, 1, 5));
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(TropicraftEntities.HAMMERHEAD.get(), 2, 1, 1));
        spawns.withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(EntityType.SQUID, 8, 1, 4));
    }

    private BiomeGenerationSettings.Builder defaultGeneration() {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();

        DefaultBiomeFeatures.withStrongholdAndMineshaft(generation);
        DefaultBiomeFeatures.withOverworldOres(generation);
        DefaultBiomeFeatures.withCommonOverworldBlocks(generation);

        generation.withStructure(structures.homeTree);
        generation.withStructure(structures.koaVillage);

        return generation;
    }

    private MobSpawnInfo.Builder defaultSpawns() {
        MobSpawnInfo.Builder spawns = new MobSpawnInfo.Builder();

        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.PARROT, 20, 1, 2));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.V_MONKEY.get(), 20, 1, 3));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.IGUANA.get(), 15, 4, 4));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.TROPI_CREEPER.get(), 4, 1, 2));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.EIH.get(), 5, 1, 1));

        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.TROPI_SKELLY.get(), 8, 2, 4));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.TROPI_SPIDER.get(), 8, 2, 2));
        spawns.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(TropicraftEntities.ASHEN.get(), 6, 2, 4));

        return spawns;
    }

    private BiomeAmbience.Builder defaultAmbience(boolean greenFog) {
        return new BiomeAmbience.Builder()
                .setFogColor(greenFog ? RAINFOREST_FOG_COLOR : TROPICS_FOG_COLOR)
                .withSkyColor(TROPICS_SKY_COLOR)
                .setWaterColor(TROPICS_WATER_COLOR)
                .setWaterFogColor(TROPICS_WATER_FOG_COLOR);
    }

    private static int getSkyColor(float temperature) {
        float shift = MathHelper.clamp(temperature / 3.0F, -1.0F, 1.0F);
        return MathHelper.hsvToRGB((224.0F / 360.0F) - shift * 0.05F, 0.5F + shift * 0.1F, 1.0F);
    }
}
