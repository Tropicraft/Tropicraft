package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityPiranha extends EntityTropicraftWaterBase implements IAtlasFish{

	public EntityPiranha(World world) {
		super(world);
		this.setSwimSpeeds(0.8f, 3f, 2f, 2f, 5f);
		this.setSize(0.3F, 0.4F);
		this.setHostile();
		this.setExpRate(5);
		this.setDropStack(ItemRegistry.blackPearl, 3);
		this.setSchoolable();
	}
	
	public EntityPiranha(EntityTropicraftWaterBase l) {
		this(l.world);
	}

	@Override
	public int getAtlasSlot() {
		return 9;
	}

}