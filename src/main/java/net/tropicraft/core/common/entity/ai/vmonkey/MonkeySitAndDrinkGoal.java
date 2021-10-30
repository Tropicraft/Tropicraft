package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionHand;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class MonkeySitAndDrinkGoal extends Goal {
    private static final int DEFAULT_WAIT = 40;

    private VMonkeyEntity entity;
    private int waitCounter;

    public MonkeySitAndDrinkGoal(VMonkeyEntity monkey) {
        this.entity = monkey;
        waitCounter = DEFAULT_WAIT;
        setFlags(EnumSet.of(Goal.Flag.LOOK, Flag.MOVE));
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        entity.setInSittingPose(false);
        entity.spawnAtLocation(new ItemStack(TropicraftItems.BAMBOO_MUG.get()));
        entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        waitCounter = DEFAULT_WAIT;
    }

    @Override
    public boolean canContinueToUse() {
        return entity.selfHoldingDrink(Drink.PINA_COLADA);
    }

    @Override
    public boolean canUse() {
        return entity.selfHoldingDrink(Drink.PINA_COLADA);
    }

    @Override
    public void start() {
        entity.setInSittingPose(true);
        entity.setAggressive(false);
        entity.setTarget(null);
        entity.setFollowing(null);
    }

    @Override
    public void tick() {
        waitCounter--;

        if (waitCounter <= 0) {
            entity.startUsingItem(InteractionHand.MAIN_HAND);
        }

        // If drinking complete
        ItemStack heldStack = entity.getMainHandItem();
        if (heldStack.getItem() == TropicraftItems.BAMBOO_MUG.get()) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 10 * 20, 2));
        }
    }
}
