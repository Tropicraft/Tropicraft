package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.core.common.TropicraftTags;

public final class MudBlock extends Block implements BonemealableBlock {
    public static final MapCodec<MudBlock> CODEC = simpleCodec(MudBlock::new);

    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public MudBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<MudBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    @Deprecated
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 0.2F;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        mainLoop:
        for (int i = 0; i < 96; ++i) {
            BlockPos blockpos1 = pos.above();
            for (int j = 0; j < i / 16; ++j) {
                blockpos1 = blockpos1.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!level.getBlockState(blockpos1.below()).is(TropicraftTags.Blocks.MUD) || level.getBlockState(blockpos1).isCollisionShapeFullBlock(level, blockpos1)) {
                    continue mainLoop;
                }
            }

            BlockState blockstate1 = level.getBlockState(blockpos1);
            if (blockstate1.is(TropicraftTags.Blocks.MUD) && random.nextInt(10) == 0) {
                ((BonemealableBlock) TropicraftBlocks.MUD.get()).performBonemeal(level, random, blockpos1, blockstate1);
            } else if (blockstate1.isAir()) {
                if (random.nextInt(2) == 0) {
                    level.setBlock(blockpos1, TropicraftBlocks.REEDS.get().defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
    }
}
