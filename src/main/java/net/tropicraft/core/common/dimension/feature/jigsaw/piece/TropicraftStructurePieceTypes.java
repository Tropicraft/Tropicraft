package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.volcano.VolcanoStructurePiece;

public final class TropicraftStructurePieceTypes {
    public static final DeferredRegister<StructurePieceType> REGISTER = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, Constants.MODID);

    public static final RegistryObject<StructurePieceType> VOLCANO = REGISTER.register("volcano", () -> VolcanoStructurePiece::new);
}
