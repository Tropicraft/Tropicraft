package net.tropicraft.core.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.IntersectionAllowedPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net/minecraft/world/level/levelgen/structure/pools/JigsawPlacement$Placer")
public class JigsawPlacerMixin {
    @Redirect(method = "tryPlacingChildren", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;getRotation()Lnet/minecraft/world/level/block/Rotation;"
    ))
    private Rotation getRotation(PoolElementStructurePiece piece) {
        if (piece.getElement() instanceof NoRotateSingleJigsawPiece) {
            return Rotation.NONE;
        }
        return piece.getRotation();
    }

    @Redirect(method = "tryPlacingChildren", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/pools/StructurePoolElement;getBoundingBox(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;",
            ordinal = 1
    ))
    private BoundingBox getPieceIntersectionBoundingBox(StructurePoolElement element, StructureTemplateManager templateManager, BlockPos pos, Rotation rotation) {
        if (element instanceof IntersectionAllowedPiece) {
            return new BoundingBox(pos);
        }
        return element.getBoundingBox(templateManager, pos, rotation);
    }

    @WrapOperation(method = "tryPlacingChildren", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/world/level/levelgen/structure/pools/StructurePoolElement;Lnet/minecraft/core/BlockPos;ILnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings;)Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;"))
    private PoolElementStructurePiece createPiece(StructureTemplateManager structureTemplateManager, StructurePoolElement element, BlockPos position, int groundLevelDelta, Rotation rotation, BoundingBox boundingBox, LiquidSettings liquidSettings, Operation<PoolElementStructurePiece> original) {
        if (element instanceof IntersectionAllowedPiece) {
            boundingBox = element.getBoundingBox(structureTemplateManager, position, rotation);
        }
        return original.call(structureTemplateManager, element, position, groundLevelDelta, rotation, boundingBox, liquidSettings);
    }
}
