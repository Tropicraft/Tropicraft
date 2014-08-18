package net.tropicraft.entity.projectile;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import CoroUtil.componentAI.AITamable;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.entity.EntityThrowableUsefull;
import CoroUtil.util.CoroUtilEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.ParticleRegistry;
import extendedrenderer.particle.behavior.ParticleBehaviors;
import extendedrenderer.particle.entity.EntityRotFX;

public class EntityTameBall extends EntityThrowableUsefull
{
	public int ticksInAir;
	
	@SideOnly(Side.CLIENT)
	public boolean hasDeathTicked;
	
	@SideOnly(Side.CLIENT)
	public ParticleBehaviors pm;

	public EntityTameBall(World world)
	{
		super(world);
	}

	public EntityTameBall(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
		
		float speed = 0.7F;
		float f = 0.4F;
        this.motionX = (double)(-MathHelper.sin(-this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(-this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (double)(MathHelper.cos(-this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(-this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin((-this.rotationPitch + this.func_70183_g()) / 180.0F * (float)Math.PI) * f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed, 1.0F);
	}

	public EntityTameBall(World world, double d, double d1, double d2)
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
			
			if (ticksInAir > 80) {
				setDead();
			}
        } else {
        	if (pm == null) {
        		pm = new ParticleBehaviors(Vec3.createVectorHelper(posX, posY, posZ));
        		pm.rateAlpha = 0.02F;
        		pm.rateBrighten = 0.02F;
        		pm.tickSmokifyTrigger = 20;
        	}
        	tickAnimate();
        }
    }
	
	@Override
	protected float getGravityVelocity() {
		return 0F;
	}
	
	@Override
	public MovingObjectPosition tickEntityCollision(Vec3 vec3, Vec3 vec31) {
		MovingObjectPosition movingobjectposition = null;
		
        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(0.5D, 1D, 0.5D));
        double d0 = 0.0D;
        EntityLivingBase entityliving = this.getThrower();

        for (int j = 0; j < list.size(); ++j)
        {
            Entity entity1 = (Entity)list.get(j);

            if (entity1.canBeCollidedWith() && (entity1 != entityliving && this.ticksInAir >= 4))
            {
                entity = entity1;
                break;
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
				
				if (movingobjectposition.entityHit instanceof ICoroAI && getThrower() instanceof EntityPlayer) {
					AITamable tamable = ((ICoroAI)movingobjectposition.entityHit).getAIAgent().jobMan.getPrimaryJob().tamable;
					if (!tamable.isTame()) {
						tamable.tameBy(CoroUtilEntity.getName(((EntityPlayer)getThrower())));
						setDead();
					}
				}
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
		if (worldObj.isRemote) tickDeath();
		super.setDead();
	}
	
	@SideOnly(Side.CLIENT)
	public void tickAnimate() {
		int amount = 3 / (Minecraft.getMinecraft().gameSettings.particleSetting+1);
		
		//System.out.println(amount);
		
		for (int i = 0; i < amount; i++)
        {
        	/*double speed = 0.15D;
        	double speedInheritFactor = 0.5D;
        	EntityRotFX entityfx = new EntityIconFX(worldObj, posX, posY, posZ, (rand.nextDouble() - rand.nextDouble()) * speed, (rand.nextDouble() - rand.nextDouble()) * speed, (rand.nextDouble() - rand.nextDouble()) * speed, ParticleRegistry.squareGrey, Minecraft.getMinecraft().renderEngine);
        	entityfx.motionX += (motionX * speedInheritFactor);
        	entityfx.motionZ += (motionZ * speedInheritFactor);
        	entityfx.setGravity(0F);
			entityfx.setRBGColorF(0.4F + (rand.nextFloat() * 0.4F), 0, 0);
			entityfx.rotationYaw = rand.nextInt(360);
			entityfx.rotationPitch = rand.nextInt(360);
			entityfx.setMaxAge(30+rand.nextInt(30));
			entityfx.spawnAsWeatherEffect();*/
			
			double speed = 0.01D;
			EntityRotFX entityfx = pm.spawnNewParticleIconFX(worldObj, ParticleRegistry.smoke, posX + rand.nextDouble(), posY + 0.2D + rand.nextDouble() * 0.2D, posZ + rand.nextDouble(), (rand.nextDouble() - rand.nextDouble()) * speed, 0.03D, (rand.nextDouble() - rand.nextDouble()) * speed);
        	pm.setParticleRandoms(entityfx, true, true);
        	pm.setParticleFire(entityfx);
			entityfx.setMaxAge(120+rand.nextInt(90));
			entityfx.setScale(0.55F + 0.2F * rand.nextFloat());
			entityfx.spawnAsWeatherEffect();
			pm.particles.add(entityfx);
			
			//ExtendedRenderer.rotEffRenderer.addEffect(entityfx);
        }
		
		//pm.tickUpdateSmoke();
	}
	
	@SideOnly(Side.CLIENT)
	public void tickDeath() {
		if (!hasDeathTicked) {
			//System.out.println("client: " + posX);
			hasDeathTicked = true;
			if (pm == null) return;
			
			int amount = 20 / (Minecraft.getMinecraft().gameSettings.particleSetting+1);
			
			//System.out.println(amount);
			
			for (int i = 0; i < amount; i++)
	        {
	        	/*
	        	double speedInheritFactor = 0.5D;
	        	EntityRotFX entityfx = new EntityFireballFX(worldObj, posX, posY, posZ, 0D, 0D, 0D, TropicraftItems.fireBall.particles[0], Minecraft.getMinecraft().renderEngine);
	        	entityfx.motionX += (motionX * speedInheritFactor);
	        	entityfx.motionZ += (motionZ * speedInheritFactor);
	        	
				entityfx.particleGravity = 0.5F;
				entityfx.rotationYaw = rand.nextInt(360);
				entityfx.setMaxAge(30+rand.nextInt(30));
				entityfx.spawnAsWeatherEffect();*/
				double speed = 0.01D;
				EntityRotFX entityfx = pm.spawnNewParticleIconFX(worldObj, ParticleRegistry.smoke, posX + rand.nextDouble(), posY + 0.2D + rand.nextDouble() * 0.2D, posZ + rand.nextDouble(), (rand.nextDouble() - rand.nextDouble()) * speed, 0.03D, (rand.nextDouble() - rand.nextDouble()) * speed);
	        	pm.setParticleRandoms(entityfx, true, true);
	        	pm.setParticleFire(entityfx);
				entityfx.setMaxAge(220+rand.nextInt(90));
				entityfx.spawnAsWeatherEffect();
				pm.particles.add(entityfx);
	        }
		}
	}
}
