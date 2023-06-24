package net.tropicraft.core.common.dimension.biome;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftMiscPlacements;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationPlacements;
import net.tropicraft.core.common.entity.TropicraftEntities;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class TropicraftBiomes {
    public static final Registrate REGISTRATE = Tropicraft.registrate();

    public static final DeferredRegister<Biome> REGISTER = DeferredRegister.create(Registry.BIOME_REGISTRY, Constants.MODID);

    public static final int TROPICS_WATER_COLOR = 0x4eecdf;
    public static final int TROPICS_WATER_FOG_COLOR = 0x041f33;
    public static final int TROPICS_FOG_COLOR = 0xC0D8FF;
    public static final int RAINFOREST_FOG_COLOR = 0xbae6c3;
    public static final int TROPICS_SKY_COLOR = getSkyColor(0.8F);

    public static final RegistryObject<Biome> TROPICS = REGISTER.register("tropics", TropicraftBiomes::createTropics);
    public static final RegistryObject<Biome> BEACH = REGISTER.register("beach", TropicraftBiomes::createBeach);
    public static final RegistryObject<Biome> RAINFOREST = REGISTER.register("rainforest", () -> createRainforest(false));
    public static final RegistryObject<Biome> BAMBOO_RAINFOREST = REGISTER.register("bamboo_rainforest", () -> createRainforest(true));
    public static final RegistryObject<Biome> OSA_RAINFOREST = REGISTER.register("osa_rainforest", TropicraftBiomes::createOsaRainforest);

    public static final RegistryObject<Biome> OCEAN = REGISTER.register("ocean", TropicraftBiomes::createOcean);
    public static final RegistryObject<Biome> KELP_FOREST = REGISTER.register("kelp_forest", TropicraftBiomes::createKelpForest);

    public static final RegistryObject<Biome> RIVER = REGISTER.register("river", TropicraftBiomes::createRiver);

    public static final RegistryObject<Biome> MANGROVES = REGISTER.register("mangroves", () -> createMangroves(false));
    public static final RegistryObject<Biome> OVERGROWN_MANGROVES = REGISTER.register("overgrown_mangroves", () -> createMangroves(true));
    public static final RegistryObject<Biome> TROPICAL_PEAKS = REGISTER.register("tropical_peaks", TropicraftBiomes::createTropicalPeaks);

    static {
        REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            final Consumer<String> register = name -> prov.add("biome." + Constants.MODID + "." + name, RegistrateLangProvider.toEnglishName(name));
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

        REGISTRATE.addDataGenerator(Tropicraft.BIOME_TAGS, prov -> {
            prov.tag(BiomeTags.IS_OCEAN).add(OCEAN.getKey(), KELP_FOREST.getKey());
            prov.tag(BiomeTags.IS_BEACH).add(BEACH.getKey());
            prov.tag(BiomeTags.IS_RIVER).add(RIVER.getKey());
            prov.tag(BiomeTags.IS_HILL).add(TROPICAL_PEAKS.getKey());
            prov.tag(BiomeTags.IS_JUNGLE).add(RAINFOREST.getKey(), OSA_RAINFOREST.getKey(), BAMBOO_RAINFOREST.getKey());
            prov.tag(BiomeTags.WATER_ON_MAP_OUTLINES).add(MANGROVES.getKey(), OVERGROWN_MANGROVES.getKey());

            prov.tag(TropicraftTags.Biomes.HAS_HOME_TREE).add(RAINFOREST.getKey(), BAMBOO_RAINFOREST.getKey(), OSA_RAINFOREST.getKey());
            prov.tag(TropicraftTags.Biomes.HAS_KOA_VILLAGE).add(BEACH.getKey());
            prov.tag(TropicraftTags.Biomes.HAS_LAND_VOLCANO).add(TROPICS.getKey(), RAINFOREST.getKey());
            prov.tag(TropicraftTags.Biomes.HAS_OCEAN_VOLCANO).add(OCEAN.getKey());
        });
    }

    private static Biome createTropics() {
        BiomeGenerationSettings.Builder generation = defaultGeneration();

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
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL.get(), 10, 5, 15));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_BEE.get(), 10, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.COWKTAIL.get(), 10, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 4, 4, 4));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(2.0F).downfall(1.5F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(true).build())
                .build();
    }

    private static Biome createBeach() {
        BiomeGenerationSettings.Builder generation = defaultGeneration();
        TropicraftVegetationPlacements.addPalmTrees(generation);
        TropicraftVegetationPlacements.addTropicsFlowers(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB.get(), 10, 1, 2));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(1.5F).downfall(1.25F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    private static Biome createOsaRainforest() {
        BiomeGenerationSettings.Builder generation = defaultGeneration();

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
        BiomeDefaultFeatures.addSavannaGrass(generation);

        TropicraftVegetationPlacements.addRainforestPlants(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 10, 1, 1));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 10, 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 25, 2, 5));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SPIDER.get(), 30, 1, 1));

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR.get(), 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), 15, 6, 12));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR.get(), 5, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD.get(), 10, 3, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY.get(), 15, 6, 8));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(1.5F).downfall(2.0F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(true).build())
                .build();
    }

    private static Biome createRainforest(boolean bamboo) {
        BiomeGenerationSettings.Builder generation = defaultGeneration();

        TropicraftConfiguredCarvers.addLand(generation);

        TropicraftVegetationPlacements.addRareFloweringBushes(generation);
        TropicraftMiscPlacements.addTropicsGems(generation);
        TropicraftVegetationPlacements.addRainforestTrees(generation);
        TropicraftVegetationPlacements.addPapaya(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);

        BiomeDefaultFeatures.addJungleGrass(generation);
        BiomeDefaultFeatures.addLightBambooVegetation(generation);

        TropicraftVegetationPlacements.addRainforestPlants(generation);
        TropicraftVegetationPlacements.addUndergrowth(generation);

        if (bamboo) {
            TropicraftVegetationPlacements.addBamboo(generation);
        }

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 10, 1, 1));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 10, 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 25, 2, 5));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SPIDER.get(), 30, 1, 1));

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR.get(), 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR.get(), 5, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD.get(), 10, 3, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY.get(), 15, 5, 8));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(1.5F).downfall(2.0F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(true).build())
                .build();
    }

    private static Biome createOcean() {
        BiomeGenerationSettings.Builder generation = defaultGeneration();

        // Needed as oceans can sometimes produce land above sea level
        TropicraftVegetationPlacements.addPalmTrees(generation);
        TropicraftMiscPlacements.addTropicsMetals(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TropicraftVegetationPlacements.TROPI_SEAGRASS.getHolder().get());

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.WARM_OCEAN_VEGETATION);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM);
        TropicraftVegetationPlacements.addUndergroundSeagrass(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEA_PICKLE);
        TropicraftVegetationPlacements.addUndergroundPickles(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        addOceanWaterCreatures(spawns);
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL.get(), 15, 5, 10));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(1.5F).downfall(1.25F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    private static Biome createKelpForest() {
        BiomeGenerationSettings.Builder generation = defaultGeneration();

        // Needed as oceans can sometimes produce land above sea level
        TropicraftVegetationPlacements.addPalmTrees(generation);

        TropicraftVegetationPlacements.addSeagrass(generation);
        // KELP!
        TropicraftVegetationPlacements.addKelp(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        addOceanWaterCreatures(spawns);

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(1.5F).downfall(1.25F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    private static Biome createRiver() {
        BiomeGenerationSettings.Builder generation = defaultGeneration();
        TropicraftConfiguredCarvers.addLand(generation);

        TropicraftVegetationPlacements.addTropicsFlowers(generation);
        TropicraftVegetationPlacements.addSeagrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        addRiverWaterCreatures(spawns);

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(1.5F).downfall(1.25F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    private static Biome createTropicalPeaks() {
        BiomeGenerationSettings.Builder generation = defaultGeneration();

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
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(1.5F).downfall(1.25F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(true).build())
                .build();
    }


    // TODO: rebalance all spawns
    private static Biome createMangroves(boolean overgrown) {
        BiomeGenerationSettings.Builder generation = defaultGeneration();

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
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL.get(), 5, 5, 15));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG.get(), 4, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR.get(), 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY.get(), 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR.get(), 8, 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD.get(), 12, 3, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY.get(), 15, 5, 8));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.BROWN_BASILISK_LIZARD.get(), 10, 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.GREEN_BASILISK_LIZARD.get(), 10, 1, 3));

        addMangroveWaterCreatures(spawns);

        BiomeSpecialEffects.Builder ambience = defaultAmbience(true);
        ambience.waterColor(0x66C197).waterFogColor(0x0C3522);
        ambience.grassColorOverride(0x6FB21C);

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(2.0F).downfall(1.5F)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(ambience.build())
                .build();
    }

    private static void addOceanWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPICAL_FISH.get(), 20, 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.STARFISH.get(), 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEAHORSE.get(), 6, 6, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_URCHIN.get(), 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.MAN_O_WAR.get(), 2, 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.MARLIN.get(), 10, 1, 4));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.CUBERA.get(), 10, 2, 4));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.EAGLE_RAY.get(), 6, 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_TURTLE.get(), 6, 3, 8));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.DOLPHIN.get(), 3, 4, 7));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.HAMMERHEAD.get(), 2, 1, 1));
    }

    private static void addRiverWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB.get(), 20, 2, 6));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.PIRANHA.get(), 15, 1, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.RIVER_SARDINE.get(), 20, 1, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 8, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, 4, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 4, 1, 5));
    }

    private static void addMangroveWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB.get(), 20, 2, 6));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.PIRANHA.get(), 15, 1, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.RIVER_SARDINE.get(), 20, 1, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, 4, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 4, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 12, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.STARFISH.get(), 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_URCHIN.get(), 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEAHORSE.get(), 6, 6, 12));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.HAMMERHEAD.get(), 2, 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 8, 1, 4));
    }

    private static BiomeGenerationSettings.Builder defaultGeneration() {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();

        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);

        return generation;
    }

    private static MobSpawnSettings.Builder defaultSpawns() {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 15, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.V_MONKEY.get(), 15, 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.IGUANA.get(), 15, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPICREEPER.get(), 4, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.EIH.get(), 5, 1, 1));

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPISKELLY.get(), 8, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SPIDER.get(), 8, 2, 2));

        return spawns;
    }

    private static BiomeSpecialEffects.Builder defaultAmbience(boolean greenFog) {
        return new BiomeSpecialEffects.Builder()
                .fogColor(greenFog ? RAINFOREST_FOG_COLOR : TROPICS_FOG_COLOR)
                .skyColor(TROPICS_SKY_COLOR)
                .waterColor(TROPICS_WATER_COLOR)
                .waterFogColor(TROPICS_WATER_FOG_COLOR);
    }

    private static int getSkyColor(float temperature) {
        float shift = Mth.clamp(temperature / 3.0F, -1.0F, 1.0F);
        return Mth.hsvToRgb((224.0F / 360.0F) - shift * 0.05F, 0.5F + shift * 0.1F, 1.0F);
    }
}
