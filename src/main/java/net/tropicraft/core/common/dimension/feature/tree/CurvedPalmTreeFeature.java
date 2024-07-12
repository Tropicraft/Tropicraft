package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class CurvedPalmTreeFeature extends PalmTreeFeature {
    private static final int Z_PLUS = 0;
    private static final int Z_MINUS = 1;
    private static final int X_PLUS = 2;
    private static final int X_MINUS = 3;
    private static final int TOP_OFFSET = 3;
    private static final int WATER_SEARCH_DIST = 10;

    private int originX, originZ;
    private int dir;

    public CurvedPalmTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        pos = pos.immutable();

        final int height = 9 + random.nextInt(3);

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!getSapling().defaultBlockState().canSurvive(world, pos)) {
            return false;
        }

        if (world.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK) {
            world.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);
        }

        final int x = pos.getX(), y = pos.getY(), z = pos.getZ();

        int dir = this.pickDirection(world, random, x, z);
        this.setDir(dir);
        this.setOrigin(x, z);

        // x bb check
        for (int xx = 0; xx < 4; xx++) {
            for (int yy = 0; yy < height; yy++) {
                final BlockPos posWithDir = getPosWithDir(xx, yy + y, 0);
                if (!isAir(world, posWithDir)) {
                    return false;
                }
            }
        }

        // z bb check
        for (int xx = 0; xx < 9; xx++) {
            for (int zz = 0; zz < 9; zz++) {
                for (int yy = height - 3; yy < height + 4; yy++) {
                    if (!isAir(world, getPosWithDir(xx + TOP_OFFSET, yy + y, zz))) {
                        return false;
                    }
                }
            }
        }

        // generate curved trunk
        for (int xx = 0, yy = 0; yy < height; yy++) {
            placeBlockWithDir(world, xx, yy + y, 0, getLog());
            if (yy == 0 || yy == 1 || yy == 3) {
                xx++;
                placeBlockWithDir(world, xx, yy + y, 0, getLog());
            }
            if (yy == height - 2) {
                spawnCoconuts(world, getPosWithDir(xx, yy + y, 0), random, 2, getLeaf());
            }
        }

        // reset origin
        setOrigin(getActualXAt(TOP_OFFSET, 0), getActualZAt(TOP_OFFSET, 0));

        // inner leaf placement
        for (int yy = 1; yy < 5; yy++) {
            if (yy == 4) {
                this.placeBlockWithDir(world, 1, yy + y + height - 1, 0, getLeaf());
            } else {
                this.placeBlockWithDir(world, 0, yy + y + height - 1, 0, getLeaf());
            }
        }

        // outer leaf placement
        for (int curDir = 0; curDir < 4; curDir++) {
            setDir(curDir);

            int yy = height - 1;

            placeBlockWithDir(world, 1, yy - 1 + y, 1, getLeaf());
            placeBlockWithDir(world, 2, yy - 2 + y, 1, getLeaf());
            placeBlockWithDir(world, 1, yy - 2 + y, 2, getLeaf());
            placeBlockWithDir(world, 2, yy - 3 + y, 2, getLeaf());
            placeBlockWithDir(world, 1, yy + 1 + y, 1, getLeaf());
            placeBlockWithDir(world, 2, yy + 2 + y, 1, getLeaf());
            placeBlockWithDir(world, 1, yy + 2 + y, 2, getLeaf());
            placeBlockWithDir(world, 2, yy + 3 + y, 2, getLeaf());

            for (int xx = 1; xx < 5; xx++) {
                if (xx == 4) {
                    yy--;
                }
                placeBlockWithDir(world, xx, yy + y, 0, getLeaf());
            }
        }

        return true;
    }

    private int findWater(final LevelSimulatedRW world, final RandomSource rand, int x, int z) {
        int iPos = 0;
        int iNeg = 0;
        int kPos = 0;
        int kNeg = 0;
        while (iPos < WATER_SEARCH_DIST && !isWater(world, new BlockPos(x + iPos, TropicraftDimension.SEA_LEVEL, z))) {
            iPos++;
        }

        while (iNeg > -WATER_SEARCH_DIST && !isWater(world, new BlockPos(x + iNeg, TropicraftDimension.SEA_LEVEL, z))) {
            iNeg--;
        }

        while (kPos < WATER_SEARCH_DIST && !isWater(world, new BlockPos(x, TropicraftDimension.SEA_LEVEL, z + kPos))) {
            kPos++;
        }

        while (kNeg > -WATER_SEARCH_DIST && !isWater(world, new BlockPos(x, TropicraftDimension.SEA_LEVEL, z + kNeg))) {
            kNeg--;
        }

        if (iPos < Math.abs(iNeg) && iPos < kPos && iPos < Math.abs(kNeg)) {
            return X_PLUS;       // 1
        } else if (Math.abs(iNeg) < iPos && Math.abs(iNeg) < kPos && Math.abs(iNeg) < Math.abs(kNeg)) {
            return X_MINUS;    // 2
        } else if (kPos < Math.abs(iNeg) && kPos < iPos && kPos < Math.abs(kNeg)) {
            return Z_PLUS;    // 3
        } else if (Math.abs(kNeg) < Math.abs(iNeg) && Math.abs(kNeg) < iPos && Math.abs(kNeg) < kPos) {
            return Z_MINUS;    // 4
        }

        if (iPos < WATER_SEARCH_DIST && iPos == Math.abs(iNeg)) {
            return rand.nextInt(2) + 1;
        } else if (iPos < WATER_SEARCH_DIST && iPos == kPos) {
            if (rand.nextInt(2) + 1 == 1) {
                return X_PLUS;
            } else {
                return Z_PLUS;
            }
        } else if (iPos < WATER_SEARCH_DIST && iPos == Math.abs(kNeg)) {
            if (rand.nextInt(2) + 1 == 1) {
                return X_PLUS;
            } else {
                return Z_MINUS;
            }
        } else if (kPos < WATER_SEARCH_DIST && Math.abs(iNeg) == kPos) {
            if (rand.nextInt(2) + 1 == 1) {
                return X_MINUS;
            } else {
                return Z_PLUS;
            }
        } else if (Math.abs(iNeg) < WATER_SEARCH_DIST && Math.abs(iNeg) == Math.abs(kNeg)) {
            if (rand.nextInt(2) + 1 == 1) {
                return X_MINUS;
            } else {
                return Z_MINUS;
            }
        } else if (kPos < WATER_SEARCH_DIST && kPos == Math.abs(kNeg)) {
            if (rand.nextInt(2) + 1 == 1) {
                return Z_PLUS;
            } else {
                return Z_MINUS;
            }
        } else {
            return -1;
        }
    }

    private static boolean isWater(LevelSimulatedRW world, BlockPos pos) {
        return world.isStateAtPosition(pos, state -> state.is(Blocks.WATER));
    }

    private int pickDirection(final LevelSimulatedRW world, final RandomSource rand, int x, int z) {
        int direction = findWater(world, rand, x, z);
        if (direction != -1) {
            return direction;
        } else {
            return rand.nextInt(4);
        }
    }

    private void setOrigin(int originX, int originZ) {
        this.originX = originX;
        this.originZ = originZ;
    }

    private void setDir(int dir) {
        this.dir = dir;
    }

    private BlockPos getPosWithDir(final int x, final int y, final int z) {
        return getPosWithDir(pos(x, y, z));
    }

    private BlockPos getPosWithDir(final BlockPos unRotatedPos) {
        int i = unRotatedPos.getX();
        int j = unRotatedPos.getY();
        int k = unRotatedPos.getZ();
        switch (this.dir) {
            case 2:
                return pos(this.originX + i, j, this.originZ + k);
            case 0:
                return pos(this.originX + k, j, this.originZ - i);
            case 3:
                return pos(this.originX - i, j, this.originZ - k);
            case 1:
                return pos(this.originX - k, j, this.originZ + i);
        }
        return BlockPos.ZERO;
    }

    private void placeBlockWithDir(final LevelWriter world, int x, int y, int z, BlockState state) {
        switch (dir) {
            case 2:
                setBlock(world, pos(this.originX + x, y, this.originZ + z), state);
                return;
            case 0:
                setBlock(world, pos(this.originX + z, y, this.originZ - x), state);
                return;
            case 3:
                setBlock(world, pos(this.originX - x, y, this.originZ - z), state);
                return;
            case 1:
                setBlock(world, pos(this.originX - z, y, this.originZ + x), state);
        }
    }

    private int getActualXAt(int i, int k) {
        switch (this.dir) {
            case 2:
                return this.originX + i;
            case 0:
                return this.originX + k;
            case 3:
                return this.originX - i;
            case 1:
                return this.originX - k;
        }
        return this.originX;
    }

    private int getActualZAt(int i, int k) {
        switch (this.dir) {
            case 2:
                return this.originZ + k;
            case 0:
                return this.originZ - i;
            case 3:
                return this.originZ - k;
            case 1:
                return this.originZ + i;
        }
        return this.originZ;
    }

    public BlockPos pos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }
}
