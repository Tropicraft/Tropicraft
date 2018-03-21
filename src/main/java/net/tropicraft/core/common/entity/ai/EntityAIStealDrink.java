package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipe;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.item.ItemCocktail;

public class EntityAIStealDrink extends EntityAIBase {
    private EntityVMonkey entity;

    public EntityAIStealDrink(EntityVMonkey monkey)
    {
        this.entity = monkey;
        this.setMutexBits(2);
    }

    private boolean selfHoldingDrink(Drink drink) {
        ItemStack heldItem = entity.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemCocktail) {
            return ItemCocktail.getDrink(heldItem) == drink;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entity.followingHoldingPinaColada() && !selfHoldingDrink(Drink.pinaColada);
    }

    @Override
    public boolean shouldExecute() {
        return entity.followingHoldingPinaColada() && !selfHoldingDrink(Drink.pinaColada) && entity.isAngry();
    }

    private void leapTowardTarget() {
        EntityLivingBase leapTarget = entity.getAttackTarget();

        if (leapTarget == null) return;

        double d0 = leapTarget.posX - this.entity.posX;
        double d1 = leapTarget.posZ - this.entity.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

        if ((double)f >= 1.0E-4D)
        {
            this.entity.motionX += d0 / (double)f * 0.5D * 0.800000011920929D + this.entity.motionX * 0.20000000298023224D;
            this.entity.motionZ += d1 / (double)f * 0.5D * 0.800000011920929D + this.entity.motionZ * 0.20000000298023224D;
        }

        // leap momentum
        this.entity.motionY = 0.25D;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {

    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        if (this.entity.getDistanceSq(this.entity.getFollowingEntity()) < 4.0F) {
            System.out.println("Stealing");
            // if main hand
            if (!entity.getFollowingEntity().getHeldItemMainhand().isEmpty() && entity.getFollowingEntity().getHeldItemMainhand().getItem() instanceof ItemCocktail) {
                if (ItemCocktail.getDrink(entity.getFollowingEntity().getHeldItemMainhand()) == Drink.pinaColada) {
                    leapTowardTarget();
                    entity.getFollowingEntity().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                    entity.setHeldItem(EnumHand.MAIN_HAND, MixerRecipes.getItemStack(Drink.pinaColada));
                }
            }
        }
    }
}
