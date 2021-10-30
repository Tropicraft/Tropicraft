package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.List;

public class ManOWarEntity extends WaterMobEntity {
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

    public ManOWarEntity(final EntityType<? extends ManOWarEntity> type, World world){
        super(type, world);
        this.random.setSeed(this.getId());
        this.rotationVelocity = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
        this.xpReward = 7;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MoveRandomGoal(this));
        this.goalSelector.addGoal(1, new FleeGoal());
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return WaterMobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return size.height * 0.5F;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public LivingEntity getTarget() {
        return null;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if ((double)this.squidRotation > 6.283185307179586D) {
            if (this.level.isClientSide) {
                this.squidRotation = 6.2831855F;
            } else {
                this.squidRotation = (float)((double)this.squidRotation - 6.283185307179586D);
                if (this.random.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }

                this.level.broadcastEntityEvent(this, (byte)19);
            }
        }

        if (attackTimer > 0) {
            attackTimer--;
        }

        if (isInWaterOrBubble()) {
            if (random.nextInt(5) == 0 && attackTimer <= 0) {
                List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2D, 4D, 2D).move(0.0D, -2.0D, 0.0D), EntityPredicates.NO_CREATIVE_OR_SPECTATOR);
                for (LivingEntity ent : list) {
                    if (ent.getType() != TropicraftEntities.MAN_O_WAR.get()) {
                        if (ent.isInWater()) {
                            // TODO change so death msg isn't "struck by lightning"
                            ent.hurt(DamageSource.LIGHTNING_BOLT, (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                            attackTimer = 20;
                        }
                    }
                }
            }

            if (this.squidRotation < 3.1415927F) {
                float lvt_1_1_ = this.squidRotation / 3.1415927F;
                this.tentacleAngle = MathHelper.sin(lvt_1_1_ * lvt_1_1_ * 3.1415927F) * 3.1415927F * 0.25F;
                if ((double)lvt_1_1_ > 0.75D) {
                    this.randomMotionSpeed = 1.0F;
                    this.rotateSpeed = 1.0F;
                } else {
                    this.rotateSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.randomMotionSpeed *= 0.9F;
                this.rotateSpeed *= 0.99F;
            }

            if (!this.level.isClientSide) {
                this.setDeltaMovement(this.randomMotionVecX * this.randomMotionSpeed, this.randomMotionVecY * this.randomMotionSpeed, this.randomMotionVecZ * this.randomMotionSpeed);
            }

            Vector3d motion = this.getDeltaMovement();
            float lvt_2_1_ = MathHelper.sqrt(getHorizontalDistanceSqr(motion));
            this.yBodyRot += (-((float)MathHelper.atan2(motion.x, motion.z)) * 57.295776F - this.yBodyRot) * 0.1F;
            this.yRot = this.yBodyRot;
            this.squidYaw = (float)((double)this.squidYaw + 3.141592653589793D * (double)this.rotateSpeed * 1.5D);
            this.squidPitch += (-((float)MathHelper.atan2(lvt_2_1_, motion.y)) * 57.295776F - this.squidPitch) * 0.1F;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * 3.1415927F * 0.25F;
            if (!this.level.isClientSide) {
                double lvt_1_3_ = this.getDeltaMovement().y;
                if (this.hasEffect(Effects.LEVITATION)) {
                    lvt_1_3_ = 0.05D * (double)(this.getEffect(Effects.LEVITATION).getAmplifier() + 1);
                } else if (!this.isNoGravity()) {
                    lvt_1_3_ -= 0.08D;
                }

                this.setDeltaMovement(0.0D, lvt_1_3_ * 0.9800000190734863D, 0.0D);
            }

            this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0F - this.squidPitch) * 0.02D);
        }

    }

    @Override
    public void die(DamageSource d) {
        super.die(d);
        if (!this.level.isClientSide) {
            int numDrops = 3 + this.random.nextInt(1);

            for (int i = 0; i < numDrops; i++) {
                spawnAtLocation(Items.SLIME_BALL, 1);
            }
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
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
    public void travel(Vector3d vector) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 19) {
            this.squidRotation = 0.0F;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector() {
        return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.MAN_O_WAR_SPAWN_EGG.get());
    }

    class FleeGoal extends Goal {
        private int tickCounter;

        private FleeGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity lvt_1_1_ = ManOWarEntity.this.getLastHurtByMob();
            if (ManOWarEntity.this.isInWater() && lvt_1_1_ != null) {
                return ManOWarEntity.this.distanceToSqr(lvt_1_1_) < 100.0D;
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.tickCounter = 0;
        }

        @Override
        public void tick() {
            ++this.tickCounter;
            LivingEntity target = ManOWarEntity.this.getLastHurtByMob();
            if (target != null) {
                Vector3d lvt_2_1_ = new Vector3d(ManOWarEntity.this.getX() - target.getX(), ManOWarEntity.this.getY() - target.getY(), ManOWarEntity.this.getZ() - target.getZ());
                BlockState block = ManOWarEntity.this.level.getBlockState(new BlockPos(ManOWarEntity.this.getX() + lvt_2_1_.x, ManOWarEntity.this.getY() + lvt_2_1_.y, ManOWarEntity.this.getZ() + lvt_2_1_.z));
                FluidState fluid = ManOWarEntity.this.level.getFluidState(new BlockPos(ManOWarEntity.this.getX() + lvt_2_1_.x, ManOWarEntity.this.getY() + lvt_2_1_.y, ManOWarEntity.this.getZ() + lvt_2_1_.z));
                if (fluid.is(FluidTags.WATER) || block.isAir()) {
                    double lvt_5_1_ = lvt_2_1_.length();
                    if (lvt_5_1_ > 0.0D) {
                        lvt_2_1_.normalize();
                        float lvt_7_1_ = 3.0F;
                        if (lvt_5_1_ > 5.0D) {
                            lvt_7_1_ = (float)((double)lvt_7_1_ - (lvt_5_1_ - 5.0D) / 5.0D);
                        }

                        if (lvt_7_1_ > 0.0F) {
                            lvt_2_1_ = lvt_2_1_.scale(lvt_7_1_);
                        }
                    }

                    if (block.isAir()) {
                        lvt_2_1_ = lvt_2_1_.subtract(0.0D, lvt_2_1_.y, 0.0D);
                    }

                    ManOWarEntity.this.setMovementVector((float)lvt_2_1_.x / 20.0F, (float)lvt_2_1_.y / 20.0F, (float)lvt_2_1_.z / 20.0F);
                }

                if (this.tickCounter % 10 == 5) {
                    ManOWarEntity.this.level.addParticle(ParticleTypes.BUBBLE, ManOWarEntity.this.getX(), ManOWarEntity.this.getY(), ManOWarEntity.this.getZ(), 0.0D, 0.0D, 0.0D);
                }

            }
        }
    }

    static class MoveRandomGoal extends Goal {
        private final ManOWarEntity manOWarEntity;

        public MoveRandomGoal(ManOWarEntity p_i48823_2_) {
            this.manOWarEntity = p_i48823_2_;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int lvt_1_1_ = this.manOWarEntity.getNoActionTime();
            if (lvt_1_1_ > 100) {
                this.manOWarEntity.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.manOWarEntity.getRandom().nextInt(50) == 0 || !this.manOWarEntity.isInWater() || !this.manOWarEntity.hasMovementVector()) {
                float lvt_2_1_ = this.manOWarEntity.getRandom().nextFloat() * 6.2831855F;
                float lvt_3_1_ = MathHelper.cos(lvt_2_1_) * 0.2F;
                float lvt_4_1_ = -0.1F + this.manOWarEntity.getRandom().nextFloat() * 0.2F;
                float lvt_5_1_ = MathHelper.sin(lvt_2_1_) * 0.2F;
                this.manOWarEntity.setMovementVector(lvt_3_1_, lvt_4_1_, lvt_5_1_);
            }

        }
    }
}
