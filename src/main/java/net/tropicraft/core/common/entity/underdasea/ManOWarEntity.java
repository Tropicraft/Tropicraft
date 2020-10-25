package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.fluid.IFluidState;
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
import net.minecraft.util.math.Vec3d;
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
        this.rand.setSeed((long)this.getEntityId());
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
        this.experienceValue = 7;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MoveRandomGoal(this));
        this.goalSelector.addGoal(1, new FleeGoal());
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
    }

    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return p_213348_2_.height * 0.5F;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    protected Entity getTarget() {
        return null;
    }

    public void livingTick() {
        super.livingTick();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if ((double)this.squidRotation > 6.283185307179586D) {
            if (this.world.isRemote) {
                this.squidRotation = 6.2831855F;
            } else {
                this.squidRotation = (float)((double)this.squidRotation - 6.283185307179586D);
                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
                }

                this.world.setEntityState(this, (byte)19);
            }
        }

        if (attackTimer > 0) {
            attackTimer--;
        }

        if (isInWaterOrBubbleColumn()) {
            if (rand.nextInt(5) == 0 && attackTimer <= 0) {
                List<LivingEntity> list = world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(2D, 4D, 2D).offset(0.0D, -2.0D, 0.0D), EntityPredicates.CAN_AI_TARGET);
                for (LivingEntity ent : list) {
                    if (ent.getType() != TropicraftEntities.MAN_O_WAR.get()) {
                        if (ent.isInWater()) {
                            // TODO change so death msg isn't "struck by lightning"
                            ent.attackEntityFrom(DamageSource.LIGHTNING_BOLT, (float) getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
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

            if (!this.world.isRemote) {
                this.setMotion((double)(this.randomMotionVecX * this.randomMotionSpeed), (double)(this.randomMotionVecY * this.randomMotionSpeed), (double)(this.randomMotionVecZ * this.randomMotionSpeed));
            }

            Vec3d lvt_1_2_ = this.getMotion();
            float lvt_2_1_ = MathHelper.sqrt(horizontalMag(lvt_1_2_));
            this.renderYawOffset += (-((float)MathHelper.atan2(lvt_1_2_.x, lvt_1_2_.z)) * 57.295776F - this.renderYawOffset) * 0.1F;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw = (float)((double)this.squidYaw + 3.141592653589793D * (double)this.rotateSpeed * 1.5D);
            this.squidPitch += (-((float)MathHelper.atan2((double)lvt_2_1_, lvt_1_2_.y)) * 57.295776F - this.squidPitch) * 0.1F;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * 3.1415927F * 0.25F;
            if (!this.world.isRemote) {
                double lvt_1_3_ = this.getMotion().y;
                if (this.isPotionActive(Effects.LEVITATION)) {
                    lvt_1_3_ = 0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1);
                } else if (!this.hasNoGravity()) {
                    lvt_1_3_ -= 0.08D;
                }

                this.setMotion(0.0D, lvt_1_3_ * 0.9800000190734863D, 0.0D);
            }

            this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0F - this.squidPitch) * 0.02D);
        }

    }

    private Vec3d func_207400_b(Vec3d p_207400_1_) {
        Vec3d lvt_2_1_ = p_207400_1_.rotatePitch(this.prevSquidPitch * 0.017453292F);
        lvt_2_1_ = lvt_2_1_.rotateYaw(-this.prevRenderYawOffset * 0.017453292F);
        return lvt_2_1_;
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }

    @Override
    public void travel(Vec3d p_213352_1_) {
        this.move(MoverType.SELF, this.getMotion());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 19) {
            this.squidRotation = 0.0F;
        } else {
            super.handleStatusUpdate(p_70103_1_);
        }
    }

    public void setMovementVector(float p_175568_1_, float p_175568_2_, float p_175568_3_) {
        this.randomMotionVecX = p_175568_1_;
        this.randomMotionVecY = p_175568_2_;
        this.randomMotionVecZ = p_175568_3_;
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

        public boolean shouldExecute() {
            LivingEntity lvt_1_1_ = ManOWarEntity.this.getRevengeTarget();
            if (ManOWarEntity.this.isInWater() && lvt_1_1_ != null) {
                return ManOWarEntity.this.getDistanceSq(lvt_1_1_) < 100.0D;
            } else {
                return false;
            }
        }

        public void startExecuting() {
            this.tickCounter = 0;
        }

        public void tick() {
            ++this.tickCounter;
            LivingEntity lvt_1_1_ = ManOWarEntity.this.getRevengeTarget();
            if (lvt_1_1_ != null) {
                Vec3d lvt_2_1_ = new Vec3d(ManOWarEntity.this.getPosX() - lvt_1_1_.getPosX(), ManOWarEntity.this.getPosY() - lvt_1_1_.getPosY(), ManOWarEntity.this.getPosZ() - lvt_1_1_.getPosZ());
                BlockState lvt_3_1_ = ManOWarEntity.this.world.getBlockState(new BlockPos(ManOWarEntity.this.getPosX() + lvt_2_1_.x, ManOWarEntity.this.getPosY() + lvt_2_1_.y, ManOWarEntity.this.getPosZ() + lvt_2_1_.z));
                IFluidState lvt_4_1_ = ManOWarEntity.this.world.getFluidState(new BlockPos(ManOWarEntity.this.getPosX() + lvt_2_1_.x, ManOWarEntity.this.getPosY() + lvt_2_1_.y, ManOWarEntity.this.getPosZ() + lvt_2_1_.z));
                if (lvt_4_1_.isTagged(FluidTags.WATER) || lvt_3_1_.isAir()) {
                    double lvt_5_1_ = lvt_2_1_.length();
                    if (lvt_5_1_ > 0.0D) {
                        lvt_2_1_.normalize();
                        float lvt_7_1_ = 3.0F;
                        if (lvt_5_1_ > 5.0D) {
                            lvt_7_1_ = (float)((double)lvt_7_1_ - (lvt_5_1_ - 5.0D) / 5.0D);
                        }

                        if (lvt_7_1_ > 0.0F) {
                            lvt_2_1_ = lvt_2_1_.scale((double)lvt_7_1_);
                        }
                    }

                    if (lvt_3_1_.isAir()) {
                        lvt_2_1_ = lvt_2_1_.subtract(0.0D, lvt_2_1_.y, 0.0D);
                    }

                    ManOWarEntity.this.setMovementVector((float)lvt_2_1_.x / 20.0F, (float)lvt_2_1_.y / 20.0F, (float)lvt_2_1_.z / 20.0F);
                }

                if (this.tickCounter % 10 == 5) {
                    ManOWarEntity.this.world.addParticle(ParticleTypes.BUBBLE, ManOWarEntity.this.getPosX(), ManOWarEntity.this.getPosY(), ManOWarEntity.this.getPosZ(), 0.0D, 0.0D, 0.0D);
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        private final ManOWarEntity manOWarEntity;

        public MoveRandomGoal(ManOWarEntity p_i48823_2_) {
            this.manOWarEntity = p_i48823_2_;
        }

        public boolean shouldExecute() {
            return true;
        }

        public void tick() {
            int lvt_1_1_ = this.manOWarEntity.getIdleTime();
            if (lvt_1_1_ > 100) {
                this.manOWarEntity.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.manOWarEntity.getRNG().nextInt(50) == 0 || !this.manOWarEntity.isInWater() || !this.manOWarEntity.hasMovementVector()) {
                float lvt_2_1_ = this.manOWarEntity.getRNG().nextFloat() * 6.2831855F;
                float lvt_3_1_ = MathHelper.cos(lvt_2_1_) * 0.2F;
                float lvt_4_1_ = -0.1F + this.manOWarEntity.getRNG().nextFloat() * 0.2F;
                float lvt_5_1_ = MathHelper.sin(lvt_2_1_) * 0.2F;
                this.manOWarEntity.setMovementVector(lvt_3_1_, lvt_4_1_, lvt_5_1_);
            }

        }
    }
}