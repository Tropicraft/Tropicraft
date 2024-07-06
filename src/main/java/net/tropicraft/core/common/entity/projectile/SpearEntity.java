package net.tropicraft.core.common.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public class SpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    public SpearEntity(EntityType<? extends SpearEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SpearEntity(Level pLevel, LivingEntity pShooter, ItemStack pPickupItemStack) {
        super(TropicraftEntities.SPEAR.get(), pShooter, pLevel, pPickupItemStack, null);
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(pPickupItemStack));
        entityData.set(ID_FOIL, pPickupItemStack.hasFoil());
    }

    private byte getLoyaltyFromItem(ItemStack pStack) {
        return level() instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, pStack, this), 0, 127)
                : 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte) 0);
        builder.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        if (inGroundTime > 4) {
            dealtDamage = true;
        }

        Entity entity = getOwner();
        int i = entityData.get(ID_LOYALTY);
        if (i > 0 && (dealtDamage || isNoPhysics()) && entity != null) {
            if (!isAcceptibleReturnOwner()) {
                if (!level().isClientSide && pickup == AbstractArrow.Pickup.ALLOWED) {
                    spawnAtLocation(getPickupItem(), 0.1f);
                }

                discard();
            } else {
                setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(position());
                setPosRaw(getX(), getY() + vec3.y * 0.015 * (double) i, getZ());
                if (level().isClientSide) {
                    yOld = getY();
                }

                double d0 = 0.05 * (double) i;
                setDeltaMovement(getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (clientSideReturnSpearTickCount == 0) {
                    playSound(SoundEvents.TRIDENT_RETURN, 10.0f, 1.0f);
                }

                ++clientSideReturnSpearTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public boolean isFoil() {
        return entityData.get(ID_FOIL);
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = 8.0f;
        Entity entity1 = getOwner();
        DamageSource damagesource = damageSources().trident(this, (Entity) (entity1 == null ? this : entity1));
        if (level() instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, getWeaponItem(), entity, damagesource, f);
        }

        dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (level() instanceof ServerLevel serverlevel1) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, entity, damagesource, getWeaponItem());
            }

            if (entity instanceof LivingEntity livingentity) {
                doKnockback(livingentity, damagesource);
                doPostHurtEffects(livingentity);
            }
        }

        setDeltaMovement(getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        playSound(SoundEvents.TRIDENT_HIT, 1.0f, 1.0f);
    }

    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel pLevel, BlockHitResult pHitResult, ItemStack pStack) {
        Vec3 vec3 = pHitResult.getBlockPos().clampLocationWithin(pHitResult.getLocation());
        EnchantmentHelper.onHitBlock(
                pLevel,
                pStack,
                getOwner() instanceof LivingEntity livingentity ? livingentity : null,
                this,
                null,
                vec3,
                pLevel.getBlockState(pHitResult.getBlockPos()),
                p_348680_ -> kill()
        );
    }

    @Override
    public ItemStack getWeaponItem() {
        return getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || isNoPhysics() && ownedBy(player) && player.getInventory().add(getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(TropicraftItems.BAMBOO_SPEAR.get());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (ownedBy(pEntity) || getOwner() == null) {
            super.playerTouch(pEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        dealtDamage = pCompound.getBoolean("DealtDamage");
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(getPickupItemStackOrigin()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("DealtDamage", dealtDamage);
    }

    @Override
    public void tickDespawn() {
        int i = entityData.get(ID_LOYALTY);
        if (pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99f;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
}
