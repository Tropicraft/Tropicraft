package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveTrunkPlacer;

public final class TropicraftTrunkPlacers {
    public static final DeferredRegister<TrunkPlacerType<?>> REGISTER = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Constants.MODID);

    public static final RegistryObject<TrunkPlacerType<?>> MANGROVE = register("mangrove", MangroveTrunkPlacer.CODEC);
    public static final RegistryObject<TrunkPlacerType<?>> SMALL_MANGROVE = register("small_mangrove", SmallMangroveTrunkPlacer.CODEC);
    public static final RegistryObject<TrunkPlacerType<?>> CITRUS = register("citrus", CitrusTrunkPlacer.CODEC);
    public static final RegistryObject<TrunkPlacerType<?>> PLEODENDRON = register("pleodendron", PleodendronTrunkPlacer.CODEC);

    private static <T extends TrunkPlacer> RegistryObject<TrunkPlacerType<?>> register(String name, Codec<T> codec) {
        return REGISTER.register(name, () -> new TrunkPlacerType<>(codec));
    }
}
