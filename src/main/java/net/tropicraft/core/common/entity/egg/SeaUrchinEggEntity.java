package net.tropicraft.core.common.entity.egg;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;
import net.tropicraft.core.common.item.TropicraftItems;

public class SeaUrchinEggEntity extends EchinodermEggEntity {
	public SeaUrchinEggEntity(final EntityType<? extends SeaUrchinEggEntity> type, Level world) {
		super(type, world);
	}

	@Override
	public String getEggTexture() {
		return "seaurchinegg";
	}

	@Override
	public Entity onHatch() {
		return new SeaUrchinEntity(TropicraftEntities.SEA_URCHIN.get(), level());
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(TropicraftItems.SEA_URCHIN_SPAWN_EGG.get());
	}
}
