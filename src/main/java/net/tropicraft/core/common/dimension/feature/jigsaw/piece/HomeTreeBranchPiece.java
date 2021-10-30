package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JigsawBlock;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public final class HomeTreeBranchPiece extends JigsawPiece implements PieceWithGenerationBounds {
    public static final Codec<HomeTreeBranchPiece> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.FLOAT.fieldOf("min_angle").forGetter(c -> c.minAngle),
                Codec.FLOAT.fieldOf("max_angle").forGetter(c -> c.maxAngle)
        ).apply(instance, HomeTreeBranchPiece::new);
    });

    private static final int MAX_SIZE = 32;
    private static final Direction.Axis[] ALL_AXIS = Direction.Axis.values();

    private static final IJigsawDeserializer<HomeTreeBranchPiece> TYPE = IJigsawDeserializer.func_236851_a_(Constants.MODID + ":home_tree_branch", CODEC);

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
    public List<Template.BlockInfo> getJigsawBlocks(TemplateManager templates, BlockPos pos, Rotation rotation, Random random) {
        JigsawOrientation orientation = JigsawOrientation.func_239641_a_(Direction.DOWN, Direction.SOUTH);
        BlockState state = Blocks.JIGSAW.getDefaultState().with(JigsawBlock.ORIENTATION, orientation);
        return ImmutableList.of(new Template.BlockInfo(pos, state, JIGSAW_NBT));
    }

    @Override
    public MutableBoundingBox getGenerationBounds(TemplateManager templates, BlockPos pos, Rotation rotation) {
        return new MutableBoundingBox(
                pos.getX() - MAX_SIZE, pos.getY() - MAX_SIZE, pos.getZ() - MAX_SIZE,
                pos.getX() + MAX_SIZE, pos.getY() + MAX_SIZE, pos.getZ() + MAX_SIZE
        );
    }

    @Override
    public MutableBoundingBox getBoundingBox(TemplateManager templates, BlockPos pos, Rotation rotation) {
        // hack: return an empty bounding box when running jigsaw assembly so that we can intersect with other branches
        return new MutableBoundingBox(pos, pos);
    }

    @Override
    public boolean func_230378_a_(TemplateManager templates, ISeedReader world, StructureManager structures, ChunkGenerator generator, BlockPos origin, BlockPos p_230378_6_, Rotation rotation, MutableBoundingBox chunkBounds, Random random, boolean p_230378_10_) {
        SharedSeedRandom rand = new SharedSeedRandom();
        rand.setDecorationSeed(world.getSeed(), origin.getX(), origin.getZ());

        final int branchLength = rand.nextInt(10) + 15;
        // TODO make configurable
        int branchX1 = origin.getX();
        int branchZ1 = origin.getZ();
        final double minAngle = Math.toRadians(this.minAngle);
        final double maxAngle = Math.toRadians(this.maxAngle);
        final double angle = minAngle + rand.nextFloat() * (maxAngle - minAngle);
        int branchX2 = (int) ((branchLength * Math.sin(angle)) + branchX1);
        int branchZ2 = (int) ((branchLength * Math.cos(angle)) + branchZ1);
        int branchY2 = rand.nextInt(4) + 4;

        BlockState wood = TropicraftBlocks.MAHOGANY_LOG.get().getDefaultState();
        final BlockState leaf = TropicraftBlocks.MAHOGANY_LEAVES.get().getDefaultState();
        final int leafCircleSizeConstant = 3;
        final int y2 = origin.getY() + branchY2;

        placeBlockLine(world, new BlockPos(branchX1, origin.getY(), branchZ1), new BlockPos(branchX2, y2, branchZ2), wood, chunkBounds);
        placeBlockLine(world, new BlockPos(branchX1 + 1, origin.getY(), branchZ1), new BlockPos(branchX2 + 1, y2, branchZ2), wood, chunkBounds);
        placeBlockLine(world, new BlockPos(branchX1 - 1, origin.getY(), branchZ1), new BlockPos(branchX2 - 1, y2, branchZ2), wood, chunkBounds);
        placeBlockLine(world, new BlockPos(branchX1, origin.getY(), branchZ1 + 1), new BlockPos(branchX2, y2, branchZ2 + 1), wood, chunkBounds);
        placeBlockLine(world, new BlockPos(branchX1, origin.getY(), branchZ1 - 1), new BlockPos(branchX2, y2, branchZ2 - 1), wood, chunkBounds);
        placeBlockLine(world, new BlockPos(branchX1, origin.getY() - 1, branchZ1), new BlockPos(branchX2, y2 - 1, branchZ2), wood, chunkBounds);
        placeBlockLine(world, new BlockPos(branchX1, origin.getY() + 1, branchZ1), new BlockPos(branchX2, y2 + 1, branchZ2), wood, chunkBounds);
        genLeafCircle(world, branchX2, y2 - 1, branchZ2, leafCircleSizeConstant + 5, leafCircleSizeConstant + 3, leaf, chunkBounds);
        genLeafCircle(world, branchX2, y2, branchZ2, leafCircleSizeConstant + 6, 0, leaf, chunkBounds);
        genLeafCircle(world, branchX2, y2 + 1, branchZ2, leafCircleSizeConstant + 10, 0, leaf, chunkBounds);
        genLeafCircle(world, branchX2, y2 + 2, branchZ2, leafCircleSizeConstant + 9, 0, leaf, chunkBounds);

        return true;
    }

    public void genLeafCircle(final IWorld world, final int x, final int y, final int z, int outerRadius, int innerRadius, BlockState state, MutableBoundingBox chunkBounds) {
        int outerRadiusSquared = outerRadius * outerRadius;
        int innerRadiusSquared = innerRadius * innerRadius;

        BlockPos origin = new BlockPos(x, y, z);
        MutableBoundingBox bounds = intersection(
                chunkBounds,
                new MutableBoundingBox(
                        origin.add(-outerRadius, 0, -outerRadius),
                        origin.add(outerRadius, 0, outerRadius)
                )
        );

        // this leaf circle does not intersect with our given chunk bounds
        if (bounds == null) {
            return;
        }

        for (BlockPos pos : BlockPos.getAllInBoxMutable(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ)) {
            double distanceSquared = pos.distanceSq(origin);
            if (distanceSquared <= outerRadiusSquared && distanceSquared >= innerRadiusSquared) {
                if (world.isAirBlock(pos) || world.getBlockState(pos).getBlock() == state.getBlock()) {
                    world.setBlockState(pos, state, BlockFlags.DEFAULT);
                }
            }
        }
    }

    private void placeBlockLine(final IWorld world, BlockPos from, BlockPos to, BlockState state, MutableBoundingBox chunkBounds) {
        MutableBoundingBox lineBounds = new MutableBoundingBox(from, to);
        if (!chunkBounds.intersectsWith(lineBounds)) {
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
            BlockPos pos = new BlockPos(
                    from.getX() + length * stepX + 0.5,
                    from.getY() + length * stepY + 0.5,
                    from.getZ() + length * stepZ + 0.5
            );
            if (chunkBounds.isVecInside(pos)) {
                world.setBlockState(pos, state, BlockFlags.DEFAULT);
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
    private static MutableBoundingBox intersection(MutableBoundingBox left, MutableBoundingBox right) {
        if (!left.intersectsWith(right)) {
            return null;
        }

        return new MutableBoundingBox(
                Math.max(left.minX, right.minX),
                Math.max(left.minY, right.minY),
                Math.max(left.minZ, right.minZ),
                Math.min(left.maxX, right.maxX),
                Math.min(left.maxY, right.maxY),
                Math.min(left.maxZ, right.maxZ)
        );
    }

    private static int getCoordinateAlong(Vector3i pos, Direction.Axis axis) {
        return axis.getCoordinate(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public IJigsawDeserializer<?> getType() {
        return TYPE;
    }
}
