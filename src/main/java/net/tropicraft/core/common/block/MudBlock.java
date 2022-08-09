package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.core.common.TropicraftTags;

import java.util.Random;

public final class MudBlock extends Block implements BonemealableBlock {
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public MudBlock(Properties properties) {
        super(properties);
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
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return Blocks.DIRT.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    @Deprecated
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 0.2F;
    }

    public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return pLevel.getBlockState(pPos.above()).isAir();
    }

    public boolean isBonemealSuccess(Level pLevel, Random pRand, BlockPos pPos, BlockState pState) {
        return true;
    }

    public void performBonemeal(ServerLevel pLevel, Random pRand, BlockPos pPos, BlockState pState) {
        mainLoop:
        for(int i = 0; i < 96; ++i) {
            BlockPos blockpos1 = pPos.above();

            for(int j = 0; j < i / 16; ++j) {
                blockpos1 = blockpos1.offset(pRand.nextInt(3) - 1, (pRand.nextInt(3) - 1) * pRand.nextInt(3) / 2, pRand.nextInt(3) - 1);
                if (!pLevel.getBlockState(blockpos1.below()).is(TropicraftTags.Blocks.MUD) || pLevel.getBlockState(blockpos1).isCollisionShapeFullBlock(pLevel, blockpos1)) {
                    continue mainLoop;
                }
            }

            BlockState blockstate1 = pLevel.getBlockState(blockpos1);
            if (blockstate1.is(TropicraftTags.Blocks.MUD) && pRand.nextInt(10) == 0) {
                ((BonemealableBlock)TropicraftBlocks.MUD.get()).performBonemeal(pLevel, pRand, blockpos1, blockstate1);
            } else if (blockstate1.isAir()) {
                if (pRand.nextInt(2) == 0) {
                    pLevel.setBlock(blockpos1, TropicraftBlocks.REEDS.get().defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }

    }
}
