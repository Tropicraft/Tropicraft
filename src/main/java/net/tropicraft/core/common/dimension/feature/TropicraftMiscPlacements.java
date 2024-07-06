package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.tropicraft.Tropicraft;

import java.util.List;

import static net.tropicraft.core.common.dimension.feature.TropicraftPlacementUtil.commonOrePlacement;
import static net.tropicraft.core.common.dimension.feature.TropicraftPlacementUtil.register;

public final class TropicraftMiscPlacements {
    public static final ResourceKey<PlacedFeature> MUD_DISK = createKey("mud_disk");

    public static final ResourceKey<PlacedFeature> EIH = createKey("eih");

    public static final ResourceKey<PlacedFeature> AZURITE = createKey("azurite");

    public static final ResourceKey<PlacedFeature> EUDIALYTE = createKey("eudialyte");

    public static final ResourceKey<PlacedFeature> ZIRCON = createKey("zircon");

    public static final ResourceKey<PlacedFeature> MANGANESE = createKey("manganese");

    public static final ResourceKey<PlacedFeature> SHAKA = createKey("shaka");

    public static void boostrap(BootstrapContext<PlacedFeature> context) {
        register(context, MUD_DISK, TropicraftMiscFeatures.MUD_DISK, List.of(
                CountPlacement.of(3),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
        ));

        register(context, EIH, TropicraftMiscFeatures.EIH, List.of(
                PlacementUtils.countExtra(0, 0.0025f, 1),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        ));

        register(context, AZURITE, TropicraftMiscFeatures.AZURITE, commonOrePlacement(
                4,
                HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-20), VerticalAnchor.belowTop(80))
        ));

        register(context, EUDIALYTE, TropicraftMiscFeatures.EUDIALYTE, commonOrePlacement(
                6,
                HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-20), VerticalAnchor.belowTop(80))
        ));

        register(context, ZIRCON, TropicraftMiscFeatures.ZIRCON, commonOrePlacement(
                10,
                HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-20), VerticalAnchor.belowTop(80))
        ));

        register(context, MANGANESE, TropicraftMiscFeatures.MANGANESE, commonOrePlacement(
                8,
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(32))
        ));

        register(context, SHAKA, TropicraftMiscFeatures.SHAKA, commonOrePlacement(
                6,
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(32))
        ));
    }

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return Tropicraft.resourceKey(Registries.PLACED_FEATURE, name);
    }

    public static void addMudDisks(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MUD_DISK);
    }

    public static void addEih(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EIH);
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

    private static void addOres(BiomeGenerationSettings.Builder generation, ResourceKey<PlacedFeature> feature) {
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
    }
}
