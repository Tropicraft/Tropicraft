package net.tropicraft.core.common.dimension.feature;

import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Blocks;
import net.minecraft.block.JigsawBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Constants;

public abstract class PathStructureProcessor extends CheatyStructureProcessor {

    protected PathStructureProcessor() {}

    protected PathStructureProcessor(Dynamic<?> dynamic) {
        this();
    }
    
    // Represents a section of the structure which is a path going in a certain direction
    private static class PathVector {
        final Direction dir;
        final MutableBoundingBox bb;
        
        PathVector(BlockPos start, Direction dir) {
            Preconditions.checkArgument(dir.getAxis().isHorizontal(), "Invalid direction for path vector at " + start);
            this.dir = dir;
            Vec3d ortho = new Vec3d(dir.rotateY().getDirectionVec());
            bb = toMutable(new AxisAlignedBB(start)
                    // Expand 16 blocks in front of the vector
                    .expand(new Vec3d(dir.getDirectionVec()).scale(16))
                    // Add 1 block to each side
                    .expand(ortho).expand(ortho.inverse())
                    // Cover a good amount of vertical space
                    .grow(0, 3, 0));
        }
        
        boolean contains(BlockPos pos, PlacementSettings settings) {
            return bb.isVecInside(Template.transformedBlockPos(settings, pos));
        }
        
        private MutableBoundingBox toMutable(AxisAlignedBB bb) {
            return new MutableBoundingBox((int) bb.minX, (int) bb.minY, (int) bb.minZ, (int) bb.maxX, (int) bb.maxY, (int) bb.maxZ);
        }
    }
    
    // Cache vectors for this structure to avoid redoing work
    private static final WeakHashMap<PlacementSettings, List<PathVector>> VECTOR_CACHE = new WeakHashMap<>(); 
    
    protected @Nullable Axis getPathDirection(BlockPos seedPos, BlockInfo current, PlacementSettings settings, Template template) {
        /*
         *  Use special marker jigsaw blocks to represent "vectors" of paths.
         *  
         *  Each jigsaw with attachment type "tropicraft:path_center" is a different vector,
         *  with the facing representing the direction of the vector. A vector extends from
         *  the jigsaw block to the end of the structure in that direction, and 1 block to
         *  either side.
         */
        return VECTOR_CACHE.computeIfAbsent(settings, s -> 
                template.func_215381_a(seedPos, settings, Blocks.JIGSAW).stream() // Find all jigsaw blocks
                        .filter(b -> b.nbt.getString("attachement_type").equals(Constants.MODID + ":path_center")) // Filter for vector markers
                        .map(bi -> new PathVector(bi.pos.subtract(seedPos), bi.state.get(JigsawBlock.FACING))) // Convert pos to structure local, extract facing
                        .collect(Collectors.toList()))
                .stream()
                .filter(pv -> pv.contains(current.pos, settings)) // Find vectors that contain this block
                .findFirst() // If there's more than one, we just choose the first, better some attempt than nothing
                .map(pv -> pv.dir.getAxis())
                .orElse(null);
    }
}
