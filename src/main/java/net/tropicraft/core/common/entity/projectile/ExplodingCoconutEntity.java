package net.tropicraft.core.common.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

public class ExplodingCoconutEntity extends ThrowableItemProjectile {
    public static final float DEFAULT_EXPLOSION_RADIUS = 2.4f;

    private float explosionRadius = DEFAULT_EXPLOSION_RADIUS;

    public ExplodingCoconutEntity(EntityType<? extends ExplodingCoconutEntity> type, Level world) {
        super(type, world);
    }

    public ExplodingCoconutEntity(Level world, LivingEntity thrower, float explosionRadius) {
        super(TropicraftEntities.EXPLODING_COCONUT.get(), thrower, world);
        this.explosionRadius = explosionRadius;
    }

    @Override
    protected void onHit(HitResult result) {
        if (!level().isClientSide) {
            level().explode(this, getX(), getY(), getZ(), Mth.clamp(explosionRadius, 0.0f, 5.0f), Level.ExplosionInteraction.BLOCK);
            remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return TropicraftItems.EXPLODING_COCONUT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("explosion_radius", explosionRadius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("explosion_radius", Tag.TAG_FLOAT)) {
            explosionRadius = tag.getFloat("explosion_radius");
        }
    }
}
