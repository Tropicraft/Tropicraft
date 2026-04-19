package net.tropicraft.core.common.dimension.biome;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.Tags;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftMiscPlacements;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationPlacements;
import net.tropicraft.core.common.entity.TropicraftEntities;

import java.util.function.Consumer;

public final class TropicraftBiomes {
    public static final int TROPICS_WATER_COLOR = 0xff4eecdf;
    public static final int TROPICS_WATER_FOG_COLOR = 0xff041f33;
    public static final int TROPICS_FOG_COLOR = 0xffC0D8FF;
    public static final int RAINFOREST_FOG_COLOR = 0xffbae6c3;
    public static final int TROPICS_SKY_COLOR = getSkyColor(0.8f);

    public static final ResourceKey<Biome> TROPICS = createKey("tropics");
    public static final ResourceKey<Biome> BEACH = createKey("beach");
    public static final ResourceKey<Biome> RAINFOREST = createKey("rainforest");
    public static final ResourceKey<Biome> BAMBOO_RAINFOREST = createKey("bamboo_rainforest");
    public static final ResourceKey<Biome> OSA_RAINFOREST = createKey("osa_rainforest");

    public static final ResourceKey<Biome> OCEAN = createKey("ocean");
    public static final ResourceKey<Biome> KELP_FOREST = createKey("kelp_forest");

    public static final ResourceKey<Biome> RIVER = createKey("river");

    public static final ResourceKey<Biome> MANGROVES = createKey("mangroves");
    public static final ResourceKey<Biome> OVERGROWN_MANGROVES = createKey("overgrown_mangroves");
    public static final ResourceKey<Biome> TROPICAL_PEAKS = createKey("tropical_peaks");

