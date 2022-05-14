package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.tropicraft.core.common.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class PleodendronTrunkPlacer extends TrunkPlacer {
    public static final Codec<PleodendronTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return trunkPlacerParts(instance)
                .apply(instance, PleodendronTrunkPlacer::new);
    });

    public PleodendronTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TropicraftTrunkPlacers.PLEODENDRON.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> acceptor, Random random, int height, BlockPos origin, TreeConfiguration config) {
        setDirtAt(world, acceptor, random, origin.below(), config);
        List<FoliagePlacer.FoliageAttachment> leafNodes = new ArrayList<>();

        for (int i = 0; i < height; ++i) {
            placeLog(world, acceptor, random, origin.above(i), config);
        }

        leafNodes.add(new FoliagePlacer.FoliageAttachment(origin.above(height + 1), -1, false));

        for (int i = 5; i < height - 4; ++i) {
            if (random.nextInt(4) == 0) {
                growBranches((LevelSimulatedRW) world, acceptor, random, origin.above(i), config, leafNodes);
            }
        }

        return leafNodes;
    }

    private void growBranches(LevelSimulatedRW world, BiConsumer<BlockPos, BlockState> acceptor, Random random, BlockPos origin, TreeConfiguration config, List<FoliagePlacer.FoliageAttachment> leafNodes) {
        int count = random.nextInt(2) + 1;
        double thetaOffset = random.nextDouble() * 2 * Math.PI;

        // Place 1-2 branches
        for (int i = 0; i < count; i++) {
            // Get angle of this branch
            double theta = (((double) i / count) * 2 * Math.PI) + thetaOffset;

            // Add a random offset to the theta
            theta += random.nextDouble() * Math.PI * 0.15;

            // Make branches 3-4 blocks long
            int dist = random.nextInt(3) == 0 ? 4 : 3;

            for (int j = 1; j <= dist; j++) {
                int x = (int) (Math.cos(theta) * j);
                int z = (int) (Math.sin(theta) * j);
                BlockPos local = origin.offset(x, 0, z);

                // Get axis based on position
                Direction.Axis axis = Util.getAxisBetween(origin, local);

                // Place branch and add to logs
                acceptor.accept(local, config.trunkProvider.getState(random, local).setValue(RotatedPillarBlock.AXIS, axis));

                // Add leaves around the branch
                if (j == dist) {
                    leafNodes.add(new FoliagePlacer.FoliageAttachment(local.above(1), -2, false));
                }
            }
        }
    }
}
