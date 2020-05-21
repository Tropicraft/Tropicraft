package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.block.CoconutBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class PalmTreeFeature extends Feature<NoFeatureConfig> {

    public PalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> placer) {
        super(placer);
    }

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
    
    protected void placeLeaf(final IWorldGenerationReader world, int x, int y, int z) {
        setBlockState(world, new BlockPos(x, y, z), getLeaf());
    }

    protected void placeLog(final IWorldGenerationReader world, int x, int y, int z) {
        setBlockState(world, new BlockPos(x, y, z), getLog());
    }

    private static final Direction[] DIRECTIONS = ArrayUtils.removeElement(Direction.values(), Direction.UP);
    public static void spawnCoconuts(IWorldGenerationReader world, BlockPos pos, Random random, int chance, BlockState leaf) {
        final BlockState coconut = TropicraftBlocks.COCONUT.get().getDefaultState();
        for (Direction d : DIRECTIONS) {
            BlockPos pos2 = pos.offset(d);
            if (random.nextInt(chance) == 0 && AbstractTreeFeature.isAirOrLeaves(world, pos2)) {
                world.setBlockState(pos2, coconut.with(CoconutBlock.FACING, d.getOpposite()), Constants.BlockFlags.DEFAULT);
            }
        }
    }
}
