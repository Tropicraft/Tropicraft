package net.tropicraft.core.common.entity.passive;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Ease;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.tropicraft.core.common.BinaryAnimation;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.entity.ai.BirdWanderInTreesGoal;
import org.jspecify.annotations.Nullable;

public class ToucanEntity extends Animal {
    private final BinaryAnimation flightAnimation = new BinaryAnimation(SharedConstants.TICKS_PER_SECOND / 4, Ease::inOutSine);

    public ToucanEntity(EntityType<? extends ToucanEntity> type, Level world) {
        super(type, world);

        moveControl = new BirdMoveControl(this);
        setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0f);
        setPathfindingMalus(PathType.FIRE, -1.0f);
        setPathfindingMalus(PathType.COCOA, -1.0f);
        setPathfindingMalus(PathType.WATER, -1.0f);
        setPathfindingMalus(PathType.FENCE, -1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.FLYING_SPEED, 0.4)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigator = new SmallBirdPathNavigation(this, level);
        navigator.setCanOpenDoors(false);
        navigator.setCanFloat(true);
        return navigator;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(2, new TemptGoal(this, 1.25, item -> item.is(TropicraftTags.Items.FRUITS), false));
        goalSelector.addGoal(3, new BirdWanderInTreesGoal(this, 1.0));
    }

    public static boolean canToucanSpawnOn(EntityType<ToucanEntity> type, LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        BlockState groundState = world.getBlockState(pos.below());
        return (groundState.is(BlockTags.LEAVES) || groundState.is(Blocks.GRASS_BLOCK) || groundState.isAir())
                && world.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            flightAnimation.tick(isFlying());
        }
    }

    public float getFlightAnimation(float partialTicks) {
        return flightAnimation.get(partialTicks);
    }

    public boolean isFlying() {
        return !onGround();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable ToucanEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public int getMaxFallDistance() {
        return 1;
    }

    @Override
    protected boolean omnidirectionalAirMover() {
        return true;
    }
}
