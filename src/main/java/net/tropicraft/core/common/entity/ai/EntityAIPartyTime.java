package net.tropicraft.core.common.entity.ai;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.NoteBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.BlockBongoDrum;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.enums.TropicraftBongos;
import net.tropicraft.core.registry.SoundRegistry;

public class EntityAIPartyTime extends Goal
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
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {

        if ((!entityObj.getWantsToParty() && this.entityObj.druggedTime <= 0) || entityObj.listPosDrums.size() == 0) {
            return false;
        }

        BlockPos blockpos = new BlockPos(this.entityObj);

        if ((this.entityObj.druggedTime > 0 || !this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && !this.entityObj.world.provider.isNether()) {
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
        if ((this.entityObj.druggedTime > 0 || !this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && !this.entityObj.world.getBiome(blockpos).canRain()) && !this.entityObj.world.provider.isNether())
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
    public void tick() {
        super.tick();

        boolean isClose = false;

        BlockPos blockposGoal = null;
        if (this.entityObj.listPosDrums.size() > assignedDrumIndex) {
            blockposGoal = entityObj.listPosDrums.get(assignedDrumIndex);
        }

        if (entityObj.world.getGameTime() % 200 == 0){
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
                this.entityObj.getJumpController().setJumping();
                this.entityObj.rotationYaw = entityObj.world.rand.nextInt(360);
            } else {
                entityObj.setDancing(false);
                if (true || lookUpdateTimer <= 0) {

                    entityObj.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);

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

                    int timeOfDay = (int)(entityObj.world.getDayTime() % 24000);
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
                        hit = entityObj.world.getGameTime() % (amp * rate) == 0;
                    }
                    //System.out.println(entityObj.world.getGameTime());

                    if (hit) {
                        //System.out.println("stage: " + entityObj.hitIndex + " - " + entityObj.hitIndex2);
                        entityObj.hitIndex2++;
                        BlockState state = entityObj.world.getBlockState(blockposGoal);
                        if (state.getOwner() instanceof BlockBongoDrum) {
                            //((BlockBongoDrum) state.getOwner()).playBongoSound(entityObj.world, null, blockposGoal, state);
                            TropicraftBongos bongo = ((BlockBongoDrum) state.getOwner()).getVariant(state);
                            float pitch = (entityObj.world.rand.nextFloat() * 1F) + 0F;
                            entityObj.world.playSound(null, blockposGoal.getX(), blockposGoal.getY() + 0.5D, blockposGoal.getZ(),
                                    bongo.getSoundEvent(), SoundCategory.BLOCKS, 2.5F, pitch);
                            entityObj.swingArm(Hand.MAIN_HAND);
                        } else if (state.getOwner() instanceof NoteBlock) {
                            TileEntity tEnt = entityObj.world.getTileEntity(blockposGoal);
                            if (tEnt instanceof TileEntityNote) {
                                TileEntityNote note = (TileEntityNote) tEnt;
                                if (entityObj.world.rand.nextInt(10) == 0) {
                                    for (int i = 0; i < 1 + entityObj.world.rand.nextInt(4); i++) {
                                        note.changePitch();
                                    }
                                } else {
                                    note.triggerNote(entityObj.world, blockposGoal);
                                }
                                entityObj.swingArm(Hand.MAIN_HAND);
                            }
                        }
                    }

                    entityObj.syncBPM();

                }

                this.entityObj.getLookController().setLookPosition(blockposGoal.getX() + randXPos, blockposGoal.getY() + randYPos + 1D, blockposGoal.getZ() + randZPos,
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
        //reset any previous path so tick can start with a fresh path
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
            blockposGoal = this.entityObj.func_213384_dI();
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


