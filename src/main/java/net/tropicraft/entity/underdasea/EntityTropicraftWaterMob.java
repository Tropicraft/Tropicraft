package net.tropicraft.entity.underdasea;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityTropicraftWaterMob extends EntityWaterMob {

    protected WaterMobType type;

    public Entity targetEntity;
    protected float climbFactor;
    protected float horFactor;
    protected int hyperness;
    protected int fickleness;
    public boolean isSurfacing;
    public boolean reachedTarget;    
    public float important1;
    public float randomMotionSpeed;
    public float important2;
    protected float randomMotionVecX;
    protected float randomMotionVecY;
    protected float randomMotionVecZ;
    public int targetHeight;    
    public int surfaceTick;
    public int targetHeightTick = 120;

    //TODO: Implement for fishing
	public boolean isCatchable;
    public int outOfWaterTick = 0;
    public double fishingMaxLookDist = 10D;
    public int fishingImmediateDispatchOdds = 10;
    public int fishingInterestOdds = 10;
    public int fishingBreakLineOdds = 500;
    public float fishingApproachSpeed = 1.4f;
    public float fishingEscapeSpeed = 2.4f;
    public static boolean fishingDebug = false;
    public List<String> fishingLog = new ArrayList<String>();

    public EntityTropicraftWaterMob(World world) {
        super(world);
        important1 = 0.0F;       
        randomMotionSpeed = 0.0F;
        important2 = 0.0F;
        important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
        randomMotionVecX = 0.0F;
        randomMotionVecY = 0.0F;
        randomMotionVecZ = 0.0F;        
        reachedTarget = true;
        targetHeight = 62;
        isSurfacing = false;
        surfaceTick = 0;
        hyperness = 30;
        fickleness = 150;
        horFactor = .1F;
        climbFactor = .1F;
        this.experienceValue = 5;
        this.type = WaterMobType.OCEAN_DWELLER;
    }

    public EntityTropicraftWaterMob(World par1World, WaterMobType type) {
        this(par1World);
        this.type = type;
    }
    
    protected abstract int attackStrength();

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (worldObj.isRemote) return;

        // Orient creature
        important1 += important2;
        if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0f)
        {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRenderYawOffset = renderYawOffset = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D);
        }
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        for(rotationPitch = (float)((Math.atan2(motionY, f3) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;

        if(important1 > 6.283185F)
        {
            important1 -= 6.283185F;
            if(rand.nextInt(10) == 0)
            {
                important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
            }
        }

        // Move towards the surface
        if(surfaceTick != 0){
            surfaceTick--;
        }

        if(!isSurfacing) {
            // Wander
            if(important1 < 3.141593F) {
                float f = important1 / 3.141593F;
                if((double)f > 0.75D) {
                    randomMotionSpeed = 1.0F;
                } 
            } else {
                randomMotionSpeed = randomMotionSpeed * 0.95F;
            }

            if(!worldObj.isRemote && targetEntity == null) {
                motionX = randomMotionVecX * randomMotionSpeed;
                motionY = randomMotionVecY * randomMotionSpeed;
                motionZ = randomMotionVecZ * randomMotionSpeed;
            }

            if(targetEntity == null){
                renderYawOffset += ((-(float)Math.atan2(motionX, motionZ) * 180F) / 3.141593F - renderYawOffset) * 0.1F;
                rotationYaw = renderYawOffset;
            }
        }
        
        if(isInWater()) {
            
        } else
        {
            // Rise to surface
            if(!worldObj.isRemote)
            {
                motionX = 0.0D;               
                motionY *= 0.98000001907348633D;
                motionZ = 0.0D;
            }

            // Done surfacing
            if(surfaceTick == 0){
                isSurfacing = false;
            }
            
            //if (isInWater()) {
            	this.setAir(20 * 20);
            //}

            if(!isInWater() && deathTime == 0) {
                //motionY += .50;
                /*this.attackEntityFrom(DamageSource.drown, 1);
                int d = 1;
                int e = 1;
                if(rand.nextInt(2) == 0){
                    d = -1;
                }
                if(rand.nextInt(2) == 0){
                    e = -1;
                }
                motionZ = rand.nextFloat()*.20F *d;
                motionX = rand.nextFloat()*.20F*e;*/
            }

            Block blockUnder = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 1), MathHelper.floor_double(posZ));
            Block blockAt = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            if (!blockUnder.getMaterial().isSolid()) {
	            if(blockAt == Blocks.air){
	                motionY -= 0.080000000000000002D;
	            }
            } else {
            	motionY += 0.2D;
            	this.moveEntity(motionX, motionY, motionZ);
            }
        }
    }

    @Override
    public boolean isAIEnabled() {
        return false;
    }

    @Override
    protected void updateEntityActionState() {
        ++this.entityAge;
            // this.despawnEntity();

        if(targetEntity != null) {
            if(targetEntity.isDead || !targetEntity.isInWater() || this.getDistanceToEntity(targetEntity) > 10) {
                targetEntity = null;
            } else if(inWater) {
                if(targetEntity.posY > this.type.shallowDepth || !targetEntity.isInWater()) {
                    targetEntity = null;
                }

                if(attackTime == 0 && targetEntity != null) {
                    attackEntity(targetEntity);
                }

                if(attackTime > 0) {
                    motionZ = Math.cos((renderYawOffset) / 57.26) * horFactor;
                    motionX = Math.sin((renderYawOffset) / 57.26) * horFactor;
                    motionY = 0;            
                } 
            }
        }
        if(targetEntity == null) {
            targetEntity = this.getAttackTarget();

            if(rand.nextInt(hyperness) == 0 || !inWater || randomMotionVecX == 0.0F && randomMotionVecY == 0.0F && randomMotionVecZ == 0.0F) {
                float f = rand.nextFloat() * 3.141593F * 2.0F;
                randomMotionVecX = MathHelper.cos(f) * horFactor;            
                randomMotionVecZ = MathHelper.sin(f) * horFactor;
            }

            if(!isSurfacing && isInWater()) {
                if(!reachedTarget) {
                    targetHeightTick --;
                }

                if(targetHeightTick == 0) {
                    targetHeightTick = 120;
                    reachedTarget = true;
                }
                if(posY <= targetHeight + .15 && posY >= targetHeight - .15 || reachedTarget == true) {
                    reachedTarget = true;
                    targetHeightTick = 120;
                    randomMotionVecY = 0;

                    if(rand.nextInt(fickleness) == 0) {              
                        reachedTarget = getTargetHeight();
                    }
                } else if(posY > targetHeight && !reachedTarget) {                 
                    randomMotionVecY = -climbFactor;
                } else if(posY < targetHeight && !reachedTarget) {
                    randomMotionVecY  = climbFactor;
                }           
            }
        }
    }

    protected int getDistanceToBase(int i, int height) {
        if (worldObj.getBlock(MathHelper.floor_double(posX), height - i, MathHelper.floor_double(posZ)).getMaterial().isLiquid()) {
            //System.out.println("I = " + i);
            return getDistanceToBase(i + 1, height);
        } else {           
            return i;
        }
    }
    
    /**
     * Recursively counts the number of water blocks between this mob and the surface
     * @param i
     * @return
     */
    protected int getDistanceToSurface(int i){
        if(worldObj.getBlock(MathHelper.floor_double(posX), (int)posY + i, MathHelper.floor_double(posZ)).getMaterial().isLiquid()) {
            return getDistanceToSurface(i + 1);
        } 
        else {
            return i;
        }
    }

    /**
     * Get the height this water mob is trying to get to
     * @return
     */
    protected boolean getTargetHeight() {
        if(isWet() && !isSurfacing ) {
            if(posY < this.type.shallowDepth + 1) {
                int depth = (int)(getDistanceToBase(0, 62));

                if(depth < 1) {
                    targetHeight = (int)posY;           
                    return false;
                } else if(depth < 63 - this.type.shallowDepth) {
                    targetHeight = 63 - rand.nextInt(depth + 1);
                    if(targetHeight == 63){
                        targetHeight--;
                    }
                    return false;
                } else {
                    depth -= 63 - this.type.shallowDepth;

                    if(depth > this.type.deepDepth) {
                        depth = this.type.deepDepth;
                    }
                    targetHeight = this.type.shallowDepth - rand.nextInt(depth + 1);
                    return false;
                } 
            } else {
                //System.out.println("Finding Distance to Surface");
                int height = getDistanceToSurface(0);
                height += (int)posY;
                //System.out.println("Height = " + height + "PosY = " + posY);
                int depth = getDistanceToBase(0, height - 1);
                //System.out.println("Depth = " + depth);
                if(depth < 1) {
                    targetHeight = (int)posY;               
                    return false;
                } else{
                    int i1 = (int)(rand.nextInt(depth));
                    if(i1 == 0) {
                        i1 = 1;
                    }
                    targetHeight =  height - i1;
                    //System.out.println("TargetHeight is " + targetHeight);
                    return false;
                }
            }
        }
        return true;
    }

    public void setAttackHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;     
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;        
    }

    /**
     * Faces an entity, then moves towards it to attack it
     * @param entity
     */
    protected void attackEntity(Entity entity) {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;          
        faceEntity(targetEntity, 360F, 360F);
        double d2 = entity.posY  - posY;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;            
        setAttackHeading(d, d2, d1, horFactor, 12F);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    @Override
    public boolean isInWater() {
        return super.isInWater();//this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 45.0D && this.posY < 63.0D && super.getCanSpawnHere();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
    //    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
    }
    
    @Override
    public void applyEntityCollision(Entity entity){
        super.applyEntityCollision(entity);
        if(targetEntity != null && entity.equals(targetEntity)){
            if(attackStrength() != 0){
                targetEntity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength());
                attackTime = 60;
            }
        }
    }

    public WaterMobType getType() {
        return this.type;
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
        return 1 + worldObj.rand.nextInt(3);
    }

    @Override
    public void moveEntityWithHeading(float f, float f1) {
        moveEntity(motionX, motionY, motionZ);
    }
    
    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return null;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    public static enum WaterMobType {
        //TODO CHANGE THIS TO BE THE WATER HEIGHT LEVEL IN THE TROPICS!
        SURFACE_TROPICS(90, 88),
        SURFACE_OVERWORLD(63, 62),
        OCEAN_DWELLER(62, 32);

        /** The highest this water mob can go in the water (eg, the highest y-value) */
        final int shallowDepth;

        /** The deepest this water mob can go in the water (eg, the smallest y-value) */
        final int deepDepth;

        private WaterMobType(int shallowDepth, int deepDepth) {
            this.shallowDepth = shallowDepth;
            this.deepDepth = deepDepth;
        }

        public int getShallowDepth() {
            return this.shallowDepth;
        }

        public int getDeepDepth() {
            return this.deepDepth;
        }
    }
    
    @Override
	public void entityInit(){
		this.dataWatcher.addObject(26, Integer.valueOf(-1));
		super.entityInit();
	}
	
	
	public int getHookID(){
		return this.dataWatcher.getWatchableObjectInt(26);
	}
	
	public void setHookID(int i){
		this.dataWatcher.updateObject(26, Integer.valueOf(i));
	}
	
	public boolean fishingIsInterested = false;
	
	public void onFishingUpdate(){
		// If we're hooked, check the validity of it
		if(this.getHookID() != -1){
			List<Entity> l = worldObj.loadedEntityList;
			for(Entity e : l){
				if(e.getEntityId() == getHookID()){
					if(e.isDead){
						// invalid, so set un-hooked
						//System.out.println("Gross");
						setHookID(-1);
					}
					if(rand.nextInt(this.fishingBreakLineOdds) == 0){
						if(!worldObj.isRemote){
							//fishDebug("I managed to get your hook, see ya ;D");
							//e.setDead();
						}
					}
				}
			}
		}
		
	/*	List<Entity> l = worldObj.loadedEntityList;
		for(Entity entity : l){
			
			// we're up against the player's fishing rod
			if(entity instanceof EntityHook && !worldObj.isRemote){
				EntityHook hook = (EntityHook)entity;
				if(hook.isDead){ 
					continue;
				}
				 if(hook.bobber == null){
					// this.isSurfacing = true;
					 // fish collision with bobber
					if(hook.boundingBox.expand(1f, 1f, 1f).intersectsWith(this.boundingBox) && rand.nextInt(this.fishingImmediateDispatchOdds) == 0){
						hook.bobber = this;
						fishDebug("I'm hooked!");
						// hook fish to the bobber, also set the hook id for reference
						this.posX = hook.posX;
						this.posY = hook.posY;
						this.posZ = hook.posZ;
						this.setHookID(hook.getEntityId());
						continue;
					}else{
						if(this.getDistanceToEntity(hook) < fishingMaxLookDist && this.canEntityBeSeen(hook) && rand.nextInt(this.fishingInterestOdds) == 0)
						{
							if(!isInWater()){
								fishingIsInterested = false;
								return;
							}
							fishingIsInterested = true;
							fishDebug("Okay player, I want your hook!");
							this.reachedTarget = false;
							this.faceEntity(hook, 100f, 100f);
							motionX = -((fishingApproachSpeed / 10) * Math.sin(Math.toRadians(rotationYaw)));
							motionZ = ((fishingApproachSpeed / 10) * Math.cos(Math.toRadians(rotationYaw)));
						//	if(this.getDistanceToEntity(hook) < 4D)
							motionY = (hook.posY > this.posY + this.height ? 0.2f : -0.2F);
							//this.isSurfacing = true;
						}else{
							fishingIsInterested = false;
						}
					}
				 }
			}
			
			// we're up against a koa's fishing rod
			if(entity instanceof EntityTropicalFishHook && !worldObj.isRemote){
				EntityTropicalFishHook hook = (EntityTropicalFishHook)entity;
				if(hook.isDead){ 
					continue;
				}
				 if(hook.bobber == null){
					 // fish collision with bobber
					 if(rand.nextInt(this.fishingImmediateDispatchOdds) == 0){
						 fishDebug("I escaped the Koa's clutches!");
						 return;
					 }
					if(hook.boundingBox.expand(2d, 2d, 2d).intersectsWith(this.boundingBox)){
						hook.bobber = this;
						// hook fish to the bobber, also set the hook id for reference
						fishDebug("Hooked by a Koa!");
						this.setHookID(hook.getEntityId());
						continue;
					}else{
						if(this.getDistanceToEntity(hook) < fishingMaxLookDist && this.canEntityBeSeen(hook) && rand.nextInt(this.fishingInterestOdds) == 0)
						{
							if(!isInWater()){
								return;
							}
							fishDebug("I see a Koa hook worth going for!");
							this.reachedTarget = false;
							this.faceEntity(hook, 100f, 100f);
							motionX = -((fishingApproachSpeed / 10) * Math.sin(Math.toRadians(rotationYaw)));
							motionZ = ((fishingApproachSpeed / 10) * Math.cos(Math.toRadians(rotationYaw)));
							motionY = hook.posY > this.posY + this.height ? 0.2f : -0.2F;
							this.isSurfacing = true;
						}
					}
				 }
			}
		}
		
		// Resist once grabbed
		if(this.getHookID() != -1){
			l = worldObj.loadedEntityList;
			for(Entity e : l){
				if(e instanceof EntityHook){
					EntityHook hook = (EntityHook) e;
					if(e.getEntityId() == getHookID() && hook.bobber != this){
						fishDebug("Taking this player's hook for a swim \\o/!");
						if(this.isInWater()){
							//motionX += -((fishingEscapeSpeed) * Math.sin(Math.toRadians(rotationYaw)));
							//motionZ += ((fishingEscapeSpeed) * Math.cos(Math.toRadians(rotationYaw)));
						}
						double y = hook.getVecToPlayer().yCoord;
						if(y > 0){
						//	hook.angler.faceEntity(this, 1f, 1f);
						}
						
						//if(!this.isCollidedVertically)
						//motionY = -0.05F;
					}
				}
				if(e instanceof EntityTropicalFishHook){
					EntityTropicalFishHook hook = (EntityTropicalFishHook) e;
					if(e.getEntityId() == getHookID() && hook.bobber != null && hook.angler != null){
						this.faceEntity(hook.angler, 100f, 100f);
						faceEntity(hook.angler, this, 100f, 100f);
						fishDebug("D: KOA'S ARE GONNA KILL AND EAT ME!!!");
						motionX = -((fishingEscapeSpeed / 10) * Math.sin(Math.toRadians(rotationYaw)));
						motionZ = ((fishingEscapeSpeed / 10) * Math.cos(Math.toRadians(rotationYaw)));
						motionY = (hook.angler.posY+(hook.angler.height/2)) > this.posY + this.height ? 0.2f : -0.2F;
						hook.posX = this.posX;
						hook.posY = this.posY;
						hook.posZ = this.posZ;
						//hook.motionY = 0;
						if(this.getDistanceToEntity(hook.angler) < 3D){
							fishDebug("This is it for me, farewell...");
							hook.angler.swingItem();
							hook.angler.attackEntityAsMob(this);
						}
					}
				}
			}
		}*/
	}
	
	public void fishDebug(String s){
		try{
		if(fishingDebug && !worldObj.isRemote){
			String out = "<"+this.getEntityString().split("\\.")[1]+this.getEntityId()+">: "+s;
			if(fishingLog.contains(out)){
				//System.out.println(out);
				return;
			}
			System.out.println(out);
			//net.minecraft.server.MinecraftServer.getServer().getConfigurationManager().sendChatMsg(ChatMessageComponent.createFromTranslationKey(out));
			fishingLog.add(out);
		}
		}catch(Exception e){
			
		}
	}
	
	public void faceEntity(Entity par1Entity, float par2, float par3)
    {
        double d0 = par1Entity.posX - this.posX;
        double d1 = par1Entity.posZ - this.posZ;
        double d2;

        if (par1Entity instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)par1Entity;
            d2 = entityliving.posY + (double)entityliving.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
        }
        else
        {
            d2 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
        }

        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f2 = (float)(Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float)(-(Math.atan2(d2, d3) * 180.0D / Math.PI));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f3, par3);
        this.renderYawOffset = this.updateRotation(this.renderYawOffset, f2, par2);
        this.rotationYaw = this.renderYawOffset;
    }
	
	public void faceEntity(EntityLivingBase entSource, Entity par1Entity, float par2, float par3)
    {
        double d0 = par1Entity.posX - entSource.posX;
        double d1 = par1Entity.posZ - entSource.posZ;
        double d2;

        if (par1Entity instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)par1Entity;
            d2 = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (entSource.posY + (double)entSource.getEyeHeight());
        }
        else
        {
            d2 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D - (entSource.posY + (double)entSource.getEyeHeight());
        }

        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f2 = (float)(Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float)(-(Math.atan2(d2, d3) * 180.0D / Math.PI));
        entSource.rotationPitch = updateRotation(entSource.rotationPitch, f3, par3);
        entSource.rotationYaw = updateRotation(entSource.rotationYaw, f2, par2);
    }
	
	public float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_)
    {
        float f3 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

        if (f3 > p_70663_3_)
        {
            f3 = p_70663_3_;
        }

        if (f3 < -p_70663_3_)
        {
            f3 = -p_70663_3_;
        }

        return p_70663_1_ + f3;
    }
}
