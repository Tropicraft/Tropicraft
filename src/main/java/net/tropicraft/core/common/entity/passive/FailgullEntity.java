package net.tropicraft.core.common.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.*;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class FailgullEntity extends AnimalEntity implements IFlyingAnimal {

	private boolean isFlockLeader;
	private static final DataParameter<Optional<UUID>> FLOCK_LEADER_UUID = EntityDataManager.defineId(FailgullEntity.class, DataSerializers.OPTIONAL_UUID);

	public FailgullEntity(EntityType<? extends FailgullEntity> type, World world) {
		super(type, world);
		xpReward = 1;
		moveControl = new FlyingMovementController(this, 5, true);
		this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
		this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
		this.setPathfindingMalus(PathNodeType.FENCE, -1.0F);
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return AnimalEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 3.0)
				.add(Attributes.MOVEMENT_SPEED, 0.6)
				.add(Attributes.FLYING_SPEED, 0.9)
				.add(Attributes.FOLLOW_RANGE, 12.0);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(FLOCK_LEADER_UUID, Optional.empty());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);
		isFlockLeader = nbt.getBoolean("IsFlockLeader");
		if (nbt.contains("FlockLeader")) {
			setFlockLeader(Optional.of(nbt.getUUID("FlockLeader")));
		} else {
			setFlockLeader(Optional.empty());
		}
	}

	@Override
	public void addAdditionalSaveData(final CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("IsFlockLeader", isFlockLeader);
		entityData.get(FLOCK_LEADER_UUID).ifPresent(uuid -> nbt.putUUID("FlockLeader", uuid));
	}

	@Override
	public float getWalkTargetValue(final BlockPos pos, final IWorldReader worldIn) {
		return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	@Override
	public void registerGoals() {
		goalSelector.addGoal(0, new ValidateFlockLeader(this));
		goalSelector.addGoal(1, new SelectFlockLeader(this));
		goalSelector.addGoal(2, new SetTravelDestination());
		goalSelector.addGoal(2, new FollowLeaderGoal());
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.5F;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	protected boolean makeFlySound() {
		return false;
	}

	@Override
	protected PathNavigator createNavigation(World worldIn) {
		FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn) {
			public boolean isStableDestination(BlockPos pos) {
				return !this.level.getBlockState(pos.below()).isAir();
			}
		};
		flyingpathnavigator.setCanOpenDoors(false);
		flyingpathnavigator.setCanFloat(false);
		flyingpathnavigator.setCanPassDoors(true);
		return flyingpathnavigator;
	}

	private void poop() {
		if (!level.isClientSide && level.random.nextInt(20) == 0) {
			SnowballEntity s = new SnowballEntity(level, getX(), getY(), getZ());
			s.shoot(0, 0, 0, 0, 0);
			level.addFreshEntity(s);
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Nullable
	@Override
	public AgeableEntity getBreedOffspring(ServerWorld world, AgeableEntity partner) {
		return null;
	}

	private void setIsFlockLeader(final boolean isFlockLeader) {
		this.isFlockLeader = isFlockLeader;
	}

	private void setFlockLeader(final Optional<UUID> flockLeaderUUID) {
		entityData.set(FLOCK_LEADER_UUID, flockLeaderUUID);
	}

	private boolean getIsFlockLeader() {
		return isFlockLeader;
	}

	private boolean hasFlockLeader() {
		return entityData.get(FLOCK_LEADER_UUID).isPresent();
	}

	@Nullable
	private Entity getFlockLeader() {
		if (level instanceof ServerWorld && hasFlockLeader()) {
			return ((ServerWorld) level).getEntity(entityData.get(FLOCK_LEADER_UUID).get());
		}

		return null;
	}

	@Nullable
	private BlockPos getRandomLocation() {
		final Random random = getRandom();
		for (int i = 0; i < 20; i++) {
			double nextXPos = getX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 48);
			double nextYPos = getY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 3);
			double nextZPos = getZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 48);
			final BlockPos pos = new BlockPos(nextXPos, nextYPos, nextZPos);
			if (level.isEmptyBlock(pos)) {
				return pos;
			}
		}

		Vector3d Vector3d = getViewVector(0.0F);

		Vector3d Vector3d2 = RandomPositionGenerator.getAboveLandPos(FailgullEntity.this, 40, 3, Vector3d, ((float)Math.PI / 2F), 2, 1);
		final Vector3d groundTarget = RandomPositionGenerator.getAirPos(FailgullEntity.this, 40, 4, -2, Vector3d, (double) ((float) Math.PI / 2F));
		return Vector3d2 != null ? new BlockPos(Vector3d2) : groundTarget != null ? new BlockPos(groundTarget) : null;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(TropicraftItems.FAILGULL_SPAWN_EGG.get());
	}

	class FollowLeaderGoal extends Goal {
		FollowLeaderGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
		}

		private boolean canFollow() {
			return !getIsFlockLeader() && hasFlockLeader();
		}

		@Override
		public boolean canUse() {
			return canFollow() && getNavigation().isDone() && FailgullEntity.this.random.nextInt(10) == 0;
		}

		@Override
		public boolean canContinueToUse() {
			return canFollow() && getNavigation().isInProgress();
		}

		@Override
		public void start() {
			final Entity flockLeader = getFlockLeader();
			final PathNavigator navigator = getNavigation();
			if (flockLeader != null && flockLeader.getType() == TropicraftEntities.FAILGULL.get()) {
				navigator.moveTo(navigator.createPath(flockLeader.blockPosition(), 1), 1.0D);
				return;
			}
			BlockPos Vector3d = getRandomLocation();
			if (Vector3d != null) {
				navigator.moveTo(navigator.createPath(Vector3d, 1), 1.0D);
			}

		}
	}

	class SetTravelDestination extends Goal {
		SetTravelDestination() {
			setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		private boolean shouldLead() {
			return getIsFlockLeader() || !hasFlockLeader();
		}

		@Override
		public boolean canUse() {
			return shouldLead() && getNavigation().isDone() && getRandom().nextInt(10) == 0;
		}

		@Override
		public boolean canContinueToUse() {
			return shouldLead() && getNavigation().isInProgress();
		}

		@Override
		public void start() {
			BlockPos Vector3d = getRandomLocation();
			if (Vector3d != null) {
				final PathNavigator navigator = getNavigation();
				navigator.moveTo(navigator.createPath(Vector3d, 1), 1.0D);
			}
		}
	}

	private static class ValidateFlockLeader extends Goal {
		final FailgullEntity mob;

		public ValidateFlockLeader(FailgullEntity failgullEntity) {
			mob = failgullEntity;
		}

		@Override
		public boolean canUse() {
			if (mob.getIsFlockLeader()) {
				return false;
			}

			final Entity flockLeader = mob.getFlockLeader();
			return flockLeader == null || !flockLeader.isAlive();
		}

		@Override
		public void start() {
			mob.setFlockLeader(Optional.empty());
		}
	}

	private static class SelectFlockLeader extends Goal {
		final FailgullEntity mob;

		public SelectFlockLeader(FailgullEntity failgullEntity) {
			mob = failgullEntity;
		}

		@Override
		public boolean canUse() {
			return !mob.hasFlockLeader();
		}

		@Override
		public void start() {
			List<FailgullEntity> list = mob.level.getEntitiesOfClass(FailgullEntity.class, mob.getBoundingBox().inflate(10D, 10D, 10D));
			list.remove(mob);

			final Optional<FailgullEntity> oldest = list.stream().min(Comparator.comparingInt(FailgullEntity::getId));
			// Found an older one nearby, set it as the flock leader
			if (oldest.isPresent() && !oldest.get().uuid.equals(mob.getUUID())) {
				final FailgullEntity oldestFailgull = oldest.get();
				oldestFailgull.setIsFlockLeader(true);
				oldestFailgull.setFlockLeader(Optional.empty());
				mob.setIsFlockLeader(false);
				mob.setFlockLeader(Optional.of(oldestFailgull.getUUID()));
			}
		}
	}
}
