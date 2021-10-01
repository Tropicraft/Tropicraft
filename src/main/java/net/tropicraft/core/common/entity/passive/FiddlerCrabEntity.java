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

public final class FiddlerCrabEntity extends AnimalEntity {
    public FiddlerCrabEntity(EntityType<? extends FiddlerCrabEntity> type, World world) {
        super(type, world);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.setPathPriority(PathNodeType.WATER_BORDER, 0.0F);

        this.moveController = new CrabMoveController(this);
        this.stepHeight = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 6.0)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15F);
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
        prevLimbSwingAmount = limbSwingAmount;

        double deltaX = getPosX() - prevPosX;
        double deltaZ = getPosZ() - prevPosZ;
        float deltaYaw = MathHelper.wrapDegrees(renderYawOffset - prevRenderYawOffset);

        float move = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float rotate = Math.abs(deltaYaw);

        float targetAmount = move * 4.0F + rotate * 0.25F;
        targetAmount = Math.min(targetAmount, 0.25F);

        limbSwingAmount += (targetAmount - limbSwingAmount) * 0.4F;
        limbSwing += limbSwingAmount;
    }

    @Override
    public void func_233629_a_(LivingEntity entity, boolean flying) {
        // limb swing logic replicated to consider rotation
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public FiddlerCrabEntity createChild(ServerWorld world, AgeableEntity mate) {
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
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    protected boolean func_241208_cS_() {
        // avoid being affected by water while on the ground
        return !onGround;
    }

    @Override
    public int getHorizontalFaceSpeed() {
        return 30;
    }

    public static boolean canCrabSpawn(EntityType<? extends FiddlerCrabEntity> type, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
        BlockPos groundPos = pos.down();
        BlockState groundBlock = world.getBlockState(groundPos);
        if (groundBlock.getMaterial() != Material.SAND) {
            return false;
        }

        if (!groundBlock.canCreatureSpawn(world, groundPos, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, type)) {
            return false;
        }

        BlockState block = world.getBlockState(pos);
        FluidState fluid = world.getFluidState(pos);
        return !block.hasOpaqueCollisionShape(world, pos) && !block.canProvidePower() && !block.isIn(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
                && (fluid.isEmpty() || fluid.isTagged(FluidTags.WATER));
    }

    static final class CrabMoveController extends MovementController {
        private static final double RAD_TO_DEG = 180.0 / Math.PI;

        CrabMoveController(MobEntity mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                this.action = MovementController.Action.WAIT;
                this.tickMoveTo();
            } else if (this.action == Action.WAIT) {
                this.mob.setMoveForward(0.0F);
                this.mob.setMoveStrafing(0.0F);
            }
        }

        private void tickMoveTo() {
            double dx = this.posX - this.mob.getPosX();
            double dz = this.posZ - this.mob.getPosZ();
            double dy = this.posY - this.mob.getPosY();
            double distance2 = dx * dx + dy * dy + dz * dz;
            if (distance2 < 5e-4 * 5e-4) {
                this.mob.setMoveForward(0.0F);
                this.mob.setMoveStrafing(0.0F);
                return;
            }

            float forward = (float) (MathHelper.atan2(dz, dx) * RAD_TO_DEG) - 90.0F;
            float leftTarget = forward - 90.0F;
            float rightTarget = forward + 90.0F;

            float yaw = this.mob.rotationYaw;
            float targetYaw = closerAngle(yaw, leftTarget, rightTarget);

            float speed = (float) (this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float strafe = targetYaw < forward ? -1.0F : 1.0F;

            this.mob.rotationYaw = this.limitAngle(yaw, targetYaw, 10.0F);

            this.mob.setAIMoveSpeed(speed);
            this.mob.setMoveForward(0.0F);
            this.mob.setMoveStrafing(strafe * speed);
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
