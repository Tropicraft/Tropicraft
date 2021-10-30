package net.tropicraft.core.common.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.Random;

import net.minecraft.entity.ai.controller.MovementController.Action;

public final class FiddlerCrabEntity extends AnimalEntity {
    public FiddlerCrabEntity(EntityType<? extends FiddlerCrabEntity> type, World world) {
        super(type, world);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.setPathfindingMalus(PathNodeType.WATER_BORDER, 0.0F);

        this.moveControl = new CrabMoveController(this);
        this.maxUpStep = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
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
        float deltaYaw = MathHelper.wrapDegrees(yBodyRot - yBodyRotO);

        float move = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
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
    public FiddlerCrabEntity getBreedOffspring(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
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

    public static boolean canCrabSpawn(EntityType<? extends FiddlerCrabEntity> type, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
        BlockPos groundPos = pos.below();
        BlockState groundBlock = world.getBlockState(groundPos);
        if (groundBlock.getMaterial() != Material.SAND) {
            return false;
        }

        if (!groundBlock.canCreatureSpawn(world, groundPos, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, type)) {
            return false;
        }

        BlockState block = world.getBlockState(pos);
        FluidState fluid = world.getFluidState(pos);
        return !block.isCollisionShapeFullBlock(world, pos) && !block.isSignalSource() && !block.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
                && (fluid.isEmpty() || fluid.is(FluidTags.WATER));
    }

    static final class CrabMoveController extends MovementController {
        private static final double RAD_TO_DEG = 180.0 / Math.PI;

        CrabMoveController(MobEntity mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                this.operation = MovementController.Action.WAIT;
                this.tickMoveTo();
            } else if (this.operation == Action.WAIT) {
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

            float forward = (float) (MathHelper.atan2(dz, dx) * RAD_TO_DEG) - 90.0F;
            float leftTarget = forward - 90.0F;
            float rightTarget = forward + 90.0F;

            float yaw = this.mob.yRot;
            float targetYaw = closerAngle(yaw, leftTarget, rightTarget);

            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float strafe = targetYaw < forward ? -1.0F : 1.0F;

            this.mob.yRot = this.rotlerp(yaw, targetYaw, 10.0F);

            this.mob.setSpeed(speed);
            this.mob.setZza(0.0F);
            this.mob.setXxa(strafe * speed);
        }

        private static float closerAngle(float reference, float left, float right) {
            float deltaLeft = Math.abs(MathHelper.wrapDegrees(reference - left));
            float deltaRight = Math.abs(MathHelper.wrapDegrees(reference - right));
            if (deltaLeft < deltaRight) {
                return left;
            } else {
                return right;
            }
        }
    }
}
