package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class PleodendronFoliagePlacer extends FoliagePlacer {
   public static final Codec<PleodendronFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
      return foliagePlacerParts(instance)
              .and(Codec.intRange(0, 16).fieldOf("height").forGetter((placer) -> placer.height))
              .apply(instance, PleodendronFoliagePlacer::new);
   });

   protected final int height;

   public PleodendronFoliagePlacer(UniformInt radius, UniformInt offset, int height) {
      super(radius, offset);
      this.height = height;
   }

   protected FoliagePlacerType<?> type() {
      return TropicraftFoliagePlacers.PLEODENDRON.get();
   }

   protected void createFoliage(LevelSimulatedRW world, Random random, TreeConfiguration config, int height, FoliagePlacer.FoliageAttachment foliage, int offset, int y, Set<BlockPos> leaves, int start, BoundingBox bounds) {
      int i = foliage.doubleTrunk() ? offset : 2;

      for(int j = start; j >= start - i; --j) {
         int k = y + foliage.radiusOffset() + 1 - j;
         this.placeLeavesRow(world, random, config, foliage.foliagePos(), k, leaves, j, foliage.doubleTrunk(), bounds);
      }

   }

   public int foliageHeight(Random random, int height, TreeConfiguration config) {
      return this.height;
   }

   protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean mega) {
      if (dx + dz >= 7) {
         return true;
      } else {
         return dx * dx + dz * dz > radius * radius;
      }
   }
}