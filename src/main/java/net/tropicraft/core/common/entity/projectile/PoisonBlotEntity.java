package net.tropicraft.core.common.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class PoisonBlotEntity extends ThrowableProjectile {

    public PoisonBlotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
    }

    public PoisonBlotEntity(EntityType<? extends ThrowableProjectile> type, LivingEntity thrower, Level world) {
        super(type, thrower, world);
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            final Entity entity = ((EntityHitResult) result).getEntity();

            if (entity instanceof Player) {
                Player player = (Player) entity;
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 12 * 20, 0));
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
