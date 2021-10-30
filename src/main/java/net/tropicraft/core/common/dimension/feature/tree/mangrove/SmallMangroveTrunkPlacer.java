package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class SmallMangroveTrunkPlacer extends AbstractTrunkPlacer {
    public static final Codec<SmallMangroveTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return trunkPlacerParts(instance)
                .and(Registry.BLOCK.fieldOf("roots_block").forGetter(c -> c.rootsBlock))
                .apply(instance, SmallMangroveTrunkPlacer::new);
    });

    private final Block rootsBlock;

    public SmallMangroveTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, Block rootsBlock) {
        super(baseHeight, heightRandA, heightRandB);
        this.rootsBlock = rootsBlock;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TropicraftTrunkPlacers.SMALL_MANGROVE;
    }

    @Override
    public List<FoliagePlacer.Foliage> placeTrunk(IWorldGenerationReader world, Random random, int height, BlockPos origin, Set<BlockPos> logs, MutableBoundingBox bounds, BaseTreeFeatureConfig config) {
        setDirtAt(world, origin.below());

        for (int i = 0; i < height; ++i) {
            placeLog(world, random, origin.above(i), logs, bounds, config);
        }

        generateRoots(world, random, origin, 0);

        return ImmutableList.of(new FoliagePlacer.Foliage(origin.above(height - 1), 1, false));
    }

    private void generateRoots(IWorldGenerationReader world, Random random, BlockPos origin, int depth) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offset = origin.relative(direction);

            if (world.isStateAtPosition(offset, AbstractBlock.AbstractBlockState::isAir)) {
                if (world.isStateAtPosition(offset.below(), state -> state.getMaterial().isSolidBlocking())) {
                    TreeFeature.setBlockKnownShape(world, offset, this.rootsBlock.defaultBlockState());

                    if (depth < 2 && random.nextInt(depth + 2) == 0) {
                        generateRoots(world, random, offset, depth + 1);
                    }
                }
            }
        }
    }
}
