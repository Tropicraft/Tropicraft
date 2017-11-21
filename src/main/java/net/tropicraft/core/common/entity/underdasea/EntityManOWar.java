package net.tropicraft.core.common.entity.underdasea;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityManOWar extends EntityWaterMob {

	public float important1;
	protected float randomMotionSpeed;
	protected float important2;
	protected float randomMotionVecX;
	protected float randomMotionVecY;
	protected float randomMotionVecZ;
	private int attackTimer = 0;

	public EntityManOWar(World world){
		super(world);
		important1 = 0.0F;       
		randomMotionSpeed = 0.0F;
		important2 = 0.0F;
		randomMotionVecX = 0.0F;
		randomMotionVecY = 0.0F;
		randomMotionVecZ = 0.0F;
		important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
		setSize(0.6f, 0.8f);
		this.experienceValue = 7;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityManOWar.EntityMOWMoveTask(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	public byte getAttackStrength() {
		switch (world.getDifficulty()) {
		case EASY: return 1;
		case NORMAL: return 2;
		case HARD: return 3;
		default: return 0;
		}	
	}

	protected Entity getTarget() {	
		return null;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		if (attackTimer > 0) {
			attackTimer--;
		} else {
			this.setAttackTarget(null);
		}

		if (world.isRemote) return;
		
		if (inWater) {
			if (this.getAttackTarget() == null) {
				List<Entity> list = world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(2D, 4D, 2D).offset(0.0D, -2.0D, 0.0D), EntitySelectors.IS_ALIVE);
				for (int i = 0; i < list.size(); i++) {
					Entity ent = list.get(i);
					if (ent instanceof EntityLivingBase && !(ent instanceof EntityManOWar)){
						if (((EntityLivingBase)ent).isInWater()) {
							byte byte0 = getAttackStrength();
							((EntityLivingBase)ent).attackEntityFrom(DamageSource.drown, byte0);
							this.setAttackTarget(((EntityLivingBase)ent));
							attackTimer = 60;
							continue;
						}
					}
				}
			}
			important1 += important2;        
			if (important1 > 6.283185F) {
				important1 -= 6.283185F;
				if (rand.nextInt(10) == 0) {
					important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
				}
			}
			if (important1 < 3.141593F) {
				float f = important1 / 3.141593F;
				if ((double)f > 0.75D) {
					randomMotionSpeed = 1.0F;
				} 
			} else {
				randomMotionSpeed = randomMotionSpeed * 0.95F;
			}

			if(!world.isRemote) {
				motionX = randomMotionVecX * randomMotionSpeed;
				motionY = randomMotionVecY * randomMotionSpeed;
				motionZ = randomMotionVecZ * randomMotionSpeed;
			}

			renderYawOffset += ((-(float)Math.atan2(motionX, motionZ) * 180F) / 3.141593F - renderYawOffset) * 0.1F;
			rotationYaw = renderYawOffset;            
		} else {
			if(!world.isRemote) {
				motionX = 0.0D;               
				motionY *= 0.98000001907348633D;
				motionZ = 0.0D;
			}

			if (onGround && deathTime == 0) {      	
				this.attackEntityFrom(DamageSource.drown, 1);
				int d = 1;
				int e = 1;
				if(rand.nextInt(2) == 0) {
					d = -1;
				}
				if(rand.nextInt(2) == 0) {
					e = -1;
				}
				motionZ = rand.nextFloat()*.20F *d;
				motionX = rand.nextFloat()*.20F*e;
			}
			if(!inWater) {
				motionY -= 0.080000000000000002D;
			}
		}
	}

	public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
		this.randomMotionVecX = randomMotionVecXIn;
		this.randomMotionVecY = randomMotionVecYIn;
		this.randomMotionVecZ = randomMotionVecZIn;
	}

	public boolean hasMovementVector() {
		return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
	}

	@Override
	public void onDeath(DamageSource d) {
		super.onDeath(d);
		if (!this.world.isRemote) {
			int numDrops = 3 + this.rand.nextInt(1);

			for (int i = 0; i < numDrops; i++)
				this.dropItem(Items.SLIME_BALL, 1);
		}
	}

	@Override
	public boolean getCanSpawnHere() {
	    return super.getCanSpawnHere();
	}

	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SQUID_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_SQUID_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SQUID_DEATH;
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return 3 + world.rand.nextInt(3);
	}

	static class EntityMOWMoveTask extends EntityAIBase {
		private EntityManOWar mow;

		public EntityMOWMoveTask(EntityManOWar mow) {
			this.mow = mow;
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute() {
			return true;
		}

		/**
		 * Updates the task
		 */
		public void updateTask() {
			if(this.mow.getRNG().nextInt(150) == 0 || !this.mow.inWater ||
					this.mow.randomMotionVecX == 0.0F && this.mow.randomMotionVecY == 0.0F && this.mow.randomMotionVecZ == 0.0F) {
				float f = this.mow.getRNG().nextFloat() * 3.141593F * 2.0F;
				this.mow.randomMotionVecX = MathHelper.cos(f) * 0.025F;            
				this.mow.randomMotionVecZ = MathHelper.sin(f) * 0.025F;
			}

			if(this.mow.inWater){

				if(this.mow.posY < this.mow.world.getActualHeight() - 0.5){
					this.mow.randomMotionVecY = .05F;
				}
				if(this.mow.posY >= this.mow.world.getActualHeight() + 0.5){
					this.mow.randomMotionVecY = 0F;
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}
	}
}