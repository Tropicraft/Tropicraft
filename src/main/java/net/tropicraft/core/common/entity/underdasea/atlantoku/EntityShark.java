package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityShark extends EntityTropicraftWaterBase implements IPredatorDiet{

	public EntityShark(World world) {
		super(world);
		this.setSwimSpeeds(1.3f, 2f, 0.2f, 3f, 5f);
		this.setSize(1.4F, 0.5F);
		this.setExpRate(5);
		this.setApproachesPlayers(true);

		this.setDropStack(ItemRegistry.fertilizer, 3);
	}
	
	@Override
	public String[] getTexturePool() {
		return new String[]{"hammerhead1", "hammerhead2", "hammerhead3"};
	}
	
	@Override
	public Class[] getPreyClasses() {
		return new Class[]{ EntityTropicalFish.class, EntityPiranha.class, 
				EntityMarlin.class, EntityPlayer.class, EntitySeahorse.class, EntityLand.class};
	}
}