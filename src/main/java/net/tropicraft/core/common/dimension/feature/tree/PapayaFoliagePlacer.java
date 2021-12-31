package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.ConstantInt;
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
    private static final Direction[] DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
    public static final Codec<PapayaFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return func_242830_b(instance).apply(instance, PapayaFoliagePlacer::new);
    });

    public PapayaFoliagePlacer(ConstantInt radius, ConstantInt offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.PAPAYA.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {

    }

    @Override
    protected void func_230372_a_(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int p_230372_4_, Foliage node, int p_230372_6_, int radius, Set<BlockPos> leaves, int p_230372_9_, MutableBoundingBox bounds) {
        // Top + shape
        this.func_236753_a_(world, random, config, node.func_236763_a_(), 1, leaves, 1, node.func_236765_c_(), bounds);

        BlockPos origin = node.func_236763_a_();
        // Center leaves
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 0; y++) {
                    if (y == -1 && (Math.abs(x) == 1 && Math.abs(z) == 1) && random.nextBoolean()) {
                        continue;
                    }

                    BlockPos local = origin.add(x, y, z);
                    set(world, local, random, config, leaves, bounds);
                }
            }
        }

        // Arms
        for (Direction direction : DIRECTIONS) {
            set(world, origin.offset(direction, 2), random, config, leaves, bounds);
            set(world, origin.offset(direction, 3), random, config, leaves, bounds);
            set(world, origin.offset(direction, 3).down(), random, config, leaves, bounds);
            set(world, origin.offset(direction, 4).down(), random, config, leaves, bounds);
        }
    }

    private static void set(IWorldGenerationReader world, BlockPos pos, Random random, BaseTreeFeatureConfig config, Set<BlockPos> leaves, MutableBoundingBox bounds) {
        if (TreeFeature.isReplaceableAt(world, pos)) {
            world.setBlockState(pos, config.leavesProvider.getBlockState(random, pos), 19);
            bounds.expandTo(new MutableBoundingBox(pos, pos));
            leaves.add(pos);
        }
    }

    @Override
    public int foliageHeight(Random pRandom, int pHeight, TreeConfiguration pConfig) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius;
    }

}
