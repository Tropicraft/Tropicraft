package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import net.tropicraft.core.common.item.CocktailItem;

import java.util.EnumSet;

public class MonkeyStealDrinkGoal extends Goal {
    private VMonkeyEntity entity;

    public MonkeyStealDrinkGoal(VMonkeyEntity monkey) {
        this.entity = monkey;
        setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entity.getOwner() == null && VMonkeyEntity.FOLLOW_PREDICATE.test(entity.getFollowing()) && !entity.selfHoldingDrink(Drink.PINA_COLADA);
    }

    @Override
    public boolean shouldExecute() {
        return entity.getOwner() == null && VMonkeyEntity.FOLLOW_PREDICATE.test(entity.getFollowing()) && !entity.selfHoldingDrink(Drink.PINA_COLADA) && entity.isAggressive();
    }

    private void leapTowardTarget() {
        LivingEntity leapTarget = entity.getAttackTarget();

        if (leapTarget == null) return;

        double d0 = leapTarget.getPosX() - entity.getPosX();
        double d1 = leapTarget.getPosZ() - entity.getPosZ();
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
        final Vec3d motion = entity.getMotion();

        if ((double)f >= 1.0E-4D) {
            entity.setMotion(motion.add(d0 / (double)f * 0.5D * 0.800000011920929D + motion.x * 0.20000000298023224D, 0, d1 / (double)f * 0.5D * 0.800000011920929D + motion.z * 0.20000000298023224D));
        }

        entity.setMotion(new Vec3d(motion.x, 0.25, motion.z));
    }

    @Override
    public void tick() {
        if (entity.getDistanceSq(entity.getFollowing()) < 4.0F) {
            for (final Hand hand : Hand.values()) {
                if (CocktailItem.getDrink(entity.getFollowing().getHeldItem(hand)) == Drink.PINA_COLADA) {
                    leapTowardTarget();
                    entity.getFollowing().setHeldItem(hand, ItemStack.EMPTY);
                    entity.setHeldItem(hand, MixerRecipes.getItemStack(Drink.PINA_COLADA));
                }
            }
        }
    }
}
