package net.tropicraft.core.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.entity.passive.FishingBobberEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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

    private final EntityKoaBase entity;
    private final RandomSource rand;

    private BlockPos posLastWaterFound;
    private BlockPos posLastLandFound;

    private final int walkingTimeoutMax = 20 * 30;
    private final int fishingTimeoutMax = 20 * 30;

    private int walkingTimeout;
    private int fishingTimeout;

    private final float moveSpeedAmp = 1F;

    //inventory placeholder
    private int fishCaught = 0;

    private int repathPenalty = 0;
    private final int repathPenaltyMax = 60;

    private int repathAttempts = 0;
    public final long timeBetweenFishing = 20 * 60 * 1;
    public final long timeBetweenFishingRandom = 30;

    public final List<ItemStack> listFishables = new ArrayList<>();

    public EntityAIGoneFishin(EntityKoaBase entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        rand = RandomSource.create();

        walkingTimeout = walkingTimeoutMax;
        fishingTimeout = fishingTimeoutMax;

        listFishables.add(new ItemStack(Items.TROPICAL_FISH));
        listFishables.add(new ItemStack(TropicraftItems.FRESH_MARLIN.get()));
        listFishables.add(new ItemStack(TropicraftItems.TROPICAL_FERTILIZER.get()));
        listFishables.add(new ItemStack(TropicraftItems.RAW_RAY.get()));
        /*for (int i = 0; i < EntityTropicalFish.NAMES.length; i++) {
            listFishables.add(new ItemStack(ItemRegistry.rawTropicalFish, 1, i));
        }
        for (int i = 0; i < ItemRiverFish.FISH_COLORS.length; i++) {
            listFishables.add(new ItemStack(ItemRegistry.rawRiverFish, 1, i));
        }*/
    }

    @Override
    public void start() {
        entity.setFishingItem();
    }

    @Override
    public boolean canUse() {

        //temp
        //entity.timeBetweenFishing = 20*60*2;
        entity.lastTimeFished = 0;
        debugTask = false;

        BlockPos blockpos = this.entity.blockPosition();

        if ((!this.entity.level().isDay() || this.entity.level().isRaining() && this.entity.level().getBiome(blockpos).value().getPrecipitationAt(blockpos) == Biome.Precipitation.RAIN)) {
            return false;
        }

        boolean result = false;//state != FISHING_STATE.IDLE || (entity.ticksExisted % 100 == 0 && findWater() != null);
        if (entity.lastTimeFished < entity.level().getGameTime() && entity.level().random.nextInt(3) == 0) {
            BlockPos posWater = findWater();

            //find close if failed
            if (posWater == null) {
                posWater = Util.findBlock(entity, 5, Util::isDeepWater);
            }

            if (posWater != null) {
                if (Util.tryMoveToXYZLongDist(entity, posWater, moveSpeedAmp)) {
                    posLastWaterFound = posWater;
                    result = true;
                    entity.lastTimeFished = entity.level().getGameTime() + timeBetweenFishing + timeBetweenFishingRandom;
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
    public boolean canContinueToUse() {
        return posLastWaterFound != null;
    }

    @Override
    public void tick() {
        super.tick();

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
                    stop();
                }
            } else {
                if (rand.nextInt(150) == 0 && entity.getNavigation().isDone()) {
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
                if (walkingTimeout <= 0 || entity.getNavigation().isDone()) {
                    if (walkingTimeout <= 0) {
                        debug("water pathing taking too long");
                    } else if (entity.getNavigation().isDone()) {
                        debug("water pathing having no path, pf find failed?");
                    }
                    if (Util.tryMoveToXYZLongDist(entity, posLastWaterFound, moveSpeedAmp)) {
                        debug("found new path to try");
                    } else {
                        stop();
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
                        stop();
                        return;
                    }
                }
            }

            //orig code had || isinWater, is contradicting to above code, hrm, then again find water code doesnt find it near shore...
            if (Util.getDistance(entity, posLastWaterFound.getX(), posLastWaterFound.getY(), posLastWaterFound.getZ()) < 8D || entity.isInWater()) {
                entity.getNavigation().stop();
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
        } else {
            BlockPos homePosition = entity.getRestrictCenter();
            if (state == FISHING_STATE.FISHING) {
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
                            stop();
                            return;
                        }
                    }
                }

                if (entity.getLure() != null && (entity.getLure().onGround() || entity.getLure().caughtEntity != null)) {
                    stop();
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
                        if (Util.tryMoveToXYZLongDist(entity, homePosition, moveSpeedAmp)) {
                            setState(FISHING_STATE.RETURN_TO_BASE);
                        } else {
                            stop();
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
                //entity.getRestrictCenter()

                if (homePosition.equals(BlockPos.ZERO)) {
                    stop();
                }

                //debug(entity.getRestrictCenter());
                if (Util.getDistance(entity, homePosition.getX(), homePosition.getY(), homePosition.getZ()) < 3D) {
                    debug("dropping off fish, reset");
                    fishCaught = 0;
                    entity.tryDumpInventoryIntoHomeChest();
                    //setState(FISHING_STATE.IDLE);
                    stop();
                }

                if (walkingTimeout <= 0 || (entity.getNavigation().isDone() && entity.level().getGameTime() % 20 == 0)) {
                    if (!retryPathOrAbort(homePosition)) return;
                }

                if (walkingTimeout > 0) {
                    walkingTimeout--;
                    //debug("walkingTimeout: " + walkingTimeout--);
                }
            } else if (state == FISHING_STATE.WALKING_TO_LAND) {

                if (Util.getDistance(entity, posLastLandFound.getX(), posLastLandFound.getY(), posLastLandFound.getZ()) < 5D || entity.onGround()) {
                    posLastLandFound = new BlockPos(entity.blockPosition());
                    entity.getNavigation().stop();
                    setState(FISHING_STATE.FISHING);
                    faceCoord(posLastWaterFound, 180, 180);
                    castLine();
                    return;
                }

                if (walkingTimeout <= 0 || entity.getNavigation().isDone()) {
                    if (walkingTimeout <= 0) {
                        debug("pathing taking too long");
                    } else if (entity.getNavigation().isDone()) {
                        debug("pathing having no path, pf find failed?");
                    }
                    if (Util.getDistance(entity, posLastLandFound.getX(), posLastLandFound.getY(), posLastLandFound.getZ()) < 64D) {
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
    }

    private void setState(FISHING_STATE state) {
        debug("setting state from " + this.state + " to " + state + " - " + this.entity.blockPosition());
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
    public void stop() {
        super.stop();
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
                debug("repathing failed - " + this.entity.getId() + " - " + this.state + " - " + pos);
                repathPenalty = repathPenaltyMax;
            }
        }
    }

    private boolean retryPathOrAbort(BlockPos pos) {
        boolean success = Util.tryMoveToXYZLongDist(entity, pos, moveSpeedAmp);
        if (!success) {
            debug("repathing failed, resetting - " + this.entity.getId() + " - " + this.state + " - " + pos);
            stop();
        } else {
            debug("repathing success - " + this.entity.getId() + " - " + this.state + " - " + pos);
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
        entity.swing(InteractionHand.MAIN_HAND);
        FishingBobberEntity lure = new FishingBobberEntity(entity, entity.level(), 0, 0);
        entity.level().addFreshEntity(lure);
    }

    private void retractLine() {
        if (entity.getLure() != null) entity.getLure().remove(Entity.RemovalReason.DISCARDED);
    }

    public void faceCoord(BlockPos coord, float maxDeltaYaw, float maxDeltaPitch) {
        faceCoord(coord.getX(), coord.getY(), coord.getZ(), maxDeltaYaw, maxDeltaPitch);
    }

    public void faceCoord(int x, int y, int z, float maxDeltaYaw, float maxDeltaPitch) {
        double d = x + 0.5F - entity.getX();
        double d2 = z + 0.5F - entity.getZ();
        double d1;
        d1 = y + 0.5F - (entity.getY() + (double) entity.getEyeHeight());

        double d3 = Mth.sqrt((float) (d * d + d2 * d2));
        float f2 = (float) ((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
        float f3 = (float) (-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
        entity.setXRot(-updateRotation(entity.getXRot(), f3, maxDeltaPitch));
        entity.setYRot(updateRotation(entity.getYRot(), f2, maxDeltaYaw));
    }

    public float updateRotation(float curRotation, float targetRotation, float maxDeltaRotation) {
        float f3;
        for (f3 = targetRotation - curRotation; f3 < -180F; f3 += 360F) {
        }
        for (; f3 >= 180F; f3 -= 360F) {
        }
        if (f3 > maxDeltaRotation) {
            f3 = maxDeltaRotation;
        }
        if (f3 < -maxDeltaRotation) {
            f3 = -maxDeltaRotation;
        }
        return curRotation + f3;
    }
}



