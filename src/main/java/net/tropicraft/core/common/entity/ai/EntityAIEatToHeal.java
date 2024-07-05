package net.tropicraft.core.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.EnumSet;

public class EntityAIEatToHeal extends Goal {
    private final EntityKoaBase entityObj;

    private final int walkingTimeoutMax = 20 * 10;

    private int walkingTimeout;
    private int repathPentalty = 0;

    private int lookUpdateTimer = 0;
    private final int randXPos = 0;
    private final int randYPos = 0;
    private final int randZPos = 0;

    private final float missingHealthToHeal = 5;

    public EntityAIEatToHeal(EntityKoaBase entityObjIn) {
        entityObj = entityObjIn;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
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
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (hasFoodSource(entityObj.inventory)) {
            consumeOneStackSizeOfFood(entityObj.inventory);
            entityObj.heal(5);
            entityObj.level().playSound(null, entityObj.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL, 1F, 1F);
            return;
        }

        if (hasFoodAtHome()) {
            boolean isClose = false;
            BlockPos blockposGoal = entityObj.getRestrictCenter();

            if (blockposGoal.equals(BlockPos.ZERO)) {
                stop();
                return;
            }

            //prevent walking into the fire
            double dist = entityObj.position().distanceTo(new Vec3(blockposGoal.getX(), blockposGoal.getY(), blockposGoal.getZ()));
            if (dist < 5D) {
                consumeOneStackSizeOfFoodAtHome();
                entityObj.heal(5);
                entityObj.level().playSound(null, entityObj.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL, 1F, 1F);
                return;
            }

            if (!isClose) {
                if ((entityObj.getNavigation().isDone() || walkingTimeout <= 0) && repathPentalty <= 0) {

                    int i = blockposGoal.getX();
                    int j = blockposGoal.getY();
                    int k = blockposGoal.getZ();

                    boolean success = false;

                    if (entityObj.distanceToSqr(Vec3.atCenterOf(blockposGoal)) > 256.0D) {
                        Vec3 Vector3d = DefaultRandomPos.getPosTowards(entityObj, 14, 3, new Vec3((double) i + 0.5D, j, (double) k + 0.5D), (float) Math.PI / 2F);

                        if (Vector3d != null) {
                            success = entityObj.getNavigation().moveTo(Vector3d.x, Vector3d.y, Vector3d.z, 1.0D);
                        }
                    } else {
                        success = entityObj.getNavigation().moveTo((double) i + 0.5D, (double) j, (double) k + 0.5D, 1.0D);
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
    public void start() {
        super.start();
        //this.insidePosX = -1;
        //reset any previous path so tick can start with a fresh path
        entityObj.getNavigation().stop();
    }

    /**
     * Resets the task
     */
    @Override
    public void stop() {
        super.stop();
        entityObj.setSitting(false);
        walkingTimeout = 0;
        /*this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;*/
    }

    public boolean isTooClose() {
        BlockPos blockposGoal = null;
        if (entityObj.posLastFireplaceFound != null) {
            //path to base of fire
            blockposGoal = entityObj.posLastFireplaceFound.offset(0, -1, 0);
        } else {
            blockposGoal = entityObj.getRestrictCenter();
        }

        if (blockposGoal.equals(BlockPos.ZERO)) {
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
        BlockPos blockposGoal = entityObj.getRestrictCenter();
        if (!blockposGoal.equals(BlockPos.ZERO)) {
            BlockEntity tile = entityObj.level().getBlockEntity(blockposGoal);
            if (tile instanceof ChestBlockEntity) {
                ChestBlockEntity chest = (ChestBlockEntity) tile;

                if (hasFoodSource(chest)) return true;
            }
        }
        return false;
    }

    public boolean hasFoodSource(Container inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (inv.getItem(i).has(DataComponents.FOOD)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack consumeOneStackSizeOfFoodAtHome() {
        BlockPos blockposGoal = entityObj.getRestrictCenter();
        if (!blockposGoal.equals(BlockPos.ZERO)) {
            BlockEntity tile = entityObj.level().getBlockEntity(blockposGoal);
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
                if (stack.has(DataComponents.FOOD)) {
                    ItemStack newStack = stack.split(1);
                    if (stack.getCount() <= 0) {
                        inv.setItem(i, ItemStack.EMPTY);
                    }
                    return newStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}


