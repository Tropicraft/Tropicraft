package net.tropicraft.core.common.dimension.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CanyonWorldCarver;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class TropicsCanyonCarver extends CanyonWorldCarver {
    private final float[] rs = new float[1024];

    public TropicsCanyonCarver(Codec<CanyonCarverConfiguration> codec) {
        super(codec);
        this.replaceableBlocks = ImmutableSet.<Block> builder().addAll(this.replaceableBlocks)
                .add(TropicraftBlocks.CORAL_SAND.get())
                .add(TropicraftBlocks.FOAMY_SAND.get())
                .add(TropicraftBlocks.MINERAL_SAND.get())
                .add(TropicraftBlocks.PACKED_PURIFIED_SAND.get())
                .add(TropicraftBlocks.PURIFIED_SAND.get())
                .add(TropicraftBlocks.VOLCANIC_SAND.get())
                .add(TropicraftBlocks.MUD.get(), TropicraftBlocks.MUD_WITH_PIANGUAS.get())
                .build();
    }

    //TODO [PORT]: Unknown what exactly needs to be ported

//    @Override
//    public boolean carve(ChunkAccess chunk, Function<BlockPos, Biome> biomePos, Random rand, int seaLevel, int chunkXOffset, int chunkZOffset, int chunkX, int chunkZ, BitSet carvingMask, ProbabilityFeatureConfiguration config) {
//        int i = (this.getRange() * 2 - 1) * 16;
//        double d0 = chunkXOffset * 16 + rand.nextInt(16);
//        double d1 = rand.nextInt(rand.nextInt(80) + 8) + 20;
//        double d2 = chunkZOffset * 16 + rand.nextInt(16);
//        float f = rand.nextFloat() * ((float) Math.PI * 2F);
//        float f1 = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
//        float f2 = (rand.nextFloat() * 2.0F + rand.nextFloat()) * 2.0F;
//        int j = i - rand.nextInt(i / 4);
//        this.genCanyon(chunk, biomePos, rand.nextLong(), seaLevel, chunkX, chunkZ, d0, d1, d2, f2, f, f1, 0, j, 3.0D, carvingMask);
//        return true;
//    }

//    // Copied from super
//    private void genCanyon(ChunkAccess p_227204_1_, Function<BlockPos, Biome> p_227204_2_, long p_227204_3_, int p_227204_5_, int p_227204_6_, int p_227204_7_, double p_227204_8_, double p_227204_10_, double p_227204_12_, float p_227204_14_, float p_227204_15_, float p_227204_16_, int p_227204_17_, int p_227204_18_, double p_227204_19_, BitSet p_227204_21_) {
//        Random random = new Random(p_227204_3_);
//        float f = 1.0F;
//
//        for(int i = 0; i < 256; ++i) {
//            if (i == 0 || random.nextInt(3) == 0) {
//                f = 1.0F + random.nextFloat() * random.nextFloat();
//            }
//
//            this.rs[i] = f * f;
//        }
//
//        float f4 = 0.0F;
//        float f1 = 0.0F;
//
//        for(int j = p_227204_17_; j < p_227204_18_; ++j) {
//            double d0 = 1.5D + (double)(Mth.sin((float)j * (float)Math.PI / (float)p_227204_18_) * p_227204_14_);
//            double d1 = d0 * p_227204_19_;
//            d0 = d0 * ((double)random.nextFloat() * 0.25D + 0.75D);
//            d1 = d1 * ((double)random.nextFloat() * 0.25D + 0.75D);
//            float f2 = Mth.cos(p_227204_16_);
//            float f3 = Mth.sin(p_227204_16_);
//            p_227204_8_ += Mth.cos(p_227204_15_) * f2;
//            p_227204_10_ += f3;
//            p_227204_12_ += Mth.sin(p_227204_15_) * f2;
//            p_227204_16_ = p_227204_16_ * 0.7F;
//            p_227204_16_ = p_227204_16_ + f1 * 0.05F;
//            p_227204_15_ += f4 * 0.05F;
//            f1 = f1 * 0.8F;
//            f4 = f4 * 0.5F;
//            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
//            f4 = f4 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
//            if (random.nextInt(4) != 0) {
//                if (!this.canReach(p_227204_6_, p_227204_7_, p_227204_8_, p_227204_12_, j, p_227204_18_, p_227204_14_)) {
//                    return;
//                }
//
//                this.carveSphere(p_227204_1_, p_227204_2_, p_227204_3_, p_227204_5_, p_227204_6_, p_227204_7_, p_227204_8_, p_227204_10_, p_227204_12_, d0, d1, p_227204_21_);
//            }
//        }
//
//    }
//
//    // Copied from super
//    @Override
//    protected boolean skip(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
//        return (p_222708_1_ * p_222708_1_ + p_222708_5_ * p_222708_5_) * (double)this.rs[p_222708_7_ - 1] + p_222708_3_ * p_222708_3_ / 6.0D >= 1.0D;
//    }
}
