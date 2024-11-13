package net.tropicraft.core.common.entity.passive;

import net.minecraft.SharedConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.BinaryAnimation;
import net.tropicraft.core.common.Easings;
import net.tropicraft.core.common.entity.IkWalker;

public class ShoebillStorkEntity extends Animal {
	public static final float BASE_FOOT_Z = 0.0152f;

	private final IkWalker walker = new IkWalker(
			6,
			4,
			SharedConstants.TICKS_PER_SECOND,
			0.25f
	);
	// Positions from actual model coordinates of the feet
	private final IkWalker.Foot leftFoot = walker.addFoot(BASE_FOOT_Z, -0.1168f);
	private final IkWalker.Foot rightFoot = walker.addFoot(BASE_FOOT_Z, 0.1168f);

	private final BinaryAnimation flightAnimation = new BinaryAnimation(3, Easings::inOutSine);

	public ShoebillStorkEntity(EntityType<? extends ShoebillStorkEntity> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0)
				.add(Attributes.MOVEMENT_SPEED, 0.08);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(0, new PanicGoal(this, 1.25));
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
		goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(ItemTags.FISHES), false));
		goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide()) {
			Vec3 deltaMovement = getDeltaMovement();
			if (!moveControl.hasWanted() && !onGround() && deltaMovement.y < 0.0) {
				setDeltaMovement(deltaMovement.multiply(1.0, 0.8, 1.0));
			}
		}
	}

	@Override
	public void calculateEntityAnimation(boolean includeHeight) {
		super.calculateEntityAnimation(includeHeight);
		if (level().isClientSide()) {
			flightAnimation.tick(!onGround());
			walker.update(this);
		}
	}

	@Override
	public void absMoveTo(double x, double y, double z) {
		super.absMoveTo(x, y, z);
		walker.reset(this);
	}

	@Override
	public void moveTo(double x, double y, double z, float yRot, float xRot) {
		super.moveTo(x, y, z, yRot, xRot);
		walker.reset(this);
	}

	public IkWalker.Foot leftFoot() {
		return leftFoot;
	}

	public IkWalker.Foot rightFoot() {
		return rightFoot;
	}

	public float getFlightAnimation(float partialTicks) {
		return flightAnimation.get(partialTicks);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Override
	public ShoebillStorkEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
		return null;
	}
}
