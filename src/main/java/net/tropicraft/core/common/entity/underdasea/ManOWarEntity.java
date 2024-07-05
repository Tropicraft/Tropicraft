package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.TropicraftEntities;

import java.util.List;

public class ManOWarEntity extends WaterAnimal {
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    private float rotationVelocity;
    private float rotateSpeed;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;
    private int attackTimer = 0;

    public ManOWarEntity(EntityType<? extends ManOWarEntity> type, Level world) {
        super(type, world);
        random.setSeed(getId());
        rotationVelocity = 1.0f / (random.nextFloat() + 1.0f) * 0.2f;
        xpReward = 7;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new MoveRandomGoal(this));
        goalSelector.addGoal(1, new FleeGoal());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Override
    public LivingEntity getTarget() {
        return null;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        prevSquidPitch = squidPitch;
        prevSquidYaw = squidYaw;
        prevSquidRotation = squidRotation;
        lastTentacleAngle = tentacleAngle;
        squidRotation += rotationVelocity;
        if ((double) squidRotation > Mth.TWO_PI) {
            if (level().isClientSide) {
                squidRotation = Mth.TWO_PI;
            } else {
                squidRotation = (float) ((double) squidRotation - Mth.TWO_PI);
                if (random.nextInt(10) == 0) {
                    rotationVelocity = 1.0f / (random.nextFloat() + 1.0f) * 0.2f;
                }

                level().broadcastEntityEvent(this, (byte) 19);
            }
        }

        if (attackTimer > 0) {
            attackTimer--;
        }

        if (isInWaterOrBubble()) {
            if (random.nextInt(5) == 0 && attackTimer <= 0) {
                List<LivingEntity> list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2D, 4D, 2D).move(0.0, -2.0, 0.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
                for (LivingEntity ent : list) {
                    if (ent.getType() != TropicraftEntities.MAN_O_WAR.get()) {
                        if (ent.isInWater()) {
                            ent.hurt(damageSources().mobAttack(this), (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                            attackTimer = 20;
                        }
                    }
                }
            }

            if (squidRotation < Mth.PI) {
                float lvt_1_1_ = squidRotation / Mth.PI;
                tentacleAngle = Mth.sin(lvt_1_1_ * lvt_1_1_ * Mth.PI) * Mth.PI * 0.25f;
                if ((double) lvt_1_1_ > 0.75) {
                    randomMotionSpeed = 1.0f;
                    rotateSpeed = 1.0f;
                } else {
                    rotateSpeed *= 0.8f;
                }
            } else {
                tentacleAngle = 0.0f;
                randomMotionSpeed *= 0.9f;
                rotateSpeed *= 0.99f;
            }

            if (!level().isClientSide) {
                setDeltaMovement(randomMotionVecX * randomMotionSpeed, randomMotionVecY * randomMotionSpeed, randomMotionVecZ * randomMotionSpeed);
            }

            Vec3 motion = getDeltaMovement();
            double horizontalDistance = motion.horizontalDistance();
            yBodyRot += (-((float) Mth.atan2(motion.x, motion.z)) * Mth.RAD_TO_DEG - yBodyRot) * 0.1f;
            setYRot(yBodyRot);
            squidYaw = (float) ((double) squidYaw + Math.PI * (double) rotateSpeed * 1.5);
            squidPitch += (-((float) Mth.atan2(horizontalDistance, motion.y)) * Mth.RAD_TO_DEG - squidPitch) * 0.1f;
        } else {
            tentacleAngle = Mth.abs(Mth.sin(squidRotation)) * Mth.PI * 0.25f;
            if (!level().isClientSide) {
                double lvt_1_3_ = getDeltaMovement().y;
                if (hasEffect(MobEffects.LEVITATION)) {
                    lvt_1_3_ = 0.05 * (double) (getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
                } else if (!isNoGravity()) {
                    lvt_1_3_ -= 0.08;
                }

                setDeltaMovement(0.0, lvt_1_3_ * 0.9800000190734863, 0.0);
            }

            squidPitch = (float) ((double) squidPitch + (double) (-90.0f - squidPitch) * 0.02);
        }
    }

    @Override
    public void die(DamageSource d) {
        super.die(d);
        if (!level().isClientSide) {
            int numDrops = 3 + random.nextInt(1);

            for (int i = 0; i < numDrops; i++) {
                spawnAtLocation(Items.SLIME_BALL, 1);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    @Override
    public void travel(Vec3 vector) {
        move(MoverType.SELF, getDeltaMovement());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 19) {
            squidRotation = 0.0f;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
        randomMotionVecX = randomMotionVecXIn;
        randomMotionVecY = randomMotionVecYIn;
        randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector() {
        return randomMotionVecX != 0.0f || randomMotionVecY != 0.0f || randomMotionVecZ != 0.0f;
    }

    class FleeGoal extends Goal {
        private int tickCounter;

        private FleeGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity lvt_1_1_ = getLastHurtByMob();
            if (isInWater() && lvt_1_1_ != null) {
                return distanceToSqr(lvt_1_1_) < 100.0;
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            tickCounter = 0;
        }

        @Override
        public void tick() {
            ++tickCounter;
            LivingEntity target = getLastHurtByMob();
            if (target != null) {
                Vec3 lvt_2_1_ = new Vec3(getX() - target.getX(), getY() - target.getY(), getZ() - target.getZ());
                BlockState block = level().getBlockState(BlockPos.containing(getX() + lvt_2_1_.x, getY() + lvt_2_1_.y, getZ() + lvt_2_1_.z));
                FluidState fluid = level().getFluidState(BlockPos.containing(getX() + lvt_2_1_.x, getY() + lvt_2_1_.y, getZ() + lvt_2_1_.z));
                if (fluid.is(FluidTags.WATER) || block.isAir()) {
                    double lvt_5_1_ = lvt_2_1_.length();
                    if (lvt_5_1_ > 0.0) {
                        lvt_2_1_.normalize();
                        float lvt_7_1_ = 3.0f;
                        if (lvt_5_1_ > 5.0) {
                            lvt_7_1_ = (float) ((double) lvt_7_1_ - (lvt_5_1_ - 5.0) / 5.0);
                        }

                        if (lvt_7_1_ > 0.0f) {
                            lvt_2_1_ = lvt_2_1_.scale(lvt_7_1_);
                        }
                    }

                    if (block.isAir()) {
                        lvt_2_1_ = lvt_2_1_.subtract(0.0, lvt_2_1_.y, 0.0);
                    }

                    setMovementVector((float) lvt_2_1_.x / 20.0f, (float) lvt_2_1_.y / 20.0f, (float) lvt_2_1_.z / 20.0f);
                }

                if (tickCounter % 10 == 5) {
                    level().addParticle(ParticleTypes.BUBBLE, getX(), getY(), getZ(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    static class MoveRandomGoal extends Goal {
        private final ManOWarEntity manOWarEntity;

        public MoveRandomGoal(ManOWarEntity p_i48823_2_) {
            manOWarEntity = p_i48823_2_;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int lvt_1_1_ = manOWarEntity.getNoActionTime();
            if (lvt_1_1_ > 100) {
                manOWarEntity.setMovementVector(0.0f, 0.0f, 0.0f);
            } else if (manOWarEntity.getRandom().nextInt(50) == 0 || !manOWarEntity.isInWater() || !manOWarEntity.hasMovementVector()) {
                float lvt_2_1_ = manOWarEntity.getRandom().nextFloat() * Mth.TWO_PI;
                float lvt_3_1_ = Mth.cos(lvt_2_1_) * 0.2f;
                float lvt_4_1_ = -0.1f + manOWarEntity.getRandom().nextFloat() * 0.2f;
                float lvt_5_1_ = Mth.sin(lvt_2_1_) * 0.2f;
                manOWarEntity.setMovementVector(lvt_3_1_, lvt_4_1_, lvt_5_1_);
            }
        }
    }
}
