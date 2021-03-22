package net.tropicraft.core.common.entity.ai.vmonkey;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Hand;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class MonkeyPickUpPinaColadaGoal extends Goal {

    private VMonkeyEntity entity;
    private ItemEntity drinkEntity;
    private final double speedModifier;
    private final PathNavigator navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    
    public MonkeyPickUpPinaColadaGoal(VMonkeyEntity monkey) {
        entity = monkey;
        setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        speedModifier = 1.0F;
        stopDistance = 1.0F;
        navigation = entity.getNavigator();
        drinkEntity = null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !entity.isTamed() && !entity.selfHoldingDrink(Drink.PINA_COLADA) && drinkEntity != null;
    }

    @Override
    public boolean shouldExecute() {
        // Add some variablity / throttling
        if (entity.getRNG().nextInt(20) != 0) {
            return false;
        }
        return !entity.isTamed() && !entity.selfHoldingDrink(Drink.PINA_COLADA) && hasNearbyDrink(Drink.PINA_COLADA) && drinkEntity != null;
    }

    @Override
    public void resetTask() {
        navigation.clearPath();
        entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void startExecuting() {
        timeToRecalcPath = 0;
        oldWaterCost = entity.getPathPriority(PathNodeType.WATER);
        entity.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    private boolean hasNearbyDrink(final Drink drink) {
        ItemStack stack = MixerRecipes.getItemStack(drink);

        List<ItemEntity> list = entity.world.getEntitiesWithinAABB(ItemEntity.class, entity.getBoundingBox().grow(10.0D));
        
        if (!list.isEmpty()) {
            for (ItemEntity item : list) {
                if (!item.isInvisible()) {
                    if (item.getItem().isItemEqual(stack) && item.isAlive()) {
                        drinkEntity = item;
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
    @Override
    public void tick() {
        if (drinkEntity != null && !entity.getLeashed()) {
            entity.getLookController().setLookPositionWithEntity(drinkEntity, 10.0F, (float) entity.getVerticalFaceSpeed());

            if (!drinkEntity.isAlive()) {
                drinkEntity = null;
                entity.setMadAboutStolenAlcohol(true);
                return;
            }

            if (entity.getDistanceSq(drinkEntity) > (double)(stopDistance * stopDistance)) {
                if (--timeToRecalcPath <= 0) {
                    timeToRecalcPath = 10;
                    double d0 = entity.getPosX() - drinkEntity.getPosX();
                    double d1 = entity.getPosY() - drinkEntity.getPosY();
                    double d2 = entity.getPosZ() - drinkEntity.getPosZ();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d3 > (double)(stopDistance * stopDistance)) {
                        navigation.tryMoveToEntityLiving(drinkEntity, speedModifier);
                    } else {
                        navigation.clearPath();

                        if (d3 <= (double)stopDistance) {
                            double d4 = drinkEntity.getPosX() - entity.getPosX();
                            double d5 = drinkEntity.getPosZ() - entity.getPosZ();
                            navigation.tryMoveToXYZ(entity.getPosX() - d4, entity.getPosY(), entity.getPosZ() - d5, speedModifier);
                        }
                    }
                }
            } else {
                entity.setHeldItem(Hand.MAIN_HAND, drinkEntity.getItem());
                drinkEntity.remove();
            }
        }
    }

}
