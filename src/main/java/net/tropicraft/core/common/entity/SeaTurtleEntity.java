package net.tropicraft.core.common.entity;

import net.minecraft.entity.*;
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

import javax.annotation.Nullable;
import java.util.List;

public class SeaTurtleEntity extends TurtleEntity {

    private static final DataParameter<Boolean> IS_MATURE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TURTLE_TYPE = EntityDataManager.createKey(SeaTurtleEntity.class, DataSerializers.VARINT);

    private static final int NUM_TYPES = 6;

    public SeaTurtleEntity(EntityType<? extends TurtleEntity> type, World world) {
        super(type, world);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(2.0D);
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
        setTurtleType(rand.nextInt(NUM_TYPES) + 1);
        return super.onInitialSpawn(world, difficultyInstance, spawnReason, data, nbt);
    }

    @Override
    public void registerData() {
        super.registerData();
        getDataManager().register(IS_MATURE, false);
        getDataManager().register(TURTLE_TYPE, 1);
    }

    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("TurtleType", getTurtleType());
        nbt.putBoolean("IsMature", isMature());
    }

    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        setTurtleType(nbt.getInt("TurtleType"));
        setIsMature(nbt.getBoolean("IsMature"));
    }

    public boolean isMature() {
        return getDataManager().get(IS_MATURE);
    }

    public void setIsMature(final boolean mature) {
        getDataManager().set(IS_MATURE, mature);
    }

    public int getTurtleType() {
        return getDataManager().get(TURTLE_TYPE);
    }

    public void setTurtleType(final int type) {
        getDataManager().set(TURTLE_TYPE, type);
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
    @Nullable
    public AgeableEntity createChild(AgeableEntity ent) {
        return TropicraftEntities.SEA_TURTLE.get().create(this.world);
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
            float f = 0.0F;
            float f1 = (float) ((!isAlive() ? 0.009999999776482582D : this.getMountedYOffset())
                    + passenger.getYOffset());
            f1+=0.1f;
            f1+=-(this.rotationPitch * 0.00525f);

            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);

                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }
            }
            f = -0.25f-(this.rotationPitch*0.00525f);

            Vec3d vec3d = (new Vec3d((double) f, 0.0D, 0.0D))
                    .rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
            passenger.setPosition(this.posX + vec3d.x, this.posY + (double) f1, this.posZ + vec3d.z);

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

                float f = controllingEntity.moveStrafing * 0.5F;
                float f1 = controllingEntity.moveForward;
                float f4 = controllingEntity.moveVertical;

                float speed = (float)this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();

                System.out.println(jumpMovementFactor);
                float f2 = -MathHelper.sin((rotationPitch) * ((float)Math.PI / 180F)) * speed;

                if (!isInWater()) {
                    // TODO fix hoppy motion out of water when turtle surfaces
                    setMotion(getMotion().add(0, -0.11, 0));
                    super.travel(new Vec3d(0, f2, f1));
                    return;
                }

                if (this.canPassengerSteer()) {
                    float xxx = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
                    float yyy = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));

                    final double motionXD = xxx * -0.1 * f1;
                    final double motionZD = yyy * 0.1 * f1;

                    if (isInWater()) {
                        setMotion(getMotion().add(motionXD, f2 * 0.01, motionZD));
                    } else {
                        setMotion(getMotion().add(0, -0.01, 0));
                    }

                    this.setAIMoveSpeed(speed);
                    // always unit vector of travel unless setMotion is called
                    super.travel(new Vec3d(0, f2, f1));
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
