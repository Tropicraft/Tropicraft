package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class EntityTropicraftWaterBase extends EntityWaterMob {
	
    private static final DataParameter<Float> SWIMYAW = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SWIMPITCH = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SWIMSPEEDCUR = EntityDataManager.<Float>createKey(EntityTropicraftWaterBase.class, DataSerializers.FLOAT);
    protected static final DataParameter<String> TEXTURE = EntityDataManager.<String>createKey(EntityTropicraftWaterBase.class, DataSerializers.STRING);

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
	private float swimSpeedCurrent = 0f;
	private float swimSpeedPanic = 2f;
	
	private float swimSpeedTurn = 5f;
	
	public boolean isPanicking = false;
	public boolean fleeFromPlayers = false;
	public boolean approachPlayers = false;
	public double fleeDistance = 2D;
	
	public boolean isAggressing = false;
	public boolean canAggress = false;
	private float swimSpeedChasing = 2f;
	private float swimSpeedCharging = 2.5f;
	
	public EntityTropicraftWaterBase leader = null;
	public Entity aggressTarget = null;
	public boolean isLeader = false;
	public boolean canSchool = false;
	
	private ItemStack dropStack = null;
	private int dropMaxAmt = 3;
	private double maxHealth = 5D;
	
	public EntityTropicraftWaterBase(EntityTropicraftWaterBase leader) {
		this(leader.world);
		this.leader = leader;
	}

	public EntityTropicraftWaterBase(World world) {
		super(world);
		this.experienceValue = 5;
		this.setSwimSpeeds(1f, 2f, 5f);
		if(this instanceof IPredatorDiet) {
			this.setHostile();
		}
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.getDataManager().register(SWIMYAW, Float.valueOf(0f));
		this.getDataManager().register(SWIMPITCH, Float.valueOf(0f));
		this.getDataManager().register(SWIMSPEEDCUR, Float.valueOf(0f));
		this.getDataManager().register(TEXTURE, "");
		this.assignRandomTexture();
		this.ticksExisted = rand.nextInt(12345);
	}

	@Override
	public void onLivingUpdate() {
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
			return;
		}	
		
		// Server Side
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
					this.setTargetHeading(diff.xCoord, posY - 2, diff.zCoord, true);
					//this.swimPitch -= 15f;
				}
				
			}
			
			// Random movements
			if(rand.nextInt(20) == 0) {
				this.setRandomTargetHeading();
			}	
			
			// Target selection
			if(canAggress) {
				if(this.ticksExisted % 200 == 0 && this.aggressTarget == null|| !world.loadedEntityList.contains(aggressTarget)) {
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
			if(this.ticksExisted % 80 == 0) {
				Vec3d angle = this.getHeading();
				double frontDist = 2f;
				double behindDist = 8f;
				
				Vec3d diff = new Vec3d(posX + (angle.xCoord*frontDist), posY + angle.yCoord, posZ + (angle.zCoord*frontDist));

				bp = new BlockPos((int)diff.xCoord, (int)posY, (int)diff.zCoord);
		
				if(!this.world.getBlockState(bp).getMaterial().isLiquid()) {
					Vec3d behind = new Vec3d(posX - (angle.xCoord*behindDist), posY + angle.yCoord, posZ - (angle.zCoord*behindDist));
					this.setTargetHeading(behind.xCoord, posY+5, behind.zCoord, true);
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
			
			if(this.canSchool) {
				if(!isLeader && leader == null) {
					List<Entity> ents = world.loadedEntityList;
					for(int i =0; i < ents.size(); i++) {
						if(ents.get(i) instanceof EntityTropicraftWaterBase) {
							EntityTropicraftWaterBase f = ((EntityTropicraftWaterBase)ents.get(i));
							
							if(f.getClass().getName().equals(this.getClass().getName())) {
								if(f instanceof IAtlasFish && this instanceof IAtlasFish) {
									int slot = ((IAtlasFish)f).getAtlasSlot();
									if(((IAtlasFish)this).getAtlasSlot() != slot) {
										continue;
									}
								}
								if(f.isLeader) {
									this.leader = f;
								}	
							}
						}
					}
					if(this.ticksExisted > 200 && leader == null) {
						this.markAsLeader();
					}
				}
				if(this.ticksExisted > 200 && leader == null) {
					this.markAsLeader();
				}
			}
			
			
			// Hunt Target and/or Do damage
			if(this.aggressTarget != null) {
				if(this.getDistanceSqToEntity(this.aggressTarget) < 1.2f) {
					if(this.aggressTarget instanceof EntityLivingBase)
					((EntityLivingBase)this.aggressTarget).attackEntityFrom(DamageSource.cactus, 1);
					this.setRandomTargetHeading();
				}else {
					if(this.canEntityBeSeen(this.aggressTarget) && this.ticksExisted % 5 == 0) {
						this.setTargetHeading(this.aggressTarget.posX, this.aggressTarget.posY, this.aggressTarget.posZ, true);
					}
				}
				if(this.aggressTarget != null) {
					if(!this.aggressTarget.isInWater()) {
						this.aggressTarget = null;
						this.setRandomTargetHeading();
					}
				}
	
			}
			
			if(!this.isAggressing && this.ticksExisted % 80 == 0) {
				List<Entity> ents = world.loadedEntityList;
				for(int i =0; i < ents.size(); i++) {
					if(ents.get(i) instanceof EntityTropicraftWaterBase) {
						EntityTropicraftWaterBase f = ((EntityTropicraftWaterBase)ents.get(i));
						if(this.canEntityBeSeen(f) && this.getDistanceSqToEntity(f) < 2D)
						if(f.aggressTarget != null)
						if(f.aggressTarget.equals(this)) {
							this.fleeEntity(f);
							this.isPanicking = true;
							break;
						}
					}
				}
			}	

			bp = new BlockPos((int)posX, (int)posY-2, (int)posZ);

			// Hitting bottom check
			if(!this.world.getBlockState(bp).getMaterial().isLiquid()) {
				if(this.swimPitch < 0f) {
					this.swimPitch+= 2f;
				}
			}

			if(this.canSchool) {
				if(this.leader != null && !isLeader) {
					this.setTargetHeading(this.leader.posX, this.leader.posY - 5 + rand.nextInt(10), this.leader.posZ, true);
					if(leader.aggressTarget != null) {
						this.aggressTarget = leader.aggressTarget;
					}
				}
			}	
			
			
			// Yaw/Pitch lerp
			float swimSpeedTurn = this.swimSpeedTurn;
			
			if (this.targetVectorHeading != null) {
				this.swimYaw = lerp(this.swimYaw, -this.targetVectorHeading.x, swimSpeedTurn * 2f);
				this.swimPitch = lerp(this.swimPitch, -this.targetVectorHeading.y, swimSpeedTurn * 2f);
			}
		}
		
		



		// Out of water
		if (!this.isInWater()) {
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
		
		if(this.isPanicking) {
			desiredSpeed = this.swimSpeedPanic;
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
			motionY = (currentSpeed) * Math.sin((this.swimPitch) * (Math.PI / 180.0));
			fallVelocity = 0f;
		}

		// out of water motion
		if (!this.isInWater()) {

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

			
		if(swimPitch > 45f) {
			swimPitch = 45f;
		}
		syncSwimAngles();
		prevSwimPitch = swimPitch;
		prevSwimYaw = swimYaw;
	}

	public void setMaxHealth(int h) {
		this.maxHealth = h;
	}
	
	public void markAsLeader() {
		isLeader = true;
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
	
	public void setSchoolable(boolean b) {
		this.canSchool = b;
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
	
	public void setRandomTargetHeading() {
		int dist = 16;
		Vec3d randBlock = new Vec3d(posX + randFlip(dist), posY + randFlip(dist), posZ + randFlip(dist));

		this.setTargetHeading(randBlock.xCoord, randBlock.yCoord, randBlock.zCoord, true);
		
		// Try to move towards a player
		if(this.approachPlayers) {
			if(rand.nextInt(15) == 0) {
				EntityPlayer closest = world.getClosestPlayerToEntity(this, 100f);
				if(closest != null) {
				if(closest.isInWater())
					this.setTargetHeading(closest.posX, closest.posY, closest.posZ, true);
				}
	
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
	
	public Vec3d getHeading() {
		return new Vec3d(Math.sin(this.swimYaw * (Math.PI / 180.0)), Math.sin(this.swimPitch * (Math.PI / 180.0)), Math.cos(this.swimYaw * (Math.PI / 180.0))).normalize();
	}

	public void setTargetHeading(double posX, double posY, double posZ, boolean waterChecks) {
		if(waterChecks) {
			BlockPos bp = new BlockPos((int)posX, (int)posY, (int)posZ);
			if(!world.getBlockState(bp).getMaterial().isLiquid()) return;
			if(world.getBlockState(bp).getMaterial().isSolid()) return;
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
			return SoundEvents.BLOCK_GRAVEL_STEP;
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
		move(motionX, motionY, motionZ);
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if(this.dropStack == null) return;
		int i = rand.nextInt(this.dropMaxAmt) + 1;
		for (int j = 0; j < i; j++) {
			if(!world.isRemote)
				entityDropItem(this.dropStack, 0.0F);
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setString("texture", this.getDataManager().get(TEXTURE));
		n.setFloat("swimYaw", this.getDataManager().get(SWIMYAW));
		n.setFloat("swimPitch", this.getDataManager().get(SWIMPITCH));
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		setTexture(n.getString("texture"));
		this.swimYaw = n.getFloat("swimYaw");
		this.swimPitch = n.getFloat("swimPitch");
		this.getDataManager().set(SWIMYAW, this.swimYaw);
		this.getDataManager().set(SWIMPITCH, n.getFloat("swimPitch"));
		super.readEntityFromNBT(n);
	}
}