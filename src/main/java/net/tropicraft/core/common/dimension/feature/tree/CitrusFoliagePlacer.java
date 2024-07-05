package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public final class CitrusFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<CitrusFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return foliagePlacerParts(instance).apply(instance, CitrusFoliagePlacer::new);
    });

    public CitrusFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.CITRUS.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config, int p_230372_4_, FoliageAttachment node, int p_230372_6_, int radius, int offset) {
        this.placeLeavesRow(level, setter, random, config, node.pos(), 1, 1, node.doubleTrunk());
        this.placeLeavesRow(level, setter, random, config, node.pos(), 2, 0, node.doubleTrunk());

        if (node.radiusOffset() == 1) {
            // Center leaf cluster, add another layer at the bottom
            this.placeLeavesRow(level, setter, random, config, node.pos(), 3, -1, node.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int p_230374_2_, TreeConfiguration p_230374_3_) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius && random.nextInt(2) == 0;
    }
}
