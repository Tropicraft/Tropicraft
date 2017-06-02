package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.world.World;

public class EntitySeaUrchinEgg extends EntityEchinodermEgg {
	public EntitySeaUrchinEgg(World world) {
		super(world);
	}

	@Override
	public EntityEchinoderm createBaby() {
		return new EntitySeaUrchin(worldObj, true);
	}
}