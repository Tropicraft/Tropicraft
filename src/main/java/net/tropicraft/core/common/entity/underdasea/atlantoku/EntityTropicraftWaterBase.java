package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.underdasea.EntityManOWar;

public abstract class EntityTropicraftWaterBase extends EntityWaterMob {
	
    private static final DataParameter<Float> SWIMYAW = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SWIMPITCH = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);


	public float swimPitch = 0f;
	public float swimYaw = 0f;
	public Vector2f targetVectorHeading;
	public Vector3f targetVector;
	
	
	public int outOfWaterTime = 0;

	public float fallVelocity = 0f;
	public float fallGravity = 0.0625f;

	public float prevSwimPitch = 0f;
	public float prevSwimYaw = 0f;

	
	private float swimSpeed = 1f;
	private float swimSpeedPanic = 2f;
	private float swimSpeedTurn = 5f;
	
	public boolean isPanicking = false;
	public boolean fleeFromPlayers = true;
	
	public boolean isAggressing = false;
	public boolean canAggress = false;
	
	public Entity aggressTarget = null;
	
	
	public void setSwimSpeeds(float s, float p, float t) {
		swimSpeed = s;
		swimSpeedPanic = p;
		swimSpeedTurn = t;
	}


	public EntityTropicraftWaterBase(World world) {
		super(world);
		this.experienceValue = 5;
		this.setSwimSpeeds(1f, 2f, 5f);
	}


	@Override
	public void entityInit() {
		this.getDataManager().register(SWIMYAW, Float.valueOf(0f));
		this.getDataManager().register(SWIMPITCH, Float.valueOf(0f));
		this.ticksExisted = rand.nextInt(12345);
		super.entityInit();
	}
	
	//@Override
	protected void resetHeight() {
		
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		// Client
		if (world.isRemote) {
			this.swimYaw = this.getDataManager().get(SWIMYAW);
			this.prevSwimYaw = this.swimYaw;

			this.swimPitch = this.getDataManager().get(SWIMPITCH);
			this.prevSwimPitch = this.swimPitch;

			return;
		}

		boolean jumping = false;

		if (this.isInWater()) {
			this.outOfWaterTime = 0;
			BlockPos bp = new BlockPos((int)posX, (int)posY-1, (int)posZ);
			
			if(!this.world.getBlockState(bp).getMaterial().isLiquid()) {
				if(this.swimPitch < 0f) {
					this.swimPitch = 0f;
				}
				
			}
			
			bp = new BlockPos((int)posX, (int)posY+1, (int)posZ);
			
			if(!this.world.getBlockState(bp).getMaterial().isSolid()) {
				if(this.swimPitch > 0f) {
					this.swimPitch = 0f;
				}
				
			}
			
			if(this.posX == this.prevPosX || this.posZ == this.prevPosZ) {
				int dist = 3;
				Vector3f randBlock = new Vector3f((float)(posX + (rand.nextBoolean() ? rand.nextInt(dist) : -rand.nextInt(dist))), 
						(float)(posY + (rand.nextBoolean() ? rand.nextInt(dist) : -rand.nextInt(dist))), 
						(float)(posZ + (rand.nextBoolean() ? rand.nextInt(dist) : -rand.nextInt(dist))));
				bp = new BlockPos((int)randBlock.x, (int)randBlock.y, (int)randBlock.z);

				if(this.world.getBlockState(bp).getMaterial().isLiquid()) 
				{
					//if(!world.getBlock((int)randBlock.x, (int)randBlock.y, (int)randBlock.z).getMaterial().isSolid())
					this.setTargetHeading(randBlock.x, randBlock.y, randBlock.z);
				//	System.out.println("SET IT");
				}
			}
			
			
				
		if(rand.nextInt(80) == 0) 
			{
				int dist = 15;
				Vector3f randBlock = new Vector3f((float)(posX + (rand.nextBoolean() ? rand.nextInt(dist) : -rand.nextInt(dist))), 
						(float)(posY + (rand.nextBoolean() ? rand.nextInt(dist) : -rand.nextInt(dist))), 
						(float)(posZ + (rand.nextBoolean() ? rand.nextInt(dist) : -rand.nextInt(dist))));
				bp = new BlockPos((int)randBlock.x, (int)randBlock.y, (int)randBlock.z);

				if(this.world.getBlockState(bp).getMaterial().isLiquid()) 
				{
					//if(!world.getBlock((int)randBlock.x, (int)randBlock.y, (int)randBlock.z).getMaterial().isSolid())
					this.setTargetHeading(randBlock.x, randBlock.y, randBlock.z);
				//	System.out.println("SET IT");
				}
				
				
				EntityPlayer closest = world.getClosestPlayerToEntity(this, 100f);
				if(closest != null && rand.nextInt(5) == 0) {
					if(closest.isInWater())
					this.setTargetHeading(closest.posX, closest.posY, closest.posZ);
	
				}
				
			}
		
		if(rand.nextInt(20) == 0) {
			//this.aggressTarget = null;
			if (this.canAggress && (aggressTarget == null || !world.getLoadedEntityList().contains(aggressTarget))) {
				List<Entity> list = world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(20D, 20D, 20D).offset(0.0D, -8.0D, 0.0D), EntitySelectors.IS_ALIVE);
				for (int i = 0; i < list.size(); i++) {
					Entity ent = list.get(i);
					if(ent.equals(this)) continue;
					if(ent.getClass().getName().equals(this.getClass().getName())) continue;
					//if(ent instanceof Ac) continue;

					if(!this.canEntityBeSeen(ent)) continue;
					if (ent instanceof EntityLivingBase){
						if (((EntityLivingBase)ent).isInWater()) {
							this.aggressTarget = ent;
						}
					}
				}
			}
		}
		
		if(rand.nextInt(200) == 0) {
			this.aggressTarget = null;
		}
	
		
		
		if(this.fleeFromPlayers) {
			EntityPlayer closest = world.getClosestPlayerToEntity(this, 2f);
			if(closest != null) {
				if(closest.isInWater()) 
					this.fleeEntity(closest);
					this.isPanicking = true;
			}else {
				this.isPanicking = false;
			}
		}else {
			this.isPanicking = false;
		}
			
			

		/*	if (this.world.isAirBlock((int) posX, (int) posY + 1, (int) posZ)) {
				if (this.swimPitch > 15f) {
					if (this.rand.nextInt(150) == 0) {
						this.swimPitch = 45f;
						this.swimSpeed = 15f;
						this.motionY = 15f;
					//	this.fallVelocity = -20f;
						jumping = true;
					}
				}
			}*/
		
		float swimSpeedTurn = this.swimSpeedTurn;
		
		if(this.aggressTarget != null) {
			if(this.getDistanceSqToEntity(this.aggressTarget) < 2f) {
				if(this.aggressTarget instanceof EntityLivingBase)
				((EntityLivingBase)this.aggressTarget).attackEntityFrom(DamageSource.cactus, 1);
				this.aggressTarget = null;
			}else {
				if(this.canEntityBeSeen(this.aggressTarget) && this.ticksExisted % 5 == 0) {
					this.setTargetHeading(this.aggressTarget.posX, this.aggressTarget.posY, this.aggressTarget.posZ);
					swimSpeedTurn = this.swimSpeedTurn*1.5f;
				}
			}

		}

			if (this.targetVectorHeading != null) {
				if (this.swimYaw < -this.targetVectorHeading.x) {
					this.swimYaw += swimSpeedTurn;
					if(this.swimYaw > -this.targetVectorHeading.x) {
						this.swimYaw = -this.targetVectorHeading.x;
					}
				} else {
					this.swimYaw -= swimSpeedTurn;
					if(this.swimYaw < -this.targetVectorHeading.x) {
						this.swimYaw = -this.targetVectorHeading.x;
					}
				}

				if (this.swimPitch < -this.targetVectorHeading.y) {
					this.swimPitch += swimSpeedTurn;
					if(this.swimPitch > -this.targetVectorHeading.y) {
						this.swimPitch = -this.targetVectorHeading.y;
					}
				} else {
					this.swimPitch -= swimSpeedTurn;
					if(this.swimPitch < -this.targetVectorHeading.y) {
						this.swimPitch = -this.targetVectorHeading.y;
					}
				}

				// this.swimYaw = -(float)Math.cos(theta);
				// this.swimPitch = -(float)Math.sin(theta);
			//	this.swimYaw = -this.targetVectorHeading.x;
			//	this.swimPitch = -this.targetVectorHeading.y;
			}

		}


		if (!this.isInWater()) {
			this.outOfWaterTime++;
			this.setTargetHeading(posX, posY-1, posZ);
			// this.swimSpeed = 0f;
		}

		if (this.ticksExisted % 500 < 150) {
			// this.swimSpeed = 1.5f;
		}

		// so we can play with bigger numbers B)
		float swimSpeed = this.swimSpeed / 10;
		if(this.isPanicking) {
			swimSpeed = this.swimSpeedPanic / 10;
		}
		if(this.aggressTarget != null) {
			swimSpeed = this.swimSpeedPanic / 5;
		}

		if(this.isInWater()) {
			motionX = swimSpeed * Math.sin(this.swimYaw * (Math.PI / 180.0));
			motionZ = swimSpeed * Math.cos(this.swimYaw * (Math.PI / 180.0));
			motionY = (swimSpeed) * Math.sin((this.swimPitch) * (Math.PI / 180.0));
		}

		if (this.swimPitch > 180f) {
			this.motionX = -this.motionX;
			this.motionZ = -this.motionZ;
		}

		if (!this.isInWater()) {

			this.motionY -= this.fallVelocity;
			this.fallVelocity += (this.fallGravity / 10);

			if (this.swimPitch > -90f)
				this.swimPitch -= this.fallVelocity * 60;

		} else {
			this.fallVelocity = 0f;
		}
		

		this.getDataManager().set(SWIMYAW, swimYaw);
		this.getDataManager().set(SWIMPITCH, swimPitch);

		prevSwimPitch = swimPitch;
		prevSwimYaw = swimYaw;

	}



	@Override
	public boolean isPushedByWater() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAIDisabled() {
		return true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.posY > 45.0D && this.posY < 63.0D && super.getCanSpawnHere();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
	}

	@Override
	public void applyEntityCollision(Entity entity) {
		super.applyEntityCollision(entity);

	}

	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return 1 + world.rand.nextInt(3);
	}

	@Override
	public void moveEntityWithHeading(float f, float f1) {
		move(motionX, motionY, motionZ);
	}



	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	public void setTargetHeading(double posX, double posY, double posZ) {
		double x = posX - this.posX;
		double y = posY - this.posY;
		double z = posZ - this.posZ;
		float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90F;
		float pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));

		if (this.targetVectorHeading == null) {
			this.targetVectorHeading = new Vector2f();
		}
		if (this.targetVector == null) {
			this.targetVector = new Vector3f();
		}
		targetVectorHeading.x = yaw;
		targetVectorHeading.y = pitch;

		targetVector.x = (float) posX;
		targetVector.y = (float) posY;
		targetVector.z = (float) posZ;
	}
	
	public void fleeEntity(Entity ent) {
		double x = ent.posX - this.posX;
		double y = ent.posY - this.posY;
		double z = ent.posZ - this.posZ;
		float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90F;
		float pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));

		if (this.targetVectorHeading == null) {
			this.targetVectorHeading = new Vector2f();
		}
		if (this.targetVector == null) {
			this.targetVector = new Vector3f();
		}
		targetVectorHeading.x = yaw+180;
		targetVectorHeading.y = -(pitch/2);

		targetVector.x = (float) ent.posX;
		targetVector.y = (float) ent.posY;
		targetVector.z = (float) ent.posZ;
	}

}