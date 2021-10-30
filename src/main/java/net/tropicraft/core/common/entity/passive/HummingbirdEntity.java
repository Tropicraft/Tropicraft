package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class HummingbirdEntity extends Animal implements FlyingAnimal {
    private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private static final int POLLINATE_THRESHOLD = 5;

    private int pollenCollected;

    public HummingbirdEntity(EntityType<? extends HummingbirdEntity> type, Level world) {
        super(type, world);

        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.FLYING_SPEED, 0.4)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation navigator = new FlyingPathNavigation(this, world);
        navigator.setCanOpenDoors(false);
        navigator.setCanFloat(true);
        navigator.setCanPassDoors(true);
        return navigator;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FlyAwayInPanicGoal());
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(Items.SUGAR), false));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new FeedFromPlantsGoal());
        this.goalSelector.addGoal(5, new FlyAroundRandomlyGoal());
    }

    public static boolean canHummingbirdSpawnOn(EntityType<HummingbirdEntity> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
        BlockState groundState = world.getBlockState(pos.below());
        return (groundState.is(BlockTags.LEAVES) || groundState.is(Blocks.GRASS_BLOCK) || groundState.isAir())
                && world.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public HummingbirdEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.HUMMINGBIRD_SPAWN_EGG.get());
    }

    private void tryPollinatePlant(BlockPos pos) {
        if (++pollenCollected >= POLLINATE_THRESHOLD) {
            pollenCollected = 0;
            tryGrowPlant(pos);
        }
    }

    private void tryGrowPlant(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        IntegerProperty age = getPlantAgeProperty(state);

        if (age != null) {
            int nextAge = state.get(age) + 1;
            if (age.getAllowedValues().contains(nextAge)) {
                world.playEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
                world.setBlockState(pos, state.with(age, nextAge));
            }
        }
    }

    @Nullable
    private IntegerProperty getPlantAgeProperty(BlockState state) {
        if (state.hasProperty(BlockStateProperties.AGE_0_3)) {
            return BlockStateProperties.AGE_0_3;
        } else if (state.hasProperty(BlockStateProperties.AGE_0_7)) {
            return BlockStateProperties.AGE_0_7;
        }
        return null;
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putByte("pollen_collected", (byte) pollenCollected);
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        pollenCollected = nbt.getByte("pollen_collected");
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
        public boolean canUse() {
            HummingbirdEntity bird = HummingbirdEntity.this;
            return bird.navigation.isDone() && bird.random.nextInt(20) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.feedingTicks > 0 || HummingbirdEntity.this.isPathFinding();
        }

        @Override
        public void stop() {
            super.stop();
            this.feedingTicks = 0;
            this.foundFood = false;
        }

        @Override
        public void tick() {
            super.tick();

            Vec3 target = this.target;
            if (target != null) {
                if (this.foundFood) {
                    this.tickFoundFood(target);
                } else {
                    this.tickFindingFood(target);
                }
            }
        }

        private void tickFoundFood(Vec3 target) {
            if (this.feedingTicks > 0) {
                HummingbirdEntity bird = HummingbirdEntity.this;
                bird.lookControl.setLookAt(target);

                if (--this.feedingTicks == 0) {
                    bird.tryPollinatePlant(new BlockPos(target));
                }
            }
        }

        private void tickFindingFood(Vec3 target) {
            if (HummingbirdEntity.this.distanceToSqr(target) <= 1.5 * 1.5) {
                this.feedingTicks = FEEDING_TICKS;
                this.foundFood = true;
            }
        }

        @Nullable
        @Override
        Vec3 generateTarget() {
            HummingbirdEntity bird = HummingbirdEntity.this;
            Level world = bird.level;
            Random random = bird.random;

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (int i = 0; i < SEARCH_TRIES; i++) {
                mutablePos.setWithOffset(bird.blockPosition(),
                        random.nextInt(SEARCH_RADIUS) - random.nextInt(SEARCH_RADIUS),
                        random.nextInt(SEARCH_RADIUS_Y) - random.nextInt(SEARCH_RADIUS_Y),
                        random.nextInt(SEARCH_RADIUS) - random.nextInt(SEARCH_RADIUS)
                );

                BlockState state = world.getBlockState(mutablePos);
                if (this.canFeedFrom(world, mutablePos, state)) {
                    return Vec3.atCenterOf(mutablePos);
                }
            }

            return null;
        }

        private boolean canFeedFrom(Level world, BlockPos pos, BlockState state) {
            if (this.canFeedFrom(state)) {
                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
                for (Direction direction : HORIZONTALS) {
                    mutablePos.setWithOffset(pos, direction);
                    if (world.isEmptyBlock(mutablePos)) {
                        return true;
                    }
                }
                return false;
            }

            return false;
        }

        private boolean canFeedFrom(BlockState state) {
            if (state.isAir()) return false;
            return state.is(BlockTags.LEAVES) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.BEE_GROWABLES);
        }
    }

    final class FlyAwayInPanicGoal extends FlyingGoal {
        FlyAwayInPanicGoal() {
            super(1.25F);
        }

        @Override
        public boolean canUse() {
            return HummingbirdEntity.this.getLastHurtByMob() != null;
        }

        @Nullable
        @Override
        Vec3 generateTarget() {
            LivingEntity target = HummingbirdEntity.this.getLastHurtByMob();
            if (target == null) {
                return null;
            }

            Vec3 direction = HummingbirdEntity.this.position().subtract(target.position())
                    .normalize();
            return this.generateTargetInDirection(direction, Math.PI / 2.0);
        }
    }

    final class FlyAroundRandomlyGoal extends FlyingGoal {
        FlyAroundRandomlyGoal() {
            super(1.0F);
        }

        @Override
        public boolean canUse() {
            HummingbirdEntity bird = HummingbirdEntity.this;
            return bird.navigation.isDone() && bird.random.nextInt(10) == 0;
        }

        @Nullable
        @Override
        Vec3 generateTarget() {
            Vec3 direction = HummingbirdEntity.this.getViewVector(1.0F);
            return this.generateTargetInDirection(direction, Math.PI / 2.0);
        }
    }

    abstract class FlyingGoal extends Goal {
        final float speed;
        Vec3 target;

        FlyingGoal(float speed) {
            this.speed = speed;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return HummingbirdEntity.this.navigation.isDone();
        }

        @Override
        public boolean canContinueToUse() {
            return HummingbirdEntity.this.navigation.isInProgress();
        }

        @Override
        public void start() {
            this.target = this.generateTarget();
            if (this.target != null) {
                Path path = HummingbirdEntity.this.navigation.createPath(new BlockPos(this.target), 1);
                HummingbirdEntity.this.navigation.moveTo(path, this.speed);
            }
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Nullable
        abstract Vec3 generateTarget();

        @Nullable
        final Vec3 generateTargetInDirection(Vec3 direction, double maxAngle) {
            Vec3 target = RandomPos.getAboveLandPos(HummingbirdEntity.this, 8, 7, direction, (float) maxAngle, 2, 1);
            if (target == null) {
                target = RandomPos.getAirPos(HummingbirdEntity.this, 8, 4, -2, direction, (float) maxAngle);
            }

            return target;
        }
    }
}
