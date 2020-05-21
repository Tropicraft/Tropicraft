package net.tropicraft.core.common.dimension.feature.jigsaw;

import java.util.List;
import java.util.Random;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.tropicraft.Constants;

public class NoRotateSingleJigsawPiece extends FixedSingleJigsawPiece {
    
    private static final IJigsawDeserializer TYPE = IJigsawDeserializer.register(Constants.MODID + ":no_rotate", NoRotateSingleJigsawPiece::new);

    public NoRotateSingleJigsawPiece(String p_i51400_1_, List<StructureProcessor> p_i51400_2_) {
        super(p_i51400_1_, p_i51400_2_);
    }
    
    public NoRotateSingleJigsawPiece(Dynamic<?> nbt) {
        super(nbt);
    }
    
    @Override
    public IJigsawDeserializer getType() {
        return TYPE;
    }

    @Override
    protected PlacementSettings createPlacementSettings(Rotation rotation, MutableBoundingBox bb) {
        return super.createPlacementSettings(Rotation.NONE, bb);
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
    public List<BlockInfo> getJigsawBlocks(TemplateManager templateManagerIn, BlockPos pos, Rotation rotationIn, Random rand) {
        return super.getJigsawBlocks(templateManagerIn, pos, Rotation.NONE, rand);
    }

    @Override
    public boolean place(TemplateManager templateManagerIn, IWorld worldIn, ChunkGenerator<?> chunkGen, BlockPos pos, Rotation rot, MutableBoundingBox boundsIn, Random rand) {
        return super.place(templateManagerIn, worldIn, chunkGen, pos, Rotation.NONE, boundsIn, rand);
    }
}
