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
      return func_242830_b(instance)
              .and(Codec.intRange(0, 16).fieldOf("height").forGetter((placer) -> placer.height))
              .apply(instance, PleodendronFoliagePlacer::new);
   });

   protected final int height;

   public PleodendronFoliagePlacer(FeatureSpread p_i242000_1_, FeatureSpread p_i242000_2_, int p_i242000_3_) {
      super(p_i242000_1_, p_i242000_2_);
      this.height = p_i242000_3_;
   }

   protected FoliagePlacerType<?> getPlacerType() {
      return TropicraftFoliagePlacers.PLEODENDRON.get();
   }

   protected void func_230372_a_(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int p_230372_4_, FoliagePlacer.Foliage foliage, int p_230372_6_, int y, Set<BlockPos> leaves, int start, MutableBoundingBox bounds) {
      int i = foliage.func_236765_c_() ? p_230372_6_ : 2;

      for(int j = start; j >= start - i; --j) {
         int k = y + foliage.func_236764_b_() + 1 - j;
         this.func_236753_a_(world, random, config, foliage.func_236763_a_(), k, leaves, j, foliage.func_236765_c_(), bounds);
      }

   }

   public int func_230374_a_(Random random, int p_230374_2_, BaseTreeFeatureConfig config) {
      return this.height;
   }

   protected boolean func_230373_a_(Random random, int dx, int y, int dz, int radius, boolean mega) {
      if (dx + dz >= 7) {
         return true;
      } else {
         return dx * dx + dz * dz > radius * radius;
      }
   }
}