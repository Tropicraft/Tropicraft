package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public abstract class TropicraftFishEntity extends WaterAnimal {

    public float swimPitch = 0f;
    public float swimYaw = 0f;

    public Vec2 targetVectorHeading;
    public Vec3 targetVector;

    public int outOfWaterTime = 0;
    public float outOfWaterAngle = 0f;

    public float fallVelocity = 0f;
    public float fallGravity = 0.0625f;

    public float prevSwimPitch = 0f;
    public float prevSwimYaw = 0f;

    private float swimSpeedDefault = 1f;
    protected float swimSpeedCurrent = 0f;
    private float swimSpeedPanic = 2f;

    private final float swimAccelRate = 0.02f;
    private final float swimDecelRate = 0.02f;

    private float swimSpeedTurn = 5f;
    public boolean isMovingAwayFromWall = false;

    public boolean isPanicking = false;
    public boolean fleeFromPlayers = false;
    public boolean approachPlayers = false;
    public double fleeDistance = 2D;

    public boolean isAggressing = false;
    public final boolean canAggress = false;

    public int eatenFishAmount = 0;
    public final int maximumEatAmount = 5;
    private float swimSpeedChasing = 2f;
    private float swimSpeedCharging = 2.5f;

    public Entity aggressTarget = null;

    private final boolean fishable = false;

    protected TropicraftFishEntity(EntityType<? extends WaterAnimal> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();

        // Client Side
        if (level().isClientSide) {
            // TODO if we ever use this class with turtles again - if(!(this instanceof IAmphibian)) {
            setXRot(-swimPitch);
            setYRot(-swimYaw);
            yHeadRot = -swimYaw;
            yHeadRotO = -prevSwimYaw;
            yBodyRot = 0;
            xRotO = -prevSwimPitch;
            yRotO = -prevSwimYaw;

            double x = (getX() - xo);
            double y = (getY() - yo);
            double z = (getZ() - zo);
            float yaw;
            float pitch;

            prevSwimYaw = swimYaw;
            prevSwimPitch = swimPitch;

            if (getX() == xo && getZ() == zo) {
                yaw = swimYaw;
            } else {
                yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
            }
            if (getY() == yo) {
                pitch = swimPitch;
            } else {
                pitch = (float) (-((Math.atan2(y, Mth.sqrt((float) (x * x + z * z))) * 180D) / Math.PI));
            }

            swimYaw = lerp(swimYaw, (int) -yaw, swimSpeedTurn * 4);
            swimPitch = lerp(swimPitch, (int) -pitch, swimSpeedTurn * 4);

            setDeltaMovement(getDeltaMovement().multiply(0.98, 0.98, 0.98));

            if (isNoAi() && isInWater()) {
                fallVelocity = 0f;
                swimSpeedCurrent = 0;
                setDeltaMovement(Vec3.ZERO);
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

        if (aggressTarget != null) {
            if (distanceToSqr(aggressTarget) < 10f) {
                desiredSpeed = swimSpeedCharging;
            } else {
                desiredSpeed = swimSpeedChasing;
            }
        }

        if (isPanicking) {
            desiredSpeed = swimSpeedPanic;
        }

        if (tickCount % 50 < 30) {
            desiredSpeed *= 0.8f;
        }

        if (isMovingAwayFromWall) {
            desiredSpeed *= 0.6f;
            currentSpeed *= 0.8f;
        }

        if (swimSpeedCurrent < desiredSpeed) {
            swimSpeedCurrent += swimAccelRate;
        }
        if (swimSpeedCurrent > desiredSpeed) {
            swimSpeedCurrent -= swimDecelRate;
        }

        // speed scaled down 1/10th
        currentSpeed *= 0.1f;

        // In water motion
        if (isInWater()) {
            setDeltaMovement(
                    currentSpeed * Math.sin(swimYaw * (Math.PI / 180.0)),
                    currentSpeed * Math.sin(swimPitch * (Math.PI / 180.0)),
                    currentSpeed * Math.cos(swimYaw * (Math.PI / 180.0))
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

        if (swimPitch > 45f) {
            swimPitch = 45f;
        }
    }

    public float rangeMap(float input, float inpMin, float inpMax, float outMin, float outMax) {
        if (Math.abs(inpMax - inpMin) < 1e-12) {
            return 0;
        }

        double ratio = (outMax - outMin) / (inpMax - inpMin);
        return (float) (ratio * (input - inpMin) + outMin);
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
        approachPlayers = b;
    }

    public float lerp(float x1, float x2, float t) {
        return x1 + (t * 0.03f) * Mth.wrapDegrees(x2 - x1);
    }

    public boolean setTargetHeading(double posX, double posY, double posZ, boolean waterChecks) {
        if (isNoAi()) {
            return false;
        }

        if (waterChecks) {
            BlockPos bp = new BlockPos((int) posX, (int) posY, (int) posZ);
            BlockState stateAtPos = level().getBlockState(bp);
            if (!stateAtPos.liquid() || stateAtPos.isSolid()) {
                return false;
            }
        }

        double x = (int) (posX - getX());
        double y = (int) (posY - getY());
        double z = (int) (posZ - getZ());
        float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
        float pitch = (float) (-((Math.atan2(y, Mth.sqrt((float) (x * x + z * z))) * 180D) / Math.PI));
        targetVector = new Vec3((int) posX, (int) posY, (int) posZ);
        targetVectorHeading = new Vec2(yaw, pitch);
        return true;
    }

    public Vec3 getHeading() {
        return new Vec3(Math.sin(swimYaw * (Math.PI / 180.0)), Math.sin(swimPitch * (Math.PI / 180.0)), Math.cos(swimYaw * (Math.PI / 180.0))).normalize();
    }

    public void setRandomTargetHeadingForce(int maxTimes) {
        for (int i = 0; i < maxTimes; i++) {
            if (setRandomTargetHeading()) {
                break;
            }
        }
    }

    public boolean setRandomTargetHeading() {
        boolean result = false;
        int dist = 16;
        Vec3 randBlock = new Vec3(getX() + randFlip(dist), getY() + randFlip(dist / 2), getZ() + randFlip(dist));

        result = setTargetHeading(randBlock.x, randBlock.y, randBlock.z, true);

        // Try to move towards a player
        if (approachPlayers) {
            if (random.nextInt(50) == 0) {
                Player closest = level().getNearestPlayer(this, 32D);
                if (closest != null) {
                    if (closest.isInWater())
                        result = setTargetHeading(closest.getX(), closest.getY(), closest.getZ(), true);
                }
            }
        }

        return result;
    }

    public void fleeEntity(Entity ent) {
        double x = ent.getX() - getX();
        double y = ent.getY() - getY();
        double z = ent.getZ() - getZ();
        float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90F;
        float pitch = (float) (-((Math.atan2(y, Mth.sqrt((float) (x * x + z * z))) * 180D) / Math.PI));

        if (targetVector == null) {
            targetVector = new Vec3(ent.getX(), ent.getY() - 5 + random.nextInt(10), ent.getZ());
        }
        targetVectorHeading = new Vec2(yaw + 180, -1 * pitch / 2);
    }

    public int randFlip(int i) {
        return random.nextBoolean() ? random.nextInt(i) : -(random.nextInt(i));
    }
}
