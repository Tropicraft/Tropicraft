package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class PleodendronFoliagePlacer extends FoliagePlacer {
   public static final Codec<PleodendronFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
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
   protected void createFoliage(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> acceptor, Random random, TreeConfiguration config, int offset, FoliageAttachment foliage, int y, int radius, int start) {
      int i = foliage.doubleTrunk() ? offset : 2;

      for(int j = start; j >= start - i; --j) {
         int k = y + foliage.radiusOffset() + 1 - j;
         this.placeLeavesRow(world, acceptor, random, config, foliage.pos(), k, j, foliage.doubleTrunk());
      }
   }

   @Override
   public int foliageHeight(Random random, int height, TreeConfiguration config) {
      return this.height;
   }

   @Override
   protected boolean shouldSkipLocation(Random random, int dx, int y, int dz, int radius, boolean mega) {
      if (dx + dz >= 7) {
         return true;
      } else {
         return dx * dx + dz * dz > radius * radius;
      }
   }
}
