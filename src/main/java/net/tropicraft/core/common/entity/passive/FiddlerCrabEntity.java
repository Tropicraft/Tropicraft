package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class FiddlerCrabEntity extends Animal implements OwnableEntity {
    private boolean rollingDownTown;

    private boolean travellingGolf;

    @Nullable
    private UUID owner = null;
    private static final String OWNER_UUID_TAG = "Owner";
    private static final String ROLLING_DOWN_TOWN_TAG = "RollingDownTown";

    public FiddlerCrabEntity(EntityType<? extends FiddlerCrabEntity> type, Level world) {
        super(type, world);
        setPathfindingMalus(PathType.WATER, 0.0f);
        setPathfindingMalus(PathType.WATER_BORDER, 0.0f);

        moveControl = new CrabMoveController(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.STEP_HEIGHT, 1.0f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 6.0f, 1.0, 1.2));
        goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    protected void updateWalkAnimation(float distance) {
        float rotation = Math.abs(Mth.wrapDegrees(yBodyRot - yBodyRotO));

        float targetAmount = distance * 4.0f + rotation * 0.25f;
        targetAmount = Math.min(targetAmount, 0.25f);

        walkAnimation.update(targetAmount, 0.4f);
    }

    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        if (!rollingDownTown) {
            return super.hurt(source, amount);
        }

        if (owner != null && !wasHurtByOwner(source)) {
            return false;
        }

        return super.hurt(source, amount);
    }

    private boolean wasHurtByOwner(final DamageSource source) {
        UUID sourceId = source.getEntity() != null ? source.getEntity().getUUID() : null;
        return owner != null && owner.equals(sourceId);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public FiddlerCrabEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        // avoid being affected by water while on the ground
        return !onGround();
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    public void travel(Vec3 input) {
        if (rollingDownTown) {
            travelGolf(input);
        } else {
            super.travel(input);
        }
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (rollingDownTown) {
            // Don't bounce up
            boolean onGround = onGround();
            setOnGround(false);
            super.knockback(strength, x, z);
            setOnGround(onGround);
        } else {
            super.knockback(strength, x, z);
        }
    }

    private void travelGolf(Vec3 input) {
        if (!isControlledByLocalInstance() || isFallFlying()) {
            super.travel(input);
            return;
        }

        FluidState fluid = level().getFluidState(blockPosition());
        if (isAffectedByFluids() && !canStandOnFluid(fluid) && (isInWater() || isInLava() || isInFluidType(fluid))) {
            super.travel(input);
            return;
        }

        try {
            setOnGround(false);
            travellingGolf = true;
            super.travel(input);
        } finally {
            travellingGolf = false;
        }
    }

    @Override
    public BlockPos getBlockPosBelowThatAffectsMyMovement() {
        if (travellingGolf) {
            // Pretend to be walking in the air!
            return blockPosition().atY(level().getMinBuildHeight() - 1);
        }
        return super.getBlockPosBelowThatAffectsMyMovement();
    }

    @Override
    public boolean isPushable() {
        return !rollingDownTown;
    }

    @Override
    protected void checkFallDamage(double y, boolean ground, BlockState state, BlockPos pos) {
        if (rollingDownTown) {
            resetFallDistance();
        }
        super.checkFallDamage(y, ground, state, pos);
    }

    public boolean bounce() {
        if (rollingDownTown) {
            Vec3 deltaMovement = getDeltaMovement();
            if (Math.abs(deltaMovement.y) > 0.1) {
                setDeltaMovement(deltaMovement.x, -deltaMovement.y * 0.8, deltaMovement.z);
                return true;
            }
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(ROLLING_DOWN_TOWN_TAG, rollingDownTown);
        final UUID ownerUUID = getOwnerUUID();
        if (ownerUUID != null) {
            tag.putUUID(OWNER_UUID_TAG, ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        rollingDownTown = tag.getBoolean(ROLLING_DOWN_TOWN_TAG);
        if (tag.contains(OWNER_UUID_TAG)) {
            setOwnerUUID(tag.getUUID(OWNER_UUID_TAG));
        } else {
            setOwnerUUID(null);
        }
    }

    public boolean isRollingDownTown() {
        return rollingDownTown;
    }

    @Override
    public float maxUpStep() {
        if (rollingDownTown) {
            return 0.0f;
        }
        return super.maxUpStep();
    }

    public static boolean canCrabSpawn(EntityType<? extends FiddlerCrabEntity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockPos groundPos = pos.below();
        BlockState groundBlock = world.getBlockState(groundPos);
        if (!groundBlock.is(BlockTags.SAND)) {
            return false;
        }

        if (!groundBlock.isValidSpawn(world, groundPos, type)) {
            return false;
        }

        BlockState block = world.getBlockState(pos);
        FluidState fluid = world.getFluidState(pos);
        return !block.isCollisionShapeFullBlock(world, pos) && !block.isSignalSource() && !block.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
                && (fluid.isEmpty() || fluid.is(FluidTags.WATER));
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return owner;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.owner = uuid;
    }

    static final class CrabMoveController extends MoveControl {
        private static final double RAD_TO_DEG = 180.0 / Math.PI;

        CrabMoveController(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (operation == MoveControl.Operation.MOVE_TO) {
                operation = MoveControl.Operation.WAIT;
                tickMoveTo();
            } else if (operation == Operation.WAIT) {
                mob.setZza(0.0f);
                mob.setXxa(0.0f);
            }
        }

        private void tickMoveTo() {
            double dx = wantedX - mob.getX();
            double dz = wantedZ - mob.getZ();
            double dy = wantedY - mob.getY();
            double distance2 = dx * dx + dy * dy + dz * dz;
            if (distance2 < 5e-4 * 5e-4) {
                mob.setZza(0.0f);
                mob.setXxa(0.0f);
                return;
            }

            float forward = (float) (Mth.atan2(dz, dx) * RAD_TO_DEG) - 90.0f;
            float leftTarget = forward - 90.0f;
            float rightTarget = forward + 90.0f;

            float yaw = mob.getYRot();
            float targetYaw = closerAngle(yaw, leftTarget, rightTarget);

            float speed = (float) (speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float strafe = targetYaw < forward ? -1.0f : 1.0f;

            mob.setYRot(rotlerp(yaw, targetYaw, 10.0f));

            mob.setSpeed(speed);
            mob.setZza(0.0f);
            mob.setXxa(strafe * speed);
        }

        private static float closerAngle(float reference, float left, float right) {
            float deltaLeft = Math.abs(Mth.wrapDegrees(reference - left));
            float deltaRight = Math.abs(Mth.wrapDegrees(reference - right));
            if (deltaLeft < deltaRight) {
                return left;
            } else {
                return right;
            }
        }
    }
}
