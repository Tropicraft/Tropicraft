package net.tropicraft.core.common.dimension.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;

public class TropicsFlowersFeature extends FlowersFeature {
    private final RegistryObject<Block>[] FLOWERS;

    @SafeVarargs
    public TropicsFlowersFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func, final RegistryObject<Block>... flowers) {
        super(func);
        FLOWERS = flowers;
    }

    @Override
    public BlockState getRandomFlower(Random rand, BlockPos pos) {
        final double noise = MathHelper.clamp((1.0D + Biome.INFO_NOISE.getValue((double)pos.getX() / 48.0D, (double)pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
        return FLOWERS[(int) (noise * (double) FLOWERS.length)].get().getDefaultState();
    }
}
