package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.function.Function;

public abstract class TropicraftFeature extends Feature<NoFeatureConfig> {

    public TropicraftFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func) {
        super(func);
    }

    protected void setBlock(IWorld world, int i, int i1, int i2, final BlockState state) {
        world.setBlockState(new BlockPos(i, i1, i2), state, 3);
    }

    protected boolean isSoil(final IWorld world, final BlockPos pos) {
        return world.getBlockState(pos).isIn(BlockTags.DIRT_LIKE);
    }
}
