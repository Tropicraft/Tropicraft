package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public final class MahoganyNutBlock extends Block implements BonemealableBlock {
    public static final MapCodec<MahoganyNutBlock> CODEC = simpleCodec(MahoganyNutBlock::new);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final int MAX_AGE = 2;
    private static final int GROW_CHANCE = 10;

    private static final VoxelShape[] SHAPES = {
            Block.box(4.5, 5.0, 4.5, 11.5, 14.0, 11.5),
            Block.box(3.5, 3.0, 3.5, 12.5, 14.0, 12.5),
            Block.box(2.5, 1.0, 2.5, 13.5, 14.0, 13.5)
    };

    public MahoganyNutBlock(final Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(AGE, 0));
    }

    @Override
    protected MapCodec<MahoganyNutBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPES[state.getValue(AGE)];
    }

    @Override
    public void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        if (random.nextInt(GROW_CHANCE) == 0) {
            level.setBlockAndUpdate(pos, state.cycle(AGE));
        }
    }

    @Override
    public boolean isRandomlyTicking(final BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public BlockState updateShape(final BlockState state, final Direction direction, final BlockState neighborState, final LevelAccessor level, final BlockPos pos, final BlockPos neighborPos) {
        if (direction == Direction.UP && !canAttachTo(level, neighborState, neighborPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        final BlockPos attachPos = pos.above();
        return canAttachTo(level, level.getBlockState(attachPos), attachPos);
    }

    private static boolean canAttachTo(final LevelReader level, final BlockState state, final BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.DOWN) || state.is(BlockTags.LEAVES);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(final LevelReader level, final BlockPos pos, final BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(final Level level, final RandomSource random, final BlockPos pos, final BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerLevel level, final RandomSource random, final BlockPos pos, final BlockState state) {
        level.setBlock(pos, state.cycle(AGE), Block.UPDATE_CLIENTS);
    }
}
