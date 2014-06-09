package extendedrenderer.particle.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.entity.EntityIconFX;
import extendedrenderer.particle.entity.EntityIconWindFX;
import extendedrenderer.particle.entity.EntityRotFX;

@SideOnly(Side.CLIENT)
public class ParticleBehaviors {

	public List<EntityRotFX> particles = new ArrayList<EntityRotFX>();
	public Vec3 coordSource;
	public Entity sourceEntity = null;
	public Random rand = new Random();
	
	//Visual tweaks
	public float rateDarken = 0.025F;
	public float rateBrighten = 0.010F;
	public float rateBrightenSlower = 0.003F;
	public float rateAlpha = 0.002F;
	public float rateScale = 0.1F;
	public int tickSmokifyTrigger = 40;
	
	public ParticleBehaviors(Vec3 source) {
		coordSource = source;
	}
	
	public void tickUpdateList() { //shouldnt be used, particles tick their own method, who removes it though?
		for (int i = 0; i < particles.size(); i++) {
			EntityRotFX particle = particles.get(i);
			
			if (particle.isDead) {
				particles.remove(particle);
			} else {
				tickUpdate(particle);
			}
		}
	}
	
	public void tickUpdate(EntityRotFX particle) {
		
		if (sourceEntity != null) {
			coordSource = Vec3.createVectorHelper(sourceEntity.posX, sourceEntity.posY, sourceEntity.posZ);
		}
		
		tickUpdateAct(particle);
	}
	
	//default is smoke effect, override for custom
	public void tickUpdateAct(EntityRotFX particle) {
		
			
		double centerX = particle.posX;
		double centerY = particle.posY;
		double centerZ = particle.posZ;
		
		if (coordSource != null) {
			centerX = coordSource.xCoord/* + 0.5D*/;
			centerY = coordSource.yCoord/* + 0.5D*/;
			centerZ = coordSource.zCoord/* + 0.5D*/;
		}
		
		double vecX = centerX - particle.posX;
		double vecZ = centerZ - particle.posZ;
		double distToCenter = Math.sqrt(vecX * vecX + vecZ * vecZ);
		double rotYaw = (float)(Math.atan2(vecZ, vecX) * 180.0D / Math.PI);
		double adjYaw = Math.min(360, 45+particle.getAge());
		
		rotYaw -= adjYaw;
		//rotYaw -= 90D;
		//rotYaw += 20D;
		double speed = 0.1D;
		if (particle.getAge() < 25 && distToCenter > 0.05D) {
			particle.motionX = Math.cos(rotYaw * 0.017453D) * speed;
			particle.motionZ = Math.sin(rotYaw * 0.017453D) * speed;
		} else {
			double speed2 = 0.008D;
			
			double pSpeed = Math.sqrt(particle.motionX * particle.motionX + particle.motionZ * particle.motionZ);
			
			//cheap air search code
			if (pSpeed < 0.2 && particle.motionY < 0.01) {
				speed2 = 0.08D;
			}
			
			if (pSpeed < 0.002 && Math.abs(particle.motionY) < 0.02) {
				particle.motionY -= 0.15D;
			}
			
			particle.motionX += (rand.nextDouble() - rand.nextDouble()) * speed2;
			particle.motionZ += (rand.nextDouble() - rand.nextDouble()) * speed2;
			
		}
		int cycle = 40;
		
		float brightnessShiftRate = rateDarken;
		
		int stateChangeTick = tickSmokifyTrigger;
		
		if (particle.getAge() < stateChangeTick) {
			particle.setGravity(-0.2F);
			particle.setRBGColorF(particle.getRedColorF() - brightnessShiftRate, particle.getGreenColorF() - brightnessShiftRate, particle.getBlueColorF() - brightnessShiftRate);
		} else if (particle.getAge() == stateChangeTick) {
			particle.setRBGColorF(0,0,0);
		} else {
			brightnessShiftRate = rateBrighten;
			particle.setGravity(-0.05F);
			//particle.motionY *= 0.99F;
			if (particle.getRedColorF() < 0.3F) {
				
			} else {
				brightnessShiftRate = rateBrightenSlower;
			}
			
			particle.setRBGColorF(particle.getRedColorF() + brightnessShiftRate, particle.getGreenColorF() + brightnessShiftRate, particle.getBlueColorF() + brightnessShiftRate);
			
			if (particle.getAlphaF() > 0) {
				particle.setAlphaF(particle.getAlphaF() - rateAlpha);
			} else {
				particle.setDead();
			}
		}
		
		if (particle.getScale() < 8F) particle.setScale(particle.getScale() + rateScale);
		
		/*if (particle.getAge() % cycle < cycle/2) {
			particle.setGravity(-0.02F);
		} else {*/
			
		//}
			
		
	}
	
