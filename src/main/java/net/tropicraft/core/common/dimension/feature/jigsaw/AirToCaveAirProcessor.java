package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Constants;

import javax.annotation.Nullable;

public class AirToCaveAirProcessor extends StructureProcessor {

    public static final Codec<AirToCaveAirProcessor> CODEC = Codec.unit(new AirToCaveAirProcessor());

    static final IStructureProcessorType<AirToCaveAirProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":air_to_cave_air", () -> CODEC);
    
    @Override
    @Nullable
    public BlockInfo process(IWorldReader world, BlockPos pos, BlockPos pos2, BlockInfo originalInfo, BlockInfo blockInfo, PlacementSettings placementSettingsIn, @Nullable Template template) {
        if (blockInfo.state.getBlock() == Blocks.AIR) {
            return new BlockInfo(blockInfo.pos, Blocks.CAVE_AIR.defaultBlockState(), blockInfo.nbt);
        }
        return super.process(world, pos, pos2, originalInfo, blockInfo, placementSettingsIn, template);
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }
}
