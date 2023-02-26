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

    public static final RegistryObject<PlacedFeature> AZURITE = REGISTER.placed("azurite", TropicraftMiscFeatures.AZURITE, () -> REGISTER.commonOrePlacement(
            4,
            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-20), VerticalAnchor.belowTop(80))
    ));

    public static final RegistryObject<PlacedFeature> EUDIALYTE = REGISTER.placed("eudialyte", TropicraftMiscFeatures.EUDIALYTE, () -> REGISTER.commonOrePlacement(
            6,
            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-20), VerticalAnchor.belowTop(80))
    ));

    public static final RegistryObject<PlacedFeature> ZIRCON = REGISTER.placed("zircon", TropicraftMiscFeatures.ZIRCON, () -> REGISTER.commonOrePlacement(
            10,
            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-20), VerticalAnchor.belowTop(80))
    ));

    public static final RegistryObject<PlacedFeature> MANGANESE = REGISTER.placed("manganese", TropicraftMiscFeatures.MANGANESE, () -> REGISTER.commonOrePlacement(
            8,
            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(32))
    ));

    public static final RegistryObject<PlacedFeature> SHAKA = REGISTER.placed("shaka", TropicraftMiscFeatures.SHAKA, () -> REGISTER.commonOrePlacement(
            6,
            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(32))
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
