package net.tropicraft.core.common.dimension.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.pools.KoaVillagePools;

public class KoaVillageStructure extends Structure<NoFeatureConfig> {

    public KoaVillageStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51419_1_) {
        super(p_i51419_1_);
    }

    @Override
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        int i = chunkGenerator.getSettings().getVillageDistance();
        int j = chunkGenerator.getSettings().getVillageSeparation();
        int k = x + i * spacingOffsetsX;
        int l = z + i * spacingOffsetsZ;
        int i1 = k < 0 ? k - i + 1 : k;
        int j1 = l < 0 ? l - i + 1 : l;
        int k1 = i1 / i;
        int l1 = j1 / i;
        ((SharedSeedRandom)random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), k1, l1, 10387312);
        k1 = k1 * i;
        l1 = l1 * i;
        k1 = k1 + random.nextInt(i - j);
        l1 = l1 + random.nextInt(i - j);
        return new ChunkPos(k1, l1);
    }

    @Override
    public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ, Biome biomeIn) {
        ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);
        if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z) {
           BlockPos pos = new BlockPos((chunkPosX << 4) + 8, 0, (chunkPosZ << 4) + 8);
           return isValid(chunkGen, biomeIn, pos.add(-4, 0, -4)) &&
                  isValid(chunkGen, biomeIn, pos.add(-4, 0, 4)) &&
                  isValid(chunkGen, biomeIn, pos.add(4, 0, 4)) &&
                  isValid(chunkGen, biomeIn, pos.add(4, 0, -4));
        } else {
           return false;
        }
    }

    private boolean isValid(ChunkGenerator<?> chunkGen, final Biome biome, BlockPos pos) {
        // TODO if not working correctly, investigate the use of biome here instead of the biome at exactly 'pos'
        return chunkGen.hasStructure(biome, TropicraftFeatures.VILLAGE.get())
                && chunkGen.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG) == chunkGen.getSeaLevel();
    }
    
    @Override
    public BlockPos findNearest(World worldIn, ChunkGenerator<? extends GenerationSettings> chunkGenerator, BlockPos pos, int radius, boolean p_211405_5_) {
        BlockPos ret = super.findNearest(worldIn, chunkGenerator, pos, radius, p_211405_5_);
//        for (int r = 0; r < 16; r++) {
//            for (int d = 0; d < 4; d++) {
//                Direction dir = Direction.byHorizontalIndex(d);
//                BlockPos check = ret.offset(dir, r);
//                IFluidState fluid = worldIn.getFluidState(new BlockPos(check.getX(), chunkGenerator.getSeaLevel(), check.getZ()));
//                if (fluid.getFluid() == Fluids.WATER) {
//                    return check;
//                }
//            }
//        }
        return ret; // Fallback ?
    }

    @Override
    public IStartFactory getStartFactory() {
        return Start::new;
    }

    @Override
    public String getStructureName() {
        return TropicraftFeatures.VILLAGE.getId().toString();
    }

    @Override
    public int getSize() {
        return 8;
    }
    
    private static final IStructurePieceType TYPE = IStructurePieceType.register(KoaVillage::new, Constants.MODID + ":koa_village");

    public static class Start extends MarginedStructureStart {

        public Start(Structure<?> p_i51110_1_, int p_i51110_2_, int p_i51110_3_, MutableBoundingBox p_i51110_5_, int p_i51110_6_, long p_i51110_7_) {
            super(p_i51110_1_, p_i51110_2_, p_i51110_3_, p_i51110_5_, p_i51110_6_, p_i51110_7_);
        }

        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            BlockPos pos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
            KoaVillagePools.init();
            JigsawManager.addPieces(new ResourceLocation(Constants.MODID, "koa_village/town_centers"), 6, KoaVillage::new, generator, templateManagerIn, pos, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }

    public static class KoaVillage extends AbstractVillagePiece {
        
        public KoaVillage(TemplateManager p_i50890_1_, JigsawPiece p_i50890_2_, BlockPos p_i50890_3_, int p_i50890_4_, Rotation p_i50890_5_, MutableBoundingBox p_i50890_6_) {
            super(TYPE, p_i50890_1_, p_i50890_2_, p_i50890_3_, p_i50890_4_, p_i50890_5_, p_i50890_6_);
        }

        public KoaVillage(TemplateManager p_i50891_1_, CompoundNBT p_i50891_2_) {
            super(p_i50891_1_, p_i50891_2_, TYPE);
        }
    }
}
