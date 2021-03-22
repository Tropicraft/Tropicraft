package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.tropicraft.Constants;

public class SmoothingGravityProcessor extends PathStructureProcessor {

    public static final Codec<SmoothingGravityProcessor> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Heightmap.Type.CODEC.fieldOf("heightmap").forGetter(p -> p.heightmap),
                Codec.INT.fieldOf("offset").forGetter(p -> p.offset)
        ).apply(instance, SmoothingGravityProcessor::new);
    });

    static final IStructureProcessorType<SmoothingGravityProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":smooth_gravity", () -> CODEC);

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
    public BlockInfo process(IWorldReader world, BlockPos seedPos, BlockPos pos2, BlockInfo originalBlockInfo, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        Axis pathDir = getPathDirection(seedPos, blockInfo, placementSettingsIn, template);
        if (pathDir == null) {
            pathDir = Axis.X; // Better than nothing
        }
        BlockPos pos = blockInfo.pos;
        BlockPos posForward = pos.offset(Direction.getFacingFromAxis(AxisDirection.POSITIVE, pathDir));
        BlockPos posBackward = pos.offset(Direction.getFacingFromAxis(AxisDirection.NEGATIVE, pathDir));
        int heightForward = world.getHeight(heightmap, posForward.getX(), posForward.getZ()) + offset;
        int heightBackward = world.getHeight(heightmap, posBackward.getX(), posBackward.getZ()) + offset;
        int height = world.getHeight(heightmap, pos.getX(), pos.getZ()) + offset;
        if (heightForward > height && heightBackward > height) {
            return new BlockInfo(new BlockPos(pos.getX(), Math.min(heightForward, heightBackward), pos.getZ()), blockInfo.state, blockInfo.nbt);
        }
        return baseline.process(world, seedPos, pos2, originalBlockInfo, blockInfo, placementSettingsIn, template);
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }
}
