package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;

import javax.vecmath.Vector2f;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.entity.underdasea.atlantoku.ai.EntityAISwimAvoidEntity;
import net.tropicraft.core.common.entity.underdasea.atlantoku.ai.EntityAISwimAvoidPredator;
import net.tropicraft.core.common.entity.underdasea.atlantoku.ai.EntityAISwimAvoidWalls;
import net.tropicraft.core.common.entity.underdasea.atlantoku.ai.EntityAISwimRandomLocation;
import net.tropicraft.core.common.entity.underdasea.atlantoku.ai.EntityAISwimTargetPrey;

public abstract class EntityTropicraftWaterBase extends EntityWaterMob {
	
    private static final DataParameter<String> TEXTURE = EntityDataManager.<String>createKey(EntityTropicraftWaterBase.class, DataSerializers.STRING);
	private static final DataParameter<Integer> HOOK_ID = EntityDataManager.<Integer>createKey(EntityTropicraftWaterBase.class, DataSerializers.VARINT);

	
	@SideOnly(Side.CLIENT)
	public boolean isInGui;
	
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
	
	
	private ItemStack dropStack = ItemStack.EMPTY;
	private int dropMaxAmt = 3;
	public float attackDamage = 1f;
	
	public boolean isMovingAwayFromWall = false;
	
	public boolean isLeader = false;
	
	public EntityTropicraftWaterBase(World world) {
		super(world);
		this.experienceValue = 5;
		this.setSwimSpeeds(1f, 2f, 5f);
		if(this instanceof IPredatorDiet) {
			this.setHostile();
		}
		this.setMaxHealth(2);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.getDataManager().register(TEXTURE, "");
		this.getDataManager().register(HOOK_ID, Integer.valueOf(-1));
		this.assignRandomTexture();
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimAvoidPredator(0, this, 2D));
        this.tasks.addTask(0, new EntityAISwimAvoidWalls(0, this));
        if(this.fleeFromPlayers) {
            this.tasks.addTask(0, new EntityAISwimAvoidEntity(1, this, 5F, new Class[] {EntityPlayer.class}));
        }
        this.tasks.addTask(2, new EntityAISwimTargetPrey(2, this));
        this.tasks.addTask(2, new EntityAISwimRandomLocation(0, this));


	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		// Remove this entity if difficulty is peaceful and it is capable of harming a player
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
            if (!isInWater() && !isInGui) {
                outOfWaterTime++;
                if (outOfWaterTime > 90) {
                    outOfWaterTime = 90;
                }
            } else {
                if (outOfWaterTime > 0) {
                    outOfWaterTime--;
                }
            }

