package net.tropicraft.core.common.entity.ai;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.item.ItemCocktail;

public class EntityAIMonkeyFollowNearestWithCondition extends EntityAIBase
{
    private final EntityVMonkey entity;
    private final Predicate<EntityLivingBase> followPredicate;
    private final double speedModifier;
    private final PathNavigate navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    private final float areaSize;
    private int followCounter = 0;

    public EntityAIMonkeyFollowNearestWithCondition(final EntityVMonkey entity, double speedModifier, float stopDistance, float areaSize, Predicate<EntityLivingBase> predicate)
    {
        this.entity = entity;
        this.followPredicate = predicate;
        this.speedModifier = speedModifier;
        this.navigation = entity.getNavigator();
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
        this.setMutexBits(3);

        if (!(entity.getNavigator() instanceof PathNavigateGround) && !(entity.getNavigator() instanceof PathNavigateFlying))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List<EntityPlayer> list = this.entity.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, this.entity.getEntityBoundingBox().grow((double)this.areaSize), this.followPredicate);

        if (!list.isEmpty())
        {
            for (EntityPlayer entityliving : list)
            {
                if (!entityliving.isInvisible())
                {
                    this.entity.setFollowingEntity(entityliving);
                    this.entity.setState(EntityVMonkey.STATE_FOLLOWING);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.entity.getFollowingEntity() != null &&
                !this.navigation.noPath() &&
                this.entity.getDistanceSq(this.entity.getFollowingEntity()) > (double)(this.stopDistance * this.stopDistance) &&
                this.entity.followingHoldingPinaColada();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
        this.entity.setState(EntityVMonkey.STATE_FOLLOWING);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.entity.setFollowingEntity(null);
        this.navigation.clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
        this.followCounter = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        System.out.println("following");
        if (this.entity.getFollowingEntity() != null && !this.entity.getLeashed())
        {
            followCounter++;

            if (followCounter >= 20) {
                this.entity.setState(EntityVMonkey.STATE_ANGRY);
            }

            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getFollowingEntity(), 10.0F, (float)this.entity.getVerticalFaceSpeed());

            if (--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = 10;
                double d0 = this.entity.posX - this.entity.getFollowingEntity().posX;
                double d1 = this.entity.posY - this.entity.getFollowingEntity().posY;
                double d2 = this.entity.posZ - this.entity.getFollowingEntity().posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > (double)(this.stopDistance * this.stopDistance))
                {
                    this.navigation.tryMoveToEntityLiving(this.entity.getFollowingEntity(), this.speedModifier);
                }
                else
                {
                    this.navigation.clearPath();
                    //EntityLookHelper entitylookhelper = this.followingEntity.getLookHelper();

                    if (d3 <= (double)this.stopDistance)
                    {
                        double d4 = this.entity.getFollowingEntity().posX - this.entity.posX;
                        double d5 = this.entity.getFollowingEntity().posZ - this.entity.posZ;
                        this.navigation.tryMoveToXYZ(this.entity.posX - d4, this.entity.posY, this.entity.posZ - d5, this.speedModifier);
                    }
                }
            }
        }
    }
}