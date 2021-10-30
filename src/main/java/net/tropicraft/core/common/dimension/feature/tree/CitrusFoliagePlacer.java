package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

import net.minecraft.world.gen.foliageplacer.FoliagePlacer.Foliage;

public final class CitrusFoliagePlacer extends FoliagePlacer {
    public static final Codec<CitrusFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, CitrusFoliagePlacer::new);
    });

    public CitrusFoliagePlacer(FeatureSpread radius, FeatureSpread offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.CITRUS.get();
    }

    @Override
    protected void createFoliage(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int p_230372_4_, Foliage node, int p_230372_6_, int radius, Set<BlockPos> leaves, int p_230372_9_, MutableBoundingBox p_230372_10_) {
        this.placeLeavesRow(world, random, config, node.foliagePos(), 1, leaves, 1, node.doubleTrunk(), p_230372_10_);
        this.placeLeavesRow(world, random, config, node.foliagePos(), 2, leaves, 0, node.doubleTrunk(), p_230372_10_);

        if (node.radiusOffset() == 1) {
            // Center leaf cluster, add another layer at the bottom
            this.placeLeavesRow(world, random, config, node.foliagePos(), 3, leaves, -1, node.doubleTrunk(), p_230372_10_);
        }
    }

    @Override
    public int foliageHeight(Random p_230374_1_, int p_230374_2_, BaseTreeFeatureConfig p_230374_3_) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius && random.nextInt(2) == 0;
    }
}
