package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.feature.volcano.VolcanoStructure;

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

    public static final RegistryObject<Structure> OCEAN_VOLCANO = REGISTER.register("ocean_volcano", () -> new VolcanoStructure(
            new Structure.StructureSettings(
                    biomes(TropicraftTags.Biomes.HAS_OCEAN_VOLCANO),
                    Map.of(),
                    GenerationStep.Decoration.SURFACE_STRUCTURES,
                    TerrainAdjustment.NONE
            ),
            ConstantHeight.of(VerticalAnchor.absolute(-50)),
            UniformInt.of(45, 64)
    ));

    public static final RegistryObject<Structure> LAND_VOLCANO = REGISTER.register("land_volcano", () -> new VolcanoStructure(
            new Structure.StructureSettings(
                    biomes(TropicraftTags.Biomes.HAS_LAND_VOLCANO),
                    Map.of(),
                    GenerationStep.Decoration.SURFACE_STRUCTURES,
                    TerrainAdjustment.NONE
            ),
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            UniformInt.of(45, 64)
    ));

    private static HolderSet<Biome> biomes(TagKey<Biome> pKey) {
        return BuiltinRegistries.BIOME.getOrCreateTag(pKey);
    }
}
