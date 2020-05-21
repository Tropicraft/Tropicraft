package net.tropicraft.core.common.dimension.biome;

import java.util.function.Supplier;

import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.config.TropicraftBiomeProviderSettings;

public class TropicraftBiomeProviderTypes {

    // TODO add back once forge supports it
//    public static final DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, Constants.MODID);
//
//    public static final RegistryObject<BiomeProviderType<TropicraftBiomeProviderSettings, TropicraftBiomeProvider>> TROPICS = register(
//            "tropicraft_biome_provider_type", () -> BiomeProviderType.FIXED.create());
//
//    private static <T extends BiomeProviderType<?, ?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
//        return BIOME_PROVIDER_TYPES.register(name, sup);
//    }
}
