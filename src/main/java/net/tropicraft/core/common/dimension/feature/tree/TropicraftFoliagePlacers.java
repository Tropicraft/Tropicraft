package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.SmallMangroveFoliagePlacer;

public final class TropicraftFoliagePlacers {
//    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, Constants.MODID);
    public static FoliagePlacerType<MangroveFoliagePlacer> MANGROVE = register("mangrove", MangroveFoliagePlacer.CODEC);
    public static FoliagePlacerType<SmallMangroveFoliagePlacer> SMALL_MANGROVE = register("small_mangrove", SmallMangroveFoliagePlacer.CODEC);
    public static FoliagePlacerType<CitrusFoliagePlacer> CITRUS = register("citrus", CitrusFoliagePlacer.CODEC);
    public static FoliagePlacerType<PleodendronFoliagePlacer> PLEODENDRON = register("pleodendron", PleodendronFoliagePlacer.CODEC);
    public static FoliagePlacerType<PapayaFoliagePlacer> PAPAYA = register("papaya", PapayaFoliagePlacer.CODEC);

    private static <T extends FoliagePlacer> FoliagePlacerType<T> register(String name, Codec<T> codec) {
        return Registry.register(Registry.FOLIAGE_PLACER_TYPES, new ResourceLocation(Constants.MODID, name), new FoliagePlacerType<>(codec));
    }
}
