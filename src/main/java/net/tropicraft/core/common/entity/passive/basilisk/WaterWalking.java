package net.tropicraft.core.common.entity.passive.basilisk;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class WaterWalking {
    private static final double HEIGHT = 14.0 / 16.0;
    public static final VoxelShape COLLIDER = Block.box(0.0, 0.0, 0.0, 16.0, HEIGHT * 16.0, 16.0);

    public static boolean canWalkOn(FluidState fluid) {
        return !fluid.isEmpty() && canWalkOn(fluid.getType()) && fluid.getAmount() == 8;
    }

    public static boolean canWalkOn(Fluid fluid) {
        return fluid.is(FluidTags.WATER);
    }

    public static Vec3 collide(BlockGetter world, AABB box, Vec3 offset) {
        if (offset.lengthSqr() == 0.0 || offset.y >= 0.0) {
            return offset;
        }

        Stream<VoxelShape> collisions = WaterWalking.collisions(world, box.expandTowards(offset));

        double dy = Shapes.collide(Direction.Axis.Y, box, collisions, offset.y);
        return new Vec3(offset.x, dy, offset.z);
    }

    public static Stream<VoxelShape> collisions(BlockGetter world, AABB box) {
        int x0 = Mth.floor(box.minX);
        int x1 = Mth.floor(box.maxX);
        int y0 = Mth.floor(box.minY);
        int y1 = Mth.floor(box.maxY);
        int z0 = Mth.floor(box.minZ);
        int z1 = Mth.floor(box.maxZ);

        Cursor3D iterator = new Cursor3D(x0, y0, z0, x1, y1, z1);
        CollisionSpliterator spliterator = new CollisionSpliterator(world, box, iterator);

        return StreamSupport.stream(spliterator, false);
    }

    public static final class CollisionSpliterator extends Spliterators.AbstractSpliterator<VoxelShape> {
        private final BlockGetter world;
        private final AABB box;

        private final Cursor3D iterator;
        private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        CollisionSpliterator(BlockGetter world, AABB box, Cursor3D iterator) {
            super(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.IMMUTABLE);
            this.world = world;
            this.box = box;
            this.iterator = iterator;
        }

        @Override
        public boolean tryAdvance(Consumer<? super VoxelShape> action) {
            BlockGetter world = this.world;
            Cursor3D iterator = this.iterator;
            BlockPos.MutableBlockPos mutablePos = this.mutablePos;

            while (iterator.advance()) {
                int x = iterator.nextX();
                int y = iterator.nextY();
                int z = iterator.nextZ();

                if (canWalkOn(world.getFluidState(mutablePos.set(x, y, z)))) {
                    if (this.box.intersects(x, y, z, x + 1.0, y + HEIGHT, z + 1.0)) {
                        action.accept(COLLIDER.move(x, y, z));
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static final class Navigator extends GroundPathNavigation {
        public Navigator(Mob entity, Level world) {
            super(entity, world);
            this.setCanFloat(true);
        }

        @Override
        protected PathFinder createPathFinder(int depth) {
            this.nodeEvaluator = new WalkNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, depth);
        }

        @Override
        protected boolean hasValidPathType(BlockPathTypes type) {
            return type == BlockPathTypes.WATER || type == BlockPathTypes.WATER_BORDER || super.hasValidPathType(type);
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            return canWalkOn(this.level.getFluidState(pos)) || super.isStableDestination(pos);
        }
    }
}
