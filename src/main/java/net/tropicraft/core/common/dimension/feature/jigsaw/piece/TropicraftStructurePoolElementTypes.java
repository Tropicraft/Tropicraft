package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;

public final class TropicraftStructurePoolElementTypes {
    public static final DeferredRegister<StructurePoolElementType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, Constants.MODID);

    public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<NoRotateSingleJigsawPiece>> SINGLE_NO_ROTATE = register("single_no_rotate", NoRotateSingleJigsawPiece.CODEC);
    public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<SingleNoAirJigsawPiece>> SINGLE_NO_AIR = register("single_no_air", SingleNoAirJigsawPiece.CODEC);
    public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<HomeTreeBranchPiece>> HOME_TREE_BRANCH = register("home_tree_branch", HomeTreeBranchPiece.CODEC);

    private static <P extends StructurePoolElement> DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<P>> register(String name, MapCodec<P> codec) {
        return REGISTER.register(name, () -> () -> codec);
    }
}
