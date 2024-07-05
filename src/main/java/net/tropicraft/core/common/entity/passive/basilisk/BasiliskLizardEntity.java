package net.tropicraft.core.common.entity.passive.basilisk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.Easings;

public final class BasiliskLizardEntity extends Animal {
    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(BasiliskLizardEntity.class, EntityDataSerializers.BOOLEAN);

    private static final float WATER_WALK_SPEED_BOOST = 1.6f;
    private static final int WATER_WALK_TIME = 10;

    private static final int RUNNING_ANIMATION_LENGTH = 10;

    private int movingTimer;
    private boolean onWaterSurface;

    private int runningAnimation;
    private int prevRunningAnimation;

    public BasiliskLizardEntity(EntityType<? extends BasiliskLizardEntity> type, Level world) {
        super(type, world);
        setPathfindingMalus(PathType.WATER, 0.0f);
        setPathfindingMalus(PathType.WATER_BORDER, 0.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25f);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new WaterWalking.Navigator(this, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0));
        goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RUNNING, false);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public BasiliskLizardEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            tickMovementTimer();
            tickSwimming();

            entityData.set(RUNNING, onWaterSurface);
        } else {
            tickRunningAnimation();
        }
    }

    private void tickRunningAnimation() {
        prevRunningAnimation = runningAnimation;

        if (entityData.get(RUNNING)) {
            if (runningAnimation < RUNNING_ANIMATION_LENGTH) {
                runningAnimation++;
            }

            spawnRunningParticles();
        } else {
            if (runningAnimation > 0) {
                runningAnimation = Math.max(runningAnimation - 2, 0);
            }
        }
    }

    private void spawnRunningParticles() {
        for (int i = 0; i < 2; i++) {
            Vec3 motion = getDeltaMovement();
            double surfaceY = Mth.floor(getY()) + 1.0;

            double dx = (random.nextDouble() * 2.0 - 1.0) * 0.25;
            double dz = (random.nextDouble() * 2.0 - 1.0) * 0.25;

            level().addParticle(
                    random.nextBoolean() ? ParticleTypes.BUBBLE : ParticleTypes.SPLASH,
                    getX() + dx, surfaceY, getZ() + dz,
                    motion.x, motion.y - random.nextDouble() * 0.2f, motion.z
            );
        }
    }

    @Override
    protected void doWaterSplashEffect() {
        // duplicating vanilla logic to add splash sounds but disable the particles
        float volume = (float) (getDeltaMovement().multiply(0.5, 1.0, 0.5).length() * 0.2f);
        volume = Math.min(volume, 1.0f);

        float pitch = 1.0f + (random.nextFloat() - random.nextFloat()) * 0.4f;

        SoundEvent sound = volume < 0.25 ? getSwimSplashSound() : getSwimHighSpeedSplashSound();
        playSound(sound, volume, pitch);
    }

    private void tickMovementTimer() {
        if (zza != 0.0f) {
            movingTimer = WATER_WALK_TIME;
        } else {
            if (movingTimer > 0) {
                movingTimer--;
            }
        }
    }

    private void tickSwimming() {
        if (!onWaterSurface && isInWater() && getFluidHeight(FluidTags.WATER) > getFluidJumpThreshold()) {
            boolean shouldWaterWalk = shouldWaterWalk();
            if (shouldWaterWalk || random.nextFloat() < 0.8f) {
                setDeltaMovement(getDeltaMovement().scale(0.5).add(0.0, 0.1, 0.0));
            }
        }
    }

    @Override
    protected Vec3 maybeBackOffFromEdge(Vec3 offset, MoverType mover) {
        if (shouldWaterWalk()) {
            Vec3 result = WaterWalking.collide(level(), getBoundingBox(), offset);
            onWaterSurface = offset.y < 0.0 && result.y != offset.y;
            return result;
        } else {
            onWaterSurface = false;
            return offset;
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        setOnGround(onGround() || onWaterSurface);
        super.checkFallDamage(y, onGround(), state, pos);
    }

    @Override
    public float getSpeed() {
        float speed = super.getSpeed();
        return onWaterSurface ? speed * WATER_WALK_SPEED_BOOST : speed;
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return WaterWalking.canWalkOn(fluidState.getType());
    }

    private boolean shouldWaterWalk() {
        return movingTimer > 0;
    }

    public float getRunningAnimation(float partialTicks) {
        float animation = Mth.lerp(partialTicks, prevRunningAnimation, runningAnimation);
        return Easings.inOutSine(animation / RUNNING_ANIMATION_LENGTH);
    }
}
