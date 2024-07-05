package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.ore;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.register;

public final class TropicraftMiscFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> MUD_DISK = createKey("mud_disk");

    public static final ResourceKey<ConfiguredFeature<?, ?>> EIH = createKey("eih");

    // TODO 1.18 decide ore placements in 1.18
    public static final ResourceKey<ConfiguredFeature<?, ?>> AZURITE = createKey("azurite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> EUDIALYTE = createKey("eudialyte");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ZIRCON = createKey("zircon");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MANGANESE = createKey("manganese");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHAKA = createKey("shaka");

    public static void bootstrap(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, MUD_DISK, Feature.DISK, new DiskConfiguration(
                RuleBasedBlockStateProvider.simple(TropicraftBlocks.MUD.get()),
                BlockPredicate.matchesBlocks(Blocks.DIRT, Blocks.GRASS_BLOCK),
                UniformInt.of(2, 4),
                2
        ));
        register(context, EIH, TropicraftFeatures.EIH, NoneFeatureConfiguration.INSTANCE);
        register(context, AZURITE, Feature.ORE, ore(8, TropicraftBlocks.AZURITE_ORE));
        register(context, EUDIALYTE, Feature.ORE, ore(12, TropicraftBlocks.EUDIALYTE_ORE));
        register(context, ZIRCON, Feature.ORE, ore(14, TropicraftBlocks.ZIRCON_ORE));
        register(context, MANGANESE, Feature.ORE, ore(10, TropicraftBlocks.MANGANESE_ORE));
        register(context, SHAKA, Feature.ORE, ore(8, TropicraftBlocks.SHAKA_ORE));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(final String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MODID, name));
    }
}
