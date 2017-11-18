package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.tropicraft.core.common.sound.TropicraftSounds;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityDolphin extends EntityTropicraftWaterBase implements IPredatorDiet, IAmphibian{

	private static final DataParameter<Boolean> MOUTH_OPEN = EntityDataManager.<Boolean>createKey(EntityDolphin.class, DataSerializers.BOOLEAN);

	
	public EntityDolphin(World world) {
		super(world);
		this.setSwimSpeeds(2f, 2f, 1.5f, 3f, 5f);
		this.setSize(1.4F, 0.5F);
		this.setExpRate(5);
		this.setTexture("dolphin");
		this.setApproachesPlayers(true);
		if(!world.isRemote) {
			if(rand.nextInt(50) == 0) {
				this.setTexture("dolphin2");
			}
		}
		this.setDropStack(ItemRegistry.fertilizer, 3);
		this.setMaxHealth(10);
	}
	
	
	
	@Override
	public void entityInit() {
		super.entityInit();
		this.getDataManager().register(MOUTH_OPEN, false);
	}
	
	public void setMouthOpen(boolean b) {
		this.getDataManager().set(MOUTH_OPEN, b);
	}
	
	public boolean getMouthOpen() {
		return this.getDataManager().get(MOUTH_OPEN);
	}



	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.setAir(30);
		this.fallVelocity *= 0.5f;
		if(!world.isRemote) {
			if(!this.isInWater() && this.onGround && this.ticksExisted % 50 == 0) {
				this.motionY = 0.3f;
				this.motionX = this.randFlip(5)*0.01f;
				this.motionZ = this.randFlip(5)*0.01f;
				this.rotationYaw+=randFlip(15);
				this.swimYaw=this.rotationYaw;
				this.fallDistance = 0;
			}
			if(!this.isInWater()) {
				if(this.motionY < -0.2f) {
					this.motionY = -0.2f;
				}
			}
			if(this.livingSoundTime < -(getTalkInterval()-20)) {
				if(this.ticksExisted % 3 > 1) {
					if(!getMouthOpen()) {
						setMouthOpen(true);
					}
				}else {
					if(getMouthOpen()) {
						setMouthOpen(false);
					}
				}
			}else {
				if(getMouthOpen()) {
					setMouthOpen(false);
				}
			}
		}
	}
	
	@Override
	public int getTalkInterval() {
		return 300;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TropicraftSounds.DOLPHIN;
	}

	@Override
	public Class[] getPreyClasses() {
		return new Class[]{ EntityTropicalFish.class, EntityPiranha.class };
	}
}