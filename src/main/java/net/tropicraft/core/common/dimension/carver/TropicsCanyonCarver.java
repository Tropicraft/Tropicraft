package net.tropicraft.core.common.dimension.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CanyonWorldCarver;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
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

    public boolean carve(CarvingContext pContext, CanyonCarverConfiguration pConfig, ChunkAccess pChunk, Function<BlockPos, Biome> pBiomeAccessor, Random pRandom, Aquifer pAquifer, ChunkPos pChunkPos, BitSet pCarvingMask) {
        int i = (this.getRange() * 2 - 1) * 16;
        double d0 = (double)pChunkPos.getBlockX(pRandom.nextInt(16));
        int j = pRandom.nextInt(pRandom.nextInt(80) + 8) + 20;
        double d1 = (double)pChunkPos.getBlockZ(pRandom.nextInt(16));
        float f = pRandom.nextFloat() * ((float)Math.PI * 2F);
        float f1 = pConfig.verticalRotation.sample(pRandom);
        double d2 = (double)pConfig.yScale.sample(pRandom);
        float f2 = pConfig.shape.thickness.sample(pRandom);
        int k = (int)((float)i * pConfig.shape.distanceFactor.sample(pRandom));
        int l = 0;
        this.genCanyon(pContext, pConfig, pChunk, pBiomeAccessor, pRandom.nextLong(), pAquifer, d0, (double)j, d1, f2, f, f1, 0, k, d2, pCarvingMask);
        return true;
    }

    // Copied from super
    private void genCanyon(CarvingContext pContext, CanyonCarverConfiguration pConfig, ChunkAccess pChunk, Function<BlockPos, Biome> pBiomeAccessor, long pSeed, Aquifer pAquifer, double pX, double pY, double pZ, float pThickness, float pYaw, float pPitch, int pBranchIndex, int pBranchCount, double pHorizontalVerticalRatio, BitSet pCarvingMask) {
        Random random = new Random(pSeed);
        float[] afloat = this.initWidthFactors(pContext, pConfig, random);
        float f = 0.0F;
        float f1 = 0.0F;

        for(int i = pBranchIndex; i < pBranchCount; ++i) {
            double d0 = 1.5D + (double)(Mth.sin((float)i * (float)Math.PI / (float)pBranchCount) * pThickness);
            double d1 = d0 * pHorizontalVerticalRatio;
            d0 = d0 * (double)pConfig.shape.horizontalRadiusFactor.sample(random);
            d1 = this.updateVerticalRadius(pConfig, random, d1, (float)pBranchCount, (float)i);
            float f2 = Mth.cos(pPitch);
            float f3 = Mth.sin(pPitch);
            pX += (double)(Mth.cos(pYaw) * f2);
            pY += (double)f3;
            pZ += (double)(Mth.sin(pYaw) * f2);
            pPitch = pPitch * 0.7F;
            pPitch = pPitch + f1 * 0.05F;
            pYaw += f * 0.05F;
            f1 = f1 * 0.8F;
            f = f * 0.5F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if (random.nextInt(4) != 0) {
                if (!canReach(pChunk.getPos(), pX, pZ, i, pBranchCount, pThickness)) {
                    return;
                }

                this.carveEllipsoid(pContext, pConfig, pChunk, pBiomeAccessor, pSeed, pAquifer, pX, pY, pZ, d0, d1, pCarvingMask, (p_159082_, p_159083_, p_159084_, p_159085_, p_159086_) -> {
                    return this.shouldSkip(p_159082_, afloat, p_159083_, p_159084_, p_159085_, p_159086_);
                });
            }
        }

    }

    // Copied from super
    private float[] initWidthFactors(CarvingContext pContext, CanyonCarverConfiguration pConfig, Random pRandom) {
        int i = pContext.getGenDepth();
        float[] afloat = new float[i];
        float f = 1.0F;

        for(int j = 0; j < i; ++j) {
            if (j == 0 || pRandom.nextInt(pConfig.shape.widthSmoothness) == 0) {
                f = 1.0F + pRandom.nextFloat() * pRandom.nextFloat();
            }

            afloat[j] = f * f;
        }

        return afloat;
    }

    // Copied from super
    private double updateVerticalRadius(CanyonCarverConfiguration p_159026_, Random p_159027_, double p_159028_, float p_159029_, float p_159030_) {
        float f = 1.0F - Mth.abs(0.5F - p_159030_ / p_159029_) * 2.0F;
        float f1 = p_159026_.shape.verticalRadiusDefaultFactor + p_159026_.shape.verticalRadiusCenterFactor * f;
        return (double)f1 * p_159028_ * (double)Mth.randomBetween(p_159027_, 0.75F, 1.0F);
    }

    // Copied from super
    private boolean shouldSkip(CarvingContext pContext, float[] pWidthFactors, double pRelativeX, double pRelativeY, double pRelativeZ, int pY) {
        int i = pY - pContext.getMinGenY();
        return (pRelativeX * pRelativeX + pRelativeZ * pRelativeZ) * (double)pWidthFactors[i - 1] + pRelativeY * pRelativeY / 6.0D >= 1.0D;
    }
}
