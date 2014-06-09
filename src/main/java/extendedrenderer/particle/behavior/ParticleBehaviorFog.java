package extendedrenderer.particle.behavior;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import extendedrenderer.ExtendedRenderer;
import extendedrenderer.particle.ParticleRegistry;
import extendedrenderer.particle.entity.EntityIconFX;
import extendedrenderer.particle.entity.EntityRotFX;

public class ParticleBehaviorFog extends ParticleBehaviors {

	//Externally updated variables, adjusting how templated behavior works
	public int curTick = 0;
	public int ticksMax = 1;
	
	public ParticleBehaviorFog(Vec3 source) {
		super(source);
	}
	
	public EntityRotFX initParticle(EntityRotFX particle) {
		super.initParticle(particle);
		
		//particle.particleGravity = 0.5F;
		//fog
		particle.rotationYaw = rand.nextInt(360);
		particle.rotationPitch = rand.nextInt(50)-rand.nextInt(50);
		
		//cloud
		particle.rotationYaw = rand.nextInt(360);
		particle.rotationPitch = -90+rand.nextInt(50)-rand.nextInt(50);
		
		particle.setMaxAge(650+rand.nextInt(10));
		particle.setGravity(0.01F);
		float randFloat = (rand.nextFloat() * 0.6F);
		float baseBright = 0.7F;
		float finalBright = Math.min(1F, baseBright+randFloat);
		particle.setRBGColorF(finalBright, finalBright, finalBright);
		//particle.setRBGColorF(72F/255F, 239F/255F, 8F/255F);
		
		//sand
		//particle.setRBGColorF(204F/255F, 198F/255F, 120F/255F);
		//red
		//particle.setRBGColorF(0.6F + (rand.nextFloat() * 0.4F), 0.2F + (rand.nextFloat() * 0.7F), 0);
		//green
		//particle.setRBGColorF(0, 0.4F + (rand.nextFloat() * 0.4F), 0);
		//tealy blue
		//particle.setRBGColorF(0, 0.4F + (rand.nextFloat() * 0.4F), 0.4F + (rand.nextFloat() * 0.4F));
		//particle.setRBGColorF(0.4F + (rand.nextFloat() * 0.4F), 0.4F + (rand.nextFloat() * 0.4F), 0.4F + (rand.nextFloat() * 0.4F));
		
		//location based color shift
		//particle.setRBGColorF((float) (0.4F + (Math.abs(particle.posX / 300D) * 0.6D)), 0.4F, (float) (0.4F + (Math.abs(particle.posZ / 300D) * 0.6D)));
		particle.setScale(0.25F + 0.2F * rand.nextFloat());
		particle.brightness = 1F;
		particle.setAlphaF(0);
		
		float sizeBase = (float) (500+(rand.nextDouble()*40));
		
		particle.setScale(sizeBase);
		//particle.spawnY = (float) particle.posY;
		particle.noClip = false;
		//entityfx.spawnAsWeatherEffect();
		
		particle.renderRange = 2048;
		
		return particle;
	}

	@Override
	public void tickUpdateAct(EntityRotFX particle) {
		//particle.particleScale = 900;
		//particle.rotationPitch = 30;
		//for (int i = 0; i < particles.size(); i++) {
			//EntityRotFX particle = particles.get(i);
			
			if (particle.isDead) {
				particles.remove(particle);
			} else {
				if (particle.getEntityId() % 2 == 0) {
					particle.rotationYaw -= 0.1;
				} else {
					particle.rotationYaw += 0.1;
				}
				
				float ticksFadeInMax = 50;
				float ticksFadeOutMax = 50;
				
				if (particle.getAge() < ticksFadeInMax) {
					//System.out.println("particle.getAge(): " + particle.getAge());
					particle.setAlphaF(particle.getAge() / ticksFadeInMax);
					//particle.setAlphaF(1);
				} else if (particle.getAge() > particle.getMaxAge() - ticksFadeOutMax) {
					float count = particle.getAge() - (particle.getMaxAge() - ticksFadeOutMax);
					float val = (ticksFadeOutMax - (count)) / ticksFadeOutMax;
					//System.out.println(val);
					particle.setAlphaF(val);
				} else {
					/*if (particle.getAlphaF() > 0) {
						particle.setAlphaF(particle.getAlphaF() - rateAlpha*1.3F);
					} else {
						particle.setDead();
					}*/
				}
				double moveSpeed = 0.001D;
				if (particle.onGround) {
					moveSpeed = 0.012D;
					particle.motionY += 0.01D;
				}
				
				if (particle.isCollidedHorizontally) {
					particle.rotationYaw += 0.1;
				}
				
				particle.motionX -= Math.sin(Math.toRadians((particle.rotationYaw + particle.getEntityId()) % 360)) * moveSpeed;
				particle.motionZ += Math.cos(Math.toRadians((particle.rotationYaw + particle.getEntityId()) % 360)) * moveSpeed;
				
				double moveSpeedRand = 0.005D;
				
				particle.motionX += rand.nextDouble() * moveSpeedRand - rand.nextDouble() * moveSpeedRand;
				particle.motionZ += rand.nextDouble() * moveSpeedRand - rand.nextDouble() * moveSpeedRand;
				
				particle.setScale(particle.getScale() - 0.1F);
				
				if (particle.spawnY != -1) {
					particle.setPosition(particle.posX, particle.spawnY, particle.posZ);
					//particle.posY = particle.spawnY;
				}
				
				//particle.setDead();
			}
		//}
	}
}
