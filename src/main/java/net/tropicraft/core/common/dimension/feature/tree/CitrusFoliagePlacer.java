package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;

public final class CitrusFoliagePlacer extends FoliagePlacer {
    public static final Codec<CitrusFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, CitrusFoliagePlacer::new);
    });

    public CitrusFoliagePlacer(ConstantInt radius, ConstantInt offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.CITRUS.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedRW world, Random random, TreeConfiguration config, int p_230372_4_, FoliageAttachment node, int p_230372_6_, int radius, Set<BlockPos> leaves, int p_230372_9_, BoundingBox p_230372_10_) {
        this.placeLeavesRow(world, random, config, node.foliagePos(), 1, leaves, 1, node.doubleTrunk(), p_230372_10_);
        this.placeLeavesRow(world, random, config, node.foliagePos(), 2, leaves, 0, node.doubleTrunk(), p_230372_10_);

        if (node.radiusOffset() == 1) {
            // Center leaf cluster, add another layer at the bottom
            this.placeLeavesRow(world, random, config, node.foliagePos(), 3, leaves, -1, node.doubleTrunk(), p_230372_10_);
        }
    }

    @Override
    public int foliageHeight(Random p_230374_1_, int p_230374_2_, TreeConfiguration p_230374_3_) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius && random.nextInt(2) == 0;
    }
}
