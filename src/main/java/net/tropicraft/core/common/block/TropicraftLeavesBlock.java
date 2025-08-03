package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class TropicraftLeavesBlock extends LeavesBlock {
    public static final List<BlockPos> AROUND_OFFSETS = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
            .map(BlockPos::immutable)
            .filter(pos -> !pos.equals(BlockPos.ZERO))
            .toList();
    public static final List<BlockPos> INDIRECT_NEIGHBOR_OFFSETS = AROUND_OFFSETS.stream()
            .filter(pos -> pos.distManhattan(BlockPos.ZERO) > 1)
            .toList();

    public static final MapCodec<TropicraftLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ExtraCodecs.floatRange(0.0f, 1.0f).fieldOf("leaf_particle_chance").forGetter(b -> b.leafParticleChance),
            ParticleTypes.CODEC.fieldOf("leaf_particle").forGetter(b -> b.leafParticle),
            propertiesCodec()
    ).apply(i, TropicraftLeavesBlock::new));

    private final ParticleOptions leafParticle;

    public TropicraftLeavesBlock(float leafParticleChance, ParticleOptions leafParticle, BlockBehaviour.Properties properties) {
        super(leafParticleChance, properties);
        this.leafParticle = leafParticle;
    }

    @Override
    public MapCodec<TropicraftLeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, updateDistance(state, level, pos), Block.UPDATE_ALL);
    }

    @Override
    protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int flags, int recursionLeft) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Vec3i offset : INDIRECT_NEIGHBOR_OFFSETS) {
            mutablePos.setWithOffset(pos, offset);
            if (level.getBlockState(mutablePos).is(this)) {
                // Note: direction is arbitrary, as we only propagate the shape update to neighboring leaves which ignore it
                level.neighborShapeChanged(Direction.DOWN, pos, mutablePos, state, flags, recursionLeft);
            }
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        int newDistance = getDistanceAt(neighborState) + 1;
        if (newDistance != 1 || state.getValue(DISTANCE) != newDistance) {
            scheduledTickAccess.scheduleTick(pos, this, 1);
        }

        return state;
    }

    private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int minDistance = DECAY_DISTANCE;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Vec3i offset : AROUND_OFFSETS) {
            mutablePos.setWithOffset(pos, offset);
            minDistance = Math.min(minDistance, getDistanceAt(level.getBlockState(mutablePos)) + 1);
            if (minDistance == 1) {
                break;
            }
        }
        return state.setValue(DISTANCE, minDistance);
    }

    private static int getDistanceAt(BlockState neighbor) {
        return getOptionalDistanceAt(neighbor).orElse(DECAY_DISTANCE);
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        ParticleUtils.spawnParticleBelow(level, pos, random, leafParticle);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockState = defaultBlockState().setValue(PERSISTENT, true).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return updateDistance(blockState, context.getLevel(), context.getClickedPos());
    }
}
