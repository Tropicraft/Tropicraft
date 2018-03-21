package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityAISitAndDrink extends EntityAIBase {

    private EntityVMonkey entity;
    private final int DEFAULT_WAIT = 40;
    private int waitCounter = DEFAULT_WAIT;

    public EntityAISitAndDrink(EntityVMonkey monkey)
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

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        this.entity.setSitting(false);
        this.entity.dropItem(ItemRegistry.bambooMug, 1);
        this.entity.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        waitCounter = DEFAULT_WAIT;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return selfHoldingDrink(Drink.pinaColada);
    }

    @Override
    public boolean shouldExecute() {
        return selfHoldingDrink(Drink.pinaColada);
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
    public void updateTask() {
        waitCounter--;

        if (waitCounter <= 0) {
            this.entity.setActiveHand(EnumHand.MAIN_HAND);
        }

        // If drinking complete
        ItemStack heldStack = this.entity.getHeldItemMainhand();
        if (!heldStack.isEmpty() && heldStack.getItem() == ItemRegistry.bambooMug) {
            this.entity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 10 * 20, 2));
        }
    }
}
