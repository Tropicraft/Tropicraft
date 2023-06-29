package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;

import java.util.function.BiConsumer;

public final class SmallMangroveFoliagePlacer extends FoliagePlacer {
    public static final Codec<SmallMangroveFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, SmallMangroveFoliagePlacer::new);
    });

    public SmallMangroveFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.SMALL_MANGROVE.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        this.placeLeavesRow(world, blockSetter, random, config, attachment.pos(), foliageRadius, 0, attachment.doubleTrunk());
        this.placeLeavesRow(world, blockSetter, random, config, attachment.pos(), foliageRadius, 1, attachment.doubleTrunk());
    }

    @Override
    public int foliageHeight(RandomSource random, int p_230374_2_, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        if (y == 0) {
            return radius != 0 && dx == radius && dz == radius && random.nextInt(2) == 0;
        }

        if (dx == 0 && dz == 0) {
            return false;
        }

        if (random.nextBoolean()) {
            return true;
        }

        return dx == radius && dz == radius;
    }
}
