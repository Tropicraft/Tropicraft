package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.tropicraft.Constants;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class NoRotateSingleJigsawPiece extends SingleJigsawPiece {

    public static final Codec<NoRotateSingleJigsawPiece> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(templateCodec(), processorsCodec(), projectionCodec())
                .apply(instance, NoRotateSingleJigsawPiece::new);
    });

    private static final IJigsawDeserializer<NoRotateSingleJigsawPiece> TYPE = IJigsawDeserializer.register(Constants.MODID + ":single_no_rotate", CODEC);

    public NoRotateSingleJigsawPiece(Either<ResourceLocation, Template> template, Supplier<StructureProcessorList> processors, JigsawPattern.PlacementBehaviour placementBehaviour) {
        super(template, processors, placementBehaviour);
    }

    public NoRotateSingleJigsawPiece(Template template) {
        super(template);
    }

    public static Function<JigsawPattern.PlacementBehaviour, NoRotateSingleJigsawPiece> createNoRotate(String id, StructureProcessorList processors) {
        return placementBehaviour -> new NoRotateSingleJigsawPiece(Either.left(new ResourceLocation(id)), () -> processors, placementBehaviour);
    }

    @Override
    public IJigsawDeserializer<?> getType() {
        return TYPE;
    }

    @Override
    protected PlacementSettings getSettings(Rotation rotation, MutableBoundingBox box, boolean b) {
        return super.getSettings(Rotation.NONE, box, b);
    }

    @Override
    public void handleDataMarker(IWorld worldIn, BlockInfo p_214846_2_, BlockPos pos, Rotation rotationIn, Random rand, MutableBoundingBox p_214846_6_) {
        super.handleDataMarker(worldIn, p_214846_2_, pos, Rotation.NONE, rand, p_214846_6_);
    }

    @Override
    public List<BlockInfo> getDataMarkers(TemplateManager p_214857_1_, BlockPos p_214857_2_, Rotation p_214857_3_, boolean p_214857_4_) {
        return super.getDataMarkers(p_214857_1_, p_214857_2_, Rotation.NONE, p_214857_4_);
    }

    @Override
    public MutableBoundingBox getBoundingBox(TemplateManager templateManagerIn, BlockPos pos, Rotation rotationIn) {
        return super.getBoundingBox(templateManagerIn, pos, Rotation.NONE);
    }

    @Override
    public List<BlockInfo> getShuffledJigsawBlocks(TemplateManager templateManager, BlockPos pos, Rotation rotation, Random random) {
        return super.getShuffledJigsawBlocks(templateManager, pos, Rotation.NONE, random);
    }

    @Override
    public boolean place(TemplateManager templates, ISeedReader world, StructureManager structures, ChunkGenerator generator, BlockPos pos, BlockPos pos2, Rotation rotation, MutableBoundingBox box, Random random, boolean b) {
        return super.place(templates, world, structures, generator, pos, pos2, Rotation.NONE, box, random, b);
    }
}
