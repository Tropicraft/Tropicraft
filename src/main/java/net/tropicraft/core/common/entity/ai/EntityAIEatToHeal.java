package net.tropicraft.core.common.entity.ai;

import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

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
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse()
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
    public boolean canContinueToUse()
    {
        return canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (hasFoodSource(entityObj.inventory)) {
            consumeOneStackSizeOfFood(entityObj.inventory);
            entityObj.heal(5);
            entityObj.level.playSound(null, entityObj.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL, 1F, 1F);
            return;
        }

        if (hasFoodAtHome()) {
            boolean isClose = false;
            BlockPos blockposGoal = this.entityObj.getRestrictCenter();

            if (blockposGoal == null) {
                stop();
                return;
            }

            //prevent walking into the fire
            double dist = entityObj.position().distanceTo(new Vec3(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
            if (dist < 5D) {
                consumeOneStackSizeOfFoodAtHome();
                entityObj.heal(5);
                entityObj.level.playSound(null, entityObj.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL, 1F, 1F);
                return;
            }

            if (!isClose) {
                if ((this.entityObj.getNavigation().isDone() || walkingTimeout <= 0) && repathPentalty <= 0) {

                    int i = blockposGoal.getX();
                    int j = blockposGoal.getY();
                    int k = blockposGoal.getZ();

                    boolean success = false;

                    if (this.entityObj.distanceToSqr(Vec3.atCenterOf(blockposGoal)) > 256.0D) {
                        Vec3 Vector3d = DefaultRandomPos.getPosAway(this.entityObj, 14, 3, new Vec3((double) i + 0.5D, (double) j, (double) k + 0.5D));

                        if (Vector3d != null) {
                            success = this.entityObj.getNavigation().moveTo(Vector3d.x, Vector3d.y, Vector3d.z, 1.0D);
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

        if (blockposGoal == null) {
            return false;
        }

        //prevent walking into the fire
        double dist = entityObj.position().distanceTo(new Vec3(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
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
        BlockPos blockposGoal = this.entityObj.getRestrictCenter();
        if (blockposGoal != null) {
            BlockEntity tile = entityObj.level.getBlockEntity(blockposGoal);
            if (tile instanceof ChestBlockEntity) {
                ChestBlockEntity chest = (ChestBlockEntity) tile;

                if (hasFoodSource(chest)) return true;
            }
        }
        return false;
    }

    public boolean hasFoodSource(Container inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem().isEdible()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack consumeOneStackSizeOfFoodAtHome() {
        BlockPos blockposGoal = this.entityObj.getRestrictCenter();
        if (blockposGoal != null) {
            BlockEntity tile = entityObj.level.getBlockEntity(blockposGoal);
            if (tile instanceof ChestBlockEntity) {
                ChestBlockEntity chest = (ChestBlockEntity) tile;

                return consumeOneStackSizeOfFood(chest);
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Return a snapshot of what its consuming incase we want to scale healing based on item/amount
     *
     * @param inv
     * @return
     */
    public ItemStack consumeOneStackSizeOfFood(Container inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem().isEdible()) {
                    stack.shrink(1);
                    if (stack.getCount() <= 0) {
                        inv.setItem(i, ItemStack.EMPTY);
                    }

                    //returning the state of the single ate item, though this return value doesnt seem to be used anywhere atm
                    ItemStack newStack = stack.copy();
                    newStack.setCount(1);
                    return newStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}


