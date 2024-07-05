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
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> acceptor, RandomSource random, int height, BlockPos origin, TreeConfiguration config) {
        if (world.isStateAtPosition(origin.below(), b -> b.is(Blocks.GRASS_BLOCK))) {
            setDirtAt(world, acceptor, random, origin.below(), config);
        }

        for (int i = 0; i < height; ++i) {
            placeLog(world, acceptor, random, origin.above(i), config);
        }

        generateRoots((LevelSimulatedRW) world, random, origin, 0);

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(origin.above(height - 1), 1, false));
    }

    private void generateRoots(LevelSimulatedRW world, RandomSource random, BlockPos origin, int depth) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offset = origin.relative(direction);

            if (world.isStateAtPosition(offset, BlockBehaviour.BlockStateBase::isAir)) {
                if (world.isStateAtPosition(offset.below(), BlockBehaviour.BlockStateBase::isSolid)) {
                    world.setBlock(offset, this.rootsBlock.defaultBlockState(), 19);

                    if (depth < 2 && random.nextInt(depth + 2) == 0) {
                        generateRoots(world, random, offset, depth + 1);
                    }
                }
            }
        }
    }
}
