package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class NoRotateSingleJigsawPiece extends SinglePoolElement {

    public static final Codec<NoRotateSingleJigsawPiece> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(templateCodec(), processorsCodec(), projectionCodec())
                .apply(instance, NoRotateSingleJigsawPiece::new);
    });

    public NoRotateSingleJigsawPiece(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection placementBehaviour) {
        super(template, processors, placementBehaviour);
    }

    public NoRotateSingleJigsawPiece(StructureTemplate template) {
        super(template);
    }

    public static Function<StructureTemplatePool.Projection, NoRotateSingleJigsawPiece> createNoRotate(String id, Holder<StructureProcessorList> processors) {
        return placementBehaviour -> new NoRotateSingleJigsawPiece(Either.left(new ResourceLocation(id)), processors, placementBehaviour);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return TropicraftStructurePoolElementTypes.SINGLE_NO_ROTATE.get();
    }

    @Override
    protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox box, boolean b) {
        return super.getSettings(Rotation.NONE, box, b);
    }

    @Override
    public void handleDataMarker(LevelAccessor worldIn, StructureBlockInfo p_214846_2_, BlockPos pos, Rotation rotationIn, Random rand, BoundingBox p_214846_6_) {
        super.handleDataMarker(worldIn, p_214846_2_, pos, Rotation.NONE, rand, p_214846_6_);
    }

    @Override
    public List<StructureBlockInfo> getDataMarkers(StructureManager p_214857_1_, BlockPos p_214857_2_, Rotation p_214857_3_, boolean p_214857_4_) {
        return super.getDataMarkers(p_214857_1_, p_214857_2_, Rotation.NONE, p_214857_4_);
    }

    @Override
    public BoundingBox getBoundingBox(StructureManager templateManagerIn, BlockPos pos, Rotation rotationIn) {
        return super.getBoundingBox(templateManagerIn, pos, Rotation.NONE);
    }

    @Override
    public List<StructureBlockInfo> getShuffledJigsawBlocks(StructureManager templateManager, BlockPos pos, Rotation rotation, Random random) {
        return super.getShuffledJigsawBlocks(templateManager, pos, Rotation.NONE, random);
    }

    @Override
    public boolean place(StructureManager templates, WorldGenLevel world, StructureFeatureManager structures, ChunkGenerator generator, BlockPos pos, BlockPos pos2, Rotation rotation, BoundingBox box, Random random, boolean b) {
        return super.place(templates, world, structures, generator, pos, pos2, Rotation.NONE, box, random, b);
    }
}
