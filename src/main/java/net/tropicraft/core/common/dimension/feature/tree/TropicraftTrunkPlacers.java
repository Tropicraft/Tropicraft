package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import net.tropicraft.Constants;

public final class TropicraftTrunkPlacers {
    public static final TrunkPlacerType<MangroveTrunkPlacer> MANGROVE = register("mangrove", MangroveTrunkPlacer.CODEC);
    public static final TrunkPlacerType<SmallMangroveTrunkPlacer> SMALL_MANGROVE = register("small_mangrove", SmallMangroveTrunkPlacer.CODEC);

    private static <T extends AbstractTrunkPlacer> TrunkPlacerType<T> register(String name, Codec<T> codec) {
        return Registry.register(Registry.TRUNK_REPLACER, new ResourceLocation(Constants.MODID, name), new TrunkPlacerType<>(codec));
    }
}
