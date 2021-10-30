package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.item.TropicraftItems;

import static java.lang.Math.PI;

public class EagleRayEntity extends AbstractFish {

	/**
	 * Number of joints the wings have. End points included.
	 */
	public static final int WING_JOINTS = 10;
	/**
	 * Number of ticks that one wing animation cycle takes.
	 */
	public static final int WING_CYCLE_TICKS = 3*20; // 3 seconds
	/**
	 * How many sine function phases to go through. Higher = more wave crests.
	 */
	public static final float PHASES = 0.33f;

	/**
	 * Wave amplitudes at the joints, between -1 and 1.
	 */
	private float[] wingAmplitudes = new float[WING_JOINTS];
	/**
	 * Wave amplitudes at the joints, between -1 and 1, from previous tick.
	 */
	private float[] prevWingAmplitudes = new float[WING_JOINTS];

	/**
	 * Counter from 0 to WING_CYCLE_TICKS for wing animation progress.
	 */
	private int animationTicks;

	public EagleRayEntity(EntityType<? extends EagleRayEntity> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractFish.createAttributes()
				.add(Attributes.MAX_HEALTH, 10.0);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		return InteractionResult.PASS;
	}

	@Override
	public void tick() {
		super.tick();
		//this.setSwimSpeeds(1f, 0.2f, 0.2f);

		if (level.isClientSide) {
			if (animationTicks < WING_CYCLE_TICKS) {
				animationTicks++;
			} else {
				animationTicks = 0;
			}

			updateWingAmplitudes();
		}
	}

	private void updateWingAmplitudes() {
		float[] temp = prevWingAmplitudes;
		prevWingAmplitudes = wingAmplitudes;
		wingAmplitudes = temp;

		// 1 because amplitude at the wing base is 0
		for (int i = 1; i < WING_JOINTS; i++) {
			wingAmplitudes[i] = amplitudeFunc(i);
		}
	}

	private float decayFunc(float n) {
		return n/(WING_JOINTS-1f);
	}

	private float amplitudeFunc(float n) {
		double angle = 2 * PI * -n / (WING_JOINTS - 1f);
		return decayFunc(n) * Mth.sin((float) (getAnimationProgress() * 2 * PI + PHASES * angle));
	}

	private float getAnimationProgress() {
		return animationTicks / (float)WING_CYCLE_TICKS;
	}

	public float[] getWingAmplitudes() {
		return wingAmplitudes;
	}

	public float[] getPrevWingAmplitudes() {
		return prevWingAmplitudes;
	}

	@Override
	protected ItemStack getBucketItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.SALMON_FLOP;
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(TropicraftItems.EAGLE_RAY_SPAWN_EGG.get());
	}
}
