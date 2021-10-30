package net.tropicraft.core.common.entity.egg;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;
import net.tropicraft.core.common.item.TropicraftItems;

public class SeaUrchinEggEntity extends EchinodermEggEntity {
	public SeaUrchinEggEntity(final EntityType<? extends SeaUrchinEggEntity> type, World world) {
		super(type, world);
	}

	@Override
	public String getEggTexture() {
		return "seaurchinegg";
	}

	@Override
	public Entity onHatch() {
		return new SeaUrchinEntity(TropicraftEntities.SEA_URCHIN.get(), level);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.SEA_URCHIN_SPAWN_EGG.get());
	}
}
