package net.tropicraft.core.common.dimension.biome;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.config.TropicraftBiomeProviderSettings;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftBiomeProviderTypes {

    public static final BiomeProviderType<TropicraftBiomeProviderSettings, TropicraftBiomeProvider> TROPICS = new BiomeProviderType<>(TropicraftBiomeProvider::new, TropicraftBiomeProviderSettings::new);

    @SubscribeEvent
    public static void onBiomeProviderTypeRegistry(final RegistryEvent.Register<BiomeProviderType<?, ?>> event) {
        register(event, TROPICS, "tropicraft_biome_provider_type");
    }

    private static final void register(final RegistryEvent.Register<BiomeProviderType<?, ?>> event, final BiomeProviderType<?,?> type, final String name) {
        event.getRegistry().register(type.setRegistryName(new ResourceLocation(Constants.MODID, name)));
    }
}
