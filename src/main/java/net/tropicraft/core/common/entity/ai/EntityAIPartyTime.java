package net.tropicraft.core.common.entity.ai;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.BlockBongoDrum;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.enums.TropicraftBongos;
import net.tropicraft.core.registry.SoundRegistry;

public class EntityAIPartyTime extends EntityAIBase
{
    private final EntityKoaBase entityObj;

    private int walkingTimeoutMax = 20*10;

    private int walkingTimeout;
    private int repathPentalty = 0;

    private int lookUpdateTimer = 0;
    private int randXPos = 0;
    private int randYPos = 0;
    private int randZPos = 0;

    private int assignedDrumIndex = 0;
    private boolean wasClose = false;
    private boolean bangDrum = false;

    public EntityAIPartyTime(EntityKoaBase entityObjIn)
    {
        this.entityObj = entityObjIn;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {

        if (!entityObj.getWantsToParty()) {
            return false;
        }

        BlockPos blockpos = new BlockPos(this.entityObj);

        if ((!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && !this.entityObj.world.provider.isNether()) {
            if (!isTooClose()) {
                if (entityObj.world.rand.nextInt(20) == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        BlockPos blockpos = new BlockPos(this.entityObj);
        //return !this.entityObj.getNavigator().noPath();
        if ((!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && !this.entityObj.world.provider.isNether())
        {
            if (!isTooClose()) {
                return true;
            } else {
                return false;
            }

        } else {
            if (entityObj.world.rand.nextInt(60) == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void updateTask() {
        super.updateTask();

        boolean isClose = false;

        BlockPos blockposGoal = null;
        if (this.entityObj.listPosDrums.size() > assignedDrumIndex) {
            blockposGoal = entityObj.listPosDrums.get(assignedDrumIndex);
        }

        if (entityObj.world.getTotalWorldTime() % 200 == 0){
            if (this.entityObj.listPosDrums.size() > 0) {
                assignedDrumIndex = entityObj.world.rand.nextInt(entityObj.listPosDrums.size());
            }
            //if (wasClose) {
                bangDrum = entityObj.world.rand.nextBoolean();
            //}
        }

        if (blockposGoal == null) {
            resetTask();
            return;
        }

        //prevent walking onto source
        double dist = entityObj.getPositionVector().distanceTo(new Vec3d(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
        if (dist < 8D) {
            wasClose = true;
        }
        if (dist < 3D && entityObj.onGround) {
            isClose = true;
            entityObj.getNavigator().clearPath();
            if (!bangDrum) {
                //entityObj.setSitting(true);
                entityObj.setDancing(true);
                this.entityObj.getJumpHelper().setJumping();
                this.entityObj.rotationYaw = entityObj.world.rand.nextInt(360);
            } else {
                entityObj.setDancing(false);
                if (true || lookUpdateTimer <= 0) {

                    entityObj.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);

                    //keep for testing, was neat sounding
                    int amp = 1;//entityObj.world.rand.nextInt(10) + 1;
                    int rate = 4 + (entityObj.getEntityId() % 7);

                    int index1 = 0;

                    HashMap<Integer, List<Integer>> lookupStateToSequence = new HashMap<>();

                    List<Integer> listDelays = Lists.newArrayList(9, 3, 3, 3, 6);

                    lookupStateToSequence.put(index1++, listDelays);
                    lookupStateToSequence.put(index1++, listDelays);
                    lookupStateToSequence.put(index1++, listDelays);

                    lookupStateToSequence.put(index1++, Lists.newArrayList(9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 12));

                    int nightStart = 12500;
                    int nightEnd = 23500;
                    int phases = 4;
                    int phaseSplit = (nightEnd - nightStart) / phases;

                    int timeOfDay = (int)(entityObj.world.getWorldTime() % 24000);
                    int nightTime = (timeOfDay - nightStart);

                    if (nightTime > phaseSplit * 3) {
                        amp = 1;
                    } else if (nightTime > phaseSplit * 2) {
                        amp = 2;
                    } else if (nightTime > phaseSplit * 1) {
                        amp = 3;
                    } else {
                        amp = 4;
                    }

                    if (entityObj.hitIndex2 >= lookupStateToSequence.get(entityObj.hitIndex).size()) {
                        entityObj.hitIndex2 = 0;
                        entityObj.hitIndex++;
                    }

                    if (entityObj.hitIndex >= lookupStateToSequence.size()) {
                        entityObj.hitIndex = 0;
                    }

                    rate = lookupStateToSequence.get(entityObj.hitIndex).get(entityObj.hitIndex2);

                    if (entityObj.hitDelay > 0) {
                        entityObj.hitDelay--;
                    }

                    boolean perEntDelay = false;

                    boolean hit = false;
                    if (perEntDelay) {
                        if (entityObj.hitDelay <= 0) {
                            entityObj.hitDelay = (amp * rate);
                            hit = true;
                        }
                    } else {
                        hit = entityObj.world.getTotalWorldTime() % (amp * rate) == 0;
                    }
                    //System.out.println(entityObj.world.getTotalWorldTime());

                    if (hit) {
                        //System.out.println("stage: " + entityObj.hitIndex + " - " + entityObj.hitIndex2);
                        entityObj.hitIndex2++;
                        IBlockState state = entityObj.world.getBlockState(blockposGoal);
                        if (state.getBlock() instanceof BlockBongoDrum) {
                            //((BlockBongoDrum) state.getBlock()).playBongoSound(entityObj.world, null, blockposGoal, state);
                            TropicraftBongos bongo = ((BlockBongoDrum) state.getBlock()).getVariant(state);
                            float pitch = (entityObj.world.rand.nextFloat() * 1F) + 0F;
                            entityObj.world.playSound(null, blockposGoal.getX(), blockposGoal.getY() + 0.5D, blockposGoal.getZ(),
                                    bongo.getSoundEvent(), SoundCategory.BLOCKS, 6.0F, pitch);
                            entityObj.swingArm(EnumHand.MAIN_HAND);
                        }
                    }

                    entityObj.syncBPM();

                }

                this.entityObj.getLookHelper().setLookPosition(blockposGoal.getX() + randXPos, blockposGoal.getY() + randYPos + 1D, blockposGoal.getZ() + randZPos,
                        8F, 8F);
            }

        } else {
            wasClose = false;
            entityObj.setSitting(false);
        }

        if (!isClose) {
            entityObj.setDancing(true);
            if ((this.entityObj.getNavigator().noPath() || walkingTimeout <= 0) && repathPentalty <= 0) {

                int i = blockposGoal.getX();
                int j = blockposGoal.getY();
                int k = blockposGoal.getZ();

                boolean success = false;

                if (this.entityObj.getDistanceSq(blockposGoal) > 256.0D) {
                    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3d((double) i + 0.5D, (double) j, (double) k + 0.5D));

                    if (vec3d != null) {
                        success = this.entityObj.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                    } else {
                        success = Util.tryMoveToXYZLongDist(this.entityObj, new BlockPos(i, j, k), 1);
                        //System.out.println("success? " + success);
                    }
                } else {
                    success = this.entityObj.getNavigator().tryMoveToXYZ((double) i + 0.5D, (double) j, (double) k + 0.5D, 1.0D);
                }

                if (!success) {
                    repathPentalty = 40;
                } else {
                    walkingTimeout = walkingTimeoutMax;
                }
            } else {
                if (walkingTimeout > 0) {
                    walkingTimeout--;
                } else {

                }
            }
        }

        if (repathPentalty > 0) {
            repathPentalty--;
        }

        if (lookUpdateTimer > 0) {
            lookUpdateTimer--;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        super.startExecuting();
        //this.insidePosX = -1;
        //reset any previous path so updateTask can start with a fresh path
        this.entityObj.getNavigator().clearPath();
        if (this.entityObj.listPosDrums.size() > 0) {
            assignedDrumIndex = entityObj.world.rand.nextInt(entityObj.listPosDrums.size());
        }
        //System.out.println("start party mode");
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        super.resetTask();
        entityObj.setSitting(false);
        walkingTimeout = 0;
        entityObj.setDancing(false);
        //System.out.println("reset party mode");
        /*this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;*/
    }

    public boolean isTooClose() {
        BlockPos blockposGoal = null;
        if (this.entityObj.posLastFireplaceFound != null) {
            //path to base of fire
            blockposGoal = this.entityObj.posLastFireplaceFound.add(0, -1, 0);
        } else {
            blockposGoal = this.entityObj.getHomePosition();
        }

        if (blockposGoal == null) {
            return false;
        }

        //prevent walking into the fire
        double dist = entityObj.getPositionVector().distanceTo(new Vec3d(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
        if (dist <= 1D) {
            return true;
        }
        return false;
    }
}