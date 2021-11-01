package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
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

   public PleodendronFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
      super(radius, offset);
      this.height = height;
   }

   protected FoliagePlacerType<?> type() {
      return TropicraftFoliagePlacers.PLEODENDRON.get();
   }

   @Override
   protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
      int i = pAttachment.doubleTrunk() ? offset.getMaxValue() : 2;

      for(int j = pFoliageRadius; j >= pFoliageRadius - i; --j) {
         int k = pFoliageHeight + pAttachment.radiusOffset() + 1 - j;
         this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pAttachment.pos(), k, j, pAttachment.doubleTrunk());
      }


      this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pAttachment.pos(), pAttachment.radiusOffset(), 1, pAttachment.doubleTrunk());
      this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, pAttachment.pos(), pAttachment.radiusOffset(), 0, pAttachment.doubleTrunk());
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