package net.tropicraft.core.common.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Hand;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.item.ItemCocktail;

public class EntityAIMonkeyPickUpPinaColada extends Goal {

	private EntityVMonkey entity;
	private ItemEntity drinkEntity;
	private final double speedModifier;
    private final PathNavigator navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
	
	public EntityAIMonkeyPickUpPinaColada(EntityVMonkey monkey) {
        this.entity = monkey;
        this.setMutexBits(2);
        this.speedModifier = 1.0F;
        this.stopDistance = 1.0F;
        this.navigation = entity.getNavigator();
        this.drinkEntity = null;
        
        if (!(entity.getNavigator() instanceof GroundPathNavigator) && !(entity.getNavigator() instanceof FlyingPathNavigator))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
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
        return !entity.isTamed() && !selfHoldingDrink(Drink.pinaColada) && this.drinkEntity != null;
    }

    @Override
    public boolean shouldExecute() {
        return !entity.isTamed() && !selfHoldingDrink(Drink.pinaColada) && hasNearbyDrink(Drink.pinaColada) && this.drinkEntity != null;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.navigation.clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    private boolean hasNearbyDrink(Drink drink) {
    	ItemStack stack = MixerRecipes.getItemStack(drink);

        List<ItemEntity> list = this.entity.world.<ItemEntity>getEntitiesWithinAABB(ItemEntity.class, this.entity.getBoundingBox().grow(10.0D));

        if (!list.isEmpty()) {
            for (ItemEntity item : list) {
                if (!item.isInvisible()) {
                    if (item.getItem().isItemEqual(stack)) {
                    	this.drinkEntity = item;
                    	return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.drinkEntity != null && !this.entity.getLeashed()) {
            this.entity.getLookController().setLookPositionWithEntity(this.drinkEntity, 10.0F, (float)this.entity.getVerticalFaceSpeed());

            if (this.entity.getDistanceSq(this.drinkEntity) > (double)(this.stopDistance * this.stopDistance)) {
                if (--this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = 10;
                    double d0 = this.entity.posX - this.drinkEntity.posX;
                    double d1 = this.entity.posY - this.drinkEntity.posY;
                    double d2 = this.entity.posZ - this.drinkEntity.posZ;
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d3 > (double)(this.stopDistance * this.stopDistance)) {
                        this.navigation.tryMoveToEntityLiving(this.drinkEntity, this.speedModifier);
                    } else {
                        this.navigation.clearPath();

                        if (d3 <= (double)this.stopDistance) {
                            double d4 = this.drinkEntity.posX - this.entity.posX;
                            double d5 = this.drinkEntity.posZ - this.entity.posZ;
                            this.navigation.tryMoveToXYZ(this.entity.posX - d4, this.entity.posY, this.entity.posZ - d5, this.speedModifier);
                        }
                    }
                }
            } else {
            	this.entity.setHeldItem(Hand.MAIN_HAND, this.drinkEntity.getItem());
            	this.drinkEntity.remove();
            }
        }
    }

}


