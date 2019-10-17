package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SeahorseEntity extends AbstractTexturedFishEntity {
	private final static String[] SEAHORSE_TEXTURE_NAMES = new String[] {"razz", "blue", "cyan", "yellow", "green", "orange"};

	public SeahorseEntity(EntityType<? extends SeahorseEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
	}

	@Override
	String getRandomTexture() {
		return SEAHORSE_TEXTURE_NAMES[rand.nextInt(SEAHORSE_TEXTURE_NAMES.length)];
	}

	@Override
	String getDefaultTexture() {
		return SEAHORSE_TEXTURE_NAMES[0];
	}

	@Override
	protected ItemStack getFishBucket() {
		return null;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return null;
	}
}
