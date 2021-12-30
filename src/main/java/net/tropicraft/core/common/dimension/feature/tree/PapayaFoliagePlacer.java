package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

public final class PapayaFoliagePlacer extends FoliagePlacer {
    public static final Codec<PapayaFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, PapayaFoliagePlacer::new);
    });

    private static final Direction[] DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public PapayaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.PAPAYA.get();
    }

    //TODO [1.17]: Double check that this is correctly ported for the changes with trees
    @Override
    protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
        this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pAttachment.pos(), 1, 1, pAttachment.doubleTrunk());

        BlockPos origin = pAttachment.pos();
        // Center leaves
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 0; y++) {
                    if (y == -1 && (Math.abs(x) == 1 && Math.abs(z) == 1) && pRandom.nextBoolean()) {
                        continue;
                    }

                    BlockPos local = origin.offset(x, y, z);
                    tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, local);
                }
            }
        }

        // Arms
        for (Direction direction : DIRECTIONS) {
            tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, origin.relative(direction, 2));
            tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, origin.relative(direction, 3));
            tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, origin.relative(direction, 3).below());
            tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, origin.relative(direction, 4).below());
        }
    }


    @Override
    public int foliageHeight(Random p_230374_1_, int p_230374_2_, TreeConfiguration p_230374_3_) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius;
    }
}
