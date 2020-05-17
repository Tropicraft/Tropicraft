package net.tropicraft.core.common.dimension.feature.jigsaw;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NoRotateSingleJigsawPiece extends FixedSingleJigsawPiece {

    public NoRotateSingleJigsawPiece(String p_i51400_1_, List<StructureProcessor> p_i51400_2_) {
        super(p_i51400_1_, p_i51400_2_);
    }

    @Override
    protected PlacementSettings createPlacementSettings(Rotation rotation, MutableBoundingBox bb) {
        return super.createPlacementSettings(Rotation.NONE, bb);
    }

    @Override
    public void func_214846_a(IWorld worldIn, BlockInfo p_214846_2_, BlockPos pos, Rotation rotationIn, Random rand, MutableBoundingBox p_214846_6_) {
        super.func_214846_a(worldIn, p_214846_2_, pos, Rotation.NONE, rand, p_214846_6_);
    }

    @Override
    public List<BlockInfo> func_214857_a(TemplateManager p_214857_1_, BlockPos p_214857_2_, Rotation p_214857_3_, boolean p_214857_4_) {
        return super.func_214857_a(p_214857_1_, p_214857_2_, Rotation.NONE, p_214857_4_);
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
    public boolean place(TemplateManager templateManagerIn, IWorld worldIn, BlockPos pos, Rotation rotationIn, MutableBoundingBox boundsIn, Random rand) {
        return super.place(templateManagerIn, worldIn, pos, Rotation.NONE, boundsIn, rand);
    }
}
