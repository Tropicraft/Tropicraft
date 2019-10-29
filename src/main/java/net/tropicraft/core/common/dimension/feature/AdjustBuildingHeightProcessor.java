package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.Template.EntityInfo;
import net.tropicraft.Constants;

public class AdjustBuildingHeightProcessor extends CheatyStructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":adjust_building_height", AdjustBuildingHeightProcessor::new);
    
    private final int base;

    public AdjustBuildingHeightProcessor(int base) {
        this.base = base;
    }

    public AdjustBuildingHeightProcessor(Dynamic<?> p_i51337_1_) {
        this(126); // FIXME
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        if (seedPos.getY() < base) {
            return new BlockInfo(blockInfo.pos.up(), blockInfo.state, blockInfo.nbt);
        }
        return blockInfo;
    }
    
    @Override
    public EntityInfo processEntity(IWorldReader world, BlockPos seedPos, EntityInfo rawEntityInfo, EntityInfo entityInfo, PlacementSettings placementSettings, Template template) {
        if (seedPos.getY() < base) {
            return new EntityInfo(entityInfo.pos.add(0, 1, 0), entityInfo.blockPos.up(), entityInfo.nbt);
        }
        return entityInfo;
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("base"), ops.createInt(this.base))));
    }
}
