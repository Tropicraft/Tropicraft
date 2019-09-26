package net.tropicraft.core.common.dimension.feature;

import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class SteepPathProcessor extends CheatyStructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Info.MODID + ":steep_path", SteepPathProcessor::new);

    public SteepPathProcessor() {}

    public SteepPathProcessor(Dynamic<?> p_i51337_1_) {
        this();
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        BlockPos pos = blockInfo.pos;

        if (p_215194_3_.pos.getY() != 1 || p_215194_3_.state.getBlock() == TropicraftBlocks.BAMBOO_STAIRS.get()) {
            return blockInfo;
        }

        Direction.Axis axis = getPathDirection(seedPos, p_215194_3_, placementSettingsIn, template);
        if (axis == null) {
            return blockInfo;
        }
        
        // If this is true, we are "bridging" upwards past an air gap, handles overhangs
        int bridgeTo = -1;
        
        BlockState ladder = null;
        for (AxisDirection axisDir : AxisDirection.values()) {
            Direction dir = Direction.getFacingFromAxis(axisDir, axis);
            // Detect an overhang by checking if the heightmap between spots differs by >2
            BlockPos nextHeight = worldReaderIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.offset(dir)).down();
            if (nextHeight.getY() > pos.getY()) {
                ladder = getLadderState(dir);
                bridgeTo = nextHeight.getY();
            }
            if (ladder != null) {
                break;
            }
        }
        if (ladder == null) {
            return blockInfo; // Nothing to do here, we're on flat ground
        }
        // The facing the ladder stores is opposite to the direction it's placed (i.e. it faces "outward")
        Direction dir = ladder.get(LadderBlock.FACING).getOpposite();
        pos = pos.up();
        if (bridgeTo == pos.getY() && canPlaceLadderAt(worldReaderIn, pos.up(), dir) == null) {
            // If the next spot up can't support a ladder, this is a one block step, so place a stair block
            setBlockState(worldReaderIn, pos, TropicraftBlocks.THATCH_STAIRS.get().getDefaultState().with(StairsBlock.FACING, dir));
        } else {
            // Otherwise, place ladders upwards until we find air (bridging over an initial gap if required)
            while (bridgeTo >= pos.getY() || canPlaceLadderAt(worldReaderIn, pos, dir) != null) {
                setBlockState(worldReaderIn, pos, ladder);
                setBlockState(worldReaderIn, pos.offset(dir), TropicraftBlocks.THATCH_BUNDLE.get().getDefaultState());
                pos = pos.up();
            }
        }
        return blockInfo;
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
                    .expand(ortho).expand(ortho.func_216371_e())
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
    
    private @Nullable Axis getPathDirection(BlockPos seedPos, BlockInfo current, PlacementSettings settings, Template template) {
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
                        .filter(b -> b.nbt.getString("attachement_type").equals(Info.MODID + ":path_center")) // Filter for vector markers
                        .map(bi -> new PathVector(bi.pos.subtract(seedPos), bi.state.get(JigsawBlock.FACING))) // Convert pos to structure local, extract facing
                        .collect(Collectors.toList()))
                .stream()
                .filter(pv -> pv.contains(current.pos, settings)) // Find vectors that contain this block
                .findFirst() // If there's more than one, we just choose the first, better some attempt than nothing
                .map(pv -> pv.dir.getAxis())
                .orElse(null);
    }
    
    // Check that there is a solid block behind the ladder at this pos, and return the correct ladder state
    // Returns null if placement is not possible
    private BlockState canPlaceLadderAt(IWorldReader worldReaderIn, BlockPos pos, Direction dir) {
        BlockPos check = pos.offset(dir);
        BlockState state = worldReaderIn.getBlockState(check);
        if (!state.isAir(worldReaderIn, check)) {
            BlockState ladderState = getLadderState(dir);
            if (ladderState.isValidPosition(worldReaderIn, pos)) {
                return ladderState;
            }
        }
        return null;
    }
    
    private BlockState getLadderState(Direction dir) {
        return TropicraftBlocks.BAMBOO_LADDER.get().getDefaultState().with(LadderBlock.FACING, dir.getOpposite());
    }
    
    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return null;
    }

}
