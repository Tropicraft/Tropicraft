package net.tropicraft.core.common.entity.ai;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.EnumSet;
import java.util.Set;

public class EntityAITemptHelmet extends Goal {
    /**
     * The entity using this AI that is tempted by the player.
     */
    private final PathfinderMob temptedEntity;
    private final double speed;
    /**
     * X position of player tempting this mob
     */
    private double targetX;
    /**
     * Y position of player tempting this mob
     */
    private double targetY;
    /**
     * Z position of player tempting this mob
     */
    private double targetZ;
    /**
     * Tempting player's pitch
     */
    private double pitch;
    /**
     * Tempting player's yaw
     */
    private double yaw;
    /**
     * The player that is tempting the entity that is using this AI.
     */
    private Player temptingPlayer;
    /**
     * A counter that is decremented each time the shouldExecute method is called. The shouldExecute method will always
     * return false if delayTemptCounter is greater than 0.
     */
    private int delayTemptCounter;
    /**
     * True if this EntityAITempt task is running
     */
    private boolean isRunning;
    private final Set<ItemEntry<? extends Item>> temptItem;
    /**
     * Whether the entity using this AI will be scared by the tempter's sudden movement.
     */
    private final boolean scaredByPlayerMovement;

    public EntityAITemptHelmet(PathfinderMob temptedEntityIn, double speedIn, ItemEntry<? extends Item> temptItemIn, boolean scaredByPlayerMovementIn) {
        this(temptedEntityIn, speedIn, scaredByPlayerMovementIn, Set.of(temptItemIn));
    }

    public EntityAITemptHelmet(PathfinderMob temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, Set<ItemEntry<? extends Item>> temptItemIn) {
        temptedEntity = temptedEntityIn;
        speed = speedIn;
        temptItem = temptItemIn;
        scaredByPlayerMovement = scaredByPlayerMovementIn;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        if (!(temptedEntityIn.getNavigation() instanceof GroundPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {

        if (temptedEntity instanceof EntityKoaBase && ((EntityKoaBase) temptedEntity).druggedTime <= 0) {
            return false;
        }

        if (delayTemptCounter > 0) {
            --delayTemptCounter;
            return false;
        } else {
            temptingPlayer = temptedEntity.level().getNearestPlayer(temptedEntity, 10.0);

            if (temptingPlayer == null) {
                return false;
            } else {
                return isTempting(temptingPlayer.getInventory().armor.get(3));
                //return this.isTempting(this.temptingPlayer.getHeldItemMainhand()) || this.isTempting(this.temptingPlayer.getHeldItemOffhand());
            }
        }
    }

    protected boolean isTempting(ItemStack stack) {
        for (ItemEntry<? extends Item> items : temptItem) {
            if (items.isBound() && items.get().asItem() == stack.getItem()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        if (scaredByPlayerMovement) {
            if (temptedEntity.distanceToSqr(temptingPlayer) < 36.0) {
                if (temptingPlayer.distanceToSqr(targetX, targetY, targetZ) > 0.010000000000000002) {
                    return false;
                }

                if (Math.abs((double) temptingPlayer.getXRot() - pitch) > 5.0 || Math.abs((double) temptingPlayer.getYRot() - yaw) > 5.0) {
                    return false;
                }
            } else {
                targetX = temptingPlayer.getX();
                targetY = temptingPlayer.getY();
                targetZ = temptingPlayer.getZ();
            }

            pitch = temptingPlayer.getXRot();
            yaw = temptingPlayer.getYRot();
        }

        return canUse();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        targetX = temptingPlayer.getX();
        targetY = temptingPlayer.getY();
        targetZ = temptingPlayer.getZ();
        isRunning = true;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        temptingPlayer = null;
        temptedEntity.getNavigation().stop();
        delayTemptCounter = 100;
        isRunning = false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        temptedEntity.getLookControl().setLookAt(temptingPlayer, (float) (temptedEntity.getMaxHeadYRot() + 20), (float) temptedEntity.getMaxHeadXRot());

        if (temptedEntity.distanceToSqr(temptingPlayer) < 6.25) {
            temptedEntity.getNavigation().stop();
        } else {
            temptedEntity.getNavigation().moveTo(temptingPlayer, speed);
        }
    }

    /**
     * @see #isRunning
     */
    public boolean isRunning() {
        return isRunning;
    }
}


