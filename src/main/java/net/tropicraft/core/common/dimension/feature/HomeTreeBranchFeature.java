package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.config.HomeTreeBranchConfig;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.tree.RainforestVinesFeature;

import java.util.Random;

public class HomeTreeBranchFeature<T extends HomeTreeBranchConfig> extends Feature<T> {
    private static final Direction.Axis[] ALL_AXIS = Direction.Axis.values();

    private final ConfiguredFeature<RainforestVinesConfig, RainforestVinesFeature> vinesFeature;

    public HomeTreeBranchFeature(Codec<T> codec) {
        super(codec);
        this.vinesFeature = new ConfiguredFeature<>(TropicraftFeatures.VINES.get(), new RainforestVinesConfig(4, 13, 12));
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, T config) {
        SharedSeedRandom rand = new SharedSeedRandom();
        rand.setDecorationSeed(world.getSeed(), pos.getX(), pos.getZ());
        final int branchLength = rand.nextInt(10) + 15;
        // TODO make configurable
        int branchX1 = pos.getX();
        int branchZ1 = pos.getZ();
        final double minAngle = Math.toRadians(config.minAngle);
        final double maxAngle = Math.toRadians(config.maxAngle);
        final double angle = minAngle + rand.nextFloat() * (maxAngle - minAngle);
        int branchX2 = (int)((branchLength * Math.sin(angle)) + branchX1);
        int branchZ2 = (int)((branchLength * Math.cos(angle)) + branchZ1);
        int branchY2 = rand.nextInt(4) + 4;

        BlockState wood = TropicraftBlocks.MAHOGANY_LOG.get().getDefaultState();
        final BlockState leaf = TropicraftBlocks.MAHOGANY_LEAVES.get().getDefaultState();
        final int leafCircleSizeConstant = 3;
        final int y2 = pos.getY() + branchY2;

        placeBlockLine(world, new BlockPos(branchX1, pos.getY(), branchZ1), new BlockPos(branchX2, y2, branchZ2 ), wood);
        placeBlockLine(world, new BlockPos(branchX1 + 1, pos.getY(), branchZ1), new BlockPos(branchX2+ 1, y2, branchZ2), wood);
        placeBlockLine(world, new BlockPos(branchX1 - 1, pos.getY(), branchZ1), new BlockPos(branchX2 - 1, y2, branchZ2), wood);
        placeBlockLine(world, new BlockPos(branchX1, pos.getY(), branchZ1 + 1), new BlockPos(branchX2, y2, branchZ2 + 1), wood);
        placeBlockLine(world, new BlockPos(branchX1, pos.getY(), branchZ1 - 1), new BlockPos(branchX2, y2, branchZ2 - 1), wood);
        placeBlockLine(world, new BlockPos(branchX1, pos.getY() - 1, branchZ1), new BlockPos(branchX2, y2 - 1, branchZ2), wood);
        placeBlockLine(world, new BlockPos(branchX1, pos.getY() + 1, branchZ1), new BlockPos(branchX2, y2 + 1, branchZ2), wood);
        genLeafCircle(world, branchX2, y2 - 1, branchZ2, leafCircleSizeConstant + 5, leafCircleSizeConstant + 3, leaf, true);
        genLeafCircle(world, branchX2, y2, branchZ2, leafCircleSizeConstant + 6, 0, leaf, true);
        genLeafCircle(world, branchX2, y2 + 1, branchZ2, leafCircleSizeConstant + 10, 0, leaf, true);
        genLeafCircle(world, branchX2, y2 + 2, branchZ2, leafCircleSizeConstant + 9, 0, leaf, true);
        this.vinesFeature.generate(world, generator, rand, new BlockPos(branchX2, y2 - 1, branchZ2));

        return false;
    }

    public void genLeafCircle(final IWorld world, final int x, final int y, final int z, int outerRadius, int innerRadius, BlockState state, boolean vines) {
        int outerRadiusSquared = outerRadius * outerRadius;
        int innerRadiusSquared = innerRadius * innerRadius;

        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int i = -outerRadius + x; i <= outerRadius + x; i++) {
            for (int k = -outerRadius + z; k <= outerRadius + z; k++) {
                double d = (x - i) * (x - i) + (z - k) * (z - k);
                if (d <= outerRadiusSquared && d >= innerRadiusSquared) {
                    pos.setPos(i, y, k);
                    if (world.isAirBlock(pos) || world.getBlockState(pos).getBlock() == state.getBlock()) {
                        world.setBlockState(pos, state, Constants.BlockFlags.DEFAULT);
                    }
                }
            }
        }
    }

    private void placeBlockLine(final IWorld world, BlockPos from, BlockPos to, BlockState state) {
        BlockPos delta = to.subtract(from);
        Direction.Axis primaryAxis = getLongestAxis(delta);

        int maxLength = Math.abs(getCoordinateAlong(delta, primaryAxis));
        if (maxLength == 0) {
            return;
        }

        double stepX = (double) getCoordinateAlong(delta, Direction.Axis.X) / maxLength;
        double stepY = (double) getCoordinateAlong(delta, Direction.Axis.Y) / maxLength;
        double stepZ = (double) getCoordinateAlong(delta, Direction.Axis.Z) / maxLength;

        for (int length = 0; length <= maxLength; length++) {
            BlockPos pos = new BlockPos(
                    from.getX() + length * stepX + 0.5,
                    from.getY() + length * stepY + 0.5,
                    from.getZ() + length * stepZ + 0.5
            );
            world.setBlockState(pos, state, Constants.BlockFlags.DEFAULT);
        }
    }

    private Direction.Axis getLongestAxis(BlockPos delta) {
        Direction.Axis longestAxis = Direction.Axis.X;
        int longestLength = 0;
        for (Direction.Axis axis : ALL_AXIS) {
            int length = Math.abs(getCoordinateAlong(delta, axis));
            if (length > longestLength) {
                longestAxis = axis;
                longestLength = length;
            }
        }
        return longestAxis;
    }

    private static int getCoordinateAlong(Vector3i pos, Direction.Axis axis) {
        return axis.getCoordinate(pos.getX(), pos.getY(), pos.getZ());
    }
}
