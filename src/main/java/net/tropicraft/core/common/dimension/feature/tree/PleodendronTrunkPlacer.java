package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import net.tropicraft.core.common.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PleodendronTrunkPlacer extends AbstractTrunkPlacer {
    public static final Codec<PleodendronTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return trunkPlacerParts(instance)
                .apply(instance, PleodendronTrunkPlacer::new);
    });

    public PleodendronTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TropicraftTrunkPlacers.PLEODENDRON;
    }

    @Override
    public List<FoliagePlacer.Foliage> placeTrunk(IWorldGenerationReader world, Random random, int height, BlockPos origin, Set<BlockPos> logs, MutableBoundingBox bounds, BaseTreeFeatureConfig config) {
        setDirtAt(world, origin.below());
        List<FoliagePlacer.Foliage> leafNodes = new ArrayList<>();

        for (int i = 0; i < height; ++i) {
            placeLog(world, random, origin.above(i), logs, bounds, config);
        }

        leafNodes.add(new FoliagePlacer.Foliage(origin.above(height + 1), -1, false));

        for (int i = 5; i < height - 4; ++i) {
            if (random.nextInt(4) == 0) {
                growBranches(world, random, origin.above(i), logs, bounds, config, leafNodes);
            }
        }

        return leafNodes;
    }

    private void growBranches(IWorldGenerationReader world, Random random, BlockPos origin, Set<BlockPos> logs, MutableBoundingBox bounds, BaseTreeFeatureConfig config, List<FoliagePlacer.Foliage> leafNodes) {
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
                setBlock(world, local, config.trunkProvider.getState(random, local).setValue(RotatedPillarBlock.AXIS, axis), bounds);
                logs.add(local);

                // Add leaves around the branch
                if (j == dist) {
                    leafNodes.add(new FoliagePlacer.Foliage(local.above(1), -2, false));
                }
            }
        }
    }
}
