package net.tropicraft.core.common.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.passive.EntityFishHook;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;
import net.tropicraft.core.common.item.ItemRiverFish;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityAIGoneFishin extends Goal {

    enum FISHING_STATE {
        IDLE,
        WALKING_TO_WATER, //w1 - found water source, walking to it
        FISHING, //w2 - line casted, waiting for a grab
        RETURN_TO_BASE, //w3 - going home, inventory maxed maybe
        WALKING_TO_LAND //w4 - was in water, pathing to land to recast
    }

    private FISHING_STATE state = FISHING_STATE.IDLE;

    private boolean debugTask = false;

    private EntityKoaBase entity;
    private Random rand;

    private BlockPos posLastWaterFound;
    private BlockPos posLastLandFound;

    private int walkingTimeoutMax = 20*30;
    private int fishingTimeoutMax = 20*30;

    private int walkingTimeout;
    private int fishingTimeout;

    private float moveSpeedAmp = 1F;

    //inventory placeholder
    private int fishCaught = 0;

    private int repathPenalty = 0;
    private int repathPenaltyMax = 60;

    private int repathAttempts = 0;
    public long timeBetweenFishing = 20*60*1;
    public long timeBetweenFishingRandom = 30;

    public List<ItemStack> listFishables = new ArrayList<>();

    public EntityAIGoneFishin(EntityKoaBase entity) {
        this.entity = entity;
        setMutexBits(3);
        rand = new Random();

        walkingTimeout = walkingTimeoutMax;
        fishingTimeout = fishingTimeoutMax;

        listFishables.add(new ItemStack(Items.FISH));
        listFishables.add(new ItemStack(ItemRegistry.freshMarlin));
        listFishables.add(new ItemStack(ItemRegistry.fertilizer));
        listFishables.add(new ItemStack(ItemRegistry.rawRay));
        for (int i = 0; i < EntityTropicalFish.NAMES.length; i++) {
            listFishables.add(new ItemStack(ItemRegistry.rawTropicalFish, 1, i));
        }
        for (int i = 0; i < ItemRiverFish.FISH_COLORS.length; i++) {
            listFishables.add(new ItemStack(ItemRegistry.rawRiverFish, 1, i));
        }
    }

    @Override
    public void startExecuting() {
        entity.setFishingItem();
    }

    @Override
    public boolean shouldExecute() {

        //temp
        //entity.timeBetweenFishing = 20*60*2;
        entity.lastTimeFished = 0;
        debugTask = false;

        BlockPos blockpos = new BlockPos(this.entity);

        if ((!this.entity.world.isDaytime() || this.entity.world.isRaining() && this.entity.world.getBiome(blockpos).canRain())) {
            return false;
        }

        boolean result = false;//state != FISHING_STATE.IDLE || (entity.ticksExisted % 100 == 0 && findWater() != null);
        if (entity.lastTimeFished < entity.world.getTotalWorldTime() && entity.world.rand.nextInt(3) == 0) {
            BlockPos posWater = findWater();

            //find close if failed
            if (posWater == null) {
                posWater = Util.findBlock(entity, 5, Util::isDeepWater);
            }

            if (posWater != null) {
                if (Util.tryMoveToXYZLongDist(entity, posWater, moveSpeedAmp)) {
                    posLastWaterFound = posWater;
                    result = true;
                    entity.lastTimeFished = entity.world.getTotalWorldTime() + timeBetweenFishing + timeBetweenFishingRandom;
                    setState(FISHING_STATE.WALKING_TO_WATER);
                    debug("found water, start executing");
                } else {
                    debug("failed the path, skip executing");
                }
            } else {
                debug("couldnt find water, skip executing");
            }
        } else {
            //debug("waiting on timeout to fish");
        }
        return result;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return posLastWaterFound != null;
    }

    @Override
    public void updateTask() {
        super.updateTask();

        if (repathPenalty > 0) {
            repathPenalty--;
        }

        //runs on reset or after shouldExecute returns true
        //this mode may have been deprecated
        if (state == FISHING_STATE.IDLE) {
            //debug("idle state used");
            if (posLastWaterFound == null) {
                posLastWaterFound = findWater();
            }
            if (posLastWaterFound != null) {
                if (Util.tryMoveToXYZLongDist(entity, posLastWaterFound, moveSpeedAmp)) {
                    setState(FISHING_STATE.WALKING_TO_WATER);
                } else {
                    debug("found water but pathing failed, abort");
                    //assume bad water spot
                    //but even bad water spots far away will get a successfull partial path
                    //hmmmm
                    resetTask();
                }
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
                    if (walkingTimeout <= 0) {
                        debug("water pathing taking too long");
                    } else if (entity.getNavigator().noPath()) {
                        debug("water pathing having no path, pf find failed?");
                    }
                    if (Util.tryMoveToXYZLongDist(entity, posLastWaterFound, moveSpeedAmp)) {
                        debug("found new path to try");
                    } else {
                        resetTask();
                        return;
                    }
                    //cases where theyre trying to get to water underground, reset task instead
                    //maintainPathToBlock(posLastWaterFound);

                }
            } else {
                //we fell in water accidentally, get to shore
                BlockPos posLand = findLand();
                if (posLand != null) {
                    posLastLandFound = posLand;
                    if (Util.tryMoveToXYZLongDist(entity, posLand, moveSpeedAmp)) {
                        setState(FISHING_STATE.WALKING_TO_LAND);
                    } else {
                        resetTask();
                        return;
                    }
                }
            }

            //orig code had || isinWater, is contradicting to above code, hrm, then again find water code doesnt find it near shore...
            if (entity.getDistance(posLastWaterFound.getX(), posLastWaterFound.getY(), posLastWaterFound.getZ()) < 8D || entity.isInWater()) {
                entity.getNavigator().clearPath();
                setState(FISHING_STATE.FISHING);
                faceCoord(posLastWaterFound, 180, 180);
                castLine();
            }

            //if within 8 blocks of target
            //- cast line
            if (walkingTimeout > 0) {
                walkingTimeout--;
                //debug("walkingTimeout: " + walkingTimeout--);
            }
        } else if (state == FISHING_STATE.FISHING) {
            //temp visual to replace casting line
            //entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40));
            if (!entity.isInWater()) {
                //force null path so they stay still
                //aim at fishing coord and wait
            } else {
                //we fell in water accidentally, get to shore
                BlockPos posLand = findLand();
                if (posLand != null) {
                    posLastLandFound = posLand;
                    if (Util.tryMoveToXYZLongDist(entity, posLand, moveSpeedAmp)) {
                        setState(FISHING_STATE.WALKING_TO_LAND);
                    } else {
                        resetTask();
                        return;
                    }
                }
            }

            if (entity.getLure() != null && (entity.getLure().onGround || entity.getLure().caughtEntity != null)) {
                resetTask();
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
                retractLine();
                fishCaught++;
                //entity.inventory.addItem(new ItemStack(Items.FISH));
                entity.inventory.addItem(listFishables.get(rand.nextInt(listFishables.size())));

                debug("caught a fish");

                if (getFishCount() > 4 || (rand.nextInt(1) == 0 && getFishCount() >= 2)) {
                    if (Util.tryMoveToXYZLongDist(entity, entity.getHomePosition(), moveSpeedAmp)) {
                        setState(FISHING_STATE.RETURN_TO_BASE);
                    } else {
                        resetTask();
                        return;
                    }
                } else {
                    if (rand.nextInt(2) == 0) {
                        setState(FISHING_STATE.IDLE);
                    } else {
                        //cast line
                        //TODO: there is a state where posLastWaterFound can be null and cause crash, unsure why, patching for now
                        if (posLastWaterFound == null) {
                            setState(FISHING_STATE.IDLE);
                        } else {
                            faceCoord(posLastWaterFound, 180, 180);
                            castLine();
                        }
                    }
                }
            } else {
                fishingTimeout--;
            }

        } else if (state == FISHING_STATE.RETURN_TO_BASE) {
            //entity.getHomePosition()

            //debug(entity.getHomePosition());
            if (entity.getDistance(entity.getHomePosition().getX(), entity.getHomePosition().getY(), entity.getHomePosition().getZ()) < 3D) {
                debug("dropping off fish, reset");
                fishCaught = 0;
                entity.tryDumpInventoryIntoHomeChest();
                //setState(FISHING_STATE.IDLE);
                resetTask();
            }

            if (walkingTimeout <= 0 || (entity.getNavigator().noPath() && entity.world.getTotalWorldTime() % 20 == 0)) {
                if (!retryPathOrAbort(entity.getHomePosition())) return;
            }

            if (walkingTimeout > 0) {
                walkingTimeout--;
                //debug("walkingTimeout: " + walkingTimeout--);
            }
        } else if (state == FISHING_STATE.WALKING_TO_LAND) {

            if (entity.getDistance(posLastLandFound.getX(), posLastLandFound.getY(), posLastLandFound.getZ()) < 5D || entity.onGround) {
                posLastLandFound = new BlockPos(entity.getPosition());
                entity.getNavigator().clearPath();
                setState(FISHING_STATE.FISHING);
                faceCoord(posLastWaterFound, 180, 180);
                castLine();
                return;
            }

            if (walkingTimeout <= 0 || entity.getNavigator().noPath()) {
                if (walkingTimeout <= 0) {
                    debug("pathing taking too long");
                } else if (entity.getNavigator().noPath()) {
                    debug("pathing having no path, pf find failed?");
                }
                if (entity.getDistance(posLastLandFound.getX(), posLastLandFound.getY(), posLastLandFound.getZ()) < 64D) {
                    if (!retryPathOrAbort(posLastLandFound)) return;
                } else {
                    if (!retryPathOrAbort(posLastWaterFound)) return;
                }
            }

            if (walkingTimeout > 0) {
                walkingTimeout--;
                //debug("walkingTimeout: " + walkingTimeout--);
            }
        }
    }

    private void setState(FISHING_STATE state) {
        debug("setting state from " + this.state + " to " + state + " - " + this.entity.getPosition());
        if (state != FISHING_STATE.FISHING) {
            retractLine();
        }
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
        debug("reset task");
        fishCaught = 0;
        posLastLandFound = null;
        posLastWaterFound = null;
        repathAttempts = 0;
        retractLine();

        this.state = FISHING_STATE.IDLE;
    }

    private void maintainPathToBlock(BlockPos pos) {
        if (repathPenalty <= 0) {
            walkingTimeout = walkingTimeoutMax;
            boolean success = Util.tryMoveToXYZLongDist(entity, pos, moveSpeedAmp);
            if (!success) {
                debug("repathing failed - " + this.entity.getEntityId() + " - " + this.state + " - " + pos);
                repathPenalty = repathPenaltyMax;
            }
        }
    }

    private boolean retryPathOrAbort(BlockPos pos) {
        boolean success = Util.tryMoveToXYZLongDist(entity, pos, moveSpeedAmp);
        if (!success) {
            debug("repathing failed, resetting - " + this.entity.getEntityId() + " - " + this.state + " - " + pos);
            resetTask();
        } else {
            debug("repathing success - " + this.entity.getEntityId() + " - " + this.state + " - " + pos);
            walkingTimeout = walkingTimeoutMax;
        }
        return success;
    }

    private void debug(String str) {
        if (debugTask) {
            System.out.println(str);
        }
    }

    private BlockPos findWater() {
        return Util.findBlock(entity, 60, Util::isDeepWater);
    }

    private BlockPos findLand() {
        return Util.findBlock(entity, 60, Util::isLand);
    }

    private int getFishCount() {
        return fishCaught;
    }

    private boolean ifCaughtFish() {
        return fishingTimeout <= 40;
    }

    private void castLine() {
        //System.out.println("cast line");
        fishingTimeout = fishingTimeoutMax;
        retractLine();
        entity.swingArm(Hand.MAIN_HAND);
        EntityFishHook lure = new EntityFishHook(entity.world, entity);
        entity.world.spawnEntity(lure);
    }

    private void retractLine() {
        if (entity.getLure() != null) entity.getLure().setDead();
    }

    public void faceCoord(BlockPos coord, float maxDeltaYaw, float maxDeltaPitch) {
        faceCoord(coord.getX(), coord.getY(), coord.getZ(), maxDeltaYaw, maxDeltaPitch);
    }

    public void faceCoord(int x, int y, int z, float maxDeltaYaw, float maxDeltaPitch)
    {
        double d = x+0.5F - entity.posX;
        double d2 = z+0.5F - entity.posZ;
        double d1;
        d1 = y+0.5F - (entity.posY + (double)entity.getEyeHeight());

        double d3 = MathHelper.sqrt(d * d + d2 * d2);
        float f2 = (float)((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
        float f3 = (float)(-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
        entity.rotationPitch = -updateRotation(entity.rotationPitch, f3, maxDeltaPitch);
        entity.rotationYaw = updateRotation(entity.rotationYaw, f2, maxDeltaYaw);
    }

    public float updateRotation(float curRotation, float targetRotation, float maxDeltaRotation)
    {
        float f3;
        for(f3 = targetRotation - curRotation; f3 < -180F; f3 += 360F) { }
        for(; f3 >= 180F; f3 -= 360F) { }
        if(f3 > maxDeltaRotation)
        {
            f3 = maxDeltaRotation;
        }
        if(f3 < -maxDeltaRotation)
        {
            f3 = -maxDeltaRotation;
        }
        return curRotation + f3;
    }
}
