package net.tropicraft.core.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class VolcanoSmokeParticle extends CampfireSmokeParticle {
    protected VolcanoSmokeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed,
                                   double zSpeed,boolean signal) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, true);
        hasPhysics = false;
    }
    @OnlyIn(Dist.CLIENT)
    public static class VolcanoProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public VolcanoProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            VolcanoSmokeParticle volcanoSmokeParticle = new VolcanoSmokeParticle(
                    level, x, y, z, xSpeed, ySpeed, zSpeed,true);
            volcanoSmokeParticle.setAlpha(0.9F);
            volcanoSmokeParticle.scale(12F);
            volcanoSmokeParticle.pickSprite(this.sprites);
            return volcanoSmokeParticle;
        }
    }
}
