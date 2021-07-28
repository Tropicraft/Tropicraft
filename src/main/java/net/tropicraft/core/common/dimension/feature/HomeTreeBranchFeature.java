package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
    private static final byte[] OTHER_COORD_PAIRS = {
        2, 0, 0, 1, 2, 1
    };
    
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

        placeBlockLine(world, new int[] { branchX1, pos.getY(), branchZ1 }, new int[] {branchX2, y2, branchZ2 }, wood);
        placeBlockLine(world, new int[] { branchX1 + 1, pos.getY(), branchZ1 }, new int[] {branchX2+ 1, y2, branchZ2 }, wood);
        placeBlockLine(world, new int[] { branchX1 - 1, pos.getY(), branchZ1 }, new int[] {branchX2 - 1, y2, branchZ2 }, wood);
        placeBlockLine(world, new int[] { branchX1, pos.getY(), branchZ1 + 1 }, new int[] {branchX2, y2, branchZ2 + 1 }, wood);
        placeBlockLine(world, new int[] { branchX1, pos.getY(), branchZ1 - 1 }, new int[] {branchX2, y2, branchZ2 - 1 }, wood);
        placeBlockLine(world, new int[] { branchX1, pos.getY() - 1, branchZ1 }, new int[] {branchX2, y2 - 1, branchZ2 }, wood);
        placeBlockLine(world, new int[] { branchX1, pos.getY() + 1, branchZ1 }, new int[] {branchX2, y2 + 1, branchZ2 }, wood);
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

    private void placeBlockLine(final IWorld world, int[] ai, int[] ai1, BlockState state) {
        int[] ai2 = {
            0, 0, 0
        };
        byte byte0 = 0;
        int j = 0;
        for (; byte0 < 3; byte0++) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if(Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
        }

        if (ai2[j] == 0) {
            return;
        }
        byte byte1 = OTHER_COORD_PAIRS[j];
        byte byte2 = OTHER_COORD_PAIRS[j + 3];
        byte byte3;
        if (ai2[j] > 0) {
            byte3 = 1;
        } else {
            byte3 = -1;
        }
        double d = (double) ai2[byte1] / (double) ai2[j];
        double d1 = (double) ai2[byte2] / (double) ai2[j];
        int[] ai3 = {
                0, 0, 0
        };
        int k = 0;
        for (int l = ai2[j] + byte3; k != l; k += byte3) {
            ai3[j] = MathHelper.floor(ai[j] + k + 0.5D);
            ai3[byte1] = MathHelper.floor(ai[byte1] + k * d + 0.5D);
            ai3[byte2] = MathHelper.floor(ai[byte2] + k * d1 + 0.5D);
            world.setBlockState(new BlockPos(ai3[0], ai3[1], ai3[2]), state, 3);
        }
    }
}
