package net.tropicraft.core.common.dimension.feature.jigsaw;

import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

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

public class StructureSupportsProcessor extends CheatyStructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":structure_supports", StructureSupportsProcessor::new);
    
    private final boolean canReplaceLand;
    private final Set<ResourceLocation> statesToExtend;

    public StructureSupportsProcessor(boolean canReplaceLand, ResourceLocation... statesToExtend) {
        this.canReplaceLand = canReplaceLand;
        this.statesToExtend = Sets.newHashSet(statesToExtend);
    }

    public StructureSupportsProcessor(Dynamic<?> p_i51337_1_) {
        this(p_i51337_1_.get("canReplaceLand").asBoolean(false),
             p_i51337_1_.get("statesToExtend").asStream()
                    .map(d -> new ResourceLocation(d.asString().get()))
                    .toArray(ResourceLocation[]::new));
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        BlockPos pos = blockInfo.pos;
        if (p_215194_3_.pos.getY() <= 1 && statesToExtend.contains(blockInfo.state.getBlock().getRegistryName())) {
            if (!canReplaceLand && !canPassThrough(worldReaderIn, pos)) {
                // Delete blocks that would generate inside land
                return null;
            }
            if (p_215194_3_.pos.getY() == 0) {
                // Don't generate blocks underneath solid land
                if (!canReplaceLand && !canPassThrough(worldReaderIn, pos.up())) {
                    return null;
                }
                BlockPos fencePos = pos.down();
                // Extend blocks at the bottom of a structure down to the ground
                while (canPassThrough(worldReaderIn, fencePos)) {
                    BlockState state = blockInfo.state;
                    if (state.has(BlockStateProperties.WATERLOGGED)) {
                        state = state.with(FenceBlock.WATERLOGGED, worldReaderIn.getBlockState(fencePos).getBlock() == Blocks.WATER);
                    }
                    setBlockState(worldReaderIn, fencePos, state);
                    fencePos = fencePos.down();
                }
            }
        }
        return blockInfo;
    }
    
    protected boolean canPassThrough(IWorldReader world, BlockPos pos) {
        return isAirOrWater(world, pos) || world.getHeight(Type.WORLD_SURFACE_WG, pos).getY() < pos.getY();
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("canReplaceLand"), ops.createBoolean(canReplaceLand),
                ops.createString("statesToExtend"), ops.createList(this.statesToExtend.stream()
                        .map(rl -> ops.createString(rl.toString()))))));
    }
}
