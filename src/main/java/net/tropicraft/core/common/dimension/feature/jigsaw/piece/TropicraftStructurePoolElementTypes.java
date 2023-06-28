package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public final class TropicraftStructurePoolElementTypes {
    public static final DeferredRegister<StructurePoolElementType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, Constants.MODID);

    public static final RegistryObject<StructurePoolElementType<?>> SINGLE_NO_ROTATE = register("single_no_rotate", NoRotateSingleJigsawPiece.CODEC);
    public static final RegistryObject<StructurePoolElementType<?>> SINGLE_NO_AIR = register("single_no_air", SingleNoAirJigsawPiece.CODEC);

    private static <P extends StructurePoolElement> RegistryObject<StructurePoolElementType<?>> register(String name, Codec<P> codec) {
        return REGISTER.register(name, () -> (StructurePoolElementType<P>) () -> codec);
    }
}
