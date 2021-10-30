package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElementType;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.gen.feature.template.*;
import net.tropicraft.Constants;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SingleNoAirJigsawPiece extends SinglePoolElement {
    public static final Codec<SingleNoAirJigsawPiece> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(templateCodec(), processorsCodec(), projectionCodec())
                .apply(instance, SingleNoAirJigsawPiece::new);
    });

    private static final StructurePoolElementType<SingleNoAirJigsawPiece> TYPE = StructurePoolElementType.register(Constants.MODID + ":single_no_air", CODEC);

    public SingleNoAirJigsawPiece(Either<ResourceLocation, StructureTemplate> template, Supplier<StructureProcessorList> processors, StructureTemplatePool.Projection placementBehaviour) {
        super(template, processors, placementBehaviour);
    }

    public SingleNoAirJigsawPiece(StructureTemplate template) {
        super(template);
    }

    public static Function<StructureTemplatePool.Projection, SingleNoAirJigsawPiece> create(String id, StructureProcessorList processors) {
        return placementBehaviour -> new SingleNoAirJigsawPiece(Either.left(new ResourceLocation(id)), () -> processors, placementBehaviour);
    }

    public static Function<StructureTemplatePool.Projection, SingleNoAirJigsawPiece> create(String id) {
        return create(id, ProcessorLists.EMPTY);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return TYPE;
    }

    @Override
    protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox box, boolean b) {
        StructurePlaceSettings settings = super.getSettings(rotation, box, b);
        settings.popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        return settings;
    }
}
