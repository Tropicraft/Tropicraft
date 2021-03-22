package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.FeatureJigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;

import net.minecraft.world.gen.feature.structure.Structure.IStartFactory;

public class HomeTreeStructure extends Structure<VillageConfig> {
    public HomeTreeStructure(Codec<VillageConfig> codec) {
        super(codec);
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider biomes, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos startChunkPos, VillageConfig config) {
        BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
        int centerY = generator.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        return isValid(generator, pos.add(-4, 0, -4), centerY) &&
                isValid(generator, pos.add(-4, 0, 4), centerY) &&
                isValid(generator, pos.add(4, 0, 4), centerY) &&
                isValid(generator, pos.add(4, 0, -4), centerY);
    }

    private boolean isValid(ChunkGenerator generator, BlockPos pos, int startY) {
        int y = generator.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        return y >= generator.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > generator.getSeaLevel() + 2;
    }

    @Override
    public IStartFactory<VillageConfig> getStartFactory() {
        return Start::new;
    }

    private static final IStructurePieceType TYPE = IStructurePieceType.register(Piece::new, Constants.MODID + ":home_tree");

    public static class Start extends StructureStart<VillageConfig> {
        public Start(Structure<VillageConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int references, long seed) {
            super(structure, chunkX, chunkZ, boundingBox, references, seed);
        }

        @Override
        public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, VillageConfig config) {
            BlockPos pos = new BlockPos(chunkX << 4, 0, chunkZ << 4);
            JigsawManager.func_242837_a(registries, config, Piece::new, generator, templates, pos, this.components, this.rand, true, true);
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

    public static class Piece extends AbstractVillagePiece {
        public Piece(TemplateManager templates, JigsawPiece piece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox bounds) {
            super(templates, piece, pos, groundLevelDelta, rotation, bounds);
        }

        public Piece(TemplateManager templates, CompoundNBT data) {
            super(templates, data);
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

        @Override
        public IStructurePieceType getStructurePieceType() {
            return TYPE;
        }
    }
}
