package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;
import net.tropicraft.core.common.entity.projectile.EntityPoisonBlot;

public class EntityTreeFrogBase extends EntityLand implements IMob, IRangedAttackMob {

	/** 0 = peaceful green, 1 = red, 2 = blue, 3 = yellow */
	public int type = 0;
    public int jumpDelay = 0;
	private int attackTime;
	
    public EntityTreeFrogBase(World world) {
        super(world);
        setSize(0.8F, 0.8F);
        this.entityCollisionReduction = 0.8F;
        this.experienceValue = 5;
        
    }
    
    public EntityTreeFrogBase(World world, int type) {
    	this(world);
    	this.type = type;
	}
    
    @Override
    protected void initEntityAI() {
    	super.initEntityAI();
    	
    	this.experienceValue = 5;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        if (!(this instanceof EntityTreeFrogGreen)) {
        	this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();
    	
    	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
    	this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }
    
    @Override
    protected void updateAITasks() {
    	super.updateAITasks();
    	
    	if (!getNavigator().noPath() || this.getAttackTarget() != null) {
            if (onGround || isInWater()) {
                if (jumpDelay > 0) jumpDelay--;
                if (jumpDelay <= 0) {
                    jumpDelay = 5 + rand.nextInt(4);
                    
                    //this.jump();
                    //this.motionY += -0.01D + rand.nextDouble() * 0.1D;
                    double speed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (speed > 0.02D) {
                    	this.motionY += 0.4D;
                    	
                    	this.motionX *= 1.1D;
                    	this.motionZ *= 1.1D;
                    }
                }
            }
    	}
    	
    	if (attackTime > 0) attackTime--;
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entity,
			float f) {
		
		if(f < 4F && !worldObj.isRemote && attackTime == 0 && worldObj.getDifficulty() != EnumDifficulty.PEACEFUL)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;

            EntityPoisonBlot entitypoisonblot = new EntityPoisonBlot(worldObj, this);
            entitypoisonblot.posY += 1.3999999761581421D;
            double d2 = (entity.posY + (double)entity.getEyeHeight()) - 0.20000000298023224D - entitypoisonblot.posY;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
            //worldObj.playSoundAtEntity(this, "frogspit", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
            worldObj.spawnEntityInWorld(entitypoisonblot);
            entitypoisonblot.setThrowableHeading(d, d2 + (double)f1, d1, 0.6F, 12F);
            attackTime = 50;

            rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
            //hasAttacked = true;
        }
		
	}
}
