package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StructureSupportsProcessor extends CheatyStructureProcessor {

    public static final Codec<StructureSupportsProcessor> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.BOOL.optionalFieldOf("can_replace_land", false).forGetter(p -> p.canReplaceLand),
            ResourceLocation.CODEC.listOf().fieldOf("states_to_extend").forGetter(p -> new ArrayList<>(p.statesToExtend))
        ).apply(instance, StructureSupportsProcessor::new);
    });

    static final IStructureProcessorType<StructureSupportsProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":structure_supports", () -> CODEC);
    
    private final boolean canReplaceLand;
    private final Set<ResourceLocation> statesToExtend;

    public StructureSupportsProcessor(boolean canReplaceLand, List<ResourceLocation> statesToExtend) {
        this.canReplaceLand = canReplaceLand;
        this.statesToExtend = new ObjectOpenHashSet<>(statesToExtend);
    }

    @Override
    public BlockInfo process(IWorldReader world, BlockPos seedPos, BlockPos pos2, BlockInfo originalInfo, BlockInfo blockInfo, PlacementSettings placement, Template template) {
        BlockPos pos = blockInfo.pos;
        if (originalInfo.pos.getY() <= 1 && statesToExtend.contains(blockInfo.state.getBlock().getRegistryName())) {
            if (!canReplaceLand && !canPassThrough(world, pos)) {
                // Delete blocks that would generate inside land
                return null;
            }
            if (originalInfo.pos.getY() == 0) {
                // Don't generate blocks underneath solid land
                if (!canReplaceLand && !canPassThrough(world, pos.above())) {
                    return null;
                }
                BlockPos fencePos = pos.below();
                // Extend blocks at the bottom of a structure down to the ground
                while (canPassThrough(world, fencePos)) {
                    BlockState state = blockInfo.state;
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
    
    protected boolean canPassThrough(IWorldReader world, BlockPos pos) {
        return isAirOrWater(world, pos) || world.getHeightmapPos(Type.WORLD_SURFACE_WG, pos).getY() < pos.getY();
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }
}
