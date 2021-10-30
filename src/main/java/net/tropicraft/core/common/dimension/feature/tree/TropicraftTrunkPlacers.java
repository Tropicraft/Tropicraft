package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveTrunkPlacer;

public final class TropicraftTrunkPlacers {
    public static final TrunkPlacerType<MangroveTrunkPlacer> MANGROVE = register("mangrove", MangroveTrunkPlacer.CODEC);
    public static final TrunkPlacerType<SmallMangroveTrunkPlacer> SMALL_MANGROVE = register("small_mangrove", SmallMangroveTrunkPlacer.CODEC);
    public static final TrunkPlacerType<CitrusTrunkPlacer> CITRUS = register("citrus", CitrusTrunkPlacer.CODEC);
    public static final TrunkPlacerType<PleodendronTrunkPlacer> PLEODENDRON = register("pleodendron", PleodendronTrunkPlacer.CODEC);

    private static <T extends TrunkPlacer> TrunkPlacerType<T> register(String name, Codec<T> codec) {
        return Registry.register(Registry.TRUNK_PLACER_TYPES, new ResourceLocation(Constants.MODID, name), new TrunkPlacerType<>(codec));
    }
}
