package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.block.ReedsBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;

public final class ReedsFeature extends Feature<NoFeatureConfig> {
    private static final BlockState REEDS = TropicraftBlocks.REEDS.get().defaultBlockState();

    private static final int HEIGHT_ABOVE_WATER = 2;
    private static final int MAX_HEIGHT = 3;

    private static final int MAX_DEPTH = MAX_HEIGHT;

    public ReedsFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos origin, NoFeatureConfig config) {
        if (!world.getBlockState(origin).is(Blocks.WATER) || !world.isEmptyBlock(origin.above())) {
            return false;
        }

        boolean generated = false;

        BlockPos.Mutable bottomPos = new BlockPos.Mutable();
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int i = 0; i < 32; i++) {
            int x = origin.getX() + random.nextInt(8) - random.nextInt(8);
            int z = origin.getZ() + random.nextInt(8) - random.nextInt(8);
            int y = world.getHeight(Heightmap.Type.OCEAN_FLOOR, x, z);

            bottomPos.set(x, y, z);
            generated |= this.generateOne(world, bottomPos, random, mutablePos);
        }

        return generated;
    }

    private boolean generateOne(ISeedReader world, BlockPos pos, Random random, BlockPos.Mutable mutablePos) {
        if (!REEDS.canSurvive(world, pos) || !this.canReplace(world.getBlockState(pos))) {
            return false;
        }

        int waterDepth = this.getWaterDepthAt(world, pos, mutablePos);
        int height = waterDepth + random.nextInt(HEIGHT_ABOVE_WATER) + 1;
        if (!this.validateHeight(world, pos, height, mutablePos)) {
            return false;
        }

        if (height == 1) {
            this.generateShort(world, pos);
        } else {
            this.generateTall(world, pos, height, mutablePos);
        }

        return true;
    }

    private boolean validateHeight(ISeedReader world, BlockPos pos, int height, BlockPos.Mutable mutablePos) {
        if (height > MAX_HEIGHT) {
            return false;
        }

        mutablePos.set(pos);
        for (int y = height; y >= 0; y--) {
            mutablePos.setY(pos.getY() + height);
            if (!this.canReplace(world.getBlockState(mutablePos))) {
                return false;
            }
        }

        return true;
    }

    private boolean canReplace(BlockState state) {
        return state.is(Blocks.WATER) || state.isAir();
    }

    private int getWaterDepthAt(ISeedReader world, BlockPos pos, BlockPos.Mutable mutablePos) {
        int depth = 0;

        mutablePos.set(pos);
        while (world.getBlockState(mutablePos).is(Blocks.WATER)) {
            mutablePos.move(Direction.UP);
            if (++depth >= MAX_DEPTH) {
                break;
            }
        }

        return depth;
    }

    private void generateShort(ISeedReader world, BlockPos pos) {
        BlockState state = REEDS
                .setValue(ReedsBlock.TYPE, ReedsBlock.Type.SINGLE)
                .setValue(ReedsBlock.WATERLOGGED, false);

        world.setBlock(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
    }

    private boolean generateTall(ISeedReader world, BlockPos pos, int height, BlockPos.Mutable mutablePos) {
        for (int y = 0; y < height; y++) {
            mutablePos.setY(pos.getY() + y);

            BlockState state = REEDS
                    .setValue(ReedsBlock.TYPE, y == height - 1 ? ReedsBlock.Type.TOP : ReedsBlock.Type.BOTTOM)
                    .setValue(ReedsBlock.WATERLOGGED, world.getBlockState(mutablePos).is(Blocks.WATER));

            world.setBlock(mutablePos, state, Constants.BlockFlags.BLOCK_UPDATE);
        }

        return true;
    }
}
