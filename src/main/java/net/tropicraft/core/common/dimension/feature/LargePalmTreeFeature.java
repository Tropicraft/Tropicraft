package net.tropicraft.core.common.dimension.feature;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class LargePalmTreeFeature extends PalmTreeFeature {
    public LargePalmTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func) {
        super(func);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        pos = pos.toImmutable();

        int height = rand.nextInt(7) + 7;

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!TropicraftFeatureUtil.isSoil(world, pos.down())) {
            return false;
        }

        // Place trunk
        for (int y = 0; y <= height; y++) {
            placeLog(world, pos.getX(), pos.getY() + y, pos.getZ());
        }
        
        final int i = pos.getX(), j = pos.getY(), k = pos.getZ();

        // Wheeee, auto-generated code!
        placeLeaf(world, i + 0, j + height + 1, k + -7);
        placeLeaf(world, i + -1, j + height + 1, k + -6);
        placeLeaf(world, i + 1, j + height + 1, k + -6);
        placeLeaf(world, i + -5, j + height + 1, k + -5);
        placeLeaf(world, i + 5, j + height + 1, k + -5);
        placeLeaf(world, i + -6, j + height + 1, k + -1);
        placeLog(world, i + 0, j + height + 1, k + -1);
        placeLeaf(world, i + 6, j + height + 1, k + -1);
        placeLeaf(world, i + -7, j + height + 1, k + 0);
        placeLog(world, i + -1, j + height + 1, k + 0);
        placeLog(world, i + 0, j + height + 1, k + 0);
        placeLog(world, i + 1, j + height + 1, k + 0);
        placeLeaf(world, i + 7, j + height + 1, k + 0);
        placeLeaf(world, i + -6, j + height + 1, k + 1);
        placeLog(world, i + 0, j + height + 1, k + 1);
        placeLeaf(world, i + 6, j + height + 1, k + 1);
        placeLeaf(world, i + -5, j + height + 1, k + 5);
        placeLeaf(world, i + 5, j + height + 1, k + 5);
        placeLeaf(world, i + -1, j + height + 1, k + 6);
        placeLeaf(world, i + 1, j + height + 1, k + 6);
        placeLeaf(world, i + 0, j + height + 1, k + 7);
        placeLeaf(world, i + 0, j + height + 2, k + -6);
        placeLeaf(world, i + -4, j + height + 2, k + -5);
        placeLeaf(world, i + -1, j + height + 2, k + -5);
        placeLeaf(world, i + 1, j + height + 2, k + -5);
        placeLeaf(world, i + 4, j + height + 2, k + -5);
        placeLeaf(world, i + -5, j + height + 2, k + -4);
        placeLeaf(world, i + -3, j + height + 2, k + -4);
        placeLeaf(world, i + -1, j + height + 2, k + -4);
        placeLeaf(world, i + 1, j + height + 2, k + -4);
        placeLeaf(world, i + 3, j + height + 2, k + -4);
        placeLeaf(world, i + 5, j + height + 2, k + -4);
        placeLeaf(world, i + -4, j + height + 2, k + -3);
        placeLeaf(world, i + -2, j + height + 2, k + -3);
        placeLeaf(world, i + -1, j + height + 2, k + -3);
        placeLeaf(world, i + 1, j + height + 2, k + -3);
        placeLeaf(world, i + 2, j + height + 2, k + -3);
        placeLeaf(world, i + 4, j + height + 2, k + -3);
        placeLeaf(world, i + -3, j + height + 2, k + -2);
        placeLeaf(world, i + -1, j + height + 2, k + -2);
        placeLeaf(world, i + 1, j + height + 2, k + -2);
        placeLeaf(world, i + 3, j + height + 2, k + -2);
        placeLeaf(world, i + -5, j + height + 2, k + -1);
        placeLeaf(world, i + -4, j + height + 2, k + -1);
        placeLeaf(world, i + -3, j + height + 2, k + -1);
        placeLeaf(world, i + -2, j + height + 2, k + -1);
        placeLeaf(world, i + -1, j + height + 2, k + -1);
        placeLeaf(world, i + 0, j + height + 2, k + -1);
        placeLeaf(world, i + 1, j + height + 2, k + -1);
        placeLeaf(world, i + 2, j + height + 2, k + -1);
        placeLeaf(world, i + 3, j + height + 2, k + -1);
        placeLeaf(world, i + 4, j + height + 2, k + -1);
        placeLeaf(world, i + 5, j + height + 2, k + -1);
        placeLeaf(world, i + -6, j + height + 2, k + 0);
        placeLeaf(world, i + -1, j + height + 2, k + 0);
        placeLeaf(world, i + 0, j + height + 2, k + 0);
        placeLeaf(world, i + 1, j + height + 2, k + 0);
        placeLeaf(world, i + 6, j + height + 2, k + 0);
        placeLeaf(world, i + -5, j + height + 2, k + 1);
        placeLeaf(world, i + -4, j + height + 2, k + 1);
        placeLeaf(world, i + -3, j + height + 2, k + 1);
        placeLeaf(world, i + -2, j + height + 2, k + 1);
        placeLeaf(world, i + -1, j + height + 2, k + 1);
        placeLeaf(world, i + 0, j + height + 2, k + 1);
        placeLeaf(world, i + 1, j + height + 2, k + 1);
        placeLeaf(world, i + 2, j + height + 2, k + 1);
        placeLeaf(world, i + 3, j + height + 2, k + 1);
        placeLeaf(world, i + 4, j + height + 2, k + 1);
        placeLeaf(world, i + 5, j + height + 2, k + 1);
        placeLeaf(world, i + -3, j + height + 2, k + 2);
        placeLeaf(world, i + -1, j + height + 2, k + 2);
        placeLeaf(world, i + 1, j + height + 2, k + 2);
        placeLeaf(world, i + 3, j + height + 2, k + 2);
        placeLeaf(world, i + -4, j + height + 2, k + 3);
        placeLeaf(world, i + -2, j + height + 2, k + 3);
        placeLeaf(world, i + -1, j + height + 2, k + 3);
        placeLeaf(world, i + 1, j + height + 2, k + 3);
        placeLeaf(world, i + 2, j + height + 2, k + 3);
        placeLeaf(world, i + 4, j + height + 2, k + 3);
        placeLeaf(world, i + -5, j + height + 2, k + 4);
        placeLeaf(world, i + -3, j + height + 2, k + 4);
        placeLeaf(world, i + -1, j + height + 2, k + 4);
        placeLeaf(world, i + 1, j + height + 2, k + 4);
        placeLeaf(world, i + 3, j + height + 2, k + 4);
        placeLeaf(world, i + 5, j + height + 2, k + 4);
        placeLeaf(world, i + -4, j + height + 2, k + 5);
        placeLeaf(world, i + -1, j + height + 2, k + 5);
        placeLeaf(world, i + 1, j + height + 2, k + 5);
        placeLeaf(world, i + 4, j + height + 2, k + 5);
        placeLeaf(world, i + 0, j + height + 2, k + 6);
        placeLeaf(world, i + 0, j + height + 3, k + -5);
        placeLeaf(world, i + -4, j + height + 3, k + -4);
        placeLeaf(world, i + 0, j + height + 3, k + -4);
        placeLeaf(world, i + 4, j + height + 3, k + -4);
        placeLeaf(world, i + -3, j + height + 3, k + -3);
        placeLeaf(world, i + 0, j + height + 3, k + -3);
        placeLeaf(world, i + 3, j + height + 3, k + -3);
        placeLeaf(world, i + -2, j + height + 3, k + -2);
        placeLeaf(world, i + 0, j + height + 3, k + -2);
        placeLeaf(world, i + 2, j + height + 3, k + -2);
        placeLeaf(world, i + -1, j + height + 3, k + -1);
        placeLeaf(world, i + 0, j + height + 3, k + -1);
        placeLeaf(world, i + 1, j + height + 3, k + -1);
        placeLeaf(world, i + -5, j + height + 3, k + 0);
        placeLeaf(world, i + -4, j + height + 3, k + 0);
        placeLeaf(world, i + -3, j + height + 3, k + 0);
        placeLeaf(world, i + -2, j + height + 3, k + 0);
        placeLeaf(world, i + -1, j + height + 3, k + 0);
        placeLeaf(world, i + 1, j + height + 3, k + 0);
        placeLeaf(world, i + 2, j + height + 3, k + 0);
        placeLeaf(world, i + 3, j + height + 3, k + 0);
        placeLeaf(world, i + 4, j + height + 3, k + 0);
        placeLeaf(world, i + 5, j + height + 3, k + 0);
        placeLeaf(world, i + -1, j + height + 3, k + 1);
        placeLeaf(world, i + 0, j + height + 3, k + 1);
        placeLeaf(world, i + 1, j + height + 3, k + 1);
        placeLeaf(world, i + -2, j + height + 3, k + 2);
        placeLeaf(world, i + 0, j + height + 3, k + 2);
        placeLeaf(world, i + 2, j + height + 3, k + 2);
        placeLeaf(world, i + -3, j + height + 3, k + 3);
        placeLeaf(world, i + 0, j + height + 3, k + 3);
        placeLeaf(world, i + 3, j + height + 3, k + 3);
        placeLeaf(world, i + -4, j + height + 3, k + 4);
        placeLeaf(world, i + 0, j + height + 3, k + 4);
        placeLeaf(world, i + 4, j + height + 3, k + 4);
        placeLeaf(world, i + 0, j + height + 3, k + 5);

        for (int c = 0; c < 4; c++) {
            spawnCoconuts(world, new BlockPos(i, j + height + 1, k).offset(Direction.byHorizontalIndex(i)), rand, 2, getLeaf());
        }

        return true;
    }
}
