package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public final class TropicraftStructureTypes {
    public static final DeferredRegister<StructureType<?>> REGISTER = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, Constants.MODID);

    public static final RegistryObject<StructureType<HomeTreeStructure>> HOME_TREE = REGISTER.register("home_tree", () -> () -> HomeTreeStructure.CODEC);
    public static final RegistryObject<StructureType<KoaVillageStructure>> KOA_VILLAGE = REGISTER.register("koa_village", () -> () -> KoaVillageStructure.CODEC);
}
