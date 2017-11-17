package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.hostile.EntityTropiSpider;

public class EntityTropiSpiderEgg extends EntityEgg{

	public int motherID;
	
	public EntityTropiSpiderEgg(World par1World) {
		super(par1World);
	}
	
	public EntityTropiSpiderEgg(EntityTropiSpider mother) {
		this(mother.world);
		motherID = mother.getEntityId();
	}

	@Override
	public boolean shouldEggRenderFlat() {
		return false;
	}

	@Override
	public String getEggTexture() {
		return "spideregg";
	}

	@Override
	public Entity onHatch() {
		EntityTropiSpider spider = new EntityTropiSpider(world, motherID);
		return spider;
	}

	@Override
	public int getHatchTime() {
		return 2000;
	}

	@Override
	public int getPreHatchMovement() {
		return 20;
	}

}
