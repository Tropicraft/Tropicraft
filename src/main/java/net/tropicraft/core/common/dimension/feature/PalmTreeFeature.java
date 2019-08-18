package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Set;
import java.util.function.Function;

public abstract class PalmTreeFeature extends AbstractTreeFeature<NoFeatureConfig> {
    protected static final BlockState LOG_STATE = TropicraftBlocks.PALM_LOG.getDefaultState();
    protected static final BlockState LEAF_STATE = TropicraftBlocks.PALM_LEAVES.getDefaultState();

    public PalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49920_1_, boolean p_i49920_2_) {
        super(p_i49920_1_, p_i49920_2_);
    }

    protected void setState(Set<BlockPos> changedBlocks, IWorldWriter world, BlockPos pos, BlockState state, MutableBoundingBox bb) {
        setBlockStateInternally(world, pos, state);
        bb.expandTo(new MutableBoundingBox(pos, pos));
        if (BlockTags.LOGS.contains(state.getBlock())) {
            changedBlocks.add(pos.toImmutable());
        }
    }

    private void setBlockStateInternally(IWorldWriter world, BlockPos pos, BlockState state) {
        if (this.doBlockNotify) {
            world.setBlockState(pos, state, 19);
        } else {
            world.setBlockState(pos, state, 18);
        }
    }

    // TODO use palm sapling, should fix them from spawning on grass
    protected net.minecraftforge.common.IPlantable getSapling() {
        return sapling;
    }

    protected void placeLeaf(final Set<BlockPos> changedBlocks, final IWorldGenerationReader world, final MutableBoundingBox bb, int x, int y, int z) {
        setState(changedBlocks, world, new BlockPos(x, y, z), LEAF_STATE, bb);
    }

    protected void placeLog(final Set<BlockPos> changedBlocks, final IWorldGenerationReader world, final MutableBoundingBox bb, int x, int y, int z) {
        setState(changedBlocks, world, new BlockPos(x, y, z), LOG_STATE, bb);
    }


    protected static boolean isSand(IWorldGenerationBaseReader reader, BlockPos pos, net.minecraftforge.common.IPlantable sapling) {
        if (!(reader instanceof net.minecraft.world.IBlockReader) || sapling == null) {
            return reader.hasBlockState(pos, (state) -> state.getMaterial() == Material.SAND);
        }
        return reader.hasBlockState(pos, state -> state.canSustainPlant((net.minecraft.world.IBlockReader)reader, pos, Direction.UP, sapling));
    }
}
