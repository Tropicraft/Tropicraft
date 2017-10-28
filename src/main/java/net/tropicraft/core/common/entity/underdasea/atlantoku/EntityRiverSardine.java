package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityRiverSardine extends EntitySchoolableFish implements IAtlasFish{

	public EntityRiverSardine(World world) {
		super(world);
		this.setSwimSpeeds(0.8f, 3f, 2f, 2f, 5f);
		this.setSize(0.3F, 0.4F);
		this.setExpRate(5);
		this.setDropStack(ItemRegistry.fertilizer, 3);
		this.setFleesPlayers(true, 5D);
		this.setSchoolSizeRange(1, 20);
	}

	@Override
	public int getAtlasSlot() {
		return 8;
	}

}