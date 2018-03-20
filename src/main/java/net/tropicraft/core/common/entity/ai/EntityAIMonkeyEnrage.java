package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.item.ItemCocktail;

public class EntityAIMonkeyEnrage  extends EntityAIBase {

    private EntityVMonkey entity;

    public EntityAIMonkeyEnrage(EntityVMonkey monkey)
    {
        this.entity = monkey;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entity.followingHoldingPinaColada();
    }

    @Override
    public boolean shouldExecute() {
        return entity.getState() == EntityVMonkey.STATE_ANGRY;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        entity.setAttackTarget(null);
        entity.setState(EntityVMonkey.STATE_FOLLOWING);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        System.out.println("angr");
        if (entity.getState() == EntityVMonkey.STATE_ANGRY) {
           // entity.setAttackTarget(entity.getFollowingEntity());
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
      //  entity.setAttackTarget(entity.getFollowingEntity());
    }
}
