package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.item.armor.ItemAshenMask;

public class EntityAIMeleeAndRangedAttack extends EntityAIBase
{
	/** The entity the AI instance has been applied to */
	private final EntityLiving entityHost;
	/** The entity (as a RangedAttackMob) the AI instance has been applied to. */
	private final IRangedAttackMob rangedAttackEntityHost;
	private EntityLivingBase attackTarget;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private double entityMoveSpeed;
	private int field_75318_f;
	private int maxMeleeAttackTime;
	/** The maximum time the AI has to wait before peforming another ranged attack. */
	private int maxRangedAttackTime;
	private float shootCutoffRange;
	private float shootCutoffRangeSqr;
	private float meleeHitRange = 2F;
	private static final String __OBFID = "CL_00001609";

	public EntityAIMeleeAndRangedAttack(IRangedAttackMob p_i1649_1_, double p_i1649_2_, int maxMeleeAttackTime, int maxRangedAttackTime, float p_i1649_5_)
	{
		this(p_i1649_1_, p_i1649_2_, maxMeleeAttackTime, maxRangedAttackTime, p_i1649_5_, 2F);
	}

	public EntityAIMeleeAndRangedAttack(IRangedAttackMob p_i1650_1_, double p_i1650_2_, int maxMeleeAttackTime, int maxRangedAttackTime, float p_i1650_6_, float meleeHitRange)
	{
		this.rangedAttackTime = -1;

		if (!(p_i1650_1_ instanceof EntityLivingBase))
		{
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}
		else
		{
			this.rangedAttackEntityHost = p_i1650_1_;
			this.entityHost = (EntityLiving)p_i1650_1_;
			this.entityMoveSpeed = p_i1650_2_;
			this.maxMeleeAttackTime = maxMeleeAttackTime;
			this.maxRangedAttackTime = maxRangedAttackTime;
			this.shootCutoffRange = p_i1650_6_;
			this.shootCutoffRangeSqr = p_i1650_6_ * p_i1650_6_;
			this.meleeHitRange = meleeHitRange;
			this.setMutexBits(3);
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
    public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();

		if (entitylivingbase == null)
		{
			return false;
		}
		else
		{
			this.attackTarget = entitylivingbase;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
    public boolean shouldContinueExecuting()
	{
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
    public void resetTask()
	{
		this.attackTarget = null;
		this.field_75318_f = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Updates the task
	 */
	@Override
    public void updateTask()
	{
	    if (this.attackTarget != null) {
	        ItemStack headGear = this.attackTarget.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
	        if (headGear != null && headGear.getItem() != null) {
	            if (headGear.getItem() instanceof ItemAshenMask)
	                return;
	        }
	    }
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
		boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

		if (flag)
		{
			++this.field_75318_f;
		}
		else
		{
			this.field_75318_f = 0;
		}

		if (d0 <= (double)this.shootCutoffRangeSqr && this.field_75318_f >= 20)
		{
			//this.entityHost.getNavigator().clearPathEntity();
		}
		else
		{
			//this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		if (this.field_75318_f >= 20) {
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		float f;

		//System.out.println(rangedAttackTime);

		if (--this.rangedAttackTime <= 0)
		{
			/*if (d0 > (double)this.shootCutoffRangeSqr || !flag)
            {
                return;
            }*/

			f = MathHelper.sqrt(d0) / this.shootCutoffRange;
			float f1 = f;

			if (f < 0.1F)
			{
				f1 = 0.1F;
			}

			if (f1 > 1.0F)
			{
				f1 = 1.0F;
			}

			if (d0 >= (double)this.shootCutoffRange * (double)this.shootCutoffRange) {
				this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
				this.rangedAttackTime = maxRangedAttackTime;
			} else if (d0 <= meleeHitRange * meleeHitRange) {
				this.entityHost.attackEntityAsMob(attackTarget);
				this.entityHost.swingArm(EnumHand.MAIN_HAND);
				rangedAttackTime = maxMeleeAttackTime;
			}
		}
	}
}