package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveFoliagePlacer;

public final class TropicraftFoliagePlacers {
    public static final DeferredRegister<FoliagePlacerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, Constants.MODID);

    public static RegistryObject<FoliagePlacerType<?>> MANGROVE = register("mangrove", MangroveFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<?>> SMALL_MANGROVE = register("small_mangrove", SmallMangroveFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<?>> CITRUS = register("citrus", CitrusFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<?>> PLEODENDRON = register("pleodendron", PleodendronFoliagePlacer.CODEC);
    public static RegistryObject<FoliagePlacerType<?>> PAPAYA = register("papaya", PapayaFoliagePlacer.CODEC);

    private static <T extends FoliagePlacer> RegistryObject<FoliagePlacerType<?>> register(String name, Codec<T> codec) {
        return REGISTER.register(name, () -> new FoliagePlacerType<>(codec));
    }
}
