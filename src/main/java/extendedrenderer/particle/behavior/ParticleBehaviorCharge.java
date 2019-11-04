package extendedrenderer.particle.behavior;

import CoroUtil.util.Vec3;
import extendedrenderer.particle.entity.EntityRotFX;

public class ParticleBehaviorCharge extends ParticleBehaviors {

	//Externally updated variables, adjusting how templated behavior works
	public int curTick = 0;
	public int ticksMax = 1;
	
	public ParticleBehaviorCharge(Vec3 source) {
		super(source);
	}
	
	public EntityRotFX initParticle(EntityRotFX particle) {
		super.initParticle(particle);
		
		//particle.particleGravity = 0.5F;
		particle.rotationYaw = rand.nextInt(360);
		//particle.rotationPitch = rand.nextInt(360);
		particle.setMaxAge(1+rand.nextInt(10));
		particle.setGravity(0F);
		particle.setColor(72F/255F, 239F/255F, 8F/255F);
		//red
		particle.setColor(0.6F + (rand.nextFloat() * 0.4F), 0.2F + (rand.nextFloat() * 0.7F), 0);
		//green
		//particle.setColor(0, 0.4F + (rand.nextFloat() * 0.4F), 0);
		//tealy blue
		//particle.setColor(0, 0.4F + (rand.nextFloat() * 0.4F), 0.4F + (rand.nextFloat() * 0.4F));
		//particle.setColor(0.4F + (rand.nextFloat() * 0.4F), 0.4F + (rand.nextFloat() * 0.4F), 0.4F + (rand.nextFloat() * 0.4F));
		particle.setScale(0.25F + 0.2F * rand.nextFloat());
		particle.brightness = 1F;
		particle.setScale(0.1F + rand.nextFloat() * 0.5F);
		particle.spawnY = (float) particle.getPosY();
		//particle.noClip = true;
		particle.setCanCollide(false);
		//entityfx.spawnAsWeatherEffect();
		
		return particle;
	}

	@Override
	public void tickUpdateAct(EntityRotFX particle) {
		
		//for (int i = 0; i < particles.size(); i++) {
			//EntityRotFX particle = particles.get(i);
			
			if (curTick == 0 || !particle.isAlive()) {
				particles.remove(particle);
			} else {
				double centerX = coordSource.xCoord + 0.0D;
				double centerY = coordSource.yCoord + 0.5D;
				double centerZ = coordSource.zCoord + 0.0D;
				
				double vecX = centerX - particle.getPosX();
				double vecZ = centerZ - particle.getPosZ();
				double rotYaw = (float)(Math.atan2(vecZ, vecX) * 180.0D / Math.PI);
				rotYaw -= 75D;// + (15D * curTickCharge / ticksToCharge);
				double speed = 0.01D + (0.50D * curTick / ticksMax);
				particle.setMotionX(Math.cos(rotYaw * 0.017453D) * speed);
				particle.setMotionZ(Math.sin(rotYaw * 0.017453D) * speed);
				int cycle = 60;
				
				if (/*curTickCharge > 100 && */curTick + 20 < ticksMax) {
					if (particle.getAge() % cycle < cycle/2) {
						particle.setGravity(-0.02F);
					} else {
						particle.setGravity(0.09F);
					}
				} else {
					if (particle.getPosY() > (double)coordSource.yCoord + 1D) {
						particle.setGravity(0.15F);
					} else {
						particle.setGravity(-0.15F);
					}
					
					//particle.posY = yCoord + 1D;
					//particle.motionY = 0D;
					//particle.setGravity(0F);
				}
			}
		//}
	}
}
