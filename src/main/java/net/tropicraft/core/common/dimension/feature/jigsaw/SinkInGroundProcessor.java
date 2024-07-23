package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import javax.annotation.Nullable;

public class SinkInGroundProcessor extends CheatyStructureProcessor {
    public static final MapCodec<SinkInGroundProcessor> CODEC = MapCodec.unit(new SinkInGroundProcessor());

    @SuppressWarnings("deprecation")
    @Override
    public StructureBlockInfo process(LevelReader world, BlockPos worldPos, BlockPos sourcePos, StructureBlockInfo sourceInfo, StructureBlockInfo worldInfo, StructurePlaceSettings placement, @Nullable StructureTemplate template) {
        worldPos = worldInfo.pos();

        if (sourceInfo.pos().getY() == 0) {
            if (!isAirOrWater(world, worldPos)) {
                return null;
            }
            return worldInfo;
        }

        // Get height of the ground at this spot
        BlockPos groundCheck = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, worldPos);
        // y == 2, we're above the path, remove fence blocks that are above sea level or next to some other block
        if (sourceInfo.pos().getY() == 2 && sourceInfo.state().is(TropicraftBlocks.BAMBOO_FENCE)) {
            if (groundCheck.getY() > TropicraftDimension.SEA_LEVEL || !isAirOrWater(world, worldPos.below(2))) {
                return null;
            }
            for (int i = 0; i < 4; i++) {
                if (!world.isEmptyBlock(worldPos.relative(Direction.from2DDataValue(i)))) {
                    return null;
                }
            }
        }

        // If above sea level, sink into the ground by one block
        if (groundCheck.getY() > TropicraftDimension.SEA_LEVEL) {
            // Convert slabs to bundles when they are over land
            if (!isAirOrWater(world, worldPos.below()) && sourceInfo.state().is(TropicraftBlocks.THATCH_SLAB)) {
                worldInfo = new StructureBlockInfo(worldPos, TropicraftBlocks.THATCH_BUNDLE.get().defaultBlockState(), null);
            }

            // Only sink solid blocks, or blocks that are above air/water -- delete all others
            if (Block.isShapeFullBlock(worldInfo.state().getShape(world, worldPos.below())) || isAirOrWater(world, worldPos.below())) {
                return new StructureBlockInfo(worldPos.below(), worldInfo.state(), worldInfo.nbt());
            }
        }

        return worldInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TropicraftProcessorTypes.SINK_IN_GROUND.get();
    }
}
