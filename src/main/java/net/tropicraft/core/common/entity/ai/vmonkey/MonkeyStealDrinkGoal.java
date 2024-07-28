package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.drinks.action.TropicraftDrinks;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.item.CocktailItem;

import java.util.EnumSet;

public class MonkeyStealDrinkGoal extends Goal {
    private final VMonkeyEntity entity;

    public MonkeyStealDrinkGoal(VMonkeyEntity monkey) {
        entity = monkey;
        setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return entity.getOwner() == null && VMonkeyEntity.FOLLOW_PREDICATE.test(entity.getFollowing()) && !entity.selfHoldingDrink(TropicraftDrinks.PINA_COLADA);
    }

    @Override
    public boolean canUse() {
        return entity.getOwner() == null && VMonkeyEntity.FOLLOW_PREDICATE.test(entity.getFollowing()) && !entity.selfHoldingDrink(TropicraftDrinks.PINA_COLADA) && entity.isAggressive();
    }

    private void leapTowardTarget() {
        LivingEntity leapTarget = entity.getTarget();

        if (leapTarget == null) return;

        double d0 = leapTarget.getX() - entity.getX();
        double d1 = leapTarget.getZ() - entity.getZ();
        float f = Mth.sqrt((float) (d0 * d0 + d1 * d1));
        Vec3 motion = entity.getDeltaMovement();

        if ((double) f >= 1.0E-4D) {
            entity.setDeltaMovement(motion.add(d0 / (double) f * 0.5 * 0.800000011920929 + motion.x * 0.20000000298023224, 0, d1 / (double) f * 0.5 * 0.800000011920929 + motion.z * 0.20000000298023224));
        }

        entity.setDeltaMovement(new Vec3(motion.x, 0.25, motion.z));
    }

    @Override
    public void tick() {
        if (entity.distanceToSqr(entity.getFollowing()) < 4.0f) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack heldItem = entity.getFollowing().getItemInHand(hand);
                if (CocktailItem.hasDrink(heldItem, TropicraftDrinks.PINA_COLADA)) {
                    leapTowardTarget();
                    entity.setItemInHand(hand, heldItem.split(heldItem.getCount()));
                    entity.getFollowing().setItemInHand(hand, ItemStack.EMPTY);
                }
            }
        }
    }
}
