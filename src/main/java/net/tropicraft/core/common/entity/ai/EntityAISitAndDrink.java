package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityAISitAndDrink extends Goal {

    private EntityVMonkey entity;
    private final int DEFAULT_WAIT = 40;
    private int waitCounter = DEFAULT_WAIT;

    public EntityAISitAndDrink(EntityVMonkey monkey)
    {
        this.entity = monkey;
        this.setMutexBits(2);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        this.entity.setSitting(false);
        this.entity.dropItem(ItemRegistry.bambooMug, 1);
        this.entity.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
        waitCounter = DEFAULT_WAIT;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.entity.selfHoldingDrink(Drink.pinaColada);
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.selfHoldingDrink(Drink.pinaColada);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.entity.setSitting(true);
        this.entity.setAngry(false);
        this.entity.setAttackTarget(null);
        this.entity.setFollowingEntity(null);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        waitCounter--;

        if (waitCounter <= 0) {
            this.entity.setActiveHand(Hand.MAIN_HAND);
        }

        // If drinking complete
        ItemStack heldStack = this.entity.getHeldItemMainhand();
        if (!heldStack.isEmpty() && heldStack.getItem() == ItemRegistry.bambooMug) {
            this.entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 10 * 20, 2));
        }
    }
}



