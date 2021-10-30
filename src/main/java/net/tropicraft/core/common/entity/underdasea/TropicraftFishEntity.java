package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class TropicraftFishEntity extends WaterMobEntity {

    public float swimPitch = 0f;
    public float swimYaw = 0f;

    public Vector2f targetVectorHeading;
    public Vector3d targetVector;

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
        if (level.isClientSide) {
            // TODO if we ever use this class with turtles again - if(!(this instanceof IAmphibian)) {
                this.xRot = -this.swimPitch;
                this.yRot = -this.swimYaw;
                this.yHeadRot = -this.swimYaw;
                this.yHeadRotO = -this.prevSwimYaw;
                this.yBodyRot = 0;
                this.xRotO = -this.prevSwimPitch;
                this.yRotO = -this.prevSwimYaw;

                double x = (this.getX() - this.xo);
                double y = (this.getY() - this.yo);
                double z = (this.getZ() - this.zo);
                float yaw ;
                float pitch;

                this.prevSwimYaw = this.swimYaw;
                this.prevSwimPitch = this.swimPitch;

                if (this.getX() == this.xo && this.getZ() == this.zo) {
                    yaw = this.swimYaw;
                } else {
                    yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
                }
                if (this.getY() == this.yo) {
                    pitch = this.swimPitch;
                } else {
                    pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));
                }

                this.swimYaw = lerp(swimYaw, (int)-yaw, this.swimSpeedTurn*4);
                this.swimPitch = lerp(swimPitch, (int)-pitch, this.swimSpeedTurn*4);

                setDeltaMovement(getDeltaMovement().multiply(0.98, 0.98, 0.98));

                if (isNoAi() && isInWater()) {
                    fallVelocity = 0f;
                    swimSpeedCurrent = 0;
                    setDeltaMovement(Vector3d.ZERO);
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
            setTargetHeading(getX(), getY() - 1, getZ(), false);
        }

        // Move speed
        float currentSpeed = swimSpeedCurrent;
        float desiredSpeed = swimSpeedDefault;

        if(aggressTarget != null) {
            if(distanceToSqr(aggressTarget) < 10f) {
                desiredSpeed = swimSpeedCharging;
            }else {
                desiredSpeed = swimSpeedChasing;
            }
        }

        if(this.isPanicking) {
            desiredSpeed = this.swimSpeedPanic;
        }

        if(this.tickCount % 50  < 30) {
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
            setDeltaMovement(
                    currentSpeed * Math.sin(this.swimYaw * (Math.PI / 180.0)),
                    currentSpeed * Math.sin(this.swimPitch * (Math.PI / 180.0)),
                    currentSpeed * Math.cos(this.swimYaw * (Math.PI / 180.0))
            );
            fallVelocity = 0f;
        }
        if (isNoAi() && isInWater()) {
            setDeltaMovement(0, 0, 0);
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
        if (isNoAi()) {
            return false;
        }

        if (waterChecks) {
            BlockPos bp = new BlockPos((int) posX, (int) posY, (int) posZ);
            BlockState stateAtPos = level.getBlockState(bp);
            if (!stateAtPos.getMaterial().isLiquid() || stateAtPos.getMaterial().isSolid()) {
                return false;
            }
        }


        double x = (int) (posX - this.getX());
        double y = (int) (posY - this.getY());
        double z = (int) (posZ - this.getZ());
        float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
        float pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));
        targetVector = new Vector3d((int) posX, (int) posY, (int) posZ);
        targetVectorHeading = new Vector2f(yaw, pitch);
        return true;
    }


    public Vector3d getHeading() {
        return new Vector3d(Math.sin(this.swimYaw * (Math.PI / 180.0)), Math.sin(this.swimPitch * (Math.PI / 180.0)), Math.cos(this.swimYaw * (Math.PI / 180.0))).normalize();
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
        Vector3d randBlock = new Vector3d(getX() + randFlip(dist), getY() + randFlip(dist/2), getZ() + randFlip(dist));

        result = this.setTargetHeading(randBlock.x, randBlock.y, randBlock.z, true);

        // Try to move towards a player
        if (this.approachPlayers) {
            if (random.nextInt(50) == 0) {
                PlayerEntity closest = level.getNearestPlayer(this, 32D);
                if (closest != null) {
                    if (closest.isInWater())
                        result = this.setTargetHeading(closest.getX(), closest.getY(), closest.getZ(), true);
                }

            }
        }

        return result;
    }

    public void fleeEntity(Entity ent) {
        double x = ent.getX() - this.getX();
        double y = ent.getY() - this.getY();
        double z = ent.getZ() - this.getZ();
        float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90F;
        float pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));

        if (targetVector == null) {
            targetVector = new Vector3d(ent.getX(), ent.getY() - 5 + random.nextInt(10), ent.getZ());
        }
        targetVectorHeading = new Vector2f(yaw + 180, -1 * pitch/2);
    }

    public int randFlip(int i) {
        return random.nextBoolean() ? random.nextInt(i) : -(random.nextInt(i));
    }
}
