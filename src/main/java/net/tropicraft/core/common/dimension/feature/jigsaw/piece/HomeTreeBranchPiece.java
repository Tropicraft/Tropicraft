package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.tropicraft.core.common.block.TropicraftBlocks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public final class HomeTreeBranchPiece extends StructurePoolElement implements IntersectionAllowedPiece {
    public static final MapCodec<HomeTreeBranchPiece> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.FLOAT.fieldOf("min_angle").forGetter(c -> c.minAngle),
            Codec.FLOAT.fieldOf("max_angle").forGetter(c -> c.maxAngle)
    ).apply(i, HomeTreeBranchPiece::new));

    private static final int MAX_RADIUS = 48;
    private static final Direction.Axis[] ALL_AXIS = Direction.Axis.values();

    private static final CompoundTag JIGSAW_NBT = createJigsawNbt();

    // TODO make home tree radius configurable
    public final float minAngle;
    public final float maxAngle;

    public HomeTreeBranchPiece(float minAngle, float maxAngle) {
        super(StructureTemplatePool.Projection.RIGID);
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    public static Function<StructureTemplatePool.Projection, HomeTreeBranchPiece> create(float minAngle, float maxAngle) {
        return placementBehaviour -> new HomeTreeBranchPiece(minAngle, maxAngle);
    }

    private static CompoundTag createJigsawNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("name", "minecraft:bottom");
        nbt.putString("final_state", "minecraft:air");
        nbt.putString("pool", "minecraft:empty");
        nbt.putString("target", "minecraft:empty");
        nbt.putString("joint", JigsawBlockEntity.JointType.ROLLABLE.getSerializedName());
        return nbt;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager templates, BlockPos pos, Rotation rotation, RandomSource random) {
        FrontAndTop orientation = FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH);
        BlockState state = Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, orientation);
        return List.of(new StructureTemplate.StructureBlockInfo(pos, state, JIGSAW_NBT));
    }

    @Override
    public Vec3i getSize(StructureTemplateManager templates, Rotation rotation) {
        return Vec3i.ZERO;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager templates, BlockPos pos, Rotation rotation) {
        return new BoundingBox(
                pos.getX() - MAX_RADIUS, pos.getY() - MAX_RADIUS, pos.getZ() - MAX_RADIUS,
                pos.getX() + MAX_RADIUS, pos.getY() + MAX_RADIUS, pos.getZ() + MAX_RADIUS
        );
    }

    @Override
    public boolean place(StructureTemplateManager templates, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos origin, BlockPos p_227341_, Rotation rotation, BoundingBox chunkBounds, RandomSource random, LiquidSettings liquidSettings, boolean p_227345_) {
        WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(level.getSeed()));
        rand.setDecorationSeed(level.getSeed(), origin.getX(), origin.getZ());

        int branchLength = rand.nextInt(10) + 15;
        // TODO make configurable
        int branchX1 = origin.getX();
        int branchZ1 = origin.getZ();
        double minAngle = Math.toRadians(this.minAngle);
        double maxAngle = Math.toRadians(this.maxAngle);
        double angle = minAngle + rand.nextFloat() * (maxAngle - minAngle);
        int branchX2 = (int) ((branchLength * Math.sin(angle)) + branchX1);
        int branchZ2 = (int) ((branchLength * Math.cos(angle)) + branchZ1);
        int branchY2 = rand.nextInt(4) + 4;

        BlockState wood = TropicraftBlocks.MAHOGANY_LOG.get().defaultBlockState();
        BlockState leaf = TropicraftBlocks.MAHOGANY_LEAVES.get().defaultBlockState();
        final int leafCircleSizeConstant = 3;
        int y2 = origin.getY() + branchY2;

        placeBlockLine(level, new BlockPos(branchX1, origin.getY(), branchZ1), new BlockPos(branchX2, y2, branchZ2), wood, chunkBounds);
        placeBlockLine(level, new BlockPos(branchX1 + 1, origin.getY(), branchZ1), new BlockPos(branchX2 + 1, y2, branchZ2), wood, chunkBounds);
        placeBlockLine(level, new BlockPos(branchX1 - 1, origin.getY(), branchZ1), new BlockPos(branchX2 - 1, y2, branchZ2), wood, chunkBounds);
        placeBlockLine(level, new BlockPos(branchX1, origin.getY(), branchZ1 + 1), new BlockPos(branchX2, y2, branchZ2 + 1), wood, chunkBounds);
        placeBlockLine(level, new BlockPos(branchX1, origin.getY(), branchZ1 - 1), new BlockPos(branchX2, y2, branchZ2 - 1), wood, chunkBounds);
        placeBlockLine(level, new BlockPos(branchX1, origin.getY() - 1, branchZ1), new BlockPos(branchX2, y2 - 1, branchZ2), wood, chunkBounds);
        placeBlockLine(level, new BlockPos(branchX1, origin.getY() + 1, branchZ1), new BlockPos(branchX2, y2 + 1, branchZ2), wood, chunkBounds);
        genLeafCircle(level, branchX2, y2 - 1, branchZ2, leafCircleSizeConstant + 5, leafCircleSizeConstant + 3, leaf, chunkBounds);
        genLeafCircle(level, branchX2, y2, branchZ2, leafCircleSizeConstant + 6, 0, leaf, chunkBounds);
        genLeafCircle(level, branchX2, y2 + 1, branchZ2, leafCircleSizeConstant + 10, 0, leaf, chunkBounds);
        genLeafCircle(level, branchX2, y2 + 2, branchZ2, leafCircleSizeConstant + 9, 0, leaf, chunkBounds);

        return true;
    }

    public void genLeafCircle(WorldGenLevel world, int x, int y, int z, int outerRadius, int innerRadius, BlockState state, BoundingBox chunkBounds) {
        int outerRadiusSquared = outerRadius * outerRadius;
        int innerRadiusSquared = innerRadius * innerRadius;

        BlockPos origin = new BlockPos(x, y, z);
        BoundingBox outerBounds = BoundingBox.fromCorners(
                origin.offset(-outerRadius, 0, -outerRadius),
                origin.offset(outerRadius, 0, outerRadius)
        );

        BoundingBox bounds = intersection(chunkBounds, outerBounds);
        if (bounds == null) {
            // this leaf circle does not intersect with our given chunk bounds
            return;
        }

        for (BlockPos pos : BlockPos.betweenClosed(bounds.minX(), bounds.minY(), bounds.minZ(), bounds.maxX(), bounds.maxY(), bounds.maxZ())) {
            double distanceSquared = pos.distSqr(origin);
            if (distanceSquared <= outerRadiusSquared && distanceSquared >= innerRadiusSquared) {
                if (world.isEmptyBlock(pos) || world.getBlockState(pos).getBlock() == state.getBlock()) {
                    world.setBlock(pos, state, Block.UPDATE_ALL);
                }
            }
        }
    }

    private void placeBlockLine(WorldGenLevel world, BlockPos from, BlockPos to, BlockState state, BoundingBox chunkBounds) {
        BoundingBox lineBounds = BoundingBox.fromCorners(from, to);
        if (!chunkBounds.intersects(lineBounds)) {
            return;
        }

        BlockPos delta = to.subtract(from);
        Direction.Axis primaryAxis = getLongestAxis(delta);

        int maxLength = Math.abs(getCoordinateAlong(delta, primaryAxis));
        if (maxLength == 0) {
            return;
        }

        double stepX = (double) getCoordinateAlong(delta, Direction.Axis.X) / maxLength;
        double stepY = (double) getCoordinateAlong(delta, Direction.Axis.Y) / maxLength;
        double stepZ = (double) getCoordinateAlong(delta, Direction.Axis.Z) / maxLength;

        for (int length = 0; length <= maxLength; length++) {
            BlockPos pos = BlockPos.containing(
                    from.getX() + length * stepX + 0.5,
                    from.getY() + length * stepY + 0.5,
                    from.getZ() + length * stepZ + 0.5
            );
            if (chunkBounds.isInside(pos)) {
                world.setBlock(pos, state, Block.UPDATE_ALL);
            }
        }
    }

    private Direction.Axis getLongestAxis(BlockPos delta) {
        Direction.Axis longestAxis = Direction.Axis.X;
        int longestLength = 0;
        for (Direction.Axis axis : ALL_AXIS) {
            int length = Math.abs(getCoordinateAlong(delta, axis));
            if (length > longestLength) {
                longestAxis = axis;
                longestLength = length;
            }
        }
        return longestAxis;
    }

    @Nullable
    private static BoundingBox intersection(BoundingBox left, BoundingBox right) {
        if (!left.intersects(right)) {
            return null;
        }

        return new BoundingBox(
                Math.max(left.minX(), right.minX()),
                Math.max(left.minY(), right.minY()),
                Math.max(left.minZ(), right.minZ()),
                Math.min(left.maxX(), right.maxX()),
                Math.min(left.maxY(), right.maxY()),
                Math.min(left.maxZ(), right.maxZ())
        );
    }

    private static int getCoordinateAlong(Vec3i pos, Direction.Axis axis) {
        return axis.choose(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return TropicraftStructurePoolElementTypes.HOME_TREE_BRANCH.get();
    }
}
