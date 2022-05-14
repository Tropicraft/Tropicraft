package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;

import java.util.Random;
import java.util.function.BiConsumer;

public final class MangroveFoliagePlacer extends FoliagePlacer {
    public static final Codec<MangroveFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, MangroveFoliagePlacer::new);
    });

    public MangroveFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.MANGROVE.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> acceptor, Random random, TreeConfiguration config, int p_230372_4_, FoliageAttachment node, int p_230372_6_, int radius, int offset) {
        this.placeLeavesRow(level, acceptor, random, config, node.pos(), node.radiusOffset(), 1, node.doubleTrunk());
        this.placeLeavesRow(level, acceptor, random, config, node.pos(), node.radiusOffset() + 1, 0, node.doubleTrunk());
        this.placeLeavesRow(level, acceptor, random, config, node.pos(), node.radiusOffset(), -1, node.doubleTrunk());
    }

    @Override
    public int foliageHeight(Random random, int p_230374_2_, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius && (random.nextInt(2) == 0 || y == 0);
    }
}
