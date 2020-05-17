package net.tropicraft.core.common.dimension.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.FeatureJigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftRainforestBiome;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import net.tropicraft.core.common.dimension.feature.jigsaw.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.pools.HomeTreePools;

public class HomeTreeFeature extends Structure<VillageConfig> {

    public HomeTreeFeature(Function<Dynamic<?>, ? extends VillageConfig> p_i51419_1_) {
        super(p_i51419_1_);
    }

    @Override
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        int distance = chunkGenerator instanceof TropicraftChunkGenerator ? (((TropicraftChunkGenerator)chunkGenerator).getSettings().getHomeTreeDistance()) : 20;
        int separation = chunkGenerator instanceof TropicraftChunkGenerator ? (((TropicraftChunkGenerator)chunkGenerator).getSettings().getHomeTreeSeparation()) : 4;
        int k = x + distance * spacingOffsetsX;
        int l = z + distance * spacingOffsetsZ;
        int i1 = k < 0 ? k - distance + 1 : k;
        int j1 = l < 0 ? l - distance + 1 : l;
        int k1 = i1 / distance;
        int l1 = j1 / distance;
        ((SharedSeedRandom)random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), k1, l1, 10387312);
        k1 = k1 * distance;
        l1 = l1 * distance;
        k1 = k1 + random.nextInt(distance - separation);
        l1 = l1 + random.nextInt(distance - separation);
        return new ChunkPos(k1, l1);
    }

    @Override
    public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ) {
        ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);
        if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z) {
           BlockPos pos = new BlockPos((chunkPosX << 4) + 8, 0, (chunkPosZ << 4) + 8);
           int centerY = chunkGen.func_222529_a(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
           return isValid(chunkGen, pos.add(-4, 0, -4), centerY) &&
                  isValid(chunkGen, pos.add(-4, 0, 4), centerY) &&
                  isValid(chunkGen, pos.add(4, 0, 4), centerY) &&
                  isValid(chunkGen, pos.add(4, 0, -4), centerY);
        } else {
           return false;
        }
    }

    private boolean isValid(ChunkGenerator<?> chunkGen, BlockPos pos, int startY) {
        int y = chunkGen.func_222532_b(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        return chunkGen.hasStructure(chunkGen.getBiomeProvider().getBiome(pos), TropicraftFeatures.HOME_TREE.get())
                && y >= chunkGen.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > chunkGen.getSeaLevel() + 2
                && chunkGen.getBiomeProvider().getBiome(pos) instanceof TropicraftRainforestBiome;
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
        return TropicraftFeatures.HOME_TREE.getId().toString();
    }

    @Override
    public int getSize() {
        return 8;
    }

    public static class Start extends StructureStart {

        public Start(Structure<?> p_i51110_1_, int p_i51110_2_, int p_i51110_3_, Biome p_i51110_4_, MutableBoundingBox p_i51110_5_, int p_i51110_6_, long p_i51110_7_) {
            super(p_i51110_1_, p_i51110_2_, p_i51110_3_, p_i51110_4_, p_i51110_5_, p_i51110_6_, p_i51110_7_);
        }

        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            final BlockPos pos = new BlockPos(chunkX * 16, -5, chunkZ * 16);
            VillageConfig config = generator.getStructureConfig(biomeIn, TropicraftFeatures.HOME_TREE.get());
            HomeTreePools.init();
            JigsawManager.func_214889_a(config.startPool, config.size, HomeTreePiece::new, generator, templateManagerIn, pos, this.components, this.rand);
            this.recalculateStructureSize();
        }
        
        @Override
        protected void recalculateStructureSize() {
            super.recalculateStructureSize();
            int margin = 24; // Double vanilla's margin
            this.bounds.minX -= margin;
            this.bounds.minY -= margin;
            this.bounds.minZ -= margin;
            this.bounds.maxX += margin;
            this.bounds.maxY += margin;
            this.bounds.maxZ += margin;
         }
    }

    public static class HomeTreePiece extends AbstractVillagePiece {
        
        private static final IStructurePieceType TYPE = IStructurePieceType.register(HomeTreePiece::new, Constants.MODID + ":home_tree");

        public HomeTreePiece(TemplateManager p_i50890_1_, JigsawPiece p_i50890_2_, BlockPos p_i50890_3_, int p_i50890_4_, Rotation p_i50890_5_, MutableBoundingBox p_i50890_6_) {
            super(TYPE, p_i50890_1_, p_i50890_2_, p_i50890_3_, p_i50890_4_, p_i50890_5_, p_i50890_6_);
        }

        public HomeTreePiece(TemplateManager p_i50891_1_, CompoundNBT p_i50891_2_) {
            super(p_i50891_1_, p_i50891_2_, TYPE);
        }
        
        @Override
        public MutableBoundingBox getBoundingBox() {
            if (this.jigsawPiece instanceof FeatureJigsawPiece) {
                MutableBoundingBox ret = super.getBoundingBox();
                ret = new MutableBoundingBox(ret);
                ret.minX -= 32;
                ret.minY -= 32;
                ret.minZ -= 32;
                ret.maxX += 32;
                ret.maxY += 32;
                ret.maxZ += 32;
            }
            return super.getBoundingBox();
        }
        
        @Override
        public Rotation getRotation() {
            if (this.jigsawPiece instanceof NoRotateSingleJigsawPiece) {
                return Rotation.NONE;
            }
            return super.getRotation();
        }
    }
}
