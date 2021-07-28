package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import net.tropicraft.core.common.block.MangroveRootsBlock;

import java.util.List;
import java.util.Random;
import java.util.Set;

public final class MangroveTrunkPlacer extends FancyTrunkPlacer {
    public static final Codec<MangroveTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return getAbstractTrunkCodec(instance)
                .and(Registry.BLOCK.fieldOf("roots_block").forGetter(c -> c.rootsBlock))
                .apply(instance, MangroveTrunkPlacer::new);
    });

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 5;

    private static final int MAX_RADIUS = MAX_LENGTH;
    private static final int MAX_SIZE = MAX_RADIUS * 2 + 1;

    private final Block rootsBlock;

    public MangroveTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, Block rootsBlock) {
        super(baseHeight, heightRandA, heightRandB);
        this.rootsBlock = rootsBlock;
    }

    @Override
    protected TrunkPlacerType<?> getPlacerType() {
        return TropicraftTrunkPlacers.MANGROVE;
    }

    @Override
    public List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader world, Random random, int height, BlockPos origin, Set<BlockPos> logs, MutableBoundingBox bounds, BaseTreeFeatureConfig config) {
        int rootLength = MathHelper.clamp(height - 5, MIN_LENGTH, MAX_LENGTH);

        RootSystem roots = new RootSystem();
        this.growRoots(roots, random, rootLength);
        this.placeRoots(world, origin, rootLength, roots);

        return super.getFoliages(world, random, height, origin, logs, bounds, config);
    }

    private void growRoots(RootSystem roots, Random random, int length) {
        RootGrower grower = new RootGrower(roots);
        grower.growAt(RootSystem.pos(-1, 0), RootSystem.seed(Direction.WEST));
        grower.growAt(RootSystem.pos(1, 0), RootSystem.seed(Direction.EAST));
        grower.growAt(RootSystem.pos(0, -1), RootSystem.seed(Direction.NORTH));
        grower.growAt(RootSystem.pos(0, 1), RootSystem.seed(Direction.SOUTH));

        while (grower.hasNext()) {
            int pos = grower.nextPos();
            int root = roots.get(pos);

            int distance = RootSystem.distance(root);
            if (distance >= length || random.nextInt(8) == 0) continue;

            Direction side = RootSystem.side(root);
            Direction flow = RootSystem.flow(root);

            if (random.nextInt((length - distance >> 1) + 1) == 0) {
                if (distance <= 1 || random.nextBoolean()) {
                    grower.growOut(pos, distance, side, flow);
                }
                grower.growOut(pos, distance, side, nextFlow(random, flow));
            } else {
                grower.growOut(pos, distance, side, flow);
            }
        }
    }

    private static Direction nextFlow(Random random, Direction side) {
        switch (random.nextInt(3)) {
            case 0: return side.rotateY();
            case 1: return side.rotateYCCW();
            default: return side;
        }
    }

    private void placeRoots(IWorldGenerationReader world, BlockPos origin, int rootLength, RootSystem roots) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int z = -MAX_RADIUS; z <= MAX_RADIUS; z++) {
            for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {
                int root = roots.get(RootSystem.pos(x, z));
                if (root == RootSystem.NULL) {
                    continue;
                }

                mutablePos.setPos(origin.getX() + x, 0, origin.getZ() + z);

                int rootHeight = rootLength - RootSystem.distance(root);

                int maxY = origin.getY() + rootHeight;
                int minY = maxY - 8;

                int y = maxY;
                while (y >= minY) {
                    mutablePos.setY(y--);

                    if (isReplaceableAt(world, mutablePos)) {
                        BlockState state = this.rootsBlock.getDefaultState()
                                .with(MangroveRootsBlock.WATERLOGGED, isWaterAt(world, mutablePos));
                        TreeFeature.setBlockStateWithoutUpdate(world, mutablePos, state);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private static boolean isWaterAt(IWorldGenerationReader world, BlockPos pos) {
        return world.hasBlockState(pos, state -> state.matchesBlock(Blocks.WATER));
    }

    private static boolean isReplaceableAt(IWorldGenerationReader world, BlockPos pos) {
        return world.hasBlockState(pos, state -> {
            return state.isAir()
                    || state.isIn(BlockTags.LEAVES)
                    || state.getMaterial() == Material.TALL_PLANTS
                    || state.getMaterial() == Material.SEA_GRASS
                    || state.matchesBlock(Blocks.WATER);
        });
    }

    static final class RootGrower {
        private final RootSystem roots;
        private final IntArrayFIFOQueue queue = new IntArrayFIFOQueue();

        RootGrower(RootSystem roots) {
            this.roots = roots;
        }

        boolean hasNext() {
            return !this.queue.isEmpty();
        }

        int nextPos() {
            return this.queue.dequeueInt();
        }

        void growOut(int pos, int distance, Direction side, Direction flow) {
            int growPos = RootSystem.offsetPos(pos, flow.getXOffset(), flow.getZOffset());
            this.growAt(growPos, RootSystem.root(distance + 1, side, flow));
        }

        void growAt(int pos, int root) {
            if (this.roots.set(pos, root)) {
                this.queue.enqueue(pos);
            }
        }
    }

    static final class RootSystem {
        static final int NULL = 0;

        private final int[] map = new int[MAX_SIZE * MAX_SIZE];

        boolean set(int pos, int root) {
            if (!this.contains(pos)) {
                this.map[pos] = root;
                return true;
            } else {
                return false;
            }
        }

        int get(int pos) {
            return this.map[pos];
        }

        boolean contains(int pos) {
            return this.get(pos) != NULL;
        }

        static int pos(int x, int z) {
            return (x + MAX_RADIUS) + (z + MAX_RADIUS) * MAX_SIZE;
        }

        static int offsetPos(int pos, int x, int z) {
            return pos + x + z * MAX_SIZE;
        }

        static int seed(Direction side) {
            return root(1, side, side);
        }

        static int root(int distance, Direction side, Direction flow) {
            return (distance << 5) | (side.getHorizontalIndex() << 3) | (flow.getHorizontalIndex() << 1) | 1;
        }

        static int distance(int root) {
            return root >> 5;
        }

        static Direction side(int root) {
            return Direction.byHorizontalIndex((root >> 3) & 0b11);
        }

        static Direction flow(int root) {
            return Direction.byHorizontalIndex((root >> 1) & 0b11);
        }
    }
}
