package net.tropicraft.core.common.dimension.surfacebuilders;

import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;

import java.util.function.Supplier;

public class TropicraftSurfaceBuilders {

    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, Constants.MODID);

    public static final RegistryObject<TropicsSurfaceBuilder> TROPICS = register("tropics", () -> new TropicsSurfaceBuilder(TropicsSurfaceBuilder.Config.CODEC));
    public static final RegistryObject<UnderwaterSurfaceBuilder> UNDERWATER = register("underwater", () -> new UnderwaterSurfaceBuilder(UnderwaterSurfaceBuilder.Config.CODEC));
    public static final RegistryObject<MangroveSurfaceBuilder> MANGROVE = register("mangrove", () -> new MangroveSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));

    private static <T extends SurfaceBuilder<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return SURFACE_BUILDERS.register(name, sup);
    }
}
