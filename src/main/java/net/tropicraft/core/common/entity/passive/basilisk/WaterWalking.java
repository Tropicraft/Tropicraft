package net.tropicraft.core.common.entity.passive.basilisk;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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

        Iterable<VoxelShape> collisions = WaterWalking.collisions(world, box.expandTowards(offset));

        double dy = Shapes.collide(Direction.Axis.Y, box, collisions, offset.y);
        return new Vec3(offset.x, dy, offset.z);
    }

    public static Iterable<VoxelShape> collisions(BlockGetter world, AABB box) {
        return () -> new CollisionIterator(world, box);
    }

    public static final class CollisionIterator extends AbstractIterator<VoxelShape> {
        private final BlockGetter world;
        private final AABB box;

        private final Cursor3D iterator;
        private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        CollisionIterator(BlockGetter world, AABB box) {
            this.world = world;
            this.box = box;
            this.iterator = new Cursor3D(
                    Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ),
                    Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ)
            );
        }

        @Override
        protected VoxelShape computeNext() {
            BlockGetter world = this.world;
            Cursor3D iterator = this.iterator;
            BlockPos.MutableBlockPos mutablePos = this.mutablePos;

            while (iterator.advance()) {
                int x = iterator.nextX();
                int y = iterator.nextY();
                int z = iterator.nextZ();

                if (canWalkOn(world.getFluidState(mutablePos.set(x, y, z)))) {
                    if (this.box.intersects(x, y, z, x + 1.0, y + HEIGHT, z + 1.0)) {
                        return COLLIDER.move(x, y, z);
                    }
                }
            }

            return this.endOfData();
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
