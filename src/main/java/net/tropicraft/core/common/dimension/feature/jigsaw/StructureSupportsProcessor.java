package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class StructureSupportsProcessor extends CheatyStructureProcessor {
    public static final MapCodec<StructureSupportsProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Codec.BOOL.optionalFieldOf("can_replace_land", false).forGetter(p -> p.canReplaceLand),
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks_to_extend").forGetter(p -> p.blocksToExtend)
    ).apply(i, StructureSupportsProcessor::new));

    private final boolean canReplaceLand;
    private final HolderSet<Block> blocksToExtend;

    public StructureSupportsProcessor(boolean canReplaceLand, HolderSet<Block> blocksToExtend) {
        this.canReplaceLand = canReplaceLand;
        this.blocksToExtend = blocksToExtend;
    }

    @Override
    public StructureBlockInfo process(LevelReader world, BlockPos seedPos, BlockPos pos2, StructureBlockInfo originalInfo, StructureBlockInfo blockInfo, StructurePlaceSettings placement, StructureTemplate template) {
        BlockPos pos = blockInfo.pos();
        if (originalInfo.pos().getY() <= 1 && blockInfo.state().is(blocksToExtend)) {
            if (!canReplaceLand && !canPassThrough(world, pos)) {
                // Delete blocks that would generate inside land
                return null;
            }
            if (originalInfo.pos().getY() == 0) {
                // Don't generate blocks underneath solid land
                if (!canReplaceLand && !canPassThrough(world, pos.above())) {
                    return null;
                }
                BlockPos fencePos = pos.below();
                // Extend blocks at the bottom of a structure down to the ground
                while (canPassThrough(world, fencePos)) {
                    BlockState state = blockInfo.state();
                    if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
                        state = state.setValue(FenceBlock.WATERLOGGED, world.getBlockState(fencePos).getBlock() == Blocks.WATER);
                    }
                    setBlockState(world, fencePos, state);
                    fencePos = fencePos.below();
                }
            }
        }
        return blockInfo;
    }

    protected boolean canPassThrough(LevelReader world, BlockPos pos) {
        return isAirOrWater(world, pos) || world.getHeightmapPos(Types.WORLD_SURFACE, pos).getY() < pos.getY();
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TropicraftProcessorTypes.STRUCTURE_SUPPORTS.get();
    }
}
