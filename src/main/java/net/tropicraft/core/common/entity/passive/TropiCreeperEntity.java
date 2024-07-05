package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.entity.ai.TropiCreeperSwellGoal;

import java.util.Collection;

public class TropiCreeperEntity extends PathfinderMob {
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(TropiCreeperEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(TropiCreeperEntity.class, EntityDataSerializers.BOOLEAN);

    private int prevTimeSinceIgnited, timeSinceIgnited;
    private int fuseTime = 30;
    private int explosionRadius = 3;

    public TropiCreeperEntity(EntityType<? extends PathfinderMob> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new TropiCreeperSwellGoal(this));
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0f, 1.0, 1.2));
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0f, 1.0, 1.2));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATE, -1);
        builder.define(IGNITED, false);
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    @Override
    public int getMaxFallDistance() {
        return getTarget() == null ? 3 : 3 + (int) (getHealth() - 1.0f);
    }

    @Override
    public boolean causeFallDamage(float distance, float pMultiplier, DamageSource pSource) {
        boolean fall = super.causeFallDamage(distance, pMultiplier, pSource);
        timeSinceIgnited = (int) ((float) timeSinceIgnited + distance * 1.5f);
        if (timeSinceIgnited > fuseTime - 5) {
            timeSinceIgnited = fuseTime - 5;
        }

        return fall;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putShort("Fuse", (short) fuseTime);
        compound.putByte("ExplosionRadius", (byte) explosionRadius);
        compound.putBoolean("ignited", hasIgnited());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Fuse", 99)) {
            fuseTime = compound.getShort("Fuse");
        }

        if (compound.contains("ExplosionRadius", 99)) {
            explosionRadius = compound.getByte("ExplosionRadius");
        }

        if (compound.getBoolean("ignited")) {
            ignite();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        if (isAlive()) {
            prevTimeSinceIgnited = timeSinceIgnited;
            if (hasIgnited()) {
                setCreeperState(1);
            }

            int i = getCreeperState();
            if (i > 0 && timeSinceIgnited == 0) {
                playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 0.5f);
            }

            timeSinceIgnited += i;
            if (timeSinceIgnited < 0) {
                timeSinceIgnited = 0;
            }

            if (timeSinceIgnited >= fuseTime) {
                timeSinceIgnited = fuseTime;
                explode();
            }
        }

        super.tick();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getCreeperState() {
        return entityData.get(STATE);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setCreeperState(int state) {
        entityData.set(STATE, state);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            level().playSound(player, getX(), getY(), getZ(), SoundEvents.FLINTANDSTEEL_USE, getSoundSource(), 1.0f, random.nextFloat() * 0.4f + 0.8f);
            player.swing(hand);
            if (!level().isClientSide) {
                ignite();
                itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    /**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
    private void explode() {
        if (!level().isClientSide) {
            dead = true;
            //this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, Explosion.Mode.NONE);
            //TODO: readd coconut bomb drop for creeper
            // this.dropItem(TCItemRegistry.coconutBomb.itemID, rand.nextInt(3) + 1);
            int radius = 5;
            int radiusSq = radius * radius;
            BlockPos center = blockPosition();
            HolderSet<Block> flowers = level().registryAccess().registryOrThrow(Registries.BLOCK).getOrCreateTag(TropicraftTags.Blocks.TROPICS_FLOWERS);
            for (int i = 0; i < 3 * radiusSq; i++) {
                BlockPos attempt = center.offset(random.nextInt((radius * 2) + 1) - radius, 0, random.nextInt((radius * 2) + 1) - radius);
                if (attempt.distSqr(center) < radiusSq) {
                    attempt = attempt.above(radius);
                    while (level().getBlockState(attempt).canBeReplaced() && attempt.getY() > center.getY() - radius) {
                        attempt = attempt.below();
                    }
                    attempt = attempt.above();
                    BlockState state = flowers.getRandomElement(random).map(Holder::value).orElse(Blocks.AIR).defaultBlockState();
                    if (state.canSurvive(level(), attempt)) {
                        level().setBlockAndUpdate(attempt, state);
                    }
                }
            }
            remove(RemovalReason.KILLED);
            spawnLingeringCloud();
        } else {
            level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY() + 1.0f, getZ(), 1.0, 0.0, 0.0);
        }
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(level(), getX(), getY(), getZ());
            areaeffectcloudentity.setRadius(2.5f);
            areaeffectcloudentity.setRadiusOnUse(-0.5f);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());

            for (MobEffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new MobEffectInstance(effectinstance));
            }

            level().addFreshEntity(areaeffectcloudentity);
        }
    }

    public boolean hasIgnited() {
        return entityData.get(IGNITED);
    }

    public void ignite() {
        entityData.set(IGNITED, true);
    }

    public float getCreeperFlashIntensity(float partialTicks) {
        return Mth.lerp(partialTicks, (float) prevTimeSinceIgnited, (float) timeSinceIgnited) / (float) (fuseTime - 2);
    }
}
