package net.tropicraft.entity.underdasea;

import net.minecraft.world.World;

public class EntitySeaUrchinEgg extends EntityEchinodermEgg {
	public EntitySeaUrchinEgg(World world) {
		super(world);
		//texture = "/mods/"+ModInfo.MODID+"/textures/entities/seaurchinegg.png";
	}

	@Override
	public EntityEchinoderm createBaby() {
		return new EntitySeaUrchin(worldObj, true);
	}
}