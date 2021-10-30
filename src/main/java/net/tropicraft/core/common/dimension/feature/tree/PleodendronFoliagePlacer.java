package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

public class PleodendronFoliagePlacer extends FoliagePlacer {
   public static final Codec<PleodendronFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
      return foliagePlacerParts(instance)
              .and(Codec.intRange(0, 16).fieldOf("height").forGetter((placer) -> placer.height))
              .apply(instance, PleodendronFoliagePlacer::new);
   });

   protected final int height;

   public PleodendronFoliagePlacer(FeatureSpread radius, FeatureSpread offset, int height) {
      super(radius, offset);
      this.height = height;
   }

   protected FoliagePlacerType<?> type() {
      return TropicraftFoliagePlacers.PLEODENDRON.get();
   }

   protected void createFoliage(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int height, FoliagePlacer.Foliage foliage, int offset, int y, Set<BlockPos> leaves, int start, MutableBoundingBox bounds) {
      int i = foliage.doubleTrunk() ? offset : 2;

      for(int j = start; j >= start - i; --j) {
         int k = y + foliage.radiusOffset() + 1 - j;
         this.placeLeavesRow(world, random, config, foliage.foliagePos(), k, leaves, j, foliage.doubleTrunk(), bounds);
      }

   }

   public int foliageHeight(Random random, int height, BaseTreeFeatureConfig config) {
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