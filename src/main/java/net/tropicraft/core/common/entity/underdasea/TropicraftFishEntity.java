package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.vecmath.Vector2f;
import java.util.List;

public abstract class TropicraftFishEntity extends WaterMobEntity {

    public float swimPitch = 0f;
    public float swimYaw = 0f;

    public Vector2f targetVectorHeading;
    public Vec3d targetVector;

    public int outOfWaterTime = 0;
    public float outOfWaterAngle = 0f;

    public float fallVelocity = 0f;
    public float fallGravity = 0.0625f;

    public float prevSwimPitch = 0f;
    public float prevSwimYaw = 0f;

    private float swimSpeedDefault = 1f;
    protected float swimSpeedCurrent = 0f;
    private float swimSpeedPanic = 2f;

    private float swimAccelRate = 0.02f;
    private float swimDecelRate = 0.02f;

    private float swimSpeedTurn = 5f;
    public boolean isMovingAwayFromWall = false;

    public boolean isPanicking = false;
    public boolean fleeFromPlayers = false;
    public boolean approachPlayers = false;
    public double fleeDistance = 2D;

    public boolean isAggressing = false;
    public boolean canAggress = false;

    public int eatenFishAmount = 0;
    public int maximumEatAmount = 5;
    private float swimSpeedChasing = 2f;
    private float swimSpeedCharging = 2.5f;

    public Entity aggressTarget = null;

    private boolean fishable = false;

    protected TropicraftFishEntity(final EntityType<? extends WaterMobEntity> type, final World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();

        // Client Side
        if (world.isRemote) {
            // TODO if we ever use this class with turtles again - if(!(this instanceof IAmphibian)) {
                this.rotationPitch = -this.swimPitch;
                this.rotationYaw = -this.swimYaw;
                this.rotationYawHead = -this.swimYaw;
                this.prevRotationYawHead = -this.prevSwimYaw;
                this.renderYawOffset = 0;
                this.prevRotationPitch = -this.prevSwimPitch;
                this.prevRotationYaw = -this.prevSwimYaw;

                double x = (this.posX - this.prevPosX);
                double y = (this.posY - this.prevPosY);
                double z = (this.posZ - this.prevPosZ);
                float yaw ;
                float pitch;

                this.prevSwimYaw = this.swimYaw;
                this.prevSwimPitch = this.swimPitch;

                if (this.posX == this.prevPosX && this.posZ == this.prevPosZ) {
                    yaw = this.swimYaw;
                } else {
                    yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
                }
                if (this.posY == this.prevPosY) {
                    pitch = this.swimPitch;
                } else {
                    pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));
                }

                this.swimYaw = lerp(swimYaw, (int)-yaw, this.swimSpeedTurn*4);
                this.swimPitch = lerp(swimPitch, (int)-pitch, this.swimSpeedTurn*4);

                setMotion(getMotion().mul(0.98, 0.98, 0.98));

                if (isAIDisabled() && isInWater()) {
                    fallVelocity = 0f;
                    swimSpeedCurrent = 0;
                    setMotion(new Vec3d(0, 0, 0));
                }
        }

        // Server Side
        if (isInWater()) {
            if (isMovingAwayFromWall) {
                swimSpeedTurn *= 1.8f;
            }

            if (targetVectorHeading != null) {
                swimYaw = lerp(swimYaw, -targetVectorHeading.x, swimSpeedTurn);
                swimPitch = lerp(swimPitch, -targetVectorHeading.y, swimSpeedTurn);
            }
        }

        // Out of water
        if (!isInWater()) {
            setTargetHeading(posX, posY - 1, posZ, false);
        }

        // Move speed
        float currentSpeed = swimSpeedCurrent;
        float desiredSpeed = swimSpeedDefault;

        if(aggressTarget != null) {
            if(getDistanceSq(aggressTarget) < 10f) {
                desiredSpeed = swimSpeedCharging;
            }else {
                desiredSpeed = swimSpeedChasing;
            }
        }

        if(this.isPanicking) {
            desiredSpeed = this.swimSpeedPanic;
        }

        if(this.ticksExisted % 50  < 30) {
            desiredSpeed *= 0.8f;
        }

        if(this.isMovingAwayFromWall) {
            desiredSpeed *= 0.6f;
            currentSpeed *= 0.8f;
        }

        if(this.swimSpeedCurrent < desiredSpeed) {
            this.swimSpeedCurrent += this.swimAccelRate;
        }
        if(this.swimSpeedCurrent > desiredSpeed) {
            this.swimSpeedCurrent -= this.swimDecelRate;
        }

        // speed scaled down 1/10th
        currentSpeed *= 0.1f;

        // In water motion
        if (isInWater()) {
            setMotion(
                    currentSpeed * Math.sin(this.swimYaw * (Math.PI / 180.0)),
                    currentSpeed * Math.sin(this.swimPitch * (Math.PI / 180.0)),
                    currentSpeed * Math.cos(this.swimYaw * (Math.PI / 180.0))
            );
            fallVelocity = 0f;
        }
        if (isAIDisabled() && isInWater()) {
            setMotion(0, 0, 0);
            fallVelocity = 0f;
            swimSpeedCurrent = 0;
        }

        // out of water motion
//        if (!this.isInWater() && !(this instanceof IAmphibian)) {
//
//            if (this.onGround) {
//                if (rand.nextInt(6) == 0) {
//
//                    this.motionX += rand.nextBoolean() ? rand.nextFloat()/8 : - rand.nextFloat()/8;
//                    this.motionZ += rand.nextBoolean() ? rand.nextFloat()/8 : - rand.nextFloat()/8;
//                }
//                this.motionX *= 0.5f;
//                this.motionZ *= 0.5f;
//                if(this.ticksExisted % 4 == 0)
//                    this.fallVelocity = -.02f;
//
//                if(rand.nextInt(20) == 0 || this.hurtTime > 0) {
//                    this.fallVelocity = -.03f;
//                    this.swimPitch = 25f;
//                }
//            }
//
//            if(this.swimPitch > 0f) {
//                this.swimPitch -= 5f;
//            }
//            if(this.ticksExisted % 20 == 0) {
//                this.outOfWaterAngle = rand.nextInt(360);
//            }
//
//            float turnSpeed = 5f;
//            if(this.hurtTime > 0) {
//                turnSpeed = 15f;
//            }
//            if(this.swimYaw > this.outOfWaterAngle) {
//                this.swimYaw-= turnSpeed;
//            }
//            if(this.swimYaw < this.outOfWaterAngle) {
//                this.swimYaw += turnSpeed;
//            }
//
//            float vel = this.fallVelocity;
//
//            if(this.getPassengers().size() > 0) {
//                vel *= 0.5f;
//            }
//
//            this.motionY -= vel;
//            this.fallVelocity += (this.fallGravity / 10);
//        }

        if(swimPitch > 45f) {
            swimPitch = 45f;
        }
    }

    public float rangeMap(float input, float inpMin, float inpMax, float outMin, float outMax) {
        if (Math.abs(inpMax - inpMin) < 1e-12) {
            return 0;
        }

        double ratio = (outMax - outMin) / (inpMax - inpMin);
        return (float)(ratio * (input - inpMin) + outMin);
    }

    public void setSwimSpeeds(float regular, float panic, float turnSpeed) {
        swimSpeedDefault = regular;
        swimSpeedPanic = panic;
        swimSpeedTurn = turnSpeed;
    }

    public void setSwimSpeeds(float r, float p, float t, float chasing, float charging) {
        setSwimSpeeds(r, p, t);
        swimSpeedChasing = chasing;
        swimSpeedCharging = charging;
    }

    public void setApproachesPlayers(boolean b) {
        this.approachPlayers = b;
    }

    public float lerp(float x1, float x2, float t) {
        return x1 + (t*0.03f) * MathHelper.wrapDegrees(x2 - x1);
    }

    public boolean setTargetHeading(double posX, double posY, double posZ, boolean waterChecks) {
        if (isAIDisabled()) {
            return false;
        }

        if (waterChecks) {
            BlockPos bp = new BlockPos((int) posX, (int) posY, (int) posZ);
            BlockState stateAtPos = world.getBlockState(bp);
            if (!stateAtPos.getMaterial().isLiquid() || stateAtPos.getMaterial().isSolid()) {
                return false;
            }
        }


        double x = (int) (posX - this.posX);
        double y = (int) (posY - this.posY);
        double z = (int) (posZ - this.posZ);
        float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
        float pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));

        if (this.targetVectorHeading == null) {
            this.targetVectorHeading = new Vector2f();
        }
        this.targetVector = new Vec3d((int)posX, (int)posY, (int)posZ);
        targetVectorHeading.x = yaw;
        targetVectorHeading.y = pitch;
        return true;
    }


    public Vec3d getHeading() {
        return new Vec3d(Math.sin(this.swimYaw * (Math.PI / 180.0)), Math.sin(this.swimPitch * (Math.PI / 180.0)), Math.cos(this.swimYaw * (Math.PI / 180.0))).normalize();
    }

    public void setRandomTargetHeadingForce(int maxTimes) {
        for(int i =0; i < maxTimes; i++) {
            if(setRandomTargetHeading()) {
                break;
            }
        }
    }

    public boolean setRandomTargetHeading() {
        boolean result = false;
        int dist = 16;
        Vec3d randBlock = new Vec3d(posX + randFlip(dist), posY + randFlip(dist/2), posZ + randFlip(dist));

        result = this.setTargetHeading(randBlock.x, randBlock.y, randBlock.z, true);

        // Try to move towards a player
        if (this.approachPlayers) {
            if (rand.nextInt(50) == 0) {
                PlayerEntity closest = world.getClosestPlayer(this, 32D);
                if (closest != null) {
                    if (closest.isInWater())
                        result = this.setTargetHeading(closest.posX, closest.posY, closest.posZ, true);
                }

            }
        }

        return result;
    }

    public void fleeEntity(Entity ent) {
        double x = ent.posX - this.posX;
        double y = ent.posY - this.posY;
        double z = ent.posZ - this.posZ;
        float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90F;
        float pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));

        if (this.targetVectorHeading == null) {
            this.targetVectorHeading = new Vector2f();
        }
        if (this.targetVector == null) {
            this.targetVector = new Vec3d(ent.posX, ent.posY - 5 + rand.nextInt(10), ent.posZ);
        }
        targetVectorHeading.x = yaw+180;
        targetVectorHeading.y = -(pitch/2);
    }

    public int randFlip(int i) {
        return rand.nextBoolean() ? rand.nextInt(i) : -(rand.nextInt(i));
    }
}
