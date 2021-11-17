package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.base.Preconditions;
import net.minecraft.block.Blocks;
import net.minecraft.block.JigsawBlock;import net.minecraft.client.Minecraft;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public abstract class PathStructureProcessor extends CheatyStructureProcessor {

    protected PathStructureProcessor() {}

    // Represents a section of the structure which is a path going in a certain direction
    private static class PathVector {
        final Direction dir;
        final MutableBoundingBox bb;

        PathVector(BlockPos start, Direction dir) {
            Preconditions.checkArgument(dir.getAxis().isHorizontal(), "Invalid direction for path vector at " + start);
            this.dir = dir;
            Vector3d ortho = Vector3d.copy(dir.rotateY().getDirectionVec());
            bb = toMutable(new AxisAlignedBB(start)
                    // Expand 16 blocks in front of the vector
                    .expand(Vector3d.copy(dir.getDirectionVec()).scale(16))
                    // Add 1 block to each side
                    .expand(ortho).expand(ortho.inverse())
                    // Cover a good amount of vertical space
                    .grow(0, 48, 0));
        }

        boolean contains(BlockPos pos, PlacementSettings settings) {
            return bb.isVecInside(pos);
        }

        private MutableBoundingBox toMutable(AxisAlignedBB bb) {
            return new MutableBoundingBox((int) bb.minX, (int) bb.minY, (int) bb.minZ, (int) bb.maxX, (int) bb.maxY, (int) bb.maxZ);
        }
    }

    // Cache vectors for this structure to avoid redoing work
    private static final WeakHashMap<PlacementSettings, List<PathVector>> VECTOR_CACHE = new WeakHashMap<>(); 

    protected @Nullable Axis getPathDirection(IWorldReader world, BlockPos seedPos, BlockInfo current, PlacementSettings settings, Template template) {
        /*
         *  Use special marker jigsaw blocks to represent "vectors" of paths.
         *
         *  Each jigsaw with attachment type "tropicraft:path_center" is a different vector,
         *  with the facing representing the direction of the vector. A vector extends from
         *  the jigsaw block to the end of the structure in that direction, and 1 block to
         *  either side.
         */
    	final PlacementSettings infiniteBounds = settings.copy();
    	infiniteBounds.setBoundingBox(MutableBoundingBox.func_236990_b_()); 
        return VECTOR_CACHE.computeIfAbsent(settings, s ->
                template.func_215386_a(seedPos, infiniteBounds, Blocks.JIGSAW, true).stream() // Find all jigsaw blocks
                        .filter(b -> b.nbt.getString("target").equals(Constants.MODID + ":path_center")) // Filter for vector markers
//                		.peek(bi -> setBlockState(world, world.getHeight(Type.WORLD_SURFACE_WG, bi.pos), bi.state))
                        .map(bi -> new PathVector(world.getHeight(Type.WORLD_SURFACE_WG, bi.pos).subtract(seedPos), JigsawBlock.getConnectingDirection(bi.state))) // Convert pos to structure local, extract facing
                        .collect(Collectors.toList()))
                .stream()
                .filter(pv -> pv.contains(current.pos.subtract(seedPos), settings)) // Find vectors that contain this block
                .findFirst() // If there's more than one, we just choose the first, better some attempt than nothing
                .map(pv -> pv.dir.getAxis())
                .orElse(null);
    }
}
