package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.DefaultFlowersFeature;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

public class TropicsFlowersFeature extends DefaultFlowersFeature {
    private final Tag<Block> flowers;

    public TropicsFlowersFeature(Function<Dynamic<?>, ? extends BlockClusterFeatureConfig> func, final Tag<Block> flowers) {
        super(func);
        this.flowers = flowers;
    }
    
    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        if (!flowers.getAllElements().isEmpty()) {
            return super.place(worldIn, generator, rand, pos, config);
        }
        return false;
    }

    @Override
    public BlockState getFlowerToPlace(Random rand, BlockPos pos, final BlockClusterFeatureConfig config) {
        final double noise = MathHelper.clamp((1.0D + Biome.INFO_NOISE.noiseAt((double)pos.getX() / 48.0D, (double)pos.getZ() / 48.0D, false)) / 2.0D, 0.0D, 0.9999D);
        Collection<Block> allFlowers = flowers.getAllElements();
        return allFlowers.toArray(new Block[0])[(int) (noise * (double) allFlowers.size())].getDefaultState();
    }
}
