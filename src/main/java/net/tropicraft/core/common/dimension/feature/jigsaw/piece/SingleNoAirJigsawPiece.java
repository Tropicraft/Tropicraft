package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.*;
import net.tropicraft.Constants;

import java.util.function.Function;
import java.util.function.Supplier;

public class SingleNoAirJigsawPiece extends SingleJigsawPiece {
    public static final Codec<SingleNoAirJigsawPiece> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(func_236846_c_(), func_236844_b_(), func_236848_d_())
                .apply(instance, SingleNoAirJigsawPiece::new);
    });

    private static final IJigsawDeserializer<SingleNoAirJigsawPiece> TYPE = IJigsawDeserializer.func_236851_a_(Constants.MODID + ":single_no_air", CODEC);

    public SingleNoAirJigsawPiece(Either<ResourceLocation, Template> template, Supplier<StructureProcessorList> processors, JigsawPattern.PlacementBehaviour placementBehaviour) {
        super(template, processors, placementBehaviour);
    }

    public SingleNoAirJigsawPiece(Template template) {
        super(template);
    }

    public static Function<JigsawPattern.PlacementBehaviour, SingleNoAirJigsawPiece> create(String id, StructureProcessorList processors) {
        return placementBehaviour -> new SingleNoAirJigsawPiece(Either.left(new ResourceLocation(id)), () -> processors, placementBehaviour);
    }

    public static Function<JigsawPattern.PlacementBehaviour, SingleNoAirJigsawPiece> create(String id) {
        return create(id, ProcessorLists.EMPTY);
    }

    @Override
    public IJigsawDeserializer<?> getType() {
        return TYPE;
    }

    @Override
    protected PlacementSettings func_230379_a_(Rotation rotation, MutableBoundingBox box, boolean b) {
        PlacementSettings settings = super.func_230379_a_(rotation, box, b);
        settings.removeProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        settings.addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
        return settings;
    }
}
