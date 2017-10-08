package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityDolphin extends EntityTropicraftWaterBase{

	public EntityDolphin(World world) {
		super(world);
		this.setSwimSpeeds(2f, 3f, 0.6f, 3f, 5f);
		this.setSize(0.3F, 0.4F);
		this.setExpRate(5);
		this.setDropStack(ItemRegistry.fertilizer, 3);
	}
	
	public EntityDolphin(EntityTropicraftWaterBase l) {
		this(l.world);
	}
}