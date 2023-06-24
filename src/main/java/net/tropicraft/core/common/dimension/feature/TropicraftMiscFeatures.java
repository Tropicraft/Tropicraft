package net.tropicraft.core.common.dimension.feature;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.common.block.TropicraftBlocks;

public final class TropicraftMiscFeatures {
    public static final TropicraftFeatures.Register REGISTER = TropicraftFeatures.Register.create();

    public static final RegistryObject<ConfiguredFeature<?, ?>> MUD_DISK = REGISTER.feature("mud_disk", Feature.DISK, () -> new DiskConfiguration(
            RuleBasedBlockStateProvider.simple(TropicraftBlocks.MUD.get()),
            BlockPredicate.matchesBlocks(Blocks.DIRT, Blocks.GRASS_BLOCK),
            UniformInt.of(2, 4),
            2
    ));

    public static final RegistryObject<ConfiguredFeature<?, ?>> EIH = REGISTER.feature("eih", TropicraftFeatures.EIH);

    // TODO 1.18 decide ore placements in 1.18
    public static final RegistryObject<ConfiguredFeature<?, ?>> AZURITE = REGISTER.feature("azurite", Feature.ORE, () -> REGISTER.ore(8, TropicraftBlocks.AZURITE_ORE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> EUDIALYTE = REGISTER.feature("eudialyte", Feature.ORE, () -> REGISTER.ore(12, TropicraftBlocks.EUDIALYTE_ORE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> ZIRCON = REGISTER.feature("zircon", Feature.ORE, () -> REGISTER.ore(14, TropicraftBlocks.ZIRCON_ORE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> MANGANESE = REGISTER.feature("manganese", Feature.ORE, () -> REGISTER.ore(10, TropicraftBlocks.MANGANESE_ORE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SHAKA = REGISTER.feature("shaka", Feature.ORE, () -> REGISTER.ore(8, TropicraftBlocks.SHAKA_ORE));
}