    public static void setup(Registrate registrate) {
        registrate.addDataGenerator(ProviderType.LANG, prov -> {
            Consumer<String> register = name -> prov.add("biome." + Tropicraft.ID + "." + name, RegistrateLangProvider.toEnglishName(name));
            register.accept("tropics");
            register.accept("beach");
            register.accept("rainforest");
            register.accept("bamboo_rainforest");
            register.accept("osa_rainforest");
            register.accept("ocean");
            register.accept("kelp_forest");
            register.accept("river");
            register.accept("mangroves");
            register.accept("overgrown_mangroves");
            register.accept("tropical_peaks");
        });

        registrate.addDataGenerator(Tropicraft.BIOME_TAGS, prov -> {
            prov.tag(Tags.Biomes.IS_OCEAN).add(OCEAN, KELP_FOREST);
            prov.tag(Tags.Biomes.IS_AQUATIC).add(OCEAN, KELP_FOREST, RIVER);

            prov.tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).add(RAINFOREST, OSA_RAINFOREST, BAMBOO_RAINFOREST);
            prov.tag(BiomeTags.WATER_ON_MAP_OUTLINES).add(OCEAN, KELP_FOREST, RIVER, MANGROVES, OVERGROWN_MANGROVES);

            prov.tag(TropicraftTags.Biomes.HAS_HOME_TREE).add(RAINFOREST, BAMBOO_RAINFOREST, OSA_RAINFOREST);
            prov.tag(TropicraftTags.Biomes.HAS_KOA_VILLAGE).add(BEACH);
            prov.tag(TropicraftTags.Biomes.HAS_LAND_VOLCANO).add(TROPICS, RAINFOREST);
            prov.tag(TropicraftTags.Biomes.HAS_OCEAN_VOLCANO).add(OCEAN);
        });
    }

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(TROPICS, createTropics(context));
        context.register(BEACH, createBeach(context));
        context.register(RAINFOREST, createRainforest(context, false));
        context.register(BAMBOO_RAINFOREST, createRainforest(context, true));
        context.register(OSA_RAINFOREST, createOsaRainforest(context));

        context.register(OCEAN, createOcean(context));
        context.register(KELP_FOREST, createKelpForest(context));

        context.register(RIVER, createRiver(context));

        context.register(MANGROVES, createMangroves(context, false));
        context.register(OVERGROWN_MANGROVES, createMangroves(context, true));
        context.register(TROPICAL_PEAKS, createTropicalPeaks(context));
    }

    private static Biome createTropics(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);

        TropicraftConfiguredCarvers.addLand(generation);

        TropicraftVegetationPlacements.addFloweringBushes(generation);

        TropicraftVegetationPlacements.addFruitTrees(generation);
        TropicraftVegetationPlacements.addPalmTrees(generation);
        TropicraftMiscPlacements.addEih(generation);
        TropicraftVegetationPlacements.addPapaya(generation);
        TropicraftVegetationPlacements.addTropicsFlowers(generation);
        TropicraftVegetationPlacements.addPineapples(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);

        TropicraftVegetationPlacements.addTropicsGrass(generation);
        BiomeDefaultFeatures.addFerns(generation);
        BiomeDefaultFeatures.addSavannaGrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.AMBIENT, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL.get(), 5, 15));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_BEE.get(), 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.COWKTAIL.get(), 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 4, 4));

        spawns.creatureGenerationProbability(0.2f);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(2.0f).downfall(1.5f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(true))
                .build();
    }

    private static Biome createBeach(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);
        TropicraftVegetationPlacements.addPalmTrees(generation);
        TropicraftVegetationPlacements.addTropicsFlowers(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);
        TropicraftVegetationPlacements.addBeachGrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB.get(), 1, 2));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(1.5f).downfall(1.25f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(false))
                .build();
    }

    private static Biome createOsaRainforest(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);

        TropicraftConfiguredCarvers.addLand(generation);

        TropicraftVegetationPlacements.addGoldenLeatherFern(generation);

        TropicraftVegetationPlacements.addFloweringBushes(generation);

        TropicraftMiscPlacements.addTropicsGems(generation);
        TropicraftVegetationPlacements.addPleodendron(generation);
        TropicraftVegetationPlacements.addRainforestTrees(generation);
        TropicraftVegetationPlacements.addPapaya(generation);

        TropicraftVegetationPlacements.addTropicsFlowers(generation);
        TropicraftVegetationPlacements.addPineapples(generation);

        TropicraftVegetationPlacements.addSeagrass(generation);
        TropicraftVegetationPlacements.addTropicsGrass(generation);
        BiomeDefaultFeatures.addFerns(generation);

        TropicraftVegetationPlacements.addRainforestPlants(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 1, 1));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, 25, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 2, 5));

        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR.get(), 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), 6, 12));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.GIBNUT.get(), 3, 8));
        spawns.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR.get(), 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD.get(), 3, 5));
        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY.get(), 6, 8));
        spawns.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(TropicraftEntities.TOUCAN.get(), 1, 2));

        spawns.creatureGenerationProbability(0.3f);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(1.5f).downfall(2.0f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(true))
                .build();
    }

    private static Biome createRainforest(BootstrapContext<Biome> context, boolean bamboo) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);

        TropicraftConfiguredCarvers.addLand(generation);

        TropicraftVegetationPlacements.addRareFloweringBushes(generation);
        TropicraftMiscPlacements.addTropicsGems(generation);
        TropicraftVegetationPlacements.addRainforestTrees(generation);
        TropicraftVegetationPlacements.addPapaya(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);

        BiomeDefaultFeatures.addLightBambooVegetation(generation);

        TropicraftVegetationPlacements.addRainforestPlants(generation);
        TropicraftVegetationPlacements.addUndergrowth(generation);

        if (bamboo) {
            TropicraftVegetationPlacements.addBamboo(generation);
        }

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 1, 1));
        spawns.addSpawn(MobCategory.CREATURE, 25, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 2, 5));

        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR.get(), 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.GIBNUT.get(), 3, 8));
        spawns.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR.get(), 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD.get(), 3, 5));
        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY.get(), 5, 8));
        spawns.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(TropicraftEntities.TOUCAN.get(), 1, 2));

        spawns.creatureGenerationProbability(0.3f);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(1.5f).downfall(2.0f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(true))
                .build();
    }

    private static Biome createOcean(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);

        // Needed as oceans can sometimes produce land above sea level
        TropicraftVegetationPlacements.addPalmTrees(generation);
        TropicraftMiscPlacements.addTropicsMetals(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TropicraftVegetationPlacements.TROPI_SEAGRASS);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.WARM_OCEAN_VEGETATION);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEA_PICKLE);

        TropicraftVegetationPlacements.addBeachGrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        addOceanWaterCreatures(spawns);
        spawns.addSpawn(MobCategory.AMBIENT, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL.get(), 5, 10));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(1.5f).downfall(1.25f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(false))
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD.withUnderwater(Musics.UNDER_WATER))
                .build();
    }

    private static Biome createKelpForest(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);

        // Needed as oceans can sometimes produce land above sea level
        TropicraftVegetationPlacements.addPalmTrees(generation);

        TropicraftVegetationPlacements.addSeagrass(generation);
        TropicraftVegetationPlacements.addBeachGrass(generation);

        // KELP!
        TropicraftVegetationPlacements.addKelp(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        addOceanWaterCreatures(spawns);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(1.5f).downfall(1.25f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(false))
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD.withUnderwater(Musics.UNDER_WATER))
                .build();
    }

    private static Biome createRiver(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);
        TropicraftConfiguredCarvers.addLand(generation);

        TropicraftVegetationPlacements.addTropicsFlowers(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);
        TropicraftVegetationPlacements.addBeachGrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        addRiverWaterCreatures(spawns);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(1.5f).downfall(1.25f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(false))
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD.withUnderwater(Musics.UNDER_WATER))
                .build();
    }

    private static Biome createTropicalPeaks(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);

        TropicraftMiscPlacements.addTropicsGems(generation);
        TropicraftVegetationPlacements.addRainforestTrees(generation);
        TropicraftVegetationPlacements.addPapaya(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);

        BiomeDefaultFeatures.addJungleGrass(generation);
        BiomeDefaultFeatures.addLightBambooVegetation(generation);

        TropicraftVegetationPlacements.addRainforestPlants(generation);
        TropicraftVegetationPlacements.addUndergrowth(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(1.5f).downfall(1.25f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience().build())
                .putAttributes(defaultAttributes(true))
                .build();
    }

    // TODO: rebalance all spawns
    private static Biome createMangroves(BootstrapContext<Biome> context, boolean overgrown) {
        BiomeGenerationSettings.Builder generation = defaultGeneration(context);

        TropicraftConfiguredCarvers.addLand(generation);

        TropicraftMiscPlacements.addMudDisks(generation);
        if (overgrown) {
            TropicraftVegetationPlacements.addOvergrownGoldenLeatherFern(generation);
        }
        TropicraftVegetationPlacements.addGoldenLeatherFern(generation);
        TropicraftVegetationPlacements.addMangroveVegetation(generation, overgrown);
        TropicraftVegetationPlacements.addTropicsFlowers(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_DEEP_WARM);
        TropicraftVegetationPlacements.addMangroveReeds(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);

        BiomeDefaultFeatures.addSavannaGrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.AMBIENT, 5, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL.get(), 5, 10));
        spawns.addSpawn(MobCategory.CREATURE, 4, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR.get(), 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.GIBNUT.get(), 3, 8));
        spawns.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR.get(), 2, 3));
        spawns.addSpawn(MobCategory.CREATURE, 12, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD.get(), 3, 5));
        spawns.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY.get(), 5, 8));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.BROWN_BASILISK_LIZARD.get(), 2, 3));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.GREEN_BASILISK_LIZARD.get(), 2, 3));

        addMangroveWaterCreatures(spawns);

        BiomeSpecialEffects.Builder ambience = defaultAmbience()
                .waterColor(0x66C197)
                .grassColorOverride(0x6FB21C);

        spawns.creatureGenerationProbability(0.3f);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(2.0f).downfall(1.5f)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(ambience.build())
                .putAttributes(defaultAttributes(true))
                .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0xff0C3522)
                .build();
    }

    private static void addOceanWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 20, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPICAL_FISH.get(), 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(TropicraftEntities.STARFISH.get(), 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 6, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEAHORSE.get(), 6, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_URCHIN.get(), 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 2, new MobSpawnSettings.SpawnerData(TropicraftEntities.MAN_O_WAR.get(), 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.MARLIN.get(), 1, 4));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.CUBERA.get(), 2, 4));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 6, new MobSpawnSettings.SpawnerData(TropicraftEntities.EAGLE_RAY.get(), 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 6, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_TURTLE.get(), 3, 8));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 3, new MobSpawnSettings.SpawnerData(TropicraftEntities.DOLPHIN.get(), 4, 7));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(TropicraftEntities.HAMMERHEAD.get(), 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(TropicraftEntities.MANATEE.get(), 1, 3));
    }

    private static void addRiverWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.CREATURE, 20, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB.get(), 2, 6));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.PIRANHA.get(), 1, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 20, new MobSpawnSettings.SpawnerData(TropicraftEntities.RIVER_SARDINE.get(), 1, 8));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(EntityType.COD, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5));
    }

    private static void addMangroveWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.CREATURE, 20, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB.get(), 2, 6));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 15, new MobSpawnSettings.SpawnerData(TropicraftEntities.PIRANHA.get(), 1, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 20, new MobSpawnSettings.SpawnerData(TropicraftEntities.RIVER_SARDINE.get(), 1, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(EntityType.COD, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 12, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(TropicraftEntities.STARFISH.get(), 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 4, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_URCHIN.get(), 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, 6, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEAHORSE.get(), 6, 12));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(TropicraftEntities.HAMMERHEAD.get(), 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4));
    }

    private static BiomeGenerationSettings.Builder defaultGeneration(BootstrapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);

        return generation;
    }

    private static MobSpawnSettings.Builder defaultSpawns() {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 2, 2));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.V_MONKEY.get(), 2, 3));
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TropicraftEntities.IGUANA.get(), 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, 7, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPICREEPER.get(), 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, 7, new MobSpawnSettings.SpawnerData(TropicraftEntities.EIH.get(), 1, 1));

        spawns.addSpawn(MobCategory.MONSTER, 200, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPISKELLY.get(), 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, 150, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SPIDER.get(), 2, 2));

        // Vanilla mob spawns
        BiomeDefaultFeatures.commonSpawns(spawns);

        return spawns;
    }

    private static BiomeSpecialEffects.Builder defaultAmbience() {
        return new BiomeSpecialEffects.Builder()
                .waterColor(TROPICS_WATER_COLOR);
    }

    private static EnvironmentAttributeMap.Builder defaultAttributes(boolean greenFog) {
        return EnvironmentAttributeMap.builder()
                .set(EnvironmentAttributes.FOG_COLOR, greenFog ? RAINFOREST_FOG_COLOR : TROPICS_FOG_COLOR)
                .set(EnvironmentAttributes.SKY_COLOR, TROPICS_SKY_COLOR)
                .set(EnvironmentAttributes.WATER_FOG_COLOR, TROPICS_WATER_FOG_COLOR);
    }

    private static int getSkyColor(float temperature) {
        float shift = Mth.clamp(temperature / 3.0f, -1.0f, 1.0f);
        return ARGB.opaque(Mth.hsvToRgb((224.0f / 360.0f) - shift * 0.05f, 0.5f + shift * 0.1f, 1.0f));
    }

    private static ResourceKey<Biome> createKey(String name) {
        return Tropicraft.resourceKey(Registries.BIOME, name);
    }
}
