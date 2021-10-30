package net.tropicraft.core.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class PoisonBlotEntity extends ThrowableEntity {

    public PoisonBlotEntity(EntityType<? extends ThrowableEntity> type, World world) {
        super(type, world);
    }

    public PoisonBlotEntity(EntityType<? extends ThrowableEntity> type, LivingEntity thrower, World world) {
        super(type, thrower, world);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            final Entity entity = ((EntityRayTraceResult) result).getEntity();

            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.addEffect(new EffectInstance(Effects.POISON, 12 * 20, 0));
                remove();
            }
        }
    }

    @Override
    protected void defineSynchedData() {

    }
}
