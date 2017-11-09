package net.tropicraft.core.common.entity.egg;

import net.minecraft.world.World;

public abstract class EntityEchinodermEgg extends EntityEgg{

	public EntityEchinodermEgg(World par1World) {
		super(par1World);
	}

	@Override
	public boolean shouldEggRenderFlat() {
		return true;
	}

	@Override
	public int getHatchTime() {
		return 2*60*20;
	}

	@Override
	public int getPreHatchMovement() {
		return 0;
	}
	
    @Override
	public boolean canBreatheUnderwater() {
		return true;
	}

}
