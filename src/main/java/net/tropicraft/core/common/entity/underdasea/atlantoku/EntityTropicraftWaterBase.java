package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;

import javax.vecmath.Vector2f;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class EntityTropicraftWaterBase extends EntityWaterMob {
	
    private static final DataParameter<Float> SWIMYAW = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SWIMPITCH = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SWIMSPEEDCUR = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);
    private static final DataParameter<String> TEXTURE = EntityDataManager.<String>createKey(EntityTropicraftWaterBase.class, DataSerializers.STRING);
	private static final DataParameter<Boolean> IS_LEADER = EntityDataManager.<Boolean>createKey(EntityTropicraftWaterBase.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> HOOK_ID = EntityDataManager.<Integer>createKey(EntityTropicraftWaterBase.class, DataSerializers.VARINT);

	
	public float swimPitch = 0f;
	public float swimYaw = 0f;
	public Vector2f targetVectorHeading;
	public Vec3d targetVector;
	
	public int outOfWaterTime = 0;
	public float outOfWaterAngle = 0f;
	
	public float fallVelocity = 0f;
	public float fallGravity = 0.0625f;

	public float prevSwimPitch = 0f;
	public float prevSwimYaw = 0f;

	private float swimAccelRate = 0.02f;
	private float swimDecelRate = 0.02f;
	
	private float swimSpeedDefault = 1f;
	protected float swimSpeedCurrent = 0f;
	private float swimSpeedPanic = 2f;
	
	private float swimSpeedTurn = 5f;
	
	public boolean isPanicking = false;
	public boolean fleeFromPlayers = false;
	public boolean approachPlayers = false;
	public double fleeDistance = 2D;
	
	public boolean isAggressing = false;
	public boolean canAggress = false;
	
	public int eatenFishAmount = 0;
	public int maximumEatAmount = 5;
	private float swimSpeedChasing = 2f;
	private float swimSpeedCharging = 2.5f;
	
	public Entity aggressTarget = null;
	public EntityHook hookTarget = null;
	
	private boolean fishable = false;
	
	
	private ItemStack dropStack = null;
	private int dropMaxAmt = 3;
	private float maxHealth = 10f;
	private float attackDamage = 1f;
	
	private boolean isMovingAwayFromWall = false;
	
	public EntityTropicraftWaterBase(World world) {
		super(world);
		this.experienceValue = 5;
		this.setSwimSpeeds(1f, 2f, 5f);
		if(this instanceof IPredatorDiet) {
			this.setHostile();
		}
		this.setHealth(maxHealth);		
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.getDataManager().register(SWIMYAW, Float.valueOf(0f));
		this.getDataManager().register(SWIMPITCH, Float.valueOf(0f));
		this.getDataManager().register(SWIMSPEEDCUR, Float.valueOf(0f));
		this.getDataManager().register(TEXTURE, "");
		this.getDataManager().register(IS_LEADER, false);
		this.getDataManager().register(HOOK_ID, Integer.valueOf(0));
		this.assignRandomTexture();
	}

	@Override
	public void onLivingUpdate() {
		if(this.ticksExisted == 0) {
			this.applyEntityAttributes();
			this.setHealth(maxHealth);
		}
		super.onLivingUpdate();
		
		//setDead();
		if(this instanceof IPredatorDiet && world.getDifficulty().equals(EnumDifficulty.PEACEFUL)) {
			boolean hasPlayer = false;
			Class[] prey = ((IPredatorDiet)this).getPreyClasses();
			for(Class c : prey) {
				if(c.getName().equals(EntityPlayer.class.getName())) {
					hasPlayer = true;
					break;
				}
			}
			if(hasPlayer) {
				this.setDead();
			}
		}
		
		// Client Side
		if (world.isRemote) {
			if(!(this instanceof IAmphibian)) {
				this.rotationPitch = -this.swimPitch;
				this.rotationYaw = -this.swimYaw;
				this.rotationYawHead = -this.swimYaw;
				this.prevRotationYawHead = -this.prevSwimYaw;
				this.renderYawOffset = 0;
				this.cameraPitch = -this.swimPitch;
				this.prevRotationPitch = -this.prevSwimPitch;
				this.prevRotationYaw = -this.prevSwimYaw;				
				this.prevSwimYaw = this.swimYaw;
				this.swimYaw = this.getDataManager().get(SWIMYAW);
				this.prevSwimPitch = this.swimPitch;
				this.swimPitch = this.getDataManager().get(SWIMPITCH);
				
				this.motionX *= 0.98f;
				this.motionY *= 0.98f;
				this.motionZ *= 0.98f;
			}else {
				this.rotationPitch = -this.swimPitch;
				if(this.isInWater()) {
					this.rotationYaw = -this.swimYaw;
					this.rotationYawHead = -this.swimYaw;
					this.prevRotationYawHead = -this.prevSwimYaw;
					this.renderYawOffset = 0;
					this.prevRotationYaw = -this.prevSwimYaw;				
				}else {
					this.prevSwimYaw = -this.prevRotationYaw;
					this.swimYaw = -this.rotationYaw;
					this.rotationYawHead = this.rotationYaw;
				
				}
				this.cameraPitch = -this.swimPitch;
				this.prevRotationPitch = -this.prevSwimPitch;
				this.prevSwimYaw = this.swimYaw;
				this.swimYaw = this.getDataManager().get(SWIMYAW);
				this.prevSwimPitch = this.swimPitch;
				this.swimPitch = this.getDataManager().get(SWIMPITCH);
				
			//	this.motionX *= 0.98f;
			//	this.motionY *= 0.98f;
			//	this.motionZ *= 0.98f;
			}
			return;
		}	
		
		// Server Side
		
		if(((this instanceof IAmphibian) && this.isInWater()) || !(this instanceof IAmphibian)) {
		
		if(this.getHook() != null) {
			if(this.getHook().isDead || !world.loadedEntityList.contains(this.getHook())) {
				this.setHook(null);
			}
		}
		
		if (this.isInWater()) {
			
			this.outOfWaterTime = 0;
			
			BlockPos bp = new BlockPos((int)posX, (int)posY-2, (int)posZ);
				
			// Near surface check
			bp = new BlockPos((int)posX, (int)posY+1, (int)posZ);
			if(!this.world.getBlockState(bp).getMaterial().isLiquid()) {
				if(this.swimPitch > 0f) {
					Vec3d angle = this.getHeading();
					double frontDist = 5f;
					Vec3d diff = new Vec3d(posX + (angle.xCoord*frontDist), posY + angle.yCoord, posZ + (angle.zCoord*frontDist));	
					this.isPanicking = false;
					this.setRandomTargetHeadingForce(32);
				//	this.setTargetHeading(diff.xCoord, posY - 2, diff.zCoord, true);
					//this.swimPitch -= 15f;
				}
				
			}
			
			// Random movements
			if(ticksExisted % 10+rand.nextInt(20) == 0) {
				this.setRandomTargetHeading();
				
				if(this.eatenFishAmount > 0 && rand.nextInt(10) == 0) {
					this.eatenFishAmount--;
				}
			}	
			
			// Target selection
			if(canAggress && this.eatenFishAmount < this.maximumEatAmount) {
				if(this.ticksExisted % 80 == 0 && this.aggressTarget == null|| !world.loadedEntityList.contains(aggressTarget)) {
					List<Entity> list = world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(20D, 20D, 20D).offset(0.0D, -8.0D, 0.0D), EntitySelectors.IS_ALIVE);
					if(list.size() > 0) {
						Entity ent = list.get(rand.nextInt(list.size()));
						boolean skip = false;
						if(ent.equals(this)) skip = true;	
						if(ent.getClass().getName().equals(this.getClass().getName())) skip = true;	
						if(this instanceof IPredatorDiet) {
							Class[] prey = ((IPredatorDiet)this).getPreyClasses();
							boolean contains = false;
							for(int i =0; i < prey.length; i++) {
								if(prey[i].getName().equals(ent.getClass().getName())) {
									contains = true;
								}
							}
							if(!contains) {
								skip = true;
							}
						}
						if(!ent.isInWater()) skip = true;				
						if(!this.canEntityBeSeen(ent)) skip = true;
						
						if(!skip) {
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
					this.setRandomTargetHeading();
				}
			}
				
			// Wall correction
				Vec3d angle = this.getHeading();
				double frontDist = 1f+rand.nextInt(4);
				double behindDist = 4f;
				
				Vec3d diff = new Vec3d(posX + (angle.xCoord*frontDist), posY + angle.yCoord, posZ + (angle.zCoord*frontDist));

				bp = new BlockPos((int)diff.xCoord, (int)posY, (int)diff.zCoord);
		
				if(!this.world.getBlockState(bp).getMaterial().isLiquid() && !isMovingAwayFromWall) 
				{
					Vec3d behind = new Vec3d(posX - (angle.xCoord*behindDist), posY + angle.yCoord, posZ - (angle.zCoord*behindDist));
					this.setRandomTargetHeadingForce(32);
					isMovingAwayFromWall = true;
				}
				
				if(ticksExisted % 20 == 0 && isMovingAwayFromWall)
				isMovingAwayFromWall = false;
				
				
				if(this.targetVector != null && isMovingAwayFromWall) {
					bp = new BlockPos((int)this.targetVector.xCoord, (int)this.targetVector.yCoord, (int)this.targetVector.zCoord);
	
					if(this.getPosition().equals(bp) && ticksExisted % 80 == 0) {
						isMovingAwayFromWall = false;
					}
				}
		
			
			// Move away from players
			if(this.fleeFromPlayers && ticksExisted % 80 == 0) {
				EntityPlayer closest = world.getClosestPlayerToEntity(this, this.fleeDistance);
				if(closest != null) {
					if(closest.isInWater()) {
						this.fleeEntity(closest);
						this.isPanicking = true;
					}
				}else {
					this.isPanicking = false;
				}
			}else {
				this.isPanicking = false;
			}				
			
			// Hunt Target and/or Do damage
			if(this.aggressTarget != null) {
				if(this.getDistanceSqToEntity(this.aggressTarget) <= this.width) {
					if(this.aggressTarget instanceof EntityLivingBase) {
						((EntityLivingBase)this.aggressTarget).attackEntityFrom(DamageSource.causeMobDamage(this), this.attackDamage);
					}
					if(this.aggressTarget instanceof EntityTropicraftWaterBase) {
						// Was eaten, cancel smoke
						
						if(this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY > this.aggressTarget.getEntityBoundingBox().maxY - this.aggressTarget.getEntityBoundingBox().minY)
						{
							((EntityTropicraftWaterBase) this.aggressTarget).setExpRate(0);
							this.aggressTarget.setDropItemsWhenDead(false);
							
							this.aggressTarget.setDead();
							this.heal(1);
							this.eatenFishAmount++;
						}
					}
					this.setRandomTargetHeading();
				}else {
					if(this.canEntityBeSeen(this.aggressTarget) && this.ticksExisted % 20 == 0) {	
						this.setTargetHeading(this.aggressTarget.posX, this.aggressTarget.posY, this.aggressTarget.posZ, true);
					}
				}
				if(this.aggressTarget != null) {
					if(!this.canEntityBeSeen(aggressTarget) || !this.aggressTarget.isInWater()) {
						this.aggressTarget = null;
						this.setRandomTargetHeading();
					}
				}
	
			}

			if(!this.isAggressing && (this.ticksExisted+this.getEntityId()) % 120 == 0) {
				List<EntityTropicraftWaterBase> ents = world.getEntitiesWithinAABB(EntityTropicraftWaterBase.class, new AxisAlignedBB(this.getPosition()).expand(4, 4, 4));
				for(int i =0; i < ents.size(); i++) {
					EntityTropicraftWaterBase f = ents.get(i);
					if(this.getDistanceSqToEntity(f) < 2D && this.canEntityBeSeen(f))
						if(f.aggressTarget != null)
							if(f.aggressTarget.equals(this)) {
								this.fleeEntity(f);
								this.isPanicking = true;
								break;
							}
				}
			}

			bp = new BlockPos(this.getPosition().down(2));

			// Hitting bottom check
			if(!this.world.getBlockState(bp).getMaterial().isLiquid()) {
				if(this.swimPitch < 0f) {
					this.swimPitch+= 2f;
				}
			}
			
			// Yaw/Pitch lerp
			float swimSpeedTurn = this.swimSpeedTurn;//*(1 + (swimSpeedCurrent/5));
			
			if(this.isMovingAwayFromWall) {
				swimSpeedTurn *= 1.8f;
			}
			
			if(this.isFishable() && (this.ticksExisted+this.getEntityId()) % 80 == 0 && rand.nextInt(35) == 0) {
				List<EntityHook> ents = world.getEntitiesWithinAABB(EntityHook.class, new AxisAlignedBB(this.getPosition()).expand(16, 8, 16));
				for(int i =0; i < ents.size(); i++) {
					EntityHook h = ents.get(i);
					if(this.canEntityBeSeen(h)) {
						if(h.getHooked() == null && h.isInWater()) {
							this.hookTarget = h;
						}
					}
				}
			}
			
			if(this.hookTarget != null) {
				if(this.hookTarget.isDead || !world.loadedEntityList.contains(this.hookTarget) || this.hookTarget.getHooked() != null) {
					this.hookTarget = null;
					this.setRandomTargetHeadingForce(10);
				}else {
					if(this.ticksExisted % 20 == 0) {
						this.setTargetHeading(hookTarget.posX, hookTarget.posY, hookTarget.posZ, false);
					}
					if(this.getDistanceSqToEntity(this.hookTarget) < 2D) {
						if(this.hookTarget.getHooked() == null) {
							this.hookTarget.setHooked(this);
							this.hookTarget = null;
						}
					}
				}
			}
			
			if(getHook() != null) {
				//swimSpeedTurn *= 2f;
				EntityHook hook = (EntityHook)getHook();
				if(hook.getHooked() != null) {
					
					if(!hook.getHooked().equals(this)) {
						this.setHook(null);
					}else {
						EntityPlayer angler = hook.getAngler();
						if(angler != null) {
							if(this.getDistanceToEntity(angler) > 2D) {
								this.setTargetHeading(angler.posX, angler.posY, angler.posZ, false);
							}else {
								hook.bringInHookedEntity();
								return;
							}
						}else {
							hook.setHooked(null);
							this.aggressTarget = null;
							this.setRandomTargetHeadingForce(5);
						}
					}
				}
			}
			if (this.targetVectorHeading != null) {
				this.swimYaw = lerp(this.swimYaw, -this.targetVectorHeading.x, swimSpeedTurn * 2f);
				this.swimPitch = lerp(this.swimPitch, -this.targetVectorHeading.y, swimSpeedTurn * 2f);
			}
		}
		
		



		// Out of water
		if (!this.isInWater() && !(this instanceof IAmphibian)) {
			this.outOfWaterTime++;
			this.setTargetHeading(posX, posY-1, posZ, false);
		}
		
		


		
		// Move speed
		float currentSpeed = this.swimSpeedCurrent;
		float desiredSpeed = this.swimSpeedDefault;
		
	
		
		
		if(this.aggressTarget != null) {
			if(this.getDistanceSqToEntity(this.aggressTarget) < 10f) {
				desiredSpeed = this.swimSpeedCharging;
			}else {
				desiredSpeed = this.swimSpeedChasing;
			}
			if(!world.loadedEntityList.contains(this.aggressTarget) || this.aggressTarget.isDead) {
				this.aggressTarget = null;
				this.setRandomTargetHeading();
			}
		}
		
		if(this.hookTarget != null) {
			desiredSpeed = this.swimSpeedChasing;
		}
		
		
		if(this.isPanicking) {
			desiredSpeed = this.swimSpeedPanic;
		}
		
		if(this.ticksExisted % 50  < 30) {
			desiredSpeed *= 0.8f;
		}
		if(this.getIsLeader()) {
			desiredSpeed *= 0.95f;
		}
		
		if(this.isMovingAwayFromWall) {
			desiredSpeed *= 0.6f;
			currentSpeed *= 0.8f;
		}
		
		if(this.swimSpeedCurrent < desiredSpeed) {
			this.swimSpeedCurrent += this.swimAccelRate;
		}
		if(this.swimSpeedCurrent > desiredSpeed) {
			this.swimSpeedCurrent -= this.swimDecelRate;
		}
		
		// speed scaled down 1/10th
		currentSpeed *= 0.1f;
		

		
		// In water motion
		if(this.isInWater()) {
			
			motionX = currentSpeed * Math.sin(this.swimYaw * (Math.PI / 180.0));
			motionZ = currentSpeed * Math.cos(this.swimYaw * (Math.PI / 180.0));
			motionY = currentSpeed * Math.sin(this.swimPitch * (Math.PI / 180.0));
			fallVelocity = 0f;
		}

		// out of water motion
		if (!this.isInWater() && !(this instanceof IAmphibian)) {

			if(this.onGround) {
				if(rand.nextInt(6) == 0) {
					this.motionX += rand.nextBoolean() ? rand.nextFloat()/8 : - rand.nextFloat()/8;
					this.motionZ += rand.nextBoolean() ? rand.nextFloat()/8 : - rand.nextFloat()/8;
				}
				this.motionX *= 0.5f;
				this.motionZ *= 0.5f;
				if(this.ticksExisted % 4 == 0)
				this.fallVelocity = -.02f;
				
				if(rand.nextInt(20) == 0 || this.hurtTime > 0) {
					this.fallVelocity = -.03f;
					this.swimPitch = 25f;
				}
			}
			
			if(this.swimPitch > 0f) {
				this.swimPitch -= 5f;
			}
			if(this.ticksExisted % 20 == 0) {
				this.outOfWaterAngle = rand.nextInt(360);
			}
			
			float turnSpeed = 5f;
			if(this.hurtTime > 0) {
				turnSpeed = 15f;
			}
			if(this.swimYaw > this.outOfWaterAngle) {
				this.swimYaw-= turnSpeed;
			}
			if(this.swimYaw < this.outOfWaterAngle) {
				this.swimYaw += turnSpeed;
			}
			
			this.motionY -= this.fallVelocity;
			this.fallVelocity += (this.fallGravity / 10);
		}
		
		if(!this.isInWater() && (this instanceof IAmphibian)) {
			if(!onGround) {
				this.motionY -= this.fallVelocity;
				this.fallVelocity += (this.fallGravity / 10);
			}else {
				this.fallVelocity = 0f;
			}
		}

			
		if(swimPitch > 45f) {
			swimPitch = 45f;
		}
		syncSwimAngles();
		prevSwimPitch = swimPitch;
		prevSwimYaw = swimYaw;
		}else {
			super.onLivingUpdate();
		}
	}

	public void setMaxHealth(int h) {
		this.maxHealth = h;
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.maxHealth);
	}
	
	public void markAsLeader() {
		setIsLeader(true);
	}
	
	public void setFleesPlayers(boolean b) {
		this.fleeFromPlayers = b;
	}
	
	public void setFleesPlayers(boolean b, double minDist) {
		this.setFleesPlayers(b);
		this.fleeDistance = minDist;
	}
	
	public void setApproachesPlayers(boolean b) {
		this.approachPlayers = b;
	}
	
	
	public void setDropStack(Item item, int max) {
		this.dropStack = new ItemStack(item);
	}
	
	public void setDropStack(Block item, int max) {
		this.dropStack = new ItemStack(item);
	}
	
	public void setSwimSpeeds(float regular, float panic, float turnSpeed) {
		swimSpeedDefault = regular;
		swimSpeedPanic = panic;
		swimSpeedTurn = turnSpeed;
	}
	
	public void setSwimSpeeds(float r, float p, float t, float chasing, float charging) {
		setSwimSpeeds(r, p, t);
		swimSpeedChasing = chasing;
		swimSpeedCharging = charging;
	}
	
	public boolean setRandomTargetHeading() {
		boolean result = false;
		int dist = 16;
		Vec3d randBlock = new Vec3d(posX + randFlip(dist), posY + randFlip(dist/2), posZ + randFlip(dist));

		result = this.setTargetHeading(randBlock.xCoord, randBlock.yCoord, randBlock.zCoord, true);
		
		// Try to move towards a player
		if(this.approachPlayers) {
			if(rand.nextInt(15) == 0) {
				EntityPlayer closest = world.getClosestPlayerToEntity(this, 32D);
				if(closest != null) {
				if(closest.isInWater())
					result = this.setTargetHeading(closest.posX, closest.posY, closest.posZ, true);
				}
	
			}
		}
		
		return result;
	}
	
	public void setRandomTargetHeadingForce(int maxTimes) {
		for(int i =0; i < maxTimes; i++) {
			if(setRandomTargetHeading()) {
				break;
			}
		}
	}
	
	public int randFlip(int i) {
		return rand.nextBoolean() ? rand.nextInt(i) : -(rand.nextInt(i));
	}
	
	public float lerp(float x1, float x2, float t) {
		 float f = MathHelper.wrapDegrees(x2 - x1);

	        if (f > t)
	        {
	            f = t;
	        }

	        if (f < -t)
	        {
	            f = -t;
	        }

	        return x1 + f;
	}
	
	public float getCurrentSwimSpeed() {
		return this.getDataManager().get(SWIMSPEEDCUR);
	}
	
	
	public void syncSwimAngles() {
		this.getDataManager().set(SWIMYAW, swimYaw);
		this.getDataManager().set(SWIMPITCH, swimPitch);
		this.getDataManager().set(SWIMSPEEDCUR, swimSpeedCurrent);
	}
	
	public void setAttackDamage(float f) {
		this.attackDamage = f;
	}
	
	public boolean isFishable() {
		return this.fishable;
	}
	public void setFishable(boolean b) {
		this.fishable = b;
	}
	
	public Vec3d getHeading() {
		return new Vec3d(Math.sin(this.swimYaw * (Math.PI / 180.0)), Math.sin(this.swimPitch * (Math.PI / 180.0)), Math.cos(this.swimYaw * (Math.PI / 180.0))).normalize();
	}

	public boolean setTargetHeading(double posX, double posY, double posZ, boolean waterChecks) {
		if(waterChecks) {
			BlockPos bp = new BlockPos((int)posX, (int)posY, (int)posZ);
			if(!world.getBlockState(bp).getMaterial().isLiquid()) return false;
			if(world.getBlockState(bp).getMaterial().isSolid()) return false;
		}


		double x = (int)(posX - this.posX);
		double y = (int)(posY - this.posY);
		double z = (int)(posZ - this.posZ);
		float yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
		float pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));

		if (this.targetVectorHeading == null) {
			this.targetVectorHeading = new Vector2f();
		}
		this.targetVector = new Vec3d((int)posX, (int)posY, (int)posZ);
		targetVectorHeading.x = yaw;
		targetVectorHeading.y = pitch;
		return true;
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
			this.targetVector = new Vec3d(ent.posX, ent.posY - 5 + rand.nextInt(10), ent.posZ);
		}
		targetVectorHeading.x = yaw+180;
		targetVectorHeading.y = -(pitch/2);
	}
	
	public void setHostile() {
		this.canAggress = true;
	}
	
	public boolean getIsLeader() {
		return this.dataManager.get(IS_LEADER);
	}
	
	public void setIsLeader(boolean flag) {
		this.dataManager.set(IS_LEADER, Boolean.valueOf(flag));
	}
	
	public void setHook(EntityHook ent) {
		if(ent == null) {
			this.getDataManager().set(HOOK_ID, 0);
			return;
		}
		this.getDataManager().set(HOOK_ID, ent.getEntityId());
	}
	
	public EntityHook getHook() {
		return (EntityHook) world.getEntityByID(this.getDataManager().get(HOOK_ID));
	}
	
	public boolean isHooked() {
		return getHook() != null;
	}
	
	public void setTexture(String s) {
		if(s.length() == 0) return;
		if(!world.isRemote) {
			this.getDataManager().set(TEXTURE, s);
		}
	}
	
	public void assignRandomTexture() {
		if(!world.isRemote && getTexturePool() != null) {
			if(getTexturePool().length > 0) {
				setTexture(getTexturePool()[rand.nextInt(getTexturePool().length)]);
			}
		}
	}
	
	public float rangeMap(float input, float inpMin, float inpMax, float outMin, float outMax) {

	    if (Math.abs(inpMax - inpMin) < 1e-12) {
	        return 0;
	    }
 
	    double ratio = (outMax - outMin) / (inpMax - inpMin);
	    return (float)(ratio * (input - inpMin) + outMin);
	}
	
	public String[] getTexturePool() {
		return null;
	}
	
	public String getTexture() {
		return this.getDataManager().get(TEXTURE);
	}
	
	public void setExpRate(int i) {
		this.experienceValue = i;
	}
	
	@Override
	public boolean isPushedByWater() {
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
		return super.getCanSpawnHere();
	}
	
	@Override
	protected float getSoundVolume() {
		return 0.3F;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.maxHealth);
	}
	
	@Override
	public boolean isNotColliding() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void resetHeight() {
		
	}

	@Override
	public int getTalkInterval() {
		return this.isInWater() ? 120 : 240;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		if(this.isInWater()) {
			return SoundEvents.ENTITY_SQUID_AMBIENT;
		}else {
			return null;
		}
	}

	@Override
	protected SoundEvent getHurtSound() {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return this.experienceValue + world.rand.nextInt(3);
	}

	@Override
	public void moveEntityWithHeading(float f, float f1) {
		if((this instanceof IAmphibian) && !isInWater()) {
			super.moveEntityWithHeading(f, f1);
			return;
		}
		move(motionX, motionY, motionZ);
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if(this.dropStack == null) return;
		if(damagesource.getEntity() instanceof EntityPlayer) {
			if(!world.isRemote) {
				int i = rand.nextInt(this.dropMaxAmt) + 1;
				this.dropStack.stackSize = 1;
				for (int j = 0; j < i; j++) {
						entityDropItem(this.dropStack, 0.0F);
				}
			}
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.getEntity() instanceof EntityPlayer) {
			if(this.canAggress) {
				this.aggressTarget = source.getEntity();
				this.setTargetHeading(this.aggressTarget.posX, this.aggressTarget.posY+1, this.aggressTarget.posZ, false);
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setString("texture", this.getDataManager().get(TEXTURE));
		n.setFloat("swimYaw", this.getDataManager().get(SWIMYAW));
		n.setFloat("swimPitch", this.getDataManager().get(SWIMPITCH));
		n.setBoolean("isLeader", getIsLeader());
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		setTexture(n.getString("texture"));
		this.swimYaw = n.getFloat("swimYaw");
		this.swimPitch = n.getFloat("swimPitch");
		this.getDataManager().set(SWIMYAW, this.swimYaw);
		this.getDataManager().set(SWIMPITCH, n.getFloat("swimPitch"));
		this.setIsLeader(n.getBoolean("isLeader"));
		super.readEntityFromNBT(n);
	}
}