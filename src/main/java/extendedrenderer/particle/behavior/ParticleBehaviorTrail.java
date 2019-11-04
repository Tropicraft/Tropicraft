package extendedrenderer.particle.behavior;

import java.util.Random;

import CoroUtil.util.Vec3;
import extendedrenderer.particle.entity.EntityRotFX;

public class ParticleBehaviorTrail extends ParticleBehaviors {

	public ParticleBehaviorTrail(Vec3 source) {
		super(source);
	}


	
	public EntityRotFX initParticle(EntityRotFX particle) {
		super.initParticle(particle);
		
		//particle.particleGravity = 0.5F;
		particle.rotationYaw = rand.nextInt(360);
		//particle.rotationPitch = rand.nextInt(360);
		particle.setMaxAge(5+rand.nextInt(10));
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
		particle.setScale(0.1F + rand.nextFloat() * 0.9F);
		particle.spawnY = (float) particle.getPosY();
		//particle.noClip = true;
		particle.setCanCollide(false);
		//entityfx.spawnAsWeatherEffect();
		
		return particle;
	}

	@Override
	public void tickUpdateAct(EntityRotFX particle) {
		
		if (!particle.isAlive()) {
			particles.remove(particle);
		} else {
			double centerX = coordSource.xCoord + 0.0D;
			double centerY = coordSource.yCoord + 0.0D;
			double centerZ = coordSource.zCoord + 0.0D;
			
			double vecX = centerX - particle.getPosX();
			double vecY = centerY - particle.getPosY();
			double vecZ = centerZ - particle.getPosZ();
			Vec3 vec = new Vec3(vecX, vecY, vecZ);
			
			double radius = 1.6D;
			double amp = 0.005D * Math.max(1, (vec.length() * 0.01D))/* / (particle.getAge())*/;
			
			double rotSpeed = (particle.getAge()) * amp;
			
			
			Vec3 vec2 = new Vec3(vecX, vecY, vecZ);
			vec = vec.normalize();
			vec2 = vec2.normalize();
			
			/*vec.xCoord *= amp;
			vec.yCoord *= amp;
			vec.zCoord *= amp;*/
			
			vec = new Vec3(vec.xCoord * amp, vec.yCoord * amp, vec.zCoord * amp);
			
			//vec.rotateAroundY((float) (Math.toRadians(10/*rotSpeed * amp*/)));
			//vec.rotateAroundZ((float) (Math.toRadians(10/*rotSpeed * amp*/)));
			//vec.rotateAroundX((float) (Math.toRadians(10/*rotSpeed * amp*/)));
			
			//vec.rotateAroundZ((float) (Math.toRadians(particle.entityId % 10) * amp));
			//vec.rotateAroundX((float) (Math.toRadians(particle.entityId % 90) * amp));
			
			//Vec3 vec3 = vec.subtract(vec2);
			
			//particle.setPosition(centerX+vec2.xCoord, centerY+vec2.yCoord, centerZ+vec2.zCoord);
			/*particle.motionX += vec.xCoord;
			particle.motionY += vec.yCoord;
			particle.motionZ += vec.zCoord;*/
			
			Random rand = new Random();
			
			double randRange = 0.01D;
			
			particle.setMotionX(particle.getMotionX() + (rand.nextDouble() * randRange - randRange/2));
			particle.setMotionY(particle.getMotionY() + (rand.nextDouble() * randRange - randRange/2));
			particle.setMotionZ(particle.getMotionZ() + (rand.nextDouble() * randRange - randRange/2));
			
			double rotYaw = (float)(Math.atan2(vecZ, vecX) * 180.0D / Math.PI);
			double rotPitch = (float)(Math.atan2(vecY, vecX) * 180.0D / Math.PI);
			double rotRoll = (float)(Math.atan2(vecY, vecZ) * 180.0D / Math.PI);
			
			double rot = 30D;
			
			rotYaw -= rot;
			rotPitch -= rot;
			rotRoll -= rot;
			
			amp = 0.01D;
			double ampYaw = amp + amp * Math.sin(Math.toRadians(90+((particle.getWorld().getGameTime() * 5) % 180)));
			double ampPitch = 0.1D * amp * Math.min(10, particle.getAge() * 0.1D);//Math.cos(Math.toRadians(90+((particle.world.getGameTime() * 5) % 180)));
			double ampRoll = 0.1D * amp * Math.min(10, particle.getAge() * 0.1D);//amp + amp * Math.sin(Math.toRadians(((particle.world.getGameTime() * 15F) % 180)));
			
			/*particle.motionX += Math.cos(rotYaw * 0.017453D) * ampYaw;
			particle.motionZ += Math.sin(rotYaw * 0.017453D) * ampYaw;
			
			particle.motionX += Math.cos(rotPitch * 0.017453D) * ampPitch;
			particle.motionY += Math.sin(rotPitch * 0.017453D) * ampPitch;
			
			particle.motionZ -= Math.cos(rotRoll * 0.017453D) * ampRoll;
			particle.motionY -= Math.sin(rotRoll * 0.017453D) * ampRoll;*/
			
			/*double vecX = centerX - particle.posX;
			double vecY = centerY - particle.posY;
			double vecZ = centerZ - particle.posZ;
			double rotYaw = (float)(Math.atan2(vecZ, vecX) * 180.0D / Math.PI);
			rotYaw -= 55D;// + (15D * curTickCharge / ticksToCharge);
			double speed = 0.4D;// + (0.50D * curTick / ticksMax);
			particle.motionX = Math.cos(rotYaw * 0.017453D) * speed;
			particle.motionY = vecY * speed;
			particle.motionZ = Math.sin(rotYaw * 0.017453D) * speed;
			int cycle = 60;*/
		}
	}
}
