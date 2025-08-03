package net.tropicraft.core.common.entity.ai;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.TropicraftTags;

import javax.annotation.Nullable;

public class BirdWanderInTreesGoal extends RandomStrollGoal {
    private static final int INTERVAL = SharedConstants.TICKS_PER_SECOND * 4;

    private static final int MIN_RANGE = 3;
    private static final int MAX_HORIZONTAL_RANGE = 4;
    private static final int MAX_VERTICAL_RANGE = 6;

    public BirdWanderInTreesGoal(PathfinderMob mob, double speed) {
        super(mob, speed, INTERVAL);
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(wantedX, wantedY, wantedZ, 0, speedModifier);
    }

    @Override
    @Nullable
    protected Vec3 getPosition() {
        if (mob.isInWater()) {
            Vec3 target = LandRandomPos.getPos(mob, 15, 15);
            if (target != null) {
                return target;
            }
        }

        // If we're already in a tree, heavily bias to stay in the trees
        float treeChance = mob.getBlockStateOn().is(TropicraftTags.Blocks.BIRDS_LIKE_TO_STAND_ON) ? 0.95f : 0.5f;

        if (mob.getRandom().nextFloat() < treeChance) {
            return findTreePos();
        }

        return LandRandomPos.getPos(mob, 5, 5);
    }

    @Nullable
    private Vec3 findTreePos() {
        Level level = mob.level();
        BlockPos origin = mob.blockPosition();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        return BlockPos.findClosestMatch(origin, MAX_HORIZONTAL_RANGE, MAX_VERTICAL_RANGE, pos -> {
            if (pos.distManhattan(origin) < MIN_RANGE) {
                return false;
            }
            if (wantsToStandAt(pos, level, mutablePos)) {
                // Small chance to pick a block that is not the closest one
                // Intentionally not having too much randomness in here, as many birds trying to sit on the same spot is cute
                return mob.getRandom().nextInt(7) != 0;
            }
            return false;
        }).map(Vec3::atBottomCenterOf).orElse(null);
    }

    private static boolean wantsToStandAt(BlockPos pos, Level level, BlockPos.MutableBlockPos mutablePos) {
        BlockState belowState = level.getBlockState(mutablePos.setWithOffset(pos, Direction.DOWN));
        if (belowState.is(TropicraftTags.Blocks.BIRDS_LIKE_TO_STAND_ON)) {
            BlockPos abovePos = mutablePos.setWithOffset(pos, Direction.UP);
            return level.isEmptyBlock(pos) && level.isEmptyBlock(abovePos);
        }
        return false;
    }
}
