package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.material.Fluids;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.MangroveRootsBlock;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class MangroveTrunkPlacer extends FancyTrunkPlacer {
    public static final MapCodec<MangroveTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i.group(
            BlockStateProvider.CODEC.fieldOf("roots_block").forGetter(c -> c.rootsBlock),
            Codec.BOOL.fieldOf("can_generate_raised").forGetter(c -> c.canGenerateRaised),
            Codec.BOOL.fieldOf("tea_mangrove").forGetter(c -> c.teaMangrove)
    )).apply(i, MangroveTrunkPlacer::new));

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 4;

    private static final int MAX_RADIUS = MAX_LENGTH;
    private static final int MAX_SIZE = MAX_RADIUS * 2 + 1;

    private final BlockStateProvider rootsBlock;
    private final boolean canGenerateRaised;
    private final boolean teaMangrove;

    public MangroveTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, BlockStateProvider rootsBlock, boolean canGenerateRaised, boolean teaMangrove) {
        super(baseHeight, heightRandA, heightRandB);
        this.rootsBlock = rootsBlock;
        this.canGenerateRaised = canGenerateRaised;
        this.teaMangrove = teaMangrove;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TropicraftTrunkPlacers.MANGROVE.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> acceptor, RandomSource random, int height, BlockPos origin, TreeConfiguration config) {
        int rootLength = Mth.clamp(height - 5, MIN_LENGTH, MAX_LENGTH);

        boolean placeDirtOnOrigin = world.isStateAtPosition(origin.below(), b -> b.is(Blocks.GRASS_BLOCK));
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

        this.placeRoots((LevelSimulatedRW) world, origin, rootLength, roots, random);

        if (placeDirtOnOrigin) {
            // Set ground to dirt
            setDirtAt(world, acceptor, random, origin.below(), config);
        }

        for (int i = 0; i < height; ++i) {
            placeLog(world, acceptor, random, origin.above(i), config);
        }

        List<FoliagePlacer.FoliageAttachment> leafNodes = new ArrayList<>();
        leafNodes.add(new FoliagePlacer.FoliageAttachment(origin.above(height), 1, false));

        this.growBranches((LevelSimulatedRW) world, acceptor, random, height, origin, config, leafNodes);

        return leafNodes;
    }

    private int getWaterDepthAbove(LevelSimulatedReader world, BlockPos origin, int maxDepth) {
        BlockPos.MutableBlockPos pos = origin.mutable();

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

    private void growBranches(LevelSimulatedRW world, BiConsumer<BlockPos, BlockState> acceptor, RandomSource random, int height, BlockPos origin, TreeConfiguration config, List<FoliagePlacer.FoliageAttachment> leafNodes) {
        int count = 2 + random.nextInt(3);

        Direction lastDirection = null;

        for (int i = 0; i < count; i++) {
            BlockPos base = origin.above(height - count + i);

            int length = 1 + random.nextInt(2);

            boolean hasBranch = false;

            Direction direction;
            do {
                direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            } while (direction == lastDirection);

            lastDirection = direction;

            for (int j = 1; j <= length + 1; j++) {
                if (j == length) {
                    placeLog(world, acceptor, random, base.relative(direction, j).above(), config);
                    leafNodes.add(new FoliagePlacer.FoliageAttachment(base.relative(direction, j).above(), random.nextInt(2), false));
                    break;
                }

                // Branch off the current branch to make a small node
                if (!hasBranch && random.nextBoolean()) {
                    hasBranch = true;
                    Direction branchBranchDir = random.nextBoolean() ? direction.getClockWise() : direction.getCounterClockWise();

                    placeLog(world, acceptor, random, base.relative(direction, j).relative(branchBranchDir), config);
                    leafNodes.add(new FoliagePlacer.FoliageAttachment(base.relative(direction, j).relative(branchBranchDir), 0, false));
                }

                placeLog(world, acceptor, random, base.relative(direction, j), config);
            }
        }
    }

    private void growRoots(RootSystem roots, RandomSource random, int length) {
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

    private static Direction nextFlow(RandomSource random, Direction side) {
        return switch (random.nextInt(3)) {
            case 0 -> side.getClockWise();
            case 1 -> side.getCounterClockWise();
            default -> side;
        };
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

    private void placeRoots(LevelSimulatedRW world, BlockPos origin, int rootLength, RootSystem roots, RandomSource random) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int z = -MAX_RADIUS; z <= MAX_RADIUS; z++) {
            for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {
                int root = roots.get(RootSystem.pos(x, z));
                if (root == RootSystem.NULL) {
                    continue;
                }

                mutablePos.set(origin.getX() + x, 0, origin.getZ() + z);

                int rootHeight = rootLength - RootSystem.distance(root);
                if (rootHeight <= 0) continue;

                int maxY = origin.getY() + rootHeight;
                int minY = maxY - 8;

                int y = maxY;
                while (y >= minY) {
                    mutablePos.setY(y--);
                    if (!this.setRootsAt(world, mutablePos, random)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean setRootsAt(LevelSimulatedRW world, BlockPos pos, RandomSource random) {
        return setRootsAt(world, pos, this.rootsBlock.getState(random, pos));
    }

    public static boolean setRootsAt(LevelSimulatedRW world, BlockPos pos, BlockState rootsBlock) {
        if (isReplaceableAt(world, pos)) {
            BlockState state = rootsBlock.setValue(MangroveRootsBlock.WATERLOGGED, isWaterAt(world, pos));
            world.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isReplaceableAt(LevelSimulatedReader world, BlockPos pos) {
        return world.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(TropicraftTags.Blocks.ROOTS));
    }

    public static boolean isWaterAt(LevelSimulatedReader world, BlockPos pos) {
        return world.isStateAtPosition(pos, state -> state.getFluidState().getType() == Fluids.WATER);
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
            int growPos = RootSystem.offsetPos(pos, flow.getStepX(), flow.getStepZ());
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
            return root(distance) | (side.get2DDataValue() << 3) | (flow.get2DDataValue() << 1);
        }

        static int root(int distance) {
            return (distance << 5) | 1;
        }

        static int distance(int root) {
            return root >> 5;
        }

        static Direction side(int root) {
            return Direction.from2DDataValue((root >> 3) & 0b11);
        }

        static Direction flow(int root) {
            return Direction.from2DDataValue((root >> 1) & 0b11);
        }
    }
}
