package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;

import java.util.Map;

public final class TropicraftStructures {
    public static final DeferredRegister<Structure> REGISTER = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, Constants.MODID);

    public static final RegistryObject<Structure> HOME_TREE = REGISTER.register("home_tree", () -> new HomeTreeStructure(
            new Structure.StructureSettings(
                    biomes(TropicraftTags.Biomes.HAS_HOME_TREE),
                    Map.of(),
                    GenerationStep.Decoration.SURFACE_STRUCTURES,
                    TerrainAdjustment.NONE
            ),
            TropicraftTemplatePools.HOME_TREE_STARTS.getHolder().orElseThrow(),
            7
    ));

    public static final RegistryObject<Structure> KOA_VILLAGE = REGISTER.register("koa_village", () -> new KoaVillageStructure(
            new Structure.StructureSettings(
                    biomes(TropicraftTags.Biomes.HAS_KOA_VILLAGE),
                    Map.of(),
                    GenerationStep.Decoration.SURFACE_STRUCTURES,
                    TerrainAdjustment.NONE
            ),
            TropicraftTemplatePools.KOA_TOWN_CENTERS.getHolder().orElseThrow(),
            6
    ));

    private static HolderSet<Biome> biomes(TagKey<Biome> pKey) {
        return BuiltinRegistries.BIOME.getOrCreateTag(pKey);
    }
}
