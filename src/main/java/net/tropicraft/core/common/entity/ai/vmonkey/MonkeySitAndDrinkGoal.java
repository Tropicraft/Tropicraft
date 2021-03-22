package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.EnumSet;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class MonkeySitAndDrinkGoal extends Goal {
    private static final int DEFAULT_WAIT = 40;

    private VMonkeyEntity entity;
    private int waitCounter;

    public MonkeySitAndDrinkGoal(VMonkeyEntity monkey) {
        this.entity = monkey;
        waitCounter = DEFAULT_WAIT;
        setMutexFlags(EnumSet.of(Goal.Flag.LOOK, Flag.MOVE));
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        entity.setQueuedToSit(false);
        entity.entityDropItem(new ItemStack(TropicraftItems.BAMBOO_MUG.get()));
        entity.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
        waitCounter = DEFAULT_WAIT;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entity.selfHoldingDrink(Drink.PINA_COLADA);
    }

    @Override
    public boolean shouldExecute() {
        return entity.selfHoldingDrink(Drink.PINA_COLADA);
    }

    @Override
    public void startExecuting() {
        entity.setQueuedToSit(true);
        entity.setAggroed(false);
        entity.setAttackTarget(null);
        entity.setFollowing(null);
    }

    @Override
    public void tick() {
        waitCounter--;

        if (waitCounter <= 0) {
            entity.setActiveHand(Hand.MAIN_HAND);
        }

        // If drinking complete
        ItemStack heldStack = entity.getHeldItemMainhand();
        if (heldStack.getItem() == TropicraftItems.BAMBOO_MUG.get()) {
            entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 10 * 20, 2));
        }
    }
}
