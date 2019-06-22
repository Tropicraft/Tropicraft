package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class EntityAIEatToHeal extends Goal
{
    private final EntityKoaBase entityObj;

    private int walkingTimeoutMax = 20*10;

    private int walkingTimeout;
    private int repathPentalty = 0;

    private int lookUpdateTimer = 0;
    private int randXPos = 0;
    private int randYPos = 0;
    private int randZPos = 0;

    private float missingHealthToHeal = 5;

    public EntityAIEatToHeal(EntityKoaBase entityObjIn)
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
        if (entityObj.getHealth() < entityObj.getMaxHealth() - missingHealthToHeal) {
            return hasFoodSource();
        } else {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return shouldExecute();
    }

    @Override
    public void tick() {
        super.tick();

        if (hasFoodSource(entityObj.inventory)) {
            consumeOneStackSizeOfFood(entityObj.inventory);
            entityObj.heal(5);
            entityObj.world.playSound(null, entityObj.getPosition(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.NEUTRAL, 1F, 1F);
            return;
        }

        if (hasFoodAtHome()) {
            boolean isClose = false;
            BlockPos blockposGoal = this.entityObj.getHomePosition();

            if (blockposGoal == null) {
                resetTask();
                return;
            }

            //prevent walking into the fire
            double dist = entityObj.getPositionVector().distanceTo(new Vec3d(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
            if (dist < 5D) {
                consumeOneStackSizeOfFoodAtHome();
                entityObj.heal(5);
                entityObj.world.playSound(null, entityObj.getPosition(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.NEUTRAL, 1F, 1F);
                return;
            }

            if (!isClose) {
                if ((this.entityObj.getNavigator().noPath() || walkingTimeout <= 0) && repathPentalty <= 0) {

                    int i = blockposGoal.getX();
                    int j = blockposGoal.getY();
                    int k = blockposGoal.getZ();

                    boolean success = false;

                    if (this.entityObj.getDistanceSq(blockposGoal) > 256.0D) {
                        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3d((double) i + 0.5D, (double) j, (double) k + 0.5D));

                        if (vec3d != null) {
                            success = this.entityObj.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
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

    public boolean hasFoodSource() {

        if (hasFoodSource(entityObj.inventory)) return true;

        return hasFoodAtHome();
    }

    public boolean hasFoodAtHome() {
        BlockPos blockposGoal = this.entityObj.getHomePosition();
        if (blockposGoal != null) {
            TileEntity tile = entityObj.world.getTileEntity(blockposGoal);
            if (tile instanceof ChestTileEntity) {
                ChestTileEntity chest = (ChestTileEntity) tile;

                if (hasFoodSource(chest)) return true;
            }
        }
        return false;
    }

    public boolean hasFoodSource(IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!Util.isEmpty(stack) && stack.getCount() > 0) {
                if (stack.getItem() instanceof ItemFood) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack consumeOneStackSizeOfFoodAtHome() {
        BlockPos blockposGoal = this.entityObj.getHomePosition();
        if (blockposGoal != null) {
            TileEntity tile = entityObj.world.getTileEntity(blockposGoal);
            if (tile instanceof ChestTileEntity) {
                ChestTileEntity chest = (ChestTileEntity) tile;

                return consumeOneStackSizeOfFood(chest);
            }
        }
        return null;
    }

    /**
     * Return a snapshot of what its consuming incase we want to scale healing based on item/amount
     *
     * @param inv
     * @return
     */
    public ItemStack consumeOneStackSizeOfFood(IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!Util.isEmpty(stack) && stack.getCount() > 0) {
                if (stack.getItem() instanceof ItemFood) {
                    stack.shrink(1);
                    if (stack.getCount() <= 0) {
                        inv.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                    return new ItemStack(stack.getItem(), 1, stack.getMetadata());
                }
            }
        }
        return null;
    }
}

