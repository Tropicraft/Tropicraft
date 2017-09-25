package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.core.common.Util;

import java.util.Random;

public class EntityAIGoneFishin extends EntityAIBase {

    enum FISHING_STATE {
        IDLE,
        WALKING_TO_WATER, //w1 - found water source, walking to it
        FISHING, //w2 - line casted, waiting for a grab
        RETURN_TO_BASE, //w3 - going home, inventory maxed maybe
        WALKING_TO_LAND //w4 - was in water, pathing to land to recast
    }

    private FISHING_STATE state = FISHING_STATE.IDLE;

    private boolean debugTask = true;

    private EntityCreature entity;
    private Random rand;

    private BlockPos posLastWaterFound;
    private BlockPos posLastLandFound;

    private int walkingTimeout;
    private int fishingTimeout;

    private int walkingTimeoutMax = 200;
    private int fishingTimeoutMax = 200;

    private float moveSpeedAmp = 1F;

    //inventory placeholder
    private int fishCaught = 0;

    public EntityAIGoneFishin(EntityCreature entity) {
        this.entity = entity;
        setMutexBits(3);
        rand = new Random();
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public boolean continueExecuting() {
        return super.continueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();

        if (state == FISHING_STATE.IDLE) {
            BlockPos posWater = findWater();
            if (posWater != null) {
                posLastWaterFound = posWater;
                Util.tryMoveToXYZLongDist(entity, posWater, moveSpeedAmp);
                setState(FISHING_STATE.WALKING_TO_WATER);
            } else {
                if (rand.nextInt(150) == 0 && entity.getNavigator().noPath()) {
                    //long distance wandering?
                    //ai.updateWanderPath();
                }
            }
        } else if (state == FISHING_STATE.WALKING_TO_WATER) {

            //copied from orig
            if (posLastWaterFound == null) {
                setState(FISHING_STATE.IDLE);
                return;
            }

            if (!entity.isInWater()) {
                if (walkingTimeout <= 0 || entity.getNavigator().noPath()) {
                    maintainPathToBlock(posLastWaterFound);
                }
            } else {
                //we fell in water accidentally, get to shore
                BlockPos posLand = findLand();
                if (posLand != null) {
                    posLastLandFound = posLand;
                    Util.tryMoveToXYZLongDist(entity, posLand, moveSpeedAmp);
                    setState(FISHING_STATE.WALKING_TO_LAND);
                }
            }

            //orig code had || isinWater, is contradicting to above code, hrm, then again find water code doesnt find it near shore...
            if (entity.getDistance(posLastWaterFound.getX(), posLastWaterFound.getY(), posLastWaterFound.getZ()) < 8D || entity.isInWater()) {
                entity.getNavigator().clearPathEntity();
                setState(FISHING_STATE.FISHING);
                //TODO: cast line
                castLine();
            }

            //if within 8 blocks of target
            //- cast line
            walkingTimeout--;
        } else if (state == FISHING_STATE.FISHING) {
            //temp visual to replace casting line
            entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1));
            if (!entity.isInWater()) {
                //force null path so they stay still
                //aim at fishing coord and wait
            } else {
                //we fell in water accidentally, get to shore
                BlockPos posLand = findLand();
                if (posLand != null) {
                    posLastLandFound = posLand;
                    Util.tryMoveToXYZLongDist(entity, posLand, moveSpeedAmp);
                    setState(FISHING_STATE.WALKING_TO_LAND);
                }
            }

            //if fish detected, and out of water for 10 ticks
            //- catch

            //if fishingTimeout done
            //- if maxed
            //-- go home
            //- else
            //-- if rand bool
            //--- reset to idle
            //-- else
            //--- recast
            //else
            //- fishingTimeout--

            if (ifCaughtFish()) {
                fishCaught++;
                System.out.println("caught a fish");

                if (getFishCount() > 4 || (rand.nextInt(1) == 0 && getFishCount() >= 2)) {
                    Util.tryMoveToXYZLongDist(entity, entity.getHomePosition(), moveSpeedAmp);
                    setState(FISHING_STATE.RETURN_TO_BASE);
                } else {
                    if (rand.nextInt(2) == 0) {
                        setState(FISHING_STATE.IDLE);
                    } else {
                        //cast line
                        castLine();
                    }
                }
            } else {
                fishingTimeout--;
            }

        } else if (state == FISHING_STATE.RETURN_TO_BASE) {
            //entity.getHomePosition()

            //System.out.println(entity.getHomePosition());
            if (entity.getDistance(entity.getHomePosition().getX(), entity.getHomePosition().getY(), entity.getHomePosition().getZ()) < 3D) {
                System.out.println("dropping off fish");
                fishCaught = 0;
                setState(FISHING_STATE.IDLE);
            }

            if (walkingTimeout <= 0 || (entity.getNavigator().noPath() && entity.world.getTotalWorldTime() % 20 == 0)) {
                maintainPathToBlock(entity.getHomePosition());
            }

            walkingTimeout--;
        } else if (state == FISHING_STATE.WALKING_TO_LAND) {

            if (entity.getDistance(posLastLandFound.getX(), posLastLandFound.getY(), posLastLandFound.getZ()) < 5D || entity.onGround) {
                posLastLandFound = new BlockPos(entity.getPosition());
                entity.getNavigator().clearPathEntity();
                setState(FISHING_STATE.FISHING);
                //TODO: cast line here too
                castLine();
                return;
            }

            if (walkingTimeout <= 0 || (entity.getNavigator().noPath() && entity.world.getTotalWorldTime() % 20 == 0)) {
                if (entity.getDistance(posLastLandFound.getX(), posLastLandFound.getY(), posLastLandFound.getZ()) < 64D) {
                    maintainPathToBlock(posLastLandFound);
                } else {
                    maintainPathToBlock(posLastWaterFound);
                }
            }

            walkingTimeout--;
        }
    }

    private void setState(FISHING_STATE state) {
        debug("setting state from " + this.state + " to " + state);
        this.state = state;
        if (this.state == FISHING_STATE.FISHING) {
            fishingTimeout = fishingTimeoutMax;
        } else if (this.state == FISHING_STATE.WALKING_TO_LAND ||
                this.state == FISHING_STATE.WALKING_TO_WATER ||
                this.state == FISHING_STATE.RETURN_TO_BASE) {
            walkingTimeout = walkingTimeoutMax;
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        fishCaught = 0;
        posLastLandFound = null;
        posLastWaterFound = null;
        this.state = FISHING_STATE.IDLE;
    }

    private void maintainPathToBlock(BlockPos pos) {
        System.out.println("repathing");
        walkingTimeout = walkingTimeoutMax;
        Util.tryMoveToXYZLongDist(entity, pos, moveSpeedAmp);
    }

    private void debug(String str) {
        if (debugTask) {
            System.out.println(str);
        }
    }

    private BlockPos findWater() {
        return Util.findBlock(entity, 60, Util::isWater);
    }

    private BlockPos findLand() {
        return Util.findBlock(entity, 60, Util::isLand);
    }

    private int getFishCount() {
        return fishCaught;
    }

    private boolean ifCaughtFish() {
        return fishingTimeout <= fishingTimeoutMax-40;
    }

    private void castLine() {
        fishingTimeout = fishingTimeoutMax;
    }
}
