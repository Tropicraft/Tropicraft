package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;

import java.util.Random;
import java.util.Set;

import net.minecraft.world.gen.foliageplacer.FoliagePlacer.Foliage;

public final class SmallMangroveFoliagePlacer extends FoliagePlacer {
    public static final Codec<SmallMangroveFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, SmallMangroveFoliagePlacer::new);
    });

    public SmallMangroveFoliagePlacer(FeatureSpread radius, FeatureSpread offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.SMALL_MANGROVE.get();
    }

    @Override
    protected void createFoliage(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int p_230372_4_, Foliage node, int p_230372_6_, int radius, Set<BlockPos> leaves, int p_230372_9_, MutableBoundingBox p_230372_10_) {
        this.placeLeavesRow(world, random, config, node.foliagePos(), node.radiusOffset(), leaves, 1, node.doubleTrunk(), p_230372_10_);
        this.placeLeavesRow(world, random, config, node.foliagePos(), node.radiusOffset(), leaves, 0, node.doubleTrunk(), p_230372_10_);
    }

    @Override
    public int foliageHeight(Random random, int p_230374_2_, BaseTreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
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
