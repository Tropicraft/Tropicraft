package net.tropicraft.core.common.dimension.chunk;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.IChunkGeneratorFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.config.TropicraftGeneratorSettings;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftChunkGeneratorType extends ChunkGeneratorType {

    public static final ChunkGeneratorType<TropicraftGeneratorSettings, TropicraftChunkGenerator> TROPICS = new ChunkGeneratorType<>(TropicraftChunkGenerator::new, true, TropicraftGeneratorSettings::new);

    public TropicraftChunkGeneratorType(IChunkGeneratorFactory factory, boolean isPublic, Supplier settingsSupplier) {
        super(factory, isPublic, settingsSupplier);
    }

    @SubscribeEvent
    public static void onChunkGeneratorTypeRegistry(final RegistryEvent.Register<ChunkGeneratorType<?, ?>> event) {
        register(event, TROPICS, "tropicraft_chunk_generator_type");
    }

    private static final void register(final RegistryEvent.Register<ChunkGeneratorType<?, ?>> event, final ChunkGeneratorType<?,?> type, final String name) {
        event.getRegistry().register(type.setRegistryName(new ResourceLocation(Constants.MODID, name)));
    }
}
