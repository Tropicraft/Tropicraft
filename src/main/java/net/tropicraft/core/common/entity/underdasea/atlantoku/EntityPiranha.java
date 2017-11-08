package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;
import net.tropicraft.core.common.entity.egg.EntityEchinodermEgg;
import net.tropicraft.core.common.entity.egg.EntitySeaUrchinEgg;
import net.tropicraft.core.common.entity.egg.EntityStarfishEgg;
import net.tropicraft.core.common.entity.underdasea.EntityEchinoderm;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityPiranha extends EntitySchoolableFish implements IAtlasFish, IPredatorDiet{

	public EntityPiranha(World world) {
		super(world);
		this.setSwimSpeeds(0.8f, 3f, 4f, 2f, 5f);
		this.setSize(0.3F, 0.4F);
		this.setExpRate(5);
		this.setDropStack(ItemRegistry.fertilizer, 3);
	}

	@Override
	public int getAtlasSlot() {
		return 9;
	}

	@Override
	public Class[] getPreyClasses() {
		return new Class[]{
				EntityTropicalFish.class, EntitySeahorse.class, 
				EntityEchinoderm.class, EntityEchinodermEgg.class,
				EntityLand.class, EntityPlayer.class, EntityAnimal.class
			};
	}

}