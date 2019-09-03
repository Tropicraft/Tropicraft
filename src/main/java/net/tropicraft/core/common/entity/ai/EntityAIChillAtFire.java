package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.registry.ItemRegistry;

import java.util.EnumSet;

public class EntityAIChillAtFire extends Goal
{
    private final EntityKoaBase entityObj;

    private int walkingTimeoutMax = 20*10;

    private int walkingTimeout;
    private int repathPentalty = 0;

    private int lookUpdateTimer = 0;
    private int randXPos = 0;
    private int randYPos = 0;
    private int randZPos = 0;

    public EntityAIChillAtFire(EntityKoaBase entityObjIn)
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

        if ((entityObj.getWantsToParty() || this.entityObj.druggedTime > 0) && entityObj.listPosDrums.size() > 0) {
            return false;
        }

        BlockPos blockpos = new BlockPos(this.entityObj);

        if (!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && this.entityObj.world.getBiome(blockpos).getPrecipitation() != Biome.RainType.RAIN) {
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

        if ((entityObj.getWantsToParty() || this.entityObj.druggedTime > 0) && entityObj.listPosDrums.size() > 0) {
            return false;
        }

        BlockPos blockpos = new BlockPos(this.entityObj);
        //return !this.entityObj.getNavigator().noPath();
        if (!this.entityObj.world.isDaytime() || this.entityObj.world.isRaining() && this.entityObj.world.getBiome(blockpos).getPrecipitation() != Biome.RainType.RAIN)
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
        if (this.entityObj.posLastFireplaceFound != null) {
            //path to base of fire
            blockposGoal = this.entityObj.posLastFireplaceFound.add(0, -1, 0);
        } else {
            blockposGoal = this.entityObj.getHomePosition();
        }

        if (blockposGoal == null) {
            resetTask();
            return;
        }

        //prevent walking into the fire
        double dist = entityObj.getPositionVector().distanceTo(new Vec3d(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
        if (dist < 4D && entityObj.onGround) {
            entityObj.setSitting(true);
            entityObj.getNavigator().clearPath();
            isClose = true;
            if (lookUpdateTimer <= 0) {
                lookUpdateTimer = 200 + entityObj.world.rand.nextInt(100);
                int range = 2;
                randXPos = entityObj.world.rand.nextInt(range) - entityObj.world.rand.nextInt(range);
                //stargaze
                if (entityObj.world.rand.nextInt(3) == 0) {
                    randYPos = 5+entityObj.world.rand.nextInt(5);
                } else {
                    randYPos = 0;
                }
                randZPos = entityObj.world.rand.nextInt(range) - entityObj.world.rand.nextInt(range);

                if (entityObj.getEntityId() % 3 == 0) {
                    entityObj.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TropicraftItems.BAMBOO_MUG));
                } else if (entityObj.getEntityId() % 5 == 0) {
                    entityObj.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ItemRegistry.cookedFrogLeg));
                } else {
                    entityObj.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TropicraftItems.ORANGE));
                }

                entityObj.heal(1);

            }
            this.entityObj.getLookController().setLookPosition(blockposGoal.getX() + randXPos, blockposGoal.getY() + randYPos + 1D, blockposGoal.getZ() + randZPos,
                    8F, 8F);
        } else {
            entityObj.setSitting(false);
        }

        if (!isClose) {
            if ((this.entityObj.getNavigator().noPath() || walkingTimeout <= 0) && repathPentalty <= 0) {

                int i = blockposGoal.getX();
                int j = blockposGoal.getY();
                int k = blockposGoal.getZ();

                boolean success = false;

                if (this.entityObj.getDistanceSq(new Vec3d(blockposGoal)) > 256.0D) {
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
        if (dist <= 3D) {
            return true;
        }
        return false;
    }
}


