package net.tropicraft.entity.underdasea;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityTropicraftWaterMob extends EntityWaterMob {

    protected WaterMobType type;
    
    public EntityTropicraftWaterMob(World world) {
        super(world);
    }
    
    public EntityTropicraftWaterMob(World par1World, WaterMobType type) {
        this(par1World);
        this.type = type;
    }
    
    @Override
    public boolean isAIEnabled() {
        return false;
    }
    
   /* @Override
    protected void updateEntityActionState() {
        if (this.fleeingTick > 0 && --this.fleeingTick == 0) {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iattributeinstance.removeModifier(field_110181_i);
        }

        
    }*/
    
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
        return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
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
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
    }
  /*  
    @Override
    public void moveEntityWithHeading(float f, float f1) {
        moveEntity(motionX, motionY, motionZ);
    }*/
    
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
        OCEAN_DWELLER(63, 32);
        
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
}
