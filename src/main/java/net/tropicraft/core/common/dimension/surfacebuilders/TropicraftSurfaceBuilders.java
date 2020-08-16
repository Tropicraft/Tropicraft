package net.tropicraft.core.common.dimension.surfacebuilders;

import java.util.function.Supplier;

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftSurfaceBuilders {

    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = new DeferredRegister<>(ForgeRegistries.SURFACE_BUILDERS, Constants.MODID);

    public static final TropicsSurfaceBuilder _TROPICS = new TropicsSurfaceBuilder(SurfaceBuilderConfig::deserialize);
    public static final RegistryObject<TropicsSurfaceBuilder> TROPICS = register(
            "tropics", () -> _TROPICS);
    
    public static final UnderwaterSurfaceBuilder _UNDERWATER = new UnderwaterSurfaceBuilder(SurfaceBuilderConfig::deserialize);
    public static final RegistryObject<UnderwaterSurfaceBuilder> UNDERWATER = register(
            "underwater", () -> _UNDERWATER);

    private static <T extends SurfaceBuilder<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return SURFACE_BUILDERS.register(name, sup);
    }
}
