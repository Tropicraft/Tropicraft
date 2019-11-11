package net.tropicraft.core.common.entity;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class SeaTurtleEntity extends TurtleEntity {

    private static final DataParameter<Boolean> IS_MATURE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TURTLE_TYPE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.VARINT);

    private static final int NUM_TYPES = 6;

    public SeaTurtleEntity(EntityType<? extends TurtleEntity> type, World world) {
        super(type, world);
    }

    protected void registerAttributes() {
        super.registerAttributes();
    }

    public boolean isPushedByWater() {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    protected float determineNextStepDistance() {
        return this.distanceWalkedOnStepModified + 0.15F;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        setRandomTurtleType();
        return super.onInitialSpawn(world, difficultyInstance, spawnReason, data, nbt);
    }

    @Override
    public void registerData() {
        super.registerData();
        getDataManager().register(IS_MATURE, true);
        getDataManager().register(TURTLE_TYPE, 1);
    }

    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("TurtleType", getTurtleType());
        nbt.putBoolean("IsMature", isMature());
    }

    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        if (nbt.contains("TurtleType")) {
            setTurtleType(nbt.getInt("TurtleType"));
        } else {
            setRandomTurtleType();
        }
        setIsMature(nbt.getBoolean("IsMature"));
    }

    public boolean isMature() {
        return getDataManager().get(IS_MATURE);
    }

    public SeaTurtleEntity setIsMature(final boolean mature) {
        getDataManager().set(IS_MATURE, mature);
        return this;
    }

    public int getTurtleType() {
        return getDataManager().get(TURTLE_TYPE);
    }
    
    public void setRandomTurtleType() {
        setTurtleType(rand.nextInt(NUM_TYPES) + 1);
    }

    public SeaTurtleEntity setTurtleType(final int type) {
        getDataManager().set(TURTLE_TYPE, MathHelper.clamp(type, 1, NUM_TYPES));
        return this;
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        final List<Entity> passengers = getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    @Override
    public boolean canBeSteered() {
        return getControllingPassenger() instanceof LivingEntity;
    }
    
    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() - 0.1;
    }

    @Override
    @Nullable
    public AgeableEntity createChild(AgeableEntity ent) {
        return TropicraftEntities.SEA_TURTLE.get().create(this.world)
                .setTurtleType(rand.nextBoolean() && ent instanceof SeaTurtleEntity ? ((SeaTurtleEntity)ent).getTurtleType() : getTurtleType())
                .setIsMature(false);
    }

    @Override
    public boolean processInteract(final PlayerEntity player, final Hand hand) {
        if (!world.isRemote && !player.isSneaking() && canFitPassenger(player)/* && this.isMature() */&& isInWater()) {
            player.startRiding(this);
        }
        return super.processInteract(player, hand);
    }

    public float lerp(float x1, float x2, float t) {
        return x1 + (t*0.03f) * MathHelper.wrapDegrees(x2 - x1);
    }

    private float swimSpeedCurrent;

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            if(passenger instanceof PlayerEntity) {
                PlayerEntity p = (PlayerEntity)passenger;
                if(this.isInWater()) {
                    if(p.moveForward > 0f) {
                        this.rotationPitch = this.lerp(rotationPitch, -(passenger.rotationPitch*0.5f), 6f);
                        this.rotationYaw = this.lerp(rotationYaw, -passenger.rotationYaw, 6f);
//                        this.targetVector = null;
//                        this.targetVectorHeading = null;
                        this.swimSpeedCurrent += 0.05f;
                        if(this.swimSpeedCurrent > 4f) {
                            this.swimSpeedCurrent = 4f;
                        }
                    }
                    if(p.moveForward < 0f) {
                        this.swimSpeedCurrent *= 0.89f;
                        if(this.swimSpeedCurrent < 0.1f) {
                            this.swimSpeedCurrent = 0.1f;
                        }
                    }
                    if(p.moveForward == 0f) {
                        if(this.swimSpeedCurrent > 1f) {
                            this.swimSpeedCurrent *= 0.94f;
                            if(this.swimSpeedCurrent <= 1f) {
                                this.swimSpeedCurrent = 1f;
                            }
                        }
                        if(this.swimSpeedCurrent < 1f) {
                            this.swimSpeedCurrent *= 1.06f;
                            if(this.swimSpeedCurrent >= 1f) {
                                this.swimSpeedCurrent = 1f;
                            }
                        }
                        //this.swimSpeedCurrent = 1f;
                    }
                    //	this.swimYaw = -passenger.rotationYaw;
                }
                //p.rotationYaw = this.rotationYaw;
            } else
            if (passenger instanceof MobEntity) {
                MobEntity mobentity = (MobEntity)passenger;
                this.renderYawOffset = mobentity.renderYawOffset;
                this.prevRotationYawHead = mobentity.prevRotationYawHead;
            }
        }
    }

    @Override
    public void travel(Vec3d input) {
        if (isAlive()) {
            if (isBeingRidden() && canBeSteered()) {
                final Entity controllingPassenger = getControllingPassenger();

                if (!(controllingPassenger instanceof LivingEntity)) {
                    return;
                }

                final LivingEntity controllingEntity = (LivingEntity) controllingPassenger;

                this.rotationYaw = controllingPassenger.rotationYaw;
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = controllingPassenger.rotationPitch;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.rotationYaw;
                this.stepHeight = 1.0F;
                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

                float strafe = controllingEntity.moveStrafing * 0.1F;
                float forward = controllingEntity.moveForward;
                float vertical = controllingEntity.moveVertical;

                double verticalFromPitch = -Math.sin(Math.toRadians(rotationPitch)) * (getMotion().length() + 0.1) * (forward >= 0 ? 1 : -1);
                forward *= MathHelper.clamp(1 - (Math.abs(rotationPitch) / 90), 0.01f, 1);

                if (!isInWater()) {
                    // Lower max speed when breaching, as a penalty to uncareful driving
                    this.setMotion(this.getMotion().mul(0.9, 0.99, 0.9).add(0, -this.getAttribute(ENTITY_GRAVITY).getValue(), 0));
                }

                if (this.canPassengerSteer()) {
                    Vec3d travel = new Vec3d(strafe, verticalFromPitch + vertical, forward);
                    // This value controls how fast speed builds up
                    moveRelative(0.05F, travel);
                    move(MoverType.SELF, getMotion());
                    // This value controls how much speed is "dampened" which effectively controls how much drift there is, and the max speed
                    this.setMotion(this.getMotion().scale(forward > 0 || !isInWater() ? 0.975 : 0.9));
                } else {
                    this.setMotion(Vec3d.ZERO);
                }

                this.prevLimbSwingAmount = this.limbSwingAmount;
                double d1 = this.posX - this.prevPosX;
                double d0 = this.posZ - this.prevPosZ;
                float swinger = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
                if (swinger > 1.0F) {
                    swinger = 1.0F;
                }

                this.limbSwingAmount += (swinger - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(input);
            }
        }
    }
}
