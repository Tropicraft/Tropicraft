package net.tropicraft.core.common.dimension.feature;

import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public final class TropicraftMiscPlacements {
    public static final TropicraftFeatures.Register REGISTER = TropicraftFeatures.Register.create();

    public static final RegistryObject<PlacedFeature> MUD_DISK = REGISTER.placed("mud_disk", TropicraftMiscFeatures.MUD_DISK, () -> List.of(
            CountPlacement.of(3),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            BiomeFilter.biome()
    ));

    public static final RegistryObject<PlacedFeature> EIH = REGISTER.placed("eih", TropicraftMiscFeatures.EIH, () -> List.of(
            PlacementUtils.countExtra(0, 0.0025f, 1),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()
    ));

    // TODO 1.18 decide ore placements in 1.18
    public static final RegistryObject<PlacedFeature> AZURITE = REGISTER.placed("azurite", TropicraftMiscFeatures.AZURITE, () -> REGISTER.rareOrePlacement(
            3,
            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))
    ));

    public static final RegistryObject<PlacedFeature> EUDIALYTE = REGISTER.placed("eudialyte", TropicraftMiscFeatures.EUDIALYTE, () -> REGISTER.rareOrePlacement(
            10,
            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))
    ));

    public static final RegistryObject<PlacedFeature> ZIRCON = REGISTER.placed("zircon", TropicraftMiscFeatures.ZIRCON, () -> REGISTER.rareOrePlacement(
            15,
            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))
    ));

    public static final RegistryObject<PlacedFeature> MANGANESE = REGISTER.placed("manganese", TropicraftMiscFeatures.MANGANESE, () -> REGISTER.rareOrePlacement(
            8,
            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-16), VerticalAnchor.aboveBottom(32))
    ));

    public static final RegistryObject<PlacedFeature> SHAKA = REGISTER.placed("shaka", TropicraftMiscFeatures.SHAKA, () -> REGISTER.rareOrePlacement(
            6,
            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-16), VerticalAnchor.aboveBottom(32))
    ));

    public static void addMudDisks(BiomeGenerationSettings.Builder generation) {
        addFeature(generation, GenerationStep.Decoration.UNDERGROUND_ORES, MUD_DISK);
    }

    public static void addEih(BiomeGenerationSettings.Builder generation) {
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, EIH);
    }

    public static void addTropicsGems(BiomeGenerationSettings.Builder generation) {
        addOres(generation, AZURITE);
        addOres(generation, EUDIALYTE);
        addOres(generation, ZIRCON);
    }

    public static void addTropicsMetals(BiomeGenerationSettings.Builder generation) {
        addOres(generation, MANGANESE);
        addOres(generation, SHAKA);
    }

    private static void addOres(BiomeGenerationSettings.Builder generation, RegistryObject<PlacedFeature> feature) {
        addFeature(generation, GenerationStep.Decoration.UNDERGROUND_ORES, feature);
    }

    private static void addFeature(BiomeGenerationSettings.Builder generation, GenerationStep.Decoration step, RegistryObject<PlacedFeature> feature) {
        generation.addFeature(step, TropicraftFeatures.holderOf(feature));
    }
}
