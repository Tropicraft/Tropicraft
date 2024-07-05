package net.tropicraft.core.common.entity.passive;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.Easings;
import net.tropicraft.core.common.TropicraftTags;

import javax.annotation.Nullable;

public class ToucanEntity extends Animal implements FlyingAnimal {
    private static final int FLIGHT_ANIMATION_LENGTH = SharedConstants.TICKS_PER_SECOND / 4;

    private int flightAnimation;
    private int lastFlightAnimation;

    public ToucanEntity(EntityType<? extends ToucanEntity> type, Level world) {
        super(type, world);

        moveControl = new FlyingMoveControl(this, 20, false);
        setPathfindingMalus(PathType.DANGER_FIRE, -1.0f);
        setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0f);
        setPathfindingMalus(PathType.COCOA, -1.0f);
        setPathfindingMalus(PathType.WATER, -1.0f);
        setPathfindingMalus(PathType.FENCE, -1.0f);
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
        goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(TropicraftTags.Items.FRUITS), false));
        goalSelector.addGoal(3, new WanderGoal(this, 1.0));
    }

    public static boolean canToucanSpawnOn(EntityType<ToucanEntity> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState groundState = world.getBlockState(pos.below());
        return (groundState.is(BlockTags.LEAVES) || groundState.is(Blocks.GRASS_BLOCK) || groundState.isAir())
                && world.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public void tick() {
        super.tick();
        int targetFlightAnimation = isFlying() ? FLIGHT_ANIMATION_LENGTH : 0;
        lastFlightAnimation = flightAnimation;
        if (targetFlightAnimation > flightAnimation) {
            flightAnimation++;
        } else if (targetFlightAnimation < flightAnimation) {
            flightAnimation--;
        }
    }

    public float getFlightAnimation(float partialTicks) {
        float animation = Mth.lerp(partialTicks, lastFlightAnimation, flightAnimation) / FLIGHT_ANIMATION_LENGTH;
        return Easings.inOutSine(animation);
    }

    @Override
    public boolean isFlying() {
        return !onGround();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public ToucanEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public int getMaxFallDistance() {
        return 1;
    }

    public static class WanderGoal extends WaterAvoidingRandomFlyingGoal {
        private static final int TREE_HORIZONTAL_RANGE = 3;
        private static final int TREE_VERTICAL_RANGE = 6;

        public WanderGoal(PathfinderMob mob, double speed) {
            super(mob, speed);
        }

        @Override
        @Nullable
        protected Vec3 getPosition() {
            Vec3 target = null;
            if (mob.isInWater()) {
                target = LandRandomPos.getPos(mob, 15, 15);
            }

            if (mob.getRandom().nextFloat() >= probability) {
                target = findTreePos();
            }

            return target == null ? super.getPosition() : target;
        }

        @Nullable
        private Vec3 findTreePos() {
            Level level = mob.level();
            BlockPos currentPos = mob.blockPosition();
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (BlockPos pos : BlockPos.betweenClosed(
                    Mth.floor(mob.getX() - TREE_HORIZONTAL_RANGE), Mth.floor(mob.getY() - TREE_VERTICAL_RANGE), Mth.floor(mob.getZ() - TREE_HORIZONTAL_RANGE),
                    Mth.floor(mob.getX() + TREE_HORIZONTAL_RANGE), Mth.floor(mob.getY() + TREE_VERTICAL_RANGE), Mth.floor(mob.getZ() + TREE_HORIZONTAL_RANGE))
            ) {
                if (currentPos.equals(pos)) {
                    continue;
                }
                BlockState belowState = level.getBlockState(mutablePos.setWithOffset(pos, Direction.DOWN));
                if (belowState.is(BlockTags.LEAVES) || belowState.is(BlockTags.LOGS)) {
                    BlockPos abovePos = mutablePos.setWithOffset(pos, Direction.UP);
                    if (level.isEmptyBlock(pos) && level.isEmptyBlock(abovePos)) {
                        return Vec3.atBottomCenterOf(pos);
                    }
                }
            }

            return null;
        }
    }
}
