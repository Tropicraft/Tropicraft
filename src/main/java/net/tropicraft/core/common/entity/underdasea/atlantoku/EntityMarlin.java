package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityMarlin extends EntityTropicraftWaterBase implements IPredatorDiet{

	public EntityMarlin(World world) {
		super(world);
		this.setSize(1.4f, 0.95f);	
		this.setSwimSpeeds(0.8f, 3f, 2f, 2f, 5f);
		this.setExpRate(5);
		this.setFishable(true);
		this.setDropStack(ItemRegistry.freshMarlin, 3);
		this.setTexture("marlin");
		if(!world.isRemote) {
			if(rand.nextInt(50) == 0) {
				this.setTexture("marlin2");
			}
		}
		this.setMaxHealth(5);
	}
	
	@Override
	public Class<?>[] getPreyClasses() {
		return new Class[]{
				EntityTropicalFish.class
				};
	}
}