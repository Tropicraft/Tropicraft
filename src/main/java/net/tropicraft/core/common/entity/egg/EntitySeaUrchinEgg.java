package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.underdasea.EntitySeaUrchin;

public class EntitySeaUrchinEgg extends EntityEchinodermEgg {
	public EntitySeaUrchinEgg(World world) {
		super(world);
	}


	@Override
	public String getEggTexture() {
		return "seaurchinegg";
	}

	@Override
	public Entity onHatch() {
		return new EntitySeaUrchin(world, true);
	}

}