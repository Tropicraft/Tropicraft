package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.tropicraft.Constants;

public class SmoothingGravityProcessor extends PathStructureProcessor {

    public static final Codec<SmoothingGravityProcessor> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(p -> p.heightmap),
                Codec.INT.fieldOf("offset").forGetter(p -> p.offset)
        ).apply(instance, SmoothingGravityProcessor::new);
    });

    static final StructureProcessorType<SmoothingGravityProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":smooth_gravity", () -> CODEC);

    private final Heightmap.Types heightmap;
    private final int offset;
    private final GravityProcessor baseline;

    public SmoothingGravityProcessor(Types heightmap, int offset) {
        super();
        this.heightmap = heightmap;
        this.offset = offset;
        this.baseline = new GravityProcessor(heightmap, offset);
    }

    @Override
    public StructureBlockInfo process(LevelReader level, BlockPos seedPos, BlockPos pos2, StructureBlockInfo originalBlockInfo, StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, StructureTemplate template) {
        Axis pathDir = getPathDirection(level, seedPos, blockInfo, placementSettingsIn, template);
        if (pathDir == null) {
            pathDir = Axis.X; // Better than nothing
        }
        BlockPos pos = blockInfo.pos;
        BlockPos posForward = pos.relative(Direction.get(AxisDirection.POSITIVE, pathDir));
        BlockPos posBackward = pos.relative(Direction.get(AxisDirection.NEGATIVE, pathDir));
        int heightForward = level.getHeight(heightmap, posForward.getX(), posForward.getZ()) + offset;
        int heightBackward = level.getHeight(heightmap, posBackward.getX(), posBackward.getZ()) + offset;
        int height = level.getHeight(heightmap, pos.getX(), pos.getZ()) + offset;
        if (heightForward > height && heightBackward > height) {
            return new StructureBlockInfo(new BlockPos(pos.getX(), Math.min(heightForward, heightBackward), pos.getZ()), blockInfo.state, blockInfo.nbt);
        }
        return baseline.process(level, seedPos, pos2, originalBlockInfo, blockInfo, placementSettingsIn, template);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
