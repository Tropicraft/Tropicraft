package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.underdasea.EntityStarfish;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

public class EntityStarfishEgg extends EntityEchinodermEgg {
	private StarfishType starfishType;

	public EntityStarfishEgg(World par1World) {
		super(par1World);
		starfishType = StarfishType.values()[rand.nextInt(StarfishType.values().length)];
	}
	
	public EntityStarfishEgg(World world, StarfishType starfishType) {
		this(world);
		this.starfishType = starfishType;
	}


	@Override
	public String getEggTexture() {
		return "starfishegg";
	}

	@Override
	public Entity onHatch() {
		EntityStarfish baby = new EntityStarfish(world, true);
		baby.setStarfishType(starfishType);
		return baby;
	}

}