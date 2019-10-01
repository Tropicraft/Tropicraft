package net.tropicraft.core.common.dimension.feature;

import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

public abstract class PalmTreeFeature extends AbstractTreeFeature<NoFeatureConfig> {

    public PalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49920_1_, boolean p_i49920_2_) {
        super(p_i49920_1_, p_i49920_2_);
    }

    @Override
    protected net.minecraftforge.common.IPlantable getSapling() {
        return TropicraftBlocks.PALM_SAPLING.get();
    }
    
    protected final BlockState getLeaf() {
        return TropicraftBlocks.PALM_LEAVES.get().getDefaultState();
    }
    
    protected final BlockState getLog() {
        return TropicraftBlocks.PALM_LOG.get().getDefaultState();
    }

    protected void placeExtra(IWorldGenerationReader world, BlockState state, int x, int y, int z) {
        setBlockState(world, new BlockPos(x, y, z), state);
    }
    
    protected void placeLeaf(final Set<BlockPos> changedBlocks, final IWorldGenerationReader world, MutableBoundingBox bb, int x, int y, int z) {
        setLogState(changedBlocks, world, new BlockPos(x, y, z), getLeaf(), bb);
    }

    protected void placeLog(final Set<BlockPos> changedBlocks, final IWorldGenerationReader world, MutableBoundingBox bb, int x, int y, int z) {
        setLogState(changedBlocks, world, new BlockPos(x, y, z), getLog(), bb);
    }
}