	public void tickUpdateCloud(EntityRotFX particle) {
		particle.rotationYaw -= 0.1;
		
		int ticksFadeInMax = 100;
		
		if (particle.getAge() < ticksFadeInMax) {
			//System.out.println("particle.getAge(): " + particle.getAge());
			particle.setAlphaF(particle.getAge() * 0.01F);
		} else {
			if (particle.getAlphaF() > 0) {
				particle.setAlphaF(particle.getAlphaF() - rateAlpha*1.3F);
			} else {
				particle.setDead();
			}
		}
	}
	
	public EntityRotFX spawnNewParticleWindFX(World world, IIcon icon, double x, double y, double z, double vecX, double vecY, double vecZ) {
		EntityRotFX entityfx = new EntityIconWindFX(world, x, y, z, vecX, vecY, vecZ, icon);
		entityfx.pb = this;
		return entityfx;
	}
	
	public EntityRotFX spawnNewParticleIconFX(World world, IIcon icon, double x, double y, double z, double vecX, double vecY, double vecZ) {
		return spawnNewParticleIconFX(world, icon, x, y, z, vecX, vecY, vecZ, -1);
	}
	
	public EntityRotFX spawnNewParticleIconFX(World world, IIcon icon, double x, double y, double z, double vecX, double vecY, double vecZ, int renderOrder) {
		EntityRotFX entityfx = new EntityIconFX(world, x, y, z, vecX, vecY, vecZ, icon);
		entityfx.pb = this;
		entityfx.renderOrder = renderOrder;
		return entityfx;
	}
	
	public EntityRotFX initParticle(EntityRotFX particle) {
		
		particle.prevPosX = particle.posX;
		particle.prevPosY = particle.posY;
		particle.prevPosZ = particle.posZ;
		particle.lastTickPosX = particle.posX;
		particle.lastTickPosY = particle.posY;
		particle.lastTickPosZ = particle.posZ;
		
		//keep AABB small, very important to performance
		particle.setSize(0.01F, 0.01F);
		
		return particle;
	}
	
	public static EntityRotFX setParticleRandoms(EntityRotFX particle, boolean yaw, boolean pitch) {
		Random rand = new Random();
		if (yaw) particle.rotationYaw = rand.nextInt(360);
		if (pitch) particle.rotationPitch = rand.nextInt(360);
		return particle;
	}
	
	public static EntityRotFX setParticleFire(EntityRotFX particle) {
		Random rand = new Random();
		particle.setRBGColorF(0.6F + (rand.nextFloat() * 0.4F), 0.2F + (rand.nextFloat() * 0.2F), 0);
		particle.setScale(0.25F + 0.2F * rand.nextFloat());
		particle.brightness = 1F;
		particle.setSize(0.1F, 0.1F);
		particle.setAlphaF(0.6F);
		return particle;
	}
	
	public static EntityRotFX setParticleCloud(EntityRotFX particle, float freezeY) {
		particle.spawnY = freezeY;
		particle.rotationPitch = 90F;
		particle.renderDistanceWeight = 999D;
        particle.noClip = true;
        particle.setSize(0.25F, 0.25F);
        particle.setScale(500F);
        //particle.particleScale = 200F;
        particle.callUpdateSuper = false;
        particle.callUpdatePB = false;
        particle.setMaxAge(500);
        particle.setRBGColorF(1F, 1F, 1F);
        particle.brightness = 0.3F;//- ((200F - particle.spawnY) * 0.05F);
        particle.renderRange = 999F;
        particle.setAlphaF(0F);
		return particle;
	}
	
	public void cleanup() {
		
	}
	
}
