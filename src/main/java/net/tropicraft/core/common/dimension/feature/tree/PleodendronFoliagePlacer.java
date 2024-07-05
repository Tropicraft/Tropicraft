package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class PleodendronFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<PleodendronFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return foliagePlacerParts(instance)
                .and(Codec.intRange(0, 16).fieldOf("height").forGetter((placer) -> placer.height))
                .apply(instance, PleodendronFoliagePlacer::new);
    });

    protected final int height;

    public PleodendronFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.PLEODENDRON.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, FoliageSetter setter, RandomSource random, TreeConfiguration config, int offset, FoliageAttachment foliage, int y, int radius, int start) {
        int i = foliage.doubleTrunk() ? offset : 2;

        for (int j = start; j >= start - i; --j) {
            int k = y + foliage.radiusOffset() + 1 - j;
            placeLeavesRow(world, setter, random, config, foliage.pos(), k, j, foliage.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean mega) {
        if (dx + dz >= 7) {
            return true;
        } else {
            return dx * dx + dz * dz > radius * radius;
        }
    }
}
