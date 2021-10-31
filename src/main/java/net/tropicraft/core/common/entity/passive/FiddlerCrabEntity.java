package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.Random;

public final class FiddlerCrabEntity extends Animal {
    public FiddlerCrabEntity(EntityType<? extends FiddlerCrabEntity> type, Level world) {
        super(type, world);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);

        this.moveControl = new CrabMoveController(this);
        this.maxUpStep = 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        this.tickLimbSwing();
    }

    private void tickLimbSwing() {
        animationSpeedOld = animationSpeed;

        double deltaX = getX() - xo;
        double deltaZ = getZ() - zo;
        float deltaYaw = Mth.wrapDegrees(yBodyRot - yBodyRotO);

        float move = Mth.sqrt((float) (deltaX * deltaX + deltaZ * deltaZ));
        float rotate = Math.abs(deltaYaw);

        float targetAmount = move * 4.0F + rotate * 0.25F;
        targetAmount = Math.min(targetAmount, 0.25F);

        animationSpeed += (targetAmount - animationSpeed) * 0.4F;
        animationPosition += animationSpeed;
    }

    @Override
    public void calculateEntityAnimation(LivingEntity entity, boolean flying) {
        // limb swing logic replicated to consider rotation
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
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.FIDDLER_CRAB_SPAWN_EGG.get());
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        // avoid being affected by water while on the ground
        return !onGround;
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    public static boolean canCrabSpawn(EntityType<? extends FiddlerCrabEntity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
        BlockPos groundPos = pos.below();
        BlockState groundBlock = world.getBlockState(groundPos);
        if (groundBlock.getMaterial() != Material.SAND) {
            return false;
        }

        if (!groundBlock.canCreatureSpawn(world, groundPos, SpawnPlacements.Type.NO_RESTRICTIONS, type)) {
            return false;
        }

        BlockState block = world.getBlockState(pos);
        FluidState fluid = world.getFluidState(pos);
        return !block.isCollisionShapeFullBlock(world, pos) && !block.isSignalSource() && !block.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
                && (fluid.isEmpty() || fluid.is(FluidTags.WATER));
    }

    static final class CrabMoveController extends MoveControl {
        private static final double RAD_TO_DEG = 180.0 / Math.PI;

        CrabMoveController(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                this.tickMoveTo();
            } else if (this.operation == Operation.WAIT) {
                this.mob.setZza(0.0F);
                this.mob.setXxa(0.0F);
            }
        }

        private void tickMoveTo() {
            double dx = this.wantedX - this.mob.getX();
            double dz = this.wantedZ - this.mob.getZ();
            double dy = this.wantedY - this.mob.getY();
            double distance2 = dx * dx + dy * dy + dz * dz;
            if (distance2 < 5e-4 * 5e-4) {
                this.mob.setZza(0.0F);
                this.mob.setXxa(0.0F);
                return;
            }

            float forward = (float) (Mth.atan2(dz, dx) * RAD_TO_DEG) - 90.0F;
            float leftTarget = forward - 90.0F;
            float rightTarget = forward + 90.0F;

            float yaw = this.mob.getYRot();
            float targetYaw = closerAngle(yaw, leftTarget, rightTarget);

            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float strafe = targetYaw < forward ? -1.0F : 1.0F;

            this.mob.setYRot(this.rotlerp(yaw, targetYaw, 10.0F));

            this.mob.setSpeed(speed);
            this.mob.setZza(0.0F);
            this.mob.setXxa(strafe * speed);
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