			if(!(this instanceof IAmphibian)) {
				this.rotationPitch = -this.swimPitch;
				this.rotationYaw = -this.swimYaw;
				this.rotationYawHead = -this.swimYaw;
				this.prevRotationYawHead = -this.prevSwimYaw;
				this.renderYawOffset = 0;
				this.cameraPitch = -this.swimPitch;
				this.prevRotationPitch = -this.prevSwimPitch;
				this.prevRotationYaw = -this.prevSwimYaw;				
				
				double x = (this.posX - this.prevPosX);
				double y = (this.posY - this.prevPosY);
				double z = (this.posZ - this.prevPosZ);
				float yaw ;
				float pitch;

				this.prevSwimYaw = this.swimYaw;
				this.prevSwimPitch = this.swimPitch;
				
				if(this.posX == this.prevPosX && this.posZ == this.prevPosZ) {
					yaw = this.swimYaw;
				}else {
					yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
				}
				if(this.posY == this.prevPosY) {
					pitch = this.swimPitch;
				}else {
					pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));
				}
				
				this.swimYaw = lerp(swimYaw, (int)-yaw, this.swimSpeedTurn*4);
				this.swimPitch = lerp(swimPitch, (int)-pitch, this.swimSpeedTurn*4);
				
			
				this.motionX *= 0.98f;
				this.motionY *= 0.98f;
				this.motionZ *= 0.98f;
				if(this.isAIDisabled() && this.isInWater()) 
				{
					motionY = 0;
					fallVelocity = 0f;
					motionX = 0;
					motionZ = 0;
					swimSpeedCurrent = 0;
				}
			}else {
				this.rotationPitch = -this.swimPitch;
				if(this.isInWater()) {
					this.rotationYaw = -this.swimYaw;
					this.rotationYawHead = -this.swimYaw;
					this.prevRotationYawHead = -this.prevSwimYaw;
					this.renderYawOffset = 0;
					this.prevRotationYaw = -this.prevSwimYaw;				
				}else {
				//	this.prevSwimYaw = this.swimYaw;
				//	this.swimYaw = -this.rotationYaw;
					this.rotationYawHead = this.rotationYaw;
				
				}
				this.cameraPitch = -this.swimPitch;
				this.prevRotationPitch = -this.prevSwimPitch;
				
				double x = (this.posX - this.prevPosX);
				double y = (this.posY - this.prevPosY);
				double z = (this.posZ - this.prevPosZ);
				float yaw;
				float pitch;

					if(this.posX == this.prevPosX && this.posZ == this.prevPosZ) {
						yaw = this.swimYaw;
					}else {
						yaw = (float) ((Math.atan2(z, x) * 180D) / Math.PI) - 90f;
					}
					if(this.posY == this.prevPosY) {
						pitch = this.swimPitch;
					}else {
						pitch = (float) (-((Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180D) / Math.PI));
					}
					
					if(this.onGround && !this.isInWater()) {
						yaw = -this.rotationYawHead;
					}
					
					this.prevSwimYaw = this.swimYaw;
					this.prevSwimPitch = this.swimPitch;
				
					this.swimYaw = lerp(swimYaw, (int)-yaw, this.swimSpeedTurn*2);
					this.swimPitch = lerp(swimPitch, (int)-pitch, this.swimSpeedTurn*2);
				
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
				// Yaw/Pitch lerp
				float swimSpeedTurn = this.swimSpeedTurn;//*(1 + (swimSpeedCurrent/5));
				
				if(this.isMovingAwayFromWall) {
					swimSpeedTurn *= 1.8f;
				}
				
				if(this.isFishable() && (this.ticksExisted+this.getEntityId()) % 80 == 0 && rand.nextInt(35) == 0) {
					List<EntityHook> ents = world.getEntitiesWithinAABB(EntityHook.class, new AxisAlignedBB(this.getPosition()).grow(16, 8, 16));
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
						if(this.getDistanceSq(this.hookTarget) < 2D) {
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
								if(this.getDistance(angler) > 2D) {
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
					this.swimYaw = lerp(this.swimYaw, -this.targetVectorHeading.x, swimSpeedTurn);
					this.swimPitch = lerp(this.swimPitch, -this.targetVectorHeading.y, swimSpeedTurn);
				}
			}
			
			
	
	
	
			// Out of water
			if (!this.isInWater() && !(this instanceof IAmphibian)) {
				this.setTargetHeading(posX, posY-1, posZ, false);
			}	
			
			// Move speed
			float currentSpeed = this.swimSpeedCurrent;
			float desiredSpeed = this.swimSpeedDefault;			
			
			if(this.aggressTarget != null) {
				if(this.getDistanceSq(this.aggressTarget) < 10f) {
					desiredSpeed = this.swimSpeedCharging;
				}else {
					desiredSpeed = this.swimSpeedChasing;
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
			if(this.isAIDisabled() && this.isInWater()) {
				motionY = 0;
				fallVelocity = 0f;
				motionX = 0;
				motionZ = 0;
				swimSpeedCurrent = 0;
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
				
				float vel = this.fallVelocity;
				
				if(this.getPassengers().size() > 0) {
					vel *= 0.5f;
				}
				
				this.motionY -= vel;
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
			
		}else {
			super.onLivingUpdate();
		}
	}

	public void setMaxHealth(int h) {
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(h);
		this.setHealth(h);
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
		this.setDropStack(new ItemStack(item), max);
	}
	
	public void setDropStack(ItemStack stack, int max) {
	    this.dropStack = stack.copy();
	    this.dropMaxAmt = max;
	}
	
	public void setDropStack(Block item, int max) {
	    this.setDropStack(new ItemStack(item), max);
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

		result = this.setTargetHeading(randBlock.x, randBlock.y, randBlock.z, true);
		
		// Try to move towards a player
		if(this.approachPlayers) {
			if(rand.nextInt(50) == 0) {
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
		return x1 + (t*0.03f) * MathHelper.wrapDegrees(x2 - x1);
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
		if(this.isAIDisabled()) {
			return false;
		}
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
		return isLeader;
	}
	
	public void setIsLeader(boolean flag) {
		isLeader = flag;
	}
	
	public void setHook(EntityHook ent) {
		if(ent == null) {
			this.getDataManager().set(HOOK_ID, -1);
			return;
		}
		this.getDataManager().set(HOOK_ID, ent.getEntityId());
	}

	public EntityHook getHook() {
	    Entity hook = world.getEntityByID(this.getDataManager().get(HOOK_ID));
	    if (hook != null && hook instanceof EntityHook) {
	        return (EntityHook) hook;
	    }
		return null;
	}

	public boolean isHooked() {
		return getHook() != null;
	}
	
	public void setTexture(String s) {
		if(s.length() == 0) return;
		this.getDataManager().set(TEXTURE, s);
	}
	
	public void assignRandomTexture() {
		if(getTexturePool() != null) {
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
		return super.isAIDisabled();
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean getCanSpawnHere() {
		if (isInLava()) return false;
		return super.getCanSpawnHere();
	}
	
	@Override
	protected float getSoundVolume() {
		return 0.3F;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		setMaxHealth(2);
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
	public void doWaterSplashEffect() {
		
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
	protected SoundEvent getHurtSound(DamageSource damageSource) {
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
	public void travel(float strafe, float vertical, float forward) {
		if((this instanceof IAmphibian) && !isInWater()) {
			super.travel(strafe, vertical, forward);
			return;
		}
		move(MoverType.SELF, motionX, motionY, motionZ);
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if(this.dropStack.isEmpty()) return;
		if(damagesource.getTrueSource() instanceof EntityPlayer) {
			if(!world.isRemote) {
				int i = rand.nextInt(this.dropMaxAmt) + 1;
				this.dropStack.setCount(1);
				for (int j = 0; j < i; j++) {
						entityDropItem(this.dropStack, 0.0F);
				}
			}
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.getTrueSource() instanceof EntityPlayer) {
			if(this.canAggress) {
				this.aggressTarget = source.getTrueSource();
				this.setTargetHeading(this.aggressTarget.posX, this.aggressTarget.posY+1, this.aggressTarget.posZ, false);
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setString("texture", this.getDataManager().get(TEXTURE));
		n.setFloat("swimYaw", swimYaw);
		n.setFloat("swimPitch", swimPitch);
		n.setBoolean("isLeader", getIsLeader());
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		setTexture(n.getString("texture"));
		this.swimYaw = n.getFloat("swimYaw");
		this.swimPitch = n.getFloat("swimPitch");
		this.setIsLeader(n.getBoolean("isLeader"));
		super.readEntityFromNBT(n);
	}
}