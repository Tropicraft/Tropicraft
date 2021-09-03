package net.tropicraft.core.common.entity.passive.basilisk;

import net.minecraft.block.Block;
import net.minecraft.entity.MobEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.CubeCoordinateIterator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class WaterWalking {
    private static final double HEIGHT = 14.0 / 16.0;
    public static final VoxelShape COLLIDER = Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, HEIGHT * 16.0, 16.0);

    public static boolean canWalkOn(FluidState fluid) {
        return !fluid.isEmpty() && canWalkOn(fluid.getFluid()) && fluid.getLevel() == 8;
    }

    public static boolean canWalkOn(Fluid fluid) {
        return fluid.isIn(FluidTags.WATER);
    }

    public static Vector3d collide(IBlockReader world, AxisAlignedBB box, Vector3d offset) {
        if (offset.lengthSquared() == 0.0 || offset.y >= 0.0) {
            return offset;
        }

        Stream<VoxelShape> collisions = WaterWalking.collisions(world, box.expand(offset));

        double dy = VoxelShapes.getAllowedOffset(Direction.Axis.Y, box, collisions, offset.y);
        return new Vector3d(offset.x, dy, offset.z);
    }

    public static Stream<VoxelShape> collisions(IBlockReader world, AxisAlignedBB box) {
        int x0 = MathHelper.floor(box.minX);
        int x1 = MathHelper.floor(box.maxX);
        int y0 = MathHelper.floor(box.minY);
        int y1 = MathHelper.floor(box.maxY);
        int z0 = MathHelper.floor(box.minZ);
        int z1 = MathHelper.floor(box.maxZ);

        CubeCoordinateIterator iterator = new CubeCoordinateIterator(x0, y0, z0, x1, y1, z1);
        CollisionSpliterator spliterator = new CollisionSpliterator(world, box, iterator);

        return StreamSupport.stream(spliterator, false);
    }

    public static final class CollisionSpliterator extends Spliterators.AbstractSpliterator<VoxelShape> {
        private final IBlockReader world;
        private final AxisAlignedBB box;

        private final CubeCoordinateIterator iterator;
        private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        CollisionSpliterator(IBlockReader world, AxisAlignedBB box, CubeCoordinateIterator iterator) {
            super(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.IMMUTABLE);
            this.world = world;
            this.box = box;
            this.iterator = iterator;
        }

        @Override
        public boolean tryAdvance(Consumer<? super VoxelShape> action) {
            IBlockReader world = this.world;
            CubeCoordinateIterator iterator = this.iterator;
            BlockPos.Mutable mutablePos = this.mutablePos;

            while (iterator.hasNext()) {
                int x = iterator.getX();
                int y = iterator.getY();
                int z = iterator.getZ();

                if (canWalkOn(world.getFluidState(mutablePos.setPos(x, y, z)))) {
                    if (this.box.intersects(x, y, z, x + 1.0, y + HEIGHT, z + 1.0)) {
                        action.accept(COLLIDER.withOffset(x, y, z));
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static final class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity entity, World world) {
            super(entity, world);
            this.setCanSwim(true);
        }

        @Override
        protected PathFinder getPathFinder(int depth) {
            this.nodeProcessor = new WalkNodeProcessor();
            return new PathFinder(this.nodeProcessor, depth);
        }

        @Override
        protected boolean func_230287_a_(PathNodeType type) {
            return type == PathNodeType.WATER || type == PathNodeType.WATER_BORDER || super.func_230287_a_(type);
        }

        @Override
        public boolean canEntityStandOnPos(BlockPos pos) {
            return canWalkOn(this.world.getFluidState(pos)) || super.canEntityStandOnPos(pos);
        }
    }
}
