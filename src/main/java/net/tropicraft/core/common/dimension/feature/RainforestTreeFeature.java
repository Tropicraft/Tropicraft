package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class RainforestTreeFeature extends AbstractTreeFeature<NoFeatureConfig> {

    /**Used in placeBlockLine*/
    protected static final byte otherCoordPairs[] = {2, 0, 0, 1, 2, 1};

    public RainforestTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, boolean doesNotifyOnPlace) {
        super(func, doesNotifyOnPlace);
    }

    protected void setState(Set<BlockPos> changedBlocks, IWorldWriter world, BlockPos pos, BlockState state, MutableBoundingBox bb) {
        setBlockStateInternally(world, pos, state);
        bb.expandTo(new MutableBoundingBox(pos, pos));
        if (BlockTags.LOGS.contains(state.getBlock())) {
            changedBlocks.add(pos.toImmutable());
        }
    }

    private void setBlockStateInternally(IWorldWriter world, BlockPos pos, BlockState state) {
        if (this.doBlockNotify) {
            world.setBlockState(pos, state, 19);
        } else {
            world.setBlockState(pos, state, 18);
        }
    }

    // TODO use rainforest sapling, if applicable
    protected net.minecraftforge.common.IPlantable getSapling() {
        return sapling;
    }

    protected final BlockState getLeaf() {
        return TropicraftBlocks.KAPOK_LEAVES.get().getDefaultState();
    }
    
    protected final BlockState getLog() {
        return TropicraftBlocks.MAHOGANY_LOG.get().getDefaultState();
    }

    protected void placeLeaf(final Set<BlockPos> changedBlocks, final IWorldGenerationReader world, final MutableBoundingBox bb, int x, int y, int z) {
        setState(changedBlocks, world, new BlockPos(x, y, z), getLeaf(), bb);
    }

    protected void placeLog(final Set<BlockPos> changedBlocks, final IWorldGenerationReader world, final MutableBoundingBox bb, int x, int y, int z) {
        setState(changedBlocks, world, new BlockPos(x, y, z), getLog(), bb);
    }

    protected boolean genCircle(IWorldGenerationReader world, int x, int y, int z, double outerRadius, double innerRadius, BlockState state, boolean solid) {
        return genCircle(world, new BlockPos(x, y, z), outerRadius, innerRadius, state, solid);
    }

    protected boolean genVines(final IWorldGenerationReader world, final Random rand, int i, int j, int k) {
        int m = 2;

        do {
            if (m > 5) {
                return false;
            }

            final BlockPos pos = new BlockPos(i, j, k);
            if (isAir(world, pos)) {
                setBlockStateInternally(world, pos, Blocks.VINE.getDefaultState());
                break;
            }

            m++;
        } while (true);

        int length = rand.nextInt(4) + 4;

        for (int y = j - 1; y > j - length; y--) {
            final BlockPos vinePos = new BlockPos(i, y, k);
            if (isAir(world, vinePos)) {
                setBlockStateInternally(world, vinePos, Blocks.VINE.getDefaultState());
            } else {
                return true;
            }
        }

        return true;
    }

    /**
     * Places a line from coords ai to coords ai1
     * @param ai One end of the line
     * @param ai1 The other end of the line
     * @param state IBlockState to place
     * @return The coords that blocks were placed on
     */
    public ArrayList<int[]> placeBlockLine(Set<BlockPos> changedBlocks, IWorldGenerationReader world, MutableBoundingBox bb, int ai[], int ai1[], BlockState state) {
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

        if(ai2[j] > 0) {
            byte3 = 1;
        } else {
            byte3 = -1;
        }

        double d = (double)ai2[byte1] / (double)ai2[j];
        double d1 = (double)ai2[byte2] / (double)ai2[j];
        int[] ai3 = {0, 0, 0};
        int k = 0;
        for (int l = ai2[j] + byte3; k != l; k += byte3) {
            ai3[j] = MathHelper.floor((double)(ai[j] + k) + 0.5D);
            ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)k * d + 0.5D);
            ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)k * d1 + 0.5D);
            BlockPos pos = new BlockPos(ai3[0], ai3[1], ai3[2]);
            setState(changedBlocks, world, pos, state, bb);
            places.add(new int[] { ai3[0], ai3[1], ai3[2] });
        }
        return places;
    }

    /**
     * Generates a circle
     * @param world World object
     * @param pos Coordinates of the center of the circle
     * @param outerRadius The radius of the circle's outside edge
     * @param innerRadius The radius of the circle's inner edge, 0 for a full circle
     * @param state BlockState to generate
     * @param solid Whether it should place the block if another block is already occupying that space
     */
    public boolean genCircle(IWorldGenerationReader world, BlockPos pos, double outerRadius, double innerRadius, BlockState state, boolean solid) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        boolean hasGenned = false;
        for (int i = (int)(-outerRadius - 1) + x; i <= (int)(outerRadius + 1) + x; i++) {
            for (int k = (int)(-outerRadius - 1) + z; k <= (int)(outerRadius + 1) + z; k++) {
                double d = (i - x) * (i - x) + (k - z) * (k - z);
                if (d <= outerRadius * outerRadius && d >= innerRadius * innerRadius) {
                    BlockPos pos2 = new BlockPos(i, y, k);
                    if (isAir(world, pos2) || solid) {
                        if (world.setBlockState(pos2, state, 3)) {
                            hasGenned = true;
                        }
                    }
                }
            }
        }
        return hasGenned;
    }
}
