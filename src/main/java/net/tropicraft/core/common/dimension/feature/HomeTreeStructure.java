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
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider biomes, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos startChunkPos, VillageConfig config) {
        BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
        int centerY = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        return isValid(generator, pos.offset(-4, 0, -4), centerY) &&
                isValid(generator, pos.offset(-4, 0, 4), centerY) &&
                isValid(generator, pos.offset(4, 0, 4), centerY) &&
                isValid(generator, pos.offset(4, 0, -4), centerY);
    }

    private boolean isValid(ChunkGenerator generator, BlockPos pos, int startY) {
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        return y >= generator.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > generator.getSeaLevel() + 2;
    }

    @Override
    public IStartFactory<VillageConfig> getStartFactory() {
        return Start::new;
    }

    private static final IStructurePieceType TYPE = IStructurePieceType.setPieceId(Piece::new, Constants.MODID + ":home_tree");

    public static class Start extends StructureStart<VillageConfig> {
        public Start(Structure<VillageConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int references, long seed) {
            super(structure, chunkX, chunkZ, boundingBox, references, seed);
        }

        @Override
        public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, VillageConfig config) {
            BlockPos pos = new BlockPos(chunkX << 4, 0, chunkZ << 4);
            JigsawManager.addPieces(registries, config, Piece::new, generator, templates, pos, this.pieces, this.random, true, true);
            this.calculateBoundingBox();
        }

        @Override
        protected void calculateBoundingBox() {
            super.calculateBoundingBox();
            int margin = 24; // Double vanilla's margin
            this.boundingBox.x0 -= margin;
            this.boundingBox.y0 -= margin;
            this.boundingBox.z0 -= margin;
            this.boundingBox.x1 += margin;
            this.boundingBox.y1 += margin;
            this.boundingBox.z1 += margin;
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
            if (this.element instanceof FeatureJigsawPiece) {
                MutableBoundingBox ret = super.getBoundingBox();
                ret = new MutableBoundingBox(ret);
                ret.x0 -= 32;
                ret.y0 -= 32;
                ret.z0 -= 32;
                ret.x1 += 32;
                ret.y1 += 32;
                ret.z1 += 32;
            }
            return super.getBoundingBox();
        }

        @Override
        public Rotation getRotation() {
            if (this.element instanceof NoRotateSingleJigsawPiece) {
                return Rotation.NONE;
            }
            return super.getRotation();
        }

        @Override
        public IStructurePieceType getType() {
            return TYPE;
        }
    }
}
