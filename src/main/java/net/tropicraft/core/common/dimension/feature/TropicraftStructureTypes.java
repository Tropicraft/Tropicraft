package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.volcano.VolcanoStructure;

public final class TropicraftStructureTypes {
    public static final DeferredRegister<StructureType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MODID);

    public static final RegistryObject<StructureType<HomeTreeStructure>> HOME_TREE = REGISTER.register("home_tree", () -> () -> HomeTreeStructure.CODEC);
    public static final RegistryObject<StructureType<KoaVillageStructure>> KOA_VILLAGE = REGISTER.register("koa_village", () -> () -> KoaVillageStructure.CODEC);
    public static final RegistryObject<StructureType<VolcanoStructure>> VOLCANO = REGISTER.register("volcano", () -> () -> VolcanoStructure.CODEC);
}
