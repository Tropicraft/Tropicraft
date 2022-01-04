package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

public final class CitrusFoliagePlacer extends FoliagePlacer {
    public static final Codec<CitrusFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, CitrusFoliagePlacer::new);
    });

    public CitrusFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.CITRUS;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> acceptor, Random random, TreeConfiguration config, int p_230372_4_, FoliageAttachment node, int p_230372_6_, int radius, int offset) {
        this.placeLeavesRow(level, acceptor, random, config, node.pos(), 1, 1, node.doubleTrunk());
        this.placeLeavesRow(level, acceptor, random, config, node.pos(), 2, 0, node.doubleTrunk());

        if (node.radiusOffset() == 1) {
            // Center leaf cluster, add another layer at the bottom
            this.placeLeavesRow(level, acceptor, random, config, node.pos(), 3, -1, node.doubleTrunk());
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
