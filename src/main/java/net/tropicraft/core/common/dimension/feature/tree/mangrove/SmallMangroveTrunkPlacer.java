package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;

import java.util.List;
import java.util.function.BiConsumer;

public class SmallMangroveTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<SmallMangroveTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return trunkPlacerParts(instance)
                .and(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("roots_block").forGetter(c -> c.rootsBlock))
                .apply(instance, SmallMangroveTrunkPlacer::new);
    });

    private final Block rootsBlock;

    public SmallMangroveTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, Block rootsBlock) {
        super(baseHeight, heightRandA, heightRandB);
        this.rootsBlock = rootsBlock;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TropicraftTrunkPlacers.SMALL_MANGROVE.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
        if (level.isStateAtPosition(origin.below(), b -> b.is(Blocks.GRASS_BLOCK))) {
            placeBelowTrunkBlock(level, trunkSetter, random, origin.below(), config);
        }

        for (int i = 0; i < treeHeight; ++i) {
            placeLog(level, trunkSetter, random, origin.above(i), config);
        }

        generateRoots(level, random, origin, 0);

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(origin.above(treeHeight - 1), 1, false));
    }

    private void generateRoots(LevelSimulatedRW level, RandomSource random, BlockPos origin, int depth) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offset = origin.relative(direction);

            if (level.isStateAtPosition(offset, BlockBehaviour.BlockStateBase::isAir)) {
                if (level.isStateAtPosition(offset.below(), BlockBehaviour.BlockStateBase::isSolid)) {
                    level.setBlock(offset, rootsBlock.defaultBlockState(), 19);

                    if (depth < 2 && random.nextInt(depth + 2) == 0) {
                        generateRoots(level, random, offset, depth + 1);
                    }
                }
            }
        }
    }
}
