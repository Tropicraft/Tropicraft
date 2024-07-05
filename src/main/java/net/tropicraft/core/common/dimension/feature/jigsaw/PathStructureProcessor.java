package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public abstract class PathStructureProcessor extends CheatyStructureProcessor {

    protected PathStructureProcessor() {
    }

    // Represents a section of the structure which is a path going in a certain direction
    private static class PathVector {
        final Direction dir;
        final BoundingBox bb;

        PathVector(BlockPos start, Direction dir) {
            Preconditions.checkArgument(dir.getAxis().isHorizontal(), "Invalid direction for path vector at " + start);
            this.dir = dir;
            Vec3 ortho = Vec3.atLowerCornerOf(dir.getClockWise().getNormal());
            bb = toMutable(new AABB(start)
                    // Expand 16 blocks in front of the vector
                    .expandTowards(Vec3.atLowerCornerOf(dir.getNormal()).scale(16))
                    // Add 1 block to each side
                    .expandTowards(ortho).expandTowards(ortho.reverse())
                    // Cover a good amount of vertical space
                    .inflate(0, 3, 0));
        }

        boolean contains(BlockPos pos, StructurePlaceSettings settings) {
            return bb.isInside(StructureTemplate.calculateRelativePosition(settings, pos));
        }

        private BoundingBox toMutable(AABB bb) {
            return new BoundingBox((int) bb.minX, (int) bb.minY, (int) bb.minZ, (int) bb.maxX, (int) bb.maxY, (int) bb.maxZ);
        }
    }

    // Cache vectors for this structure to avoid redoing work
    private static final WeakHashMap<StructurePlaceSettings, List<PathVector>> VECTOR_CACHE = new WeakHashMap<>();

    @Nullable
    protected Direction.Axis getPathDirection(LevelReader level, BlockPos seedPos, StructureTemplate.StructureBlockInfo current, StructurePlaceSettings settings, StructureTemplate template) {
        /*
         *  Use special marker jigsaw blocks to represent "vectors" of paths.
         *
         *  Each jigsaw with attachment type "tropicraft:path_center" is a different vector,
         *  with the facing representing the direction of the vector. A vector extends from
         *  the jigsaw block to the end of the structure in that direction, and 1 block to
         *  either side.
         */
        final StructurePlaceSettings infiniteBounds = settings.copy();
        infiniteBounds.setBoundingBox(BoundingBox.infinite());
        return VECTOR_CACHE.computeIfAbsent(settings, s ->
                        template.filterBlocks(seedPos, infiniteBounds, Blocks.JIGSAW, true).stream() // Find all jigsaw blocks
                                .filter(b -> b.nbt().getString("target").equals(Constants.MODID + ":path_center")) // Filter for vector markers
//                		.peek(bi -> setBlockState(world, world.getHeight(Type.WORLD_SURFACE_WG, bi.pos), bi.state))
                                .map(bi -> new PathVector(level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, bi.pos()).subtract(seedPos), JigsawBlock.getFrontFacing(bi.state()))) // Convert pos to structure local, extract facing
                                .collect(Collectors.toList()))
                .stream()
                .filter(pv -> pv.contains(current.pos().subtract(seedPos), settings)) // Find vectors that contain this block
                .findFirst() // If there's more than one, we just choose the first, better some attempt than nothing
                .map(pv -> pv.dir.getAxis())
                .orElse(null);
    }
}
