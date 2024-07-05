package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.ArrayList;

public abstract class RainforestTreeFeature extends Feature<NoneFeatureConfiguration> {

    /**
     * Used in placeBlockLine
     */
    protected static final byte[] otherCoordPairs = {2, 0, 0, 1, 2, 1};

    public RainforestTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    protected void setState(LevelWriter world, BlockPos pos, BlockState state) {
        setBlockStateInternally(world, pos, state);
    }

    private void setBlockStateInternally(LevelWriter world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 19);
    }

    protected SaplingBlock getSapling() {
        return TropicraftBlocks.MAHOGANY_SAPLING.get();
    }

    protected final BlockState getLeaf() {
        return TropicraftBlocks.KAPOK_LEAVES.get().defaultBlockState();
    }

    protected final BlockState getLog() {
        return TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState();
    }

    protected void placeLog(LevelSimulatedRW world, int x, int y, int z) {
        setState(world, new BlockPos(x, y, z), getLog());
    }

    protected boolean genCircle(LevelSimulatedRW world, int x, int y, int z, double outerRadius, double innerRadius, BlockState state, boolean solid) {
        return genCircle(world, new BlockPos(x, y, z), outerRadius, innerRadius, state, solid);
    }

    /**
     * Places a line from coords ai to coords ai1
     *
     * @param ai One end of the line
     * @param ai1 The other end of the line
     * @param state IBlockState to place
     * @return The coords that blocks were placed on
     */
    public ArrayList<int[]> placeBlockLine(LevelSimulatedRW world, int[] ai, int[] ai1, BlockState state) {
        ArrayList<int[]> places = new ArrayList<>();
        int[] ai2 = {0, 0, 0};
        byte byte0 = 0;
        int j = 0;
        for (; byte0 < 3; byte0++) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
        }

        if (ai2[j] == 0) {
            return null;
        }

        byte byte1 = otherCoordPairs[j];
        byte byte2 = otherCoordPairs[j + 3];
        byte byte3;

        if (ai2[j] > 0) {
            byte3 = 1;
        } else {
            byte3 = -1;
        }

        double d = (double) ai2[byte1] / (double) ai2[j];
        double d1 = (double) ai2[byte2] / (double) ai2[j];
        int[] ai3 = {0, 0, 0};
        int k = 0;
        for (int l = ai2[j] + byte3; k != l; k += byte3) {
            ai3[j] = Mth.floor((double) (ai[j] + k) + 0.5D);
            ai3[byte1] = Mth.floor((double) ai[byte1] + (double) k * d + 0.5D);
            ai3[byte2] = Mth.floor((double) ai[byte2] + (double) k * d1 + 0.5D);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
            setState(world, pos, state);
            places.add(new int[]{ai3[0], ai3[1], ai3[2]});
        }
        return places;
    }

    /**
     * Generates a circle
     *
     * @param world World object
     * @param pos Coordinates of the center of the circle
     * @param outerRadius The radius of the circle's outside edge
     * @param innerRadius The radius of the circle's inner edge, 0 for a full circle
     * @param state BlockState to generate
     * @param solid Whether it should place the block if another block is already occupying that space
     */
    public boolean genCircle(LevelSimulatedRW world, BlockPos pos, double outerRadius, double innerRadius, BlockState state, boolean solid) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        boolean hasGenned = false;
        for (int i = (int) (-outerRadius - 1) + x; i <= (int) (outerRadius + 1) + x; i++) {
            for (int k = (int) (-outerRadius - 1) + z; k <= (int) (outerRadius + 1) + z; k++) {
                double d = (i - x) * (i - x) + (k - z) * (k - z);
                if (d <= outerRadius * outerRadius && d >= innerRadius * innerRadius) {
                    BlockPos pos2 = new BlockPos(i, y, k);
                    if (world.isStateAtPosition(pos2, BlockBehaviour.BlockStateBase::isAir) || solid) {
                        if (world.setBlock(pos2, state, Block.UPDATE_ALL)) {
                            hasGenned = true;
                        }
                    }
                }
            }
        }
        return hasGenned;
    }
}
