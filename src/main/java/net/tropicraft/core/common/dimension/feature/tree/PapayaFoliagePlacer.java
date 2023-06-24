package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.function.BiConsumer;

public final class PapayaFoliagePlacer extends FoliagePlacer {
    private static final Direction[] DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
    public static final Codec<PapayaFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, PapayaFoliagePlacer::new);
    });

    public PapayaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.PAPAYA.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> acceptor, RandomSource random, TreeConfiguration config, int pMaxFreeTreeHeight, FoliageAttachment node, int pFoliageHeight, int radius, int pOffset) {
        // Top + shape
        this.placeLeavesRow(world, acceptor, random, config, node.pos(), 1, 1, node.doubleTrunk());

        BlockPos origin = node.pos();
        // Center leaves
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 0; y++) {
                    if (y == -1 && (Math.abs(x) == 1 && Math.abs(z) == 1) && random.nextBoolean()) {
                        continue;
                    }

                    BlockPos local = origin.offset(x, y, z);
                    set(world, local, random, config);
                }
            }
        }

        // Arms
        for (Direction direction : DIRECTIONS) {
            set(world, origin.relative(direction, 2), random, config);
            set(world, origin.relative(direction, 3), random, config);
            set(world, origin.relative(direction, 3).below(), random, config);
            set(world, origin.relative(direction, 4).below(), random, config);
        }
    }

    private static void set(LevelSimulatedReader world, BlockPos pos, RandomSource random, TreeConfiguration config) {
        if (TreeFeature.isAirOrLeaves(world, pos)) {
            ((LevelSimulatedRW)world).setBlock(pos, config.foliageProvider.getState(random, pos), 19);
        }
    }

    @Override
    public int foliageHeight(RandomSource pRandom, int pHeight, TreeConfiguration pConfig) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius;
    }

}
