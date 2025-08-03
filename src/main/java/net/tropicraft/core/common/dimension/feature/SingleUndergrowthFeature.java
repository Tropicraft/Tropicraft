package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftLeavesFixer;

import java.util.Set;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;

public class SingleUndergrowthFeature extends Feature<SimpleTreeFeatureConfig> {
    private static final int LARGE_BUSH_CHANCE = 4;

    public SingleUndergrowthFeature(Codec<SimpleTreeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleTreeFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource rand = context.random();
        BlockPos pos = context.origin();
        SimpleTreeFeatureConfig config = context.config();

        int size = rand.nextInt(LARGE_BUSH_CHANCE) == 0 ? 3 : 2;

        if (!isValidPosition(level, pos)) {
            return false;
        }

        if (goesBeyondWorldSize(level, pos.getY(), size)) {
            return false;
        }

        if (!TropicraftFeatureUtil.isSoil(level, pos.below())) {
            return false;
        }

        level.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);

        Set<BlockPos> leaves = new ObjectOpenHashSet<>();
        BlockState leavesState = config.leaves().get();

        int count = 0;

        if (isValidPosition(level, pos) && pos.getY() < level.getMaxY()) {
            for (int y = pos.getY(); y < pos.getY() + size; y++) {
                int bushWidth = size - (y - pos.getY());
                for (int x = pos.getX() - bushWidth; x < pos.getX() + bushWidth; x++) {
                    int xVariance = x - pos.getX();
                    for (int z = pos.getZ() - bushWidth; z < pos.getZ() + bushWidth; z++) {
                        int zVariance = z - pos.getZ();
                        BlockPos newPos = new BlockPos(x, y, z);
                        if ((Math.abs(xVariance) != bushWidth || Math.abs(zVariance) != bushWidth || rand.nextInt(2) != 0) && isValidPosition(level, newPos)) {
                            setBlock(level, newPos, leavesState);
                            leaves.add(newPos);
                        }
                    }
                }
            }
            ++count;
        }

        setBlock(level, pos, config.log().get());

        return count > 0 && TropicraftLeavesFixer.updateLeaves(level, Set.of(pos), leaves, leavesState);
    }

    protected boolean isValidPosition(LevelSimulatedReader level, BlockPos pos) {
        return TreeFeature.isAirOrLeaves(level, pos) && !level.isStateAtPosition(pos, Blocks.CAVE_AIR.defaultBlockState()::equals);
    }
}
