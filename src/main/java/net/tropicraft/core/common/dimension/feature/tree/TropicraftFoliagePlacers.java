package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveFoliagePlacer;

public final class TropicraftFoliagePlacers {
    public static final DeferredRegister<FoliagePlacerType<?>> REGISTER = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Tropicraft.ID);

    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<?>> MANGROVE = register("mangrove", MangroveFoliagePlacer.CODEC);
    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<?>> SMALL_MANGROVE = register("small_mangrove", SmallMangroveFoliagePlacer.CODEC);
    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<?>> CITRUS = register("citrus", CitrusFoliagePlacer.CODEC);
    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<?>> PLEODENDRON = register("pleodendron", PleodendronFoliagePlacer.CODEC);
    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<?>> PAPAYA = register("papaya", PapayaFoliagePlacer.CODEC);

    private static <T extends FoliagePlacer> DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<?>> register(String name, MapCodec<T> codec) {
        return REGISTER.register(name, () -> new FoliagePlacerType<>(codec));
    }
}
