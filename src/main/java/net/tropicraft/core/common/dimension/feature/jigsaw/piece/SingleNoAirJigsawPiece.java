package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SingleNoAirJigsawPiece extends SinglePoolElement {
    public static final MapCodec<SingleNoAirJigsawPiece> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            templateCodec(),
            processorsCodec(),
            projectionCodec(),
            overrideLiquidSettingsCodec(),
            Codec.BOOL.optionalFieldOf("unprojected", false).forGetter(p -> p.unprojected)
    ).apply(i, SingleNoAirJigsawPiece::new));

    private static final Holder<StructureProcessorList> EMPTY_PROCESSOR_LIST = Holder.direct(new StructureProcessorList(List.of()));

    private final boolean unprojected;

    public SingleNoAirJigsawPiece(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection placementBehaviour, Optional<LiquidSettings> overrideLiquidSettings, boolean unproject) {
        super(template, processors, placementBehaviour, overrideLiquidSettings);
        this.unprojected = unproject;
    }

    public static Function<StructureTemplatePool.Projection, SingleNoAirJigsawPiece> create(ResourceLocation id, Holder<StructureProcessorList> processors, boolean unprojected) {
        return placementBehaviour -> new SingleNoAirJigsawPiece(Either.left(id), processors, placementBehaviour, Optional.empty(), unprojected);
    }

    public static Function<StructureTemplatePool.Projection, SingleNoAirJigsawPiece> create(ResourceLocation id) {
        return create(id, EMPTY_PROCESSOR_LIST, false);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return TropicraftStructurePoolElementTypes.SINGLE_NO_AIR.get();
    }

    @Override
    protected StructurePlaceSettings getSettings(final Rotation rotation, final BoundingBox box, final LiquidSettings liquidSettings, final boolean offset) {
        StructurePlaceSettings settings = super.getSettings(rotation, box, liquidSettings, offset);
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
