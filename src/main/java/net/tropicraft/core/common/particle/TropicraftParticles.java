package net.tropicraft.core.common.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.tropicraft.Tropicraft.ID;

@EventBusSubscriber(modid = ID, bus = EventBusSubscriber.Bus.MOD)
public final class TropicraftParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ID);

    public static final Supplier<SimpleParticleType> VOLCANO_SMOKE_PARTICLE = PARTICLE_TYPES.register("volcano_smoke"
            , () -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(VOLCANO_SMOKE_PARTICLE.get(), VolcanoSmokeParticle.VolcanoProvider::new);
    }
}
