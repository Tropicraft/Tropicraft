package net.tropicraft.core.common.entity.ai;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class EntityAIPartyTime extends Goal
{
    private EntityKoaBase entityObj;

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
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse()
    {

        if ((!entityObj.getWantsToParty() && this.entityObj.druggedTime <= 0) || entityObj.listPosDrums.size() == 0) {
            return false;
        }

        BlockPos blockpos = this.entityObj.blockPosition();

        if ((this.entityObj.druggedTime > 0 || !this.entityObj.level.isDay() || this.entityObj.level.isRaining() && this.entityObj.level.getBiome(blockpos).getPrecipitation() != Biome.Precipitation.RAIN)) {
            if (!isTooClose()) {
                if (entityObj.level.random.nextInt(20) == 0) {
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
    public boolean canContinueToUse()
    {
        BlockPos blockpos = this.entityObj.blockPosition();
        //return !this.entityObj.getNavigation().noPath();
        if ((this.entityObj.druggedTime > 0 || !this.entityObj.level.isDay() || this.entityObj.level.isRaining() && this.entityObj.level.getBiome(blockpos).getPrecipitation() != Biome.Precipitation.RAIN))
        {
            return !isTooClose();

        } else {
            return entityObj.level.random.nextInt(60) != 0;
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

        if (entityObj.level.getGameTime() % 200 == 0){
            if (this.entityObj.listPosDrums.size() > 0) {
                assignedDrumIndex = entityObj.level.random.nextInt(entityObj.listPosDrums.size());
            }
            //if (wasClose) {
                bangDrum = entityObj.level.random.nextBoolean();
            //}
        }

        if (blockposGoal == null) {
            stop();
            return;
        }

        //prevent walking onto source
        double dist = entityObj.position().distanceTo(new Vec3(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
        if (dist < 8D) {
            wasClose = true;
        }
        if (dist < 3D && entityObj.isOnGround()) {
            isClose = true;
            entityObj.getNavigation().stop();
            if (!bangDrum) {
                //entityObj.setSitting(true);
                entityObj.setDancing(true);
                this.entityObj.getJumpControl().jump();
                this.entityObj.setYRot(entityObj.level.random.nextInt(360));
            } else {
                entityObj.setDancing(false);
                if (true || lookUpdateTimer <= 0) {

                    entityObj.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);

                    //keep for testing, was neat sounding
                    int amp = 1;//entityObj.level.random.nextInt(10) + 1;
                    int rate = 4 + (entityObj.getId() % 7);

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

                    int timeOfDay = (int)(entityObj.level.getDayTime() % 24000);
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
                        hit = entityObj.level.getGameTime() % (amp * rate) == 0;
                    }
                    //System.out.println(entityObj.world.getGameTime());

                    if (hit) {
                        //System.out.println("stage: " + entityObj.hitIndex + " - " + entityObj.hitIndex2);
                        entityObj.hitIndex2++;
                        BlockState state = entityObj.level.getBlockState(blockposGoal);
                        //TODO: 1.14 readd
                        /*if (state.getBlock() instanceof BlockBongoDrum) {
                            //((BlockBongoDrum) state.getOwner()).playBongoSound(entityObj.world, null, blockposGoal, state);
                            TropicraftBongos bongo = ((BlockBongoDrum) state.getOwner()).getVariant(state);
                            float pitch = (entityObj.level.random.nextFloat() * 1F) + 0F;
                            entityObj.world.playSound(null, blockposGoal.getX(), blockposGoal.getY() + 0.5D, blockposGoal.getZ(),
                                    bongo.getSoundEvent(), SoundCategory.BLOCKS, 2.5F, pitch);
                            entityObj.swingArm(Hand.MAIN_HAND);
                        } else */
                        if (state.getBlock() instanceof NoteBlock) {
                            if (entityObj.level.random.nextInt(10) == 0) {
                                for (int i = 0; i < 1 + entityObj.level.random.nextInt(4); i++) {
                                    //note.changePitch();
                                    state.cycle(NoteBlock.NOTE).getValue(NoteBlock.NOTE);
                                }
                            } else {
                                //note.triggerNote(entityObj.world, blockposGoal);
                                state.getBlock().attack(state, entityObj.level, blockposGoal,
                                        FakePlayerFactory.get((ServerLevel) entityObj.level,
                                                new GameProfile(UUID.fromString(" e517cf6a-ce31-4ac8-b48d-44b4f0f918a7"), "tropicraftKoa")));
                            }
                            entityObj.swing(InteractionHand.MAIN_HAND);

                        }
                    }

                    entityObj.syncBPM();

                }

                this.entityObj.getLookControl().setLookAt(blockposGoal.getX() + randXPos, blockposGoal.getY() + randYPos + 1D, blockposGoal.getZ() + randZPos,
                        8F, 8F);
            }


        } else {
            wasClose = false;
            entityObj.setSitting(false);
        }

        if (!isClose) {
            entityObj.setDancing(true);
            if ((this.entityObj.getNavigation().isDone() || walkingTimeout <= 0) && repathPentalty <= 0) {

                int i = blockposGoal.getX();
                int j = blockposGoal.getY();
                int k = blockposGoal.getZ();

                boolean success = false;

                if (this.entityObj.distanceToSqr(Vec3.atCenterOf(blockposGoal)) > 256.0) {
                    Vec3 Vector3d = DefaultRandomPos.getPosAway(this.entityObj, 14, 3, new Vec3((double) i + 0.5D, (double) j, (double) k + 0.5D));

                    if (Vector3d != null) {
                        success = this.entityObj.getNavigation().moveTo(Vector3d.x, Vector3d.y, Vector3d.z, 1.0D);
                    } else {
                        success = Util.tryMoveToXYZLongDist(this.entityObj, new BlockPos(i, j, k), 1);
                        //System.out.println("success? " + success);
                    }
                } else {
                    success = this.entityObj.getNavigation().moveTo((double) i + 0.5D, (double) j, (double) k + 0.5D, 1.0D);
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
    public void start()
    {
        super.start();
        //this.insidePosX = -1;
        //reset any previous path so tick can start with a fresh path
        this.entityObj.getNavigation().stop();
        if (this.entityObj.listPosDrums.size() > 0) {
            assignedDrumIndex = entityObj.level.random.nextInt(entityObj.listPosDrums.size());
        }
        //System.out.println("start party mode");
    }

    /**
     * Resets the task
     */
    @Override
    public void stop()
    {
        super.stop();
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
            blockposGoal = this.entityObj.posLastFireplaceFound.offset(0, -1, 0);
        } else {
            blockposGoal = this.entityObj.getRestrictCenter();
        }

        if (blockposGoal == null || blockposGoal == BlockPos.ZERO) {
            return false;
        }

        //prevent walking into the fire
        return entityObj.position().closerThan(new Vec3(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()), 1.0);
    }
}


