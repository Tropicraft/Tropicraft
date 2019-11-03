package net.tropicraft.core.common.minigames.dimensions;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.dimension.biome.*;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGeneratorTypes;
import net.tropicraft.core.common.dimension.config.TropicraftGeneratorSettings;
import net.tropicraft.core.common.minigames.definitions.IslandRoyaleMinigameDefinition;

import javax.annotation.Nullable;

public class IslandRoyaleDimension extends Dimension {
    public IslandRoyaleDimension(final World worldIn, final DimensionType typeIn) {
        super(worldIn, typeIn);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        BiomeProviderType<SingleBiomeProviderSettings, SingleBiomeProvider> biomeType = BiomeProviderType.FIXED;
        ChunkGeneratorType chunkType = TropicraftChunkGeneratorTypes.TROPICS.get();
        TropicraftGeneratorSettings genSettings = (TropicraftGeneratorSettings) chunkType.createSettings();
        SingleBiomeProviderSettings settings2 = biomeType.createSettings().setBiome(TropicraftBiomes.TROPICS_OCEAN.get());
        return chunkType.create(this.world, biomeType.create(settings2), genSettings);
    }

    /** Copied from OverworldDimension */
    @Override
    @Nullable
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
        return IslandRoyaleMinigameDefinition.RESPAWN_POS;
    }

    /** Copied from OverworldDimension */
    @Override
    @Nullable
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
        return IslandRoyaleMinigameDefinition.RESPAWN_POS;
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     * mimics overworld code
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        int i = (int)(worldTime % 24000L);
        float f = ((float)i + partialTicks) / 24000.0F - 0.25F;
        if (f < 0.0F) {
            ++f;
        }

        if (f > 1.0F) {
            --f;
        }

        float f1 = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getCloudHeight() {
        return 192;
    }

    /**
     * Return Vec3D with biome specific fog color
     * Copies overworld fog code
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d(f1, f2, f3);
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }
}
