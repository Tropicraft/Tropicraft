package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class TropicsFlowersFeature extends FlowersFeature {
    private final Block[] FLOWERS;

    public TropicsFlowersFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, final Block[] flowers) {
        super(func);
        FLOWERS = flowers;
    }

    @Override
    public BlockState getRandomFlower(Random rand, BlockPos pos) {
        final double noise = MathHelper.clamp((1.0D + Biome.INFO_NOISE.getValue((double)pos.getX() / 48.0D, (double)pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
        return FLOWERS[(int) (noise * (double) FLOWERS.length)].getDefaultState();
    }
}
