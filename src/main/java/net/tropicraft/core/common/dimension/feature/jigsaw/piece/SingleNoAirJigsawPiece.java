package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.function.Function;

public class SingleNoAirJigsawPiece extends SinglePoolElement {
    public static final Codec<SingleNoAirJigsawPiece> CODEC = RecordCodecBuilder.create(i -> i.group(
            templateCodec(),
            processorsCodec(),
            projectionCodec(),
            Codec.BOOL.optionalFieldOf("unprojected", false).forGetter(p -> p.unprojected)
    ).apply(i, SingleNoAirJigsawPiece::new));

    private boolean unprojected;

    public SingleNoAirJigsawPiece(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection placementBehaviour, boolean unproject) {
        super(template, processors, placementBehaviour);
        this.unprojected = unproject;
    }

    public SingleNoAirJigsawPiece(StructureTemplate template) {
        super(template);
    }

    public static Function<StructureTemplatePool.Projection, SingleNoAirJigsawPiece> create(String id, Holder<StructureProcessorList> processors, boolean unprojected) {
        return placementBehaviour -> new SingleNoAirJigsawPiece(Either.left(new ResourceLocation(id)), processors, placementBehaviour, unprojected);
    }

    public static Function<StructureTemplatePool.Projection, SingleNoAirJigsawPiece> create(String id) {
        return create(id, ProcessorLists.EMPTY, false);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return TropicraftStructurePoolElementTypes.SINGLE_NO_AIR.get();
    }

    @Override
    protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox box, boolean b) {
        StructurePlaceSettings settings = super.getSettings(rotation, box, b);
        settings.popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        if (unprojected) {
            for (StructureProcessor processor : getProjection().getProcessors()) {
                settings.popProcessor(processor);
            }
        }
        return settings;
    }
}
