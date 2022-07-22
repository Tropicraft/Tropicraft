package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.item.TropicraftItems;

public class SeahorseEntity extends AbstractTexturedFishEntity {
	private final static String[] SEAHORSE_TEXTURE_NAMES = new String[] {"razz", "blue", "cyan", "yellow", "green", "orange"};

	public SeahorseEntity(EntityType<? extends SeahorseEntity> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractFish.createAttributes()
				.add(Attributes.MAX_HEALTH, 4.0);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		return InteractionResult.PASS;
	}

	@Override
	String getRandomTexture() {
		return SEAHORSE_TEXTURE_NAMES[random.nextInt(SEAHORSE_TEXTURE_NAMES.length)];
	}

	@Override
	String getDefaultTexture() {
		return SEAHORSE_TEXTURE_NAMES[0];
	}

	@Override
	public ItemStack getBucketItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.SALMON_FLOP;
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(TropicraftItems.SEAHORSE_SPAWN_EGG.get());
	}
}
