package net.tropicraft.core.common.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

public class ExplodingCoconutEntity extends ThrowableItemProjectile {
    public static final float DEFAULT_EXPLOSION_RADIUS = 2.4f;
    public static final float MAX_EXPLOSION_RADIUS = 5.0f;

    public static final boolean DEFAULT_DESTROYS_BLOCKS = true;

    private float explosionRadius = DEFAULT_EXPLOSION_RADIUS;
    private boolean destroysBlocks;

    public ExplodingCoconutEntity(EntityType<? extends ExplodingCoconutEntity> type, Level world) {
        super(type, world);
    }

    public ExplodingCoconutEntity(Level world, LivingEntity thrower, float explosionRadius, boolean destroysBlocks, ItemStack item) {
        super(TropicraftEntities.EXPLODING_COCONUT.get(), thrower, world, item);
        this.explosionRadius = explosionRadius;
        this.destroysBlocks = destroysBlocks;
    }

    @Override
    protected void onHit(HitResult result) {
        if (!level().isClientSide()) {
            level().explode(
                    this,
                    level().damageSources().explosion(this, getOwner()),
                    null,
                    getX(), getY(), getZ(),
                    Mth.clamp(explosionRadius, 0.0f, MAX_EXPLOSION_RADIUS),
                    false,
                    destroysBlocks ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE,
                    ParticleTypes.EXPLOSION,
                    ParticleTypes.EXPLOSION_EMITTER,
                    WeightedList.of(),
                    SoundEvents.GENERIC_EXPLODE
            );
            remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return TropicraftItems.EXPLODING_COCONUT.get();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("explosion_radius", explosionRadius);
        output.putBoolean("destroys_blocks", destroysBlocks);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        explosionRadius = input.getFloatOr("explosion_radius", DEFAULT_EXPLOSION_RADIUS);
        destroysBlocks = input.getBooleanOr("destroys_blocks", DEFAULT_DESTROYS_BLOCKS);
    }
}
