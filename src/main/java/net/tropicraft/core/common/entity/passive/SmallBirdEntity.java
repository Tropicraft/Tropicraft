package net.tropicraft.core.common.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.BinaryAnimation;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.entity.ai.BirdWanderInTreesGoal;

public class SmallBirdEntity extends Animal implements FlyingAnimal {
    private final BinaryAnimation flightAnimation = new BinaryAnimation(3, Mth::easeInOutSine);

    public SmallBirdEntity(EntityType<? extends SmallBirdEntity> type, Level world) {
        super(type, world);

        moveControl = new BirdMoveControl(this);
        setPathfindingMalus(PathType.DANGER_FIRE, -1.0f);
        setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0f);
        setPathfindingMalus(PathType.COCOA, -1.0f);
        setPathfindingMalus(PathType.WATER, -1.0f);
        setPathfindingMalus(PathType.FENCE, -1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.FLYING_SPEED, 1.0)
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

    public static boolean canSmallBirdSpawnOn(EntityType<SmallBirdEntity> type, LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        BlockState groundState = world.getBlockState(pos.below());
        return (groundState.is(TropicraftTags.Blocks.BIRDS_LIKE_TO_STAND_ON) || groundState.is(Blocks.GRASS_BLOCK) || groundState.isAir())
                && world.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            flightAnimation.tick(isFlying());
        } else {
            Vec3 deltaMovement = getDeltaMovement();
            if (!moveControl.hasWanted() && !onGround() && deltaMovement.y < 0.0) {
                setDeltaMovement(deltaMovement.multiply(1.0, 0.6, 1.0));
            }
        }
    }

    public float getFlightAnimation(float partialTicks) {
        return flightAnimation.get(partialTicks);
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
    public SmallBirdEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public int getMaxFallDistance() {
        return 0;
    }

    @Override
    public int getHeadRotSpeed() {
        return 90;
    }

    @Override
    public int getMaxHeadXRot() {
        return 90;
    }

    @Override
    public int getMaxHeadYRot() {
        return 90;
    }
}
