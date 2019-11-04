package extendedrenderer.particle.behavior;

import CoroUtil.util.Vec3;
import extendedrenderer.particle.entity.EntityRotFX;

public class ParticleBehaviorMiniTornado extends ParticleBehaviors {

	//Externally updated variables, adjusting how templated behavior works
	public int curTick = 0;
	public int ticksMax = 1;
	
	public ParticleBehaviorMiniTornado(Vec3 source) {
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
		float greyScale = 0.5F + (rand.nextFloat() * 0.3F);
		particle.setColor(greyScale, greyScale, greyScale);
		
		particle.setScale(0.25F + 0.2F * rand.nextFloat());
		particle.brightness = 1F;
		particle.setScale(0.5F + rand.nextFloat() * 0.5F);
		particle.spawnY = (float) particle.getPosY();
		//particle.noClip = true;
		particle.setCanCollide(false);
		particle.isTransparent = false;
		//entityfx.spawnAsWeatherEffect();
		
		particle.setMaxAge(100);
		
		return particle;
	}

	@Override
	public void tickUpdateAct(EntityRotFX particle) {
		
		//for (int i = 0; i < particles.size(); i++) {
			//EntityRotFX particle = particles.get(i);
			
			if (/*curTick == 0 || */!particle.isAlive()) {
				particles.remove(particle);
			} else {
				/*double centerX = coordSource.xCoord + 0.0D;
				double centerY = coordSource.yCoord + 0.5D;
				double centerZ = coordSource.zCoord + 0.0D;
				
				
				
				double vecX = centerX - particle.getPosX();
				double vecZ = centerZ - particle.getPosZ();
				double rotYaw = (float)(Math.atan2(vecZ, vecX) * 180.0D / Math.PI);
				rotYaw -= 75D;// + (15D * curTickCharge / ticksToCharge);
				double speed = 0.01D + (0.50D * curTick / ticksMax);*/
				/*particle.setMotionX(Math.cos(rotYaw * 0.017453D) * speed);
				particle.setMotionZ(Math.sin(rotYaw * 0.017453D) * speed);
				int cycle = 60;*/
				
				particle.setMotionX(0);
				particle.setMotionY(0);
				particle.setMotionZ(0);
				
				//particle.setPosY(coordSource.yCoord - 3);
				double x = particle.getPosX();
				double y = particle.getPosY();
				double z = particle.getPosZ();

				double age = particle.getAge();
				double ageOffset = age + particle.getEntityId();
				
				double yAdj = age * 0.01D;
				
				double ageScale;
				
				double distFromCenter = 0.2D + (yAdj * 0.3D);
				
				ageScale = (Math.PI / 45) * ageOffset * 3D;
				
				//System.out.println(ageScale);
				
				double timeAdj = Math.toRadians(particle.getWorld().getGameTime() % 360);
				
				double twistScale = 0.035D * Math.sin(timeAdj);
				
				double centerX = coordSource.xCoord;// + Math.sin(age * twistScale);
				double centerZ = coordSource.zCoord;// + Math.cos(age * twistScale);
				
				x = centerX + (Math.sin(ageScale) * distFromCenter);
				z = centerZ + (Math.cos(ageScale) * distFromCenter);
				
				particle.setPosition(x, coordSource.yCoord + yAdj, z);
				
				double var16 = centerX - x;
                double var18 = centerZ - z;
                particle.rotationYaw = (float)Math.toDegrees(Math.atan2(var18, var16)) + 90;
				
				/*
				*/
			}
		//}
	}
}
