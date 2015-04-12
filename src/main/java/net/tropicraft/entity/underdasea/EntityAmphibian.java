package net.tropicraft.entity.underdasea;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAmphibian extends EntityCreature {

    /** Constant for the amphibian age in the datawatcher */
    public static final int DATAWATCHER_AGE = 16;

    public boolean desireToReturn;
    public boolean returnToLand;
    public boolean reachedTarget;
    public float important1;
    protected float randomMotionSpeed;
    protected float important2;
    protected float randomMotionVecX;
    protected float randomMotionVecY;
    protected float randomMotionVecZ;
    public int targetHeight;
    protected int growthRate;
    protected int fickleness;
    protected float landSpeed;
    public float moveSpeed;

    public static AttributeModifier speedBoostReturnToLand = (new AttributeModifier(UUID.randomUUID(), "Speed boost return to land", 0.25D, 0)).setSaved(false);

    public EntityAmphibian(World par1World) {
        super(par1World);
        important1 = 0.0F;       
        randomMotionSpeed = 0.0F;
        important2 = 0.0F;
        randomMotionVecX = 0.0F;
        randomMotionVecY = 0.0F;
        randomMotionVecZ = 0.0F;
        important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
        reachedTarget = false;
        returnToLand = false;
        desireToReturn = true;
        targetHeight = 61;
        setAmphibianAge(1.0F);
        growthRate = 12000;
        fickleness = 1200;
        landSpeed = .25F;
    }

    public EntityAmphibian(World world, float age) {
        this(world);
        this.setAmphibianAge(age);
    }

    @Override
    protected void entityInit() {
        super.entityInit();        
        dataWatcher.addObject(DATAWATCHER_AGE, Integer.valueOf(10000));                
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("Age", getAmphibianAge());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        setAmphibianAge(nbttagcompound.getFloat("Age"));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if(ticksExisted % growthRate == 0) {
            setAmphibianAge(getAmphibianAge() + .05f);
        }

        if(rand.nextInt(fickleness / 2) == 0 && returnToLand) {
            returnToLand = false;
        }
        if(isInWater() && returnToLand) {
            moveSpeed = 1.5F;
        } else                           //--Cojo - added 'else' here
            if(!isInWater()) {
                moveSpeed = landSpeed;
            } else                           //--Cojo - added 'else' here
                if(isInWater() && !returnToLand) {   
                    if(rand.nextInt(fickleness) == 0) {
                        returnToLand = true;
                    }
                    important1 += important2;
                    if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
                        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                        prevRenderYawOffset = renderYawOffset = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
                        prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D);
                    }

                    float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                    for(rotationPitch = (float)((Math.atan2(motionY, f3) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
                    for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
                    rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;

                    if(important1 > 6.283185F) {
                        important1 -= 6.283185F;
                        if(rand.nextInt(10) == 0) {
                            important2 = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
                        }
                    }

                    if(important1 < 3.141593F) {
                        float f = important1 / 3.141593F;
                        if((double)f > 0.75D) {
                            randomMotionSpeed = 1.0F;
                        } 
                    } else {
                        randomMotionSpeed = randomMotionSpeed * 0.95F;
                    }

                    if(!worldObj.isRemote) {
                        motionX = randomMotionVecX * randomMotionSpeed;
                        motionY = randomMotionVecY * randomMotionSpeed;
                        motionZ = randomMotionVecZ * randomMotionSpeed;
                    }

                    float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                    renderYawOffset += ((-(float)Math.atan2(motionX, motionZ) * 180F) / 3.141593F - renderYawOffset) * 0.1F;
                    rotationYaw = renderYawOffset;            
                }      
    }
    
    @Override
    public float getBlockPathWeight(int i, int j, int k) {
        Block block = worldObj.getBlock(i, j - 1, k);
        
        if (returnToLand) {
            if (block instanceof BlockSand) return 10F;
            if (block.getMaterial() == Material.grass) return 20F;
        } else
            if (block.getMaterial().isLiquid()) {
                return 20F;
            }
        
        return 0.0F;
    }
    
    
    @Override
    protected void updateEntityActionState()
    {
        if(returnToLand || (!returnToLand && !isInWater())) {
            super.updateEntityActionState();
        }
        
        if(!returnToLand) {
            if(rand.nextInt(70) == 0 || !inWater || randomMotionVecX == 0.0F && randomMotionVecY == 0.0F && randomMotionVecZ == 0.0F) {
                float f = rand.nextFloat() * 3.141593F * 2.0F;
                randomMotionVecX = MathHelper.cos(f) * 0.15F;
                randomMotionVecZ = MathHelper.sin(f) * 0.15F;
            }
            
            if(isInWater()) {
                if(posY <= targetHeight + .15 && posY >= targetHeight - .15 || reachedTarget == true) {
                    reachedTarget = true;
                    randomMotionVecY = 0;
                    if(rand.nextInt(300) == 0) {
                        getTargetHeight();
                    }
                }
                else if(posY > targetHeight && !reachedTarget) { 
                    randomMotionVecY = -.15F;
                }
                else if(posY < targetHeight && !reachedTarget) {
                    randomMotionVecY  = .15F;
                }            
            }
        }
    }

    protected void getTargetHeight() {
        if(isWet()) {
            int range = (int)(getDistanceToBase(0, worldObj.getActualHeight()));
            if(range >1) {
                targetHeight = (int)posY;
            }
            targetHeight = worldObj.getActualHeight() - rand.nextInt(range + 1);
            if(targetHeight == worldObj.getActualHeight()) {
                targetHeight--;
            }
            reachedTarget = false;
        }
    }     

    @Override
    protected boolean isAIEnabled() {
        return false;
    }

    protected int getDistanceToBase(int i, int height) {
        if (i == 5) return i;

        if (worldObj.getBlock(MathHelper.floor_double(posX), height - i, MathHelper.floor_double(posZ)).getMaterial().isLiquid()) {
            return getDistanceToBase(i + 1, height);
        } else {           
            return i;
        }
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        return worldObj.checkNoEntityCollision(boundingBox);
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }
    
    @Override
    protected boolean canDespawn() {
        return true;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityplayer) {
        return 1 + worldObj.rand.nextInt(3);
    }

    public void setAmphibianAge(float age) {
        this.dataWatcher.updateObject(DATAWATCHER_AGE, new Integer((int)(age * 10000)));
    }

    public float getAmphibianAge() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_AGE) / 10000F;
    }
}
