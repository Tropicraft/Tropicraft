package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityMarlin extends EntityTropicraftWaterBase {

	public EntityMarlin(World world) {
		super(world);
		this.setSize(1.4f, 0.95f);	
		this.setSwimSpeeds(0.8f, 3f, 2f, 2f, 5f);
		this.setHostile();
		this.setExpRate(5);
		this.setDropStack(ItemRegistry.freshMarlin, 3);
		this.setTexture("marlin");
		if(!world.isRemote) {
			if(rand.nextInt(50) == 0) {
				this.setTexture("marlin2");
			}
		}
	}
}