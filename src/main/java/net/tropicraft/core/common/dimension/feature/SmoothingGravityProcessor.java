package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.template.GravityStructureProcessor;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Info;

public class SmoothingGravityProcessor extends PathStructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Info.MODID + ":smooth_gravity", SteepPathProcessor::new);

    private final Heightmap.Type heightmap;
    private final int offset;
    private final GravityStructureProcessor baseline;

    public SmoothingGravityProcessor(Type heightmap, int offset) {
        super();
        this.heightmap = heightmap;
        this.offset = offset;
        this.baseline = new GravityStructureProcessor(heightmap, offset);
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        Axis pathDir = getPathDirection(seedPos, blockInfo, placementSettingsIn, template);
        if (pathDir == null) {
            pathDir = Axis.X; // Better than nothing
        }
        BlockPos pos = blockInfo.pos;
        BlockPos posForward = pos.offset(Direction.getFacingFromAxis(AxisDirection.POSITIVE, pathDir));
        BlockPos posBackward = pos.offset(Direction.getFacingFromAxis(AxisDirection.NEGATIVE, pathDir));
        int heightForward = worldReaderIn.getHeight(heightmap, posForward.getX(), posForward.getZ()) + offset;
        int heightBackward = worldReaderIn.getHeight(heightmap, posBackward.getX(), posBackward.getZ()) + offset;
        int height = worldReaderIn.getHeight(heightmap, pos.getX(), pos.getZ()) + offset;
        if (heightForward > height && heightBackward > height) {
            return new BlockInfo(new BlockPos(pos.getX(), Math.min(heightForward, heightBackward), pos.getZ()), blockInfo.state, blockInfo.nbt);
        }
        return baseline.process(worldReaderIn, seedPos, p_215194_3_, blockInfo, placementSettingsIn, template);
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("heightmap"), ops.createString(this.heightmap.getId()), ops.createString("offset"), ops.createInt(this.offset))));
    }
}
