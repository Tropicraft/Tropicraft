package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;

import java.util.Random;
import java.util.Set;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;

public final class MangroveFoliagePlacer extends FoliagePlacer {
    public static final Codec<MangroveFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).apply(instance, MangroveFoliagePlacer::new);
    });

    public MangroveFoliagePlacer(UniformInt radius, UniformInt offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.MANGROVE.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedRW world, Random random, TreeConfiguration config, int p_230372_4_, FoliageAttachment node, int p_230372_6_, int radius, Set<BlockPos> leaves, int p_230372_9_, BoundingBox bounds) {
        this.placeLeavesRow(world, random, config, node.foliagePos(), node.radiusOffset(), leaves, 1, node.doubleTrunk(), bounds);
        this.placeLeavesRow(world, random, config, node.foliagePos(), node.radiusOffset() + 1, leaves, 0, node.doubleTrunk(), bounds);
        this.placeLeavesRow(world, random, config, node.foliagePos(), node.radiusOffset(), leaves, -1, node.doubleTrunk(), bounds);
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
