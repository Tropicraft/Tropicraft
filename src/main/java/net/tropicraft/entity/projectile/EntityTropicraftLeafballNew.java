package net.tropicraft.entity.projectile;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.entity.EntityThrowableUsefull;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.entity.EntityIconFX;
import extendedrenderer.particle.entity.EntityRotFX;

public class EntityTropicraftLeafballNew extends EntityThrowableUsefull
{
	public int ticksInAir;
	
	@SideOnly(Side.CLIENT)
	public boolean hasDeathTicked;

	public EntityTropicraftLeafballNew(World world)
	{
		super(world);
	}

	public EntityTropicraftLeafballNew(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}

	public EntityTropicraftLeafballNew(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	@Override
	public void onUpdate()
    {
		super.onUpdate();
		
		if (!this.worldObj.isRemote)
        {
			
			ticksInAir++;
			
			
        }
    }
	
	@Override
	public MovingObjectPosition tickEntityCollision(Vec3 vec3, Vec3 vec31) {
		MovingObjectPosition movingobjectposition = null;
		
        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;
        EntityLivingBase entityliving = this.getThrower();

        for (int j = 0; j < list.size(); ++j)
        {
            Entity entity1 = (Entity)list.get(j);

            if (entity1.canBeCollidedWith() && /*(entity1 != entityliving || */this.ticksInAir >= 2)
            {
                float f = 0.3F;
                //AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
                //MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                //if (movingobjectposition1 != null)
                //{
                    //double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                    //if (d1 < d0 || d0 == 0.0D)
                    //{
                        entity = entity1;
                        //d0 = d1;
                    //}
                //}
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
            /*if (movingobjectposition != null) {
            	this.onImpact(movingobjectposition);
            	setDead();
            }*/
        }
        return movingobjectposition;
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition)
	{
		
		if (movingobjectposition.entityHit != null)
		{
			if (!worldObj.isRemote)
			{
				byte byte0 = 2;
				if (movingobjectposition.entityHit instanceof ICoroAI && (getThrower() == null || getThrower() instanceof ICoroAI)) {
					if (getThrower() != null && ((ICoroAI) getThrower()).getAIAgent().dipl_info.isEnemy(((ICoroAI) movingobjectposition.entityHit).getAIAgent().dipl_info)) {
						movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), byte0);
					} else {

					}
				} else {
					movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), byte0);
				}

				/*if (movingobjectposition.entityHit instanceof EntityBlaze)
            {
                byte0 = 3;
            }*/
            /*     if (movingobjectposition.entityHit instanceof EntityKoaMember && thrower instanceof EntityKoaMember) {
    			if (((EntityKoaMember) thrower).dipl_team != ((EntityKoaMember) movingobjectposition.entityHit).dipl_team) {
    				movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), byte0);
    			} else {

    			}
    		}
            else if (!(movingobjectposition.entityHit instanceof EntityKoaMemberNew)) { 
            	if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), byte0));
            	if (thrower instanceof EntityPlayer) {
            		int what = 0;
            	}
            } else if (movingobjectposition.entityHit instanceof EntityKoaMemberNew && thrower instanceof EntityKoaMemberNew) {
    			if (((EntityKoaMemberNew) thrower).dipl_team != ((EntityKoaMemberNew) movingobjectposition.entityHit).dipl_team) {
    				movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), byte0);
    			} else {

    			}
    		}
        } 
        for (int i = 0; i < 30; i++)
        {
            //worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        	double speed = 0.01D;
        	EntityTexFX var31 = new EntityTexFX(worldObj, posX, posY, posZ, rand.nextGaussian()*rand.nextGaussian()*speed, rand.nextGaussian()*speed, rand.nextGaussian()*rand.nextGaussian()*speed, (rand.nextInt(80)/10), 0, mod_EntMover.effLeafID);
            var31.setGravity(0.3F);
            Random rand = new Random();
            var31.rotationYaw = rand.nextInt(360);
            mod_ExtendedRenderer.rotEffRenderer.addEffect(var31);
        }
             */

				
			}
		}
		
		
		
		if (!worldObj.isRemote) {
			setDead();
			//System.out.println("server: " + posX);
		} else {
			tickDeath();
		}
		
	}
	
	@Override
	public void setDead() {
		// TODO Auto-generated method stub
		//System.out.println(worldObj.isRemote);
		if (worldObj.isRemote) tickDeath();
		super.setDead();
	}
	
	@SideOnly(Side.CLIENT)
	public void tickDeath() {
		if (!hasDeathTicked) {
			//System.out.println("client: " + posX);
			hasDeathTicked = true;
			
			int amount = 20 / (Minecraft.getMinecraft().gameSettings.particleSetting+1);
			
			//System.out.println(amount);
			
			for (int i = 0; i < amount; i++)
	        {
	        	double speed = 0.01D;
	        	double speedInheritFactor = 0.5D;
	        	EntityRotFX entityfx = new EntityIconFX(worldObj, posX, posY, posZ, TCItemRegistry.leafBall.getIconFromDamage(0));
	        	entityfx.motionX += (motionX * speedInheritFactor);
	        	entityfx.motionZ += (motionZ * speedInheritFactor);
	        	/*entityfx.motionX = (motionX * speedInheritFactor) + (rand.nextGaussian()*rand.nextGaussian()*speed);
	        	entityfx.motionY = (motionY * speedInheritFactor) + (rand.nextGaussian()*speed);
	        	entityfx.motionZ = (motionZ * speedInheritFactor) + (rand.nextGaussian()*rand.nextGaussian()*speed);*/
	        	
				entityfx.setGravity(0.5F);
				entityfx.rotationYaw = rand.nextInt(360);
				entityfx.setMaxAge(30+rand.nextInt(30));
				entityfx.spawnAsWeatherEffect();
				//ExtendedRenderer.rotEffRenderer.addEffect(entityfx);
	        }
		}
	}
}
