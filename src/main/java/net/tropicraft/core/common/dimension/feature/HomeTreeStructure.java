package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.FeaturePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.PieceWithGenerationBounds;

public class HomeTreeStructure extends StructureFeature<JigsawConfiguration> {
    public HomeTreeStructure(Codec<JigsawConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeSource biomes, long seed, WorldgenRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos startChunkPos, JigsawConfiguration config) {
        BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
        int centerY = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
        return isValid(generator, pos.offset(-4, 0, -4), centerY) &&
                isValid(generator, pos.offset(-4, 0, 4), centerY) &&
                isValid(generator, pos.offset(4, 0, 4), centerY) &&
                isValid(generator, pos.offset(4, 0, -4), centerY);
    }

    private boolean isValid(ChunkGenerator generator, BlockPos pos, int startY) {
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
        return y >= generator.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > generator.getSeaLevel() + 2;
    }

    @Override
    public StructureStartFactory<JigsawConfiguration> getStartFactory() {
        return Start::new;
    }

    private static final StructurePieceType TYPE = StructurePieceType.setPieceId(Piece::new, Constants.MODID + ":home_tree");

    public static class Start extends StructureStart<JigsawConfiguration> {
        public Start(StructureFeature<JigsawConfiguration> structure, int chunkX, int chunkZ, BoundingBox boundingBox, int references, long seed) {
            super(structure, chunkX, chunkZ, boundingBox, references, seed);
        }

        @Override
        public void generatePieces(RegistryAccess registries, ChunkGenerator generator, StructureManager templates, int chunkX, int chunkZ, Biome biome, JigsawConfiguration config) {
            BlockPos pos = new BlockPos(chunkX << 4, 0, chunkZ << 4);
            JigsawPlacement.addPieces(registries, config, Piece::new, generator, templates, pos, this.pieces, this.random, true, true);
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

    public static class Piece extends PoolElementStructurePiece {
        public Piece(StructureManager templates, StructurePoolElement piece, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds) {
            super(templates, piece, pos, groundLevelDelta, rotation, bounds);
            this.boundingBox = this.fixGenerationBoundingBox(templates);
        }

        public Piece(StructureManager templates, CompoundTag data) {
            super(templates, data);
            this.boundingBox = this.fixGenerationBoundingBox(templates);
        }

        private BoundingBox fixGenerationBoundingBox(StructureManager templates) {
            StructurePoolElement piece = this.element;
            if (piece instanceof PieceWithGenerationBounds) {
                return ((PieceWithGenerationBounds) piece).getGenerationBounds(templates, this.position, Rotation.NONE);
            } else {
                return boundingBox;
            }
        }

        @Override
        public BoundingBox getBoundingBox() {
            if (this.element instanceof FeaturePoolElement) {
                BoundingBox ret = super.getBoundingBox();
                ret = new BoundingBox(ret);
                ret.x0 -= 32;
                ret.y0 -= 32;
                ret.z0 -= 32;
                ret.x1 += 32;
                ret.y1 += 32;
                ret.z1 += 32;
                return ret;
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
        public StructurePieceType getType() {
            return TYPE;
        }
    }
}
