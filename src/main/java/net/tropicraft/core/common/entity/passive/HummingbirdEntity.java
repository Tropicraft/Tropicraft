package net.tropicraft.core.common.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class HummingbirdEntity extends AnimalEntity implements IFlyingAnimal {
    private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public HummingbirdEntity(EntityType<? extends HummingbirdEntity> type, World world) {
        super(type, world);

        this.moveController = new FlyingMovementController(this, 20, true);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.COCOA, -1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.FENCE, -1.0F);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 6.0)
                .createMutableAttribute(Attributes.FLYING_SPEED, 0.4)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        FlyingPathNavigator navigator = new FlyingPathNavigator(this, world);
        navigator.setCanOpenDoors(false);
        navigator.setCanSwim(true);
        navigator.setCanEnterDoors(true);
        return navigator;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FlyAwayInPanicGoal());
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.fromItems(Items.SUGAR), false));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new FeedFromPlantsGoal());
        this.goalSelector.addGoal(5, new FlyAroundRandomlyGoal());
    }

    public static boolean canHummingbirdSpawnOn(EntityType<HummingbirdEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        BlockState groundState = world.getBlockState(pos.down());
        return (groundState.isIn(BlockTags.LEAVES) || groundState.matchesBlock(Blocks.GRASS_BLOCK) || groundState.isAir())
                && world.getLightSubtracted(pos, 0) > 8;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public HummingbirdEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.HUMMINGBIRD_SPAWN_EGG.get());
    }

    final class FeedFromPlantsGoal extends FlyingGoal {
        private static final int SEARCH_TRIES = 50;
        private static final int SEARCH_RADIUS = 5;
        private static final int SEARCH_RADIUS_Y = 3;

        private static final int FEEDING_TICKS = 20 * 4;

        private int feedingTicks;
        private boolean foundFood;

        FeedFromPlantsGoal() {
            super(1.0F);
        }

        @Override
        public boolean shouldExecute() {
            HummingbirdEntity bird = HummingbirdEntity.this;
            return bird.navigator.noPath() && bird.rand.nextInt(20) == 0;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.feedingTicks > 0 || HummingbirdEntity.this.hasPath();
        }

        @Override
        public void resetTask() {
            super.resetTask();
            this.feedingTicks = 0;
            this.foundFood = false;
        }

        @Override
        public void tick() {
            super.tick();

            Vector3d target = this.target;
            if (target != null) {
                if (this.foundFood) {
                    this.tickFoundFood(target);
                } else {
                    this.tickFindingFood(target);
                }
            }
        }

        private void tickFoundFood(Vector3d target) {
            if (this.feedingTicks > 0) {
                HummingbirdEntity.this.getLookController().setLookPosition(target);
                this.feedingTicks--;
            }
        }

        private void tickFindingFood(Vector3d target) {
            if (HummingbirdEntity.this.getDistanceSq(target) <= 1.5 * 1.5) {
                this.feedingTicks = FEEDING_TICKS;
                this.foundFood = true;
            }
        }

        @Nullable
        @Override
        Vector3d generateTarget() {
            HummingbirdEntity bird = HummingbirdEntity.this;
            World world = bird.world;
            Random random = bird.rand;

            BlockPos.Mutable mutablePos = new BlockPos.Mutable();

            for (int i = 0; i < SEARCH_TRIES; i++) {
                mutablePos.setAndOffset(bird.getPosition(),
                        random.nextInt(SEARCH_RADIUS) - random.nextInt(SEARCH_RADIUS),
                        random.nextInt(SEARCH_RADIUS_Y) - random.nextInt(SEARCH_RADIUS_Y),
                        random.nextInt(SEARCH_RADIUS) - random.nextInt(SEARCH_RADIUS)
                );

                BlockState state = world.getBlockState(mutablePos);
                if (this.canFeedFrom(world, mutablePos, state)) {
                    return Vector3d.copyCentered(mutablePos);
                }
            }

            return null;
        }

        private boolean canFeedFrom(World world, BlockPos pos, BlockState state) {
            if (this.canFeedFrom(state)) {
                BlockPos.Mutable mutablePos = new BlockPos.Mutable();
                for (Direction direction : HORIZONTALS) {
                    mutablePos.setAndMove(pos, direction);
                    if (world.isAirBlock(mutablePos)) {
                        return true;
                    }
                }
                return false;
            }

            return false;
        }

        private boolean canFeedFrom(BlockState state) {
            if (state.isAir()) return false;
            return state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.FLOWERS);
        }
    }

    final class FlyAwayInPanicGoal extends FlyingGoal {
        FlyAwayInPanicGoal() {
            super(1.25F);
        }

        @Override
        public boolean shouldExecute() {
            return HummingbirdEntity.this.getRevengeTarget() != null;
        }

        @Nullable
        @Override
        Vector3d generateTarget() {
            LivingEntity target = HummingbirdEntity.this.getRevengeTarget();
            if (target == null) {
                return null;
            }

            Vector3d direction = HummingbirdEntity.this.getPositionVec().subtract(target.getPositionVec())
                    .normalize();
            return this.generateTargetInDirection(direction, Math.PI / 2.0);
        }
    }

    final class FlyAroundRandomlyGoal extends FlyingGoal {
        FlyAroundRandomlyGoal() {
            super(1.0F);
        }

        @Override
        public boolean shouldExecute() {
            HummingbirdEntity bird = HummingbirdEntity.this;
            return bird.navigator.noPath() && bird.rand.nextInt(10) == 0;
        }

        @Nullable
        @Override
        Vector3d generateTarget() {
            Vector3d direction = HummingbirdEntity.this.getLook(1.0F);
            return this.generateTargetInDirection(direction, Math.PI / 2.0);
        }
    }

    abstract class FlyingGoal extends Goal {
        final float speed;
        Vector3d target;

        FlyingGoal(float speed) {
            this.speed = speed;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return HummingbirdEntity.this.navigator.noPath();
        }

        @Override
        public boolean shouldContinueExecuting() {
            return HummingbirdEntity.this.navigator.hasPath();
        }

        @Override
        public void startExecuting() {
            this.target = this.generateTarget();
            if (this.target != null) {
                Path path = HummingbirdEntity.this.navigator.getPathToPos(new BlockPos(this.target), 1);
                HummingbirdEntity.this.navigator.setPath(path, this.speed);
            }
        }

        @Override
        public void resetTask() {
            this.target = null;
        }

        @Nullable
        abstract Vector3d generateTarget();

        @Nullable
        final Vector3d generateTargetInDirection(Vector3d direction, double maxAngle) {
            Vector3d target = RandomPositionGenerator.findAirTarget(HummingbirdEntity.this, 8, 7, direction, (float) maxAngle, 2, 1);
            if (target == null) {
                target = RandomPositionGenerator.findGroundTarget(HummingbirdEntity.this, 8, 4, -2, direction, (float) maxAngle);
            }

            return target;
        }
    }
}
