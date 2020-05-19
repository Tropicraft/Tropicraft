package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.tropicraft.Constants;

import java.util.List;

/**
 * Implementation of SingleJigsawPiece that properly uses structure void
 */
public class FixedSingleJigsawPiece extends SingleJigsawPiece {
    
    private static final IJigsawDeserializer TYPE = IJigsawDeserializer.register(Constants.MODID + ":fixed", FixedSingleJigsawPiece::new);
    
    public FixedSingleJigsawPiece(String p_i51400_1_, List<StructureProcessor> p_i51400_2_) {
        super(p_i51400_1_, p_i51400_2_);
    }
    
    public FixedSingleJigsawPiece(Dynamic<?> nbt) {
        super(nbt);
    }

    @Override
    public IJigsawDeserializer getType() {
        return TYPE;
    }

    @Override
    protected PlacementSettings createPlacementSettings(final Rotation rotation, final MutableBoundingBox bb) {
        PlacementSettings placementsettings = super.createPlacementSettings(rotation, bb);
        placementsettings.removeProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
        placementsettings.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        return placementsettings;
    }
}
