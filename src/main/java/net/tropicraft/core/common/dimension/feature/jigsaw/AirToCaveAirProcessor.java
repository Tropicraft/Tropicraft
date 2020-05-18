package net.tropicraft.core.common.dimension.feature.jigsaw;

import javax.annotation.Nullable;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

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

public class AirToCaveAirProcessor extends StructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":air_to_cave_air", AirToCaveAirProcessor::new);
    
    public AirToCaveAirProcessor() {}

    public AirToCaveAirProcessor(Dynamic<?> p_i51337_1_) {
        this();
    }
    
    @Override
    @Nullable
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, @Nullable Template template) {
        if (blockInfo.state.getBlock() == Blocks.AIR) {
            return new BlockInfo(blockInfo.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfo.nbt);
        }
        return super.process(worldReaderIn, pos, p_215194_3_, blockInfo, placementSettingsIn, template);
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }
}
