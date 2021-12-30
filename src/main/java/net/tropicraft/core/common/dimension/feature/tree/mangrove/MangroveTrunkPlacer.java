package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
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
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.MangroveRootsBlock;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class MangroveTrunkPlacer extends FancyTrunkPlacer {
    public static final Codec<MangroveTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return getAbstractTrunkCodec(instance)
                .and(Registry.BLOCK.fieldOf("roots_block").forGetter(c -> c.rootsBlock))
                .and(Codec.BOOL.fieldOf("can_generate_raised").forGetter(c -> c.canGenerateRaised))
                .and(Codec.BOOL.fieldOf("tea_mangrove").forGetter(c -> c.teaMangrove))
                .apply(instance, MangroveTrunkPlacer::new);
    });

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 4;

    private static final int MAX_RADIUS = MAX_LENGTH;
    private static final int MAX_SIZE = MAX_RADIUS * 2 + 1;

    private final Block rootsBlock;
    private final boolean canGenerateRaised;
    private final boolean teaMangrove;

    public MangroveTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, Block rootsBlock, boolean canGenerateRaised, boolean teaMangrove) {
        super(baseHeight, heightRandA, heightRandB);
        this.rootsBlock = rootsBlock;
        this.canGenerateRaised = canGenerateRaised;
        this.teaMangrove = teaMangrove;
    }

    @Override
    protected TrunkPlacerType<?> getPlacerType() {
        return TropicraftTrunkPlacers.MANGROVE;
    }

    @Override
    public List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader world, Random random, int height, BlockPos origin, Set<BlockPos> logs, MutableBoundingBox bounds, BaseTreeFeatureConfig config) {
        int rootLength = MathHelper.clamp(height - 5, MIN_LENGTH, MAX_LENGTH);

        boolean placeDirtOnOrigin = true;
        if (this.canGenerateRaised) {
            int waterDepth = getWaterDepthAbove(world, origin, 3);

            // If we're in 1 or 2 deep water or land, we have a 1/2 chance of making the mangrove raised from the surface of the water
            if (waterDepth <= 2 && random.nextInt(2) == 0) {
                int surfaceY = origin.getY() + waterDepth;
                origin = new BlockPos(origin.getX(), surfaceY + 1, origin.getZ());
                placeDirtOnOrigin = false;
            }
        }

        RootSystem roots = new RootSystem();
        if (this.teaMangrove) {
            this.growTeaRoots(roots, rootLength);
        } else {
            this.growRoots(roots, random, rootLength);
        }

        this.placeRoots(world, origin, rootLength, roots);

        if (placeDirtOnOrigin) {
            // Set ground to dirt
            func_236909_a_(world, origin.down());
        }

        for (int i = 0; i < height; ++i) {
            func_236911_a_(world, random, origin.up(i), logs, bounds, config);
        }

        List<FoliagePlacer.Foliage> leafNodes = new ArrayList<>();
        leafNodes.add(new FoliagePlacer.Foliage(origin.up(height), 1, false));

        this.growBranches(world, random, height, origin, logs, bounds, config, leafNodes);

        return leafNodes;
    }

    private int getWaterDepthAbove(IWorldGenerationReader world, BlockPos origin, int maxDepth) {
        BlockPos.Mutable pos = origin.toMutable();

        int depth = 0;
        while (depth <= maxDepth) {
            pos.setY(origin.getY() + depth);
            if (!isWaterAt(world, pos)) {
                break;
            }
            depth++;
        }

        return depth;
    }

    private void growBranches(IWorldGenerationReader world, Random random, int height, BlockPos origin, Set<BlockPos> logs, MutableBoundingBox bounds, BaseTreeFeatureConfig config, List<FoliagePlacer.Foliage> leafNodes) {
        int count = 2 + random.nextInt(3);

        Direction lastDirection = null;

        for (int i = 0; i < count; i++) {
            BlockPos base = origin.up(height - count + i);

            int length = 1 + random.nextInt(2);

            boolean hasBranch = false;

            Direction direction;
            do {
                direction = Direction.Plane.HORIZONTAL.random(random);
            } while (direction == lastDirection);

            lastDirection = direction;

            for (int j = 1; j <= length + 1; j++) {
                if (j == length) {
                    func_236911_a_(world, random, base.offset(direction, j).up(), logs, bounds, config);
                    leafNodes.add(new FoliagePlacer.Foliage(base.offset(direction, j).up(), random.nextInt(2), false));
                    break;
                }

                // Branch off the current branch to make a small node
                if (!hasBranch && random.nextBoolean()) {
                    hasBranch = true;
                    Direction branchBranchDir = random.nextBoolean() ? direction.rotateY() : direction.rotateYCCW();

                    func_236911_a_(world, random, base.offset(direction, j).offset(branchBranchDir), logs, bounds, config);
                    leafNodes.add(new FoliagePlacer.Foliage(base.offset(direction, j).offset(branchBranchDir), 0, false));
                }

                func_236911_a_(world, random, base.offset(direction, j), logs, bounds, config);
            }
        }
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

    private void growTeaRoots(RootSystem roots, int length) {
        int radius = length / 2;
        for (int z = -radius; z <= radius; z++) {
            for (int x = -radius; x <= radius; x++) {
                if (x == 0 && z == 0) continue;
                int distance = (Math.abs(x) + Math.abs(z) - 1) * 2;
                roots.set(RootSystem.pos(x, z), RootSystem.root(distance));
            }
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
                if (rootHeight <= 0) continue;

                int maxY = origin.getY() + rootHeight;
                int minY = maxY - 8;

                int y = maxY;
                while (y >= minY) {
                    mutablePos.setY(y--);
                    if (!this.setRootsAt(world, mutablePos)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean setRootsAt(IWorldGenerationReader world, BlockPos pos) {
        return setRootsAt(world, pos, this.rootsBlock);
    }

    public static boolean setRootsAt(IWorldGenerationReader world, BlockPos pos, Block rootsBlock) {
        if (isReplaceableAt(world, pos)) {
            BlockState state = rootsBlock.getDefaultState()
                    .with(MangroveRootsBlock.WATERLOGGED, isWaterAt(world, pos));
            TreeFeature.setBlockStateWithoutUpdate(world, pos, state);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isReplaceableAt(IWorldGenerationReader world, BlockPos pos) {
        return world.hasBlockState(pos, state -> {
            return state.isAir()
                    || state.isIn(BlockTags.LEAVES)
                    || state.getMaterial() == Material.TALL_PLANTS
                    || state.getMaterial() == Material.SEA_GRASS
                    || state.getMaterial() == Material.PLANTS
                    || state.matchesBlock(Blocks.WATER)
                    || state.isIn(TropicraftTags.Blocks.ROOTS);
        });
    }

    public static boolean isWaterAt(IWorldGenerationReader world, BlockPos pos) {
        return world.hasBlockState(pos, state -> state.getFluidState().getFluid() == Fluids.WATER);
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
            return root(distance) | (side.getHorizontalIndex() << 3) | (flow.getHorizontalIndex() << 1);
        }

        static int root(int distance) {
            return (distance << 5) | 1;
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
