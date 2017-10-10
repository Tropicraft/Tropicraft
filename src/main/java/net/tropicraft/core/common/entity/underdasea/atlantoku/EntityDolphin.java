package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityDolphin extends EntityTropicraftWaterBase{

	public EntityDolphin(World world) {
		super(world);
		this.setSwimSpeeds(2f, 2f, 1.5f, 3f, 5f);
		this.setSize(1.4F, 0.5F);
		this.setExpRate(5);
		this.setTexture("dolphin");
		
		if(!world.isRemote) {
			if(rand.nextInt(50) == 0) {
				this.setTexture("dolphin2");
			}
		}
		this.setDropStack(ItemRegistry.fertilizer, 3);
	}
	
	public EntityDolphin(EntityTropicraftWaterBase l) {
		this(l.world);
	}
}