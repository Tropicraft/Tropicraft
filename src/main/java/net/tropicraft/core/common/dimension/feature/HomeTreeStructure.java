package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.PieceWithGenerationBounds;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

public class HomeTreeStructure extends StructureFeature<JigsawConfiguration> {
    public HomeTreeStructure(Codec<JigsawConfiguration> codec, Predicate<PieceGeneratorSupplier.Context<JigsawConfiguration>> context) {
        super(codec, (p_197102_) -> {
            if (!context.test(p_197102_)) {
                return Optional.empty();
            } else {
                BlockPos blockpos = new BlockPos(p_197102_.chunkPos().getMinBlockX(), 0, p_197102_.chunkPos().getMinBlockZ());
                return JigsawPlacement.addPieces(p_197102_, Piece::new, blockpos, true, true);
            }
        });
    }

    private static boolean isValid(ChunkGenerator generator, BlockPos pos, int startY, final LevelHeightAccessor level) {
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level);
        return y >= generator.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > generator.getSeaLevel() + 2;
    }

    public static boolean checkLocation(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        final ChunkGenerator generator = context.chunkGenerator();
        final BlockPos pos = context.chunkPos().getWorldPosition();
        final LevelHeightAccessor level = context.heightAccessor();
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level);
        return isValid(generator, pos.offset(-4, 0, -4), y, level) &&
                isValid(generator, pos.offset(-4, 0, 4), y, level) &&
                isValid(generator, pos.offset(4, 0, 4), y, level) &&
                isValid(generator, pos.offset(4, 0, -4), y, level);
    }

    private static final StructurePieceType TYPE = setFullContextPieceId(Piece::new, Constants.MODID + ":home_tree");

    private static StructurePieceType setFullContextPieceId(StructurePieceType p_191152_, String p_191153_) {
        return Registry.register(Registry.STRUCTURE_PIECE, p_191153_.toLowerCase(Locale.ROOT), p_191152_);
    }

    public static class Piece extends PoolElementStructurePiece {
        public Piece(StructureManager templates, StructurePoolElement piece, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds) {
            super(templates, piece, pos, groundLevelDelta, rotation, bounds);
            this.boundingBox = this.fixGenerationBoundingBox(templates);
        }

        public Piece(StructurePieceSerializationContext pContext, CompoundTag data) {
            super(pContext, data);
            this.boundingBox = this.fixGenerationBoundingBox(pContext.structureManager());
        }

        private BoundingBox fixGenerationBoundingBox(StructureManager templates) {
            if (this.element instanceof PieceWithGenerationBounds piece) {
                return piece.getGenerationBounds(templates, this.position, Rotation.NONE);
            } else {
                return boundingBox;
            }
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
