package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

public class UnderwaterCoveFeature extends Structure<NoFeatureConfig> {

    public UnderwaterCoveFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> func) {
        super(func);
    }

    @Override
    public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn) {
        // TODO
        return false;
    }

    @Override
    public IStartFactory getStartFactory() {
        return UnderwaterCoveFeature.Start::new;
    }

    @Override
    public String getStructureName() {
        return TropicraftFeatures.UNDERWATER_COVE.getId().toString();
    }

    @Override
    public int getSize() {
        return 0;
    }

    public static class Start extends StructureStart {

        public Start(Structure<?> p_i51110_1_, int p_i51110_2_, int p_i51110_3_, MutableBoundingBox p_i51110_5_, int p_i51110_6_, long p_i51110_7_) {
            super(p_i51110_1_, p_i51110_2_, p_i51110_3_, p_i51110_5_, p_i51110_6_, p_i51110_7_);
        }

        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            // Add no components, this doesn't generate anything, it's just for /locate
        }

        @Override
        public boolean isValid() {
            return true;
        }
    }
}