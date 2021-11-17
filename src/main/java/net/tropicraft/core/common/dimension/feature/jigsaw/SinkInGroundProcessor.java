package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;

import javax.annotation.Nullable;

public class SinkInGroundProcessor extends CheatyStructureProcessor {
    public static final Codec<SinkInGroundProcessor> CODEC = Codec.unit(new SinkInGroundProcessor());

    static final IStructureProcessorType<SinkInGroundProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":sink_in_ground", () -> CODEC);

    @SuppressWarnings("deprecation")
	@Override
    public BlockInfo process(IWorldReader world, BlockPos worldPos, BlockPos sourcePos, BlockInfo sourceInfo, BlockInfo worldInfo, PlacementSettings placement, @Nullable Template template) {
        worldPos = worldInfo.pos;

        if (sourceInfo.pos.getY() == 0) {
            if (!isAirOrWater(world, worldPos)) {
                return null;
            }
            return worldInfo;
        }
        
        // Get height of the ground at this spot
        BlockPos groundCheck = world.getHeight(Heightmap.Type.WORLD_SURFACE, worldPos);
        // y == 2, we're above the path, remove fence blocks that are above sea level or next to some other block
        if (sourceInfo.pos.getY() == 2 && sourceInfo.state.getBlock() == TropicraftBlocks.BAMBOO_FENCE.get()) {
            if (groundCheck.getY() > 127 || !isAirOrWater(world, worldPos.down(2))) {
                return null;
            }
            for (int i = 0; i < 4; i++) {
                if (!world.isAirBlock(worldPos.offset(Direction.byHorizontalIndex(i)))) {
                    return null;
                }
            }
        }
        
        // If above sea level, sink into the ground by one block
        if (groundCheck.getY() > 127) {
            // Convert slabs to bundles when they are over land
            if (!isAirOrWater(world, worldPos.down()) && sourceInfo.state.getBlock() == TropicraftBlocks.THATCH_SLAB.get()) {
                worldInfo = new BlockInfo(worldPos, TropicraftBlocks.THATCH_BUNDLE.get().getDefaultState(), null);
            }
            
            // Only sink solid blocks, or blocks that are above air/water -- delete all others
            if (Block.isOpaque(worldInfo.state.getShape(world, worldPos.down())) || isAirOrWater(world, worldPos.down())) {
                worldInfo = new BlockInfo(worldPos = worldPos.down(), worldInfo.state, worldInfo.nbt);
            }
        }

        return worldInfo;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }
}
