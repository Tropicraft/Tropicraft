package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.feature.volcano.VolcanoStructure;

import java.util.Map;

public final class TropicraftStructures {
    public static final ResourceKey<Structure> HOME_TREE = createKey("home_tree");
    public static final ResourceKey<Structure> KOA_VILLAGE = createKey("koa_village");
    public static final ResourceKey<Structure> OCEAN_VOLCANO = createKey("ocean_volcano");
    public static final ResourceKey<Structure> LAND_VOLCANO = createKey("land_volcano");

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(HOME_TREE, new HomeTreeStructure(
                new Structure.StructureSettings(
                        biomes.get(TropicraftTags.Biomes.HAS_HOME_TREE).orElseThrow(),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                pools.getOrThrow(TropicraftTemplatePools.HOME_TREE_STARTS),
                7
        ));

        context.register(KOA_VILLAGE, new KoaVillageStructure(
                new Structure.StructureSettings(
                        biomes.get(TropicraftTags.Biomes.HAS_KOA_VILLAGE).orElseThrow(),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                pools.getOrThrow(TropicraftTemplatePools.KOA_TOWN_CENTERS),
                6
        ));

        context.register(OCEAN_VOLCANO, new VolcanoStructure(
                new Structure.StructureSettings(
                        biomes.get(TropicraftTags.Biomes.HAS_OCEAN_VOLCANO).orElseThrow(),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                ConstantHeight.of(VerticalAnchor.absolute(-50)),
                UniformInt.of(45, 64)
        ));

        context.register(LAND_VOLCANO, new VolcanoStructure(
                new Structure.StructureSettings(
                        biomes.get(TropicraftTags.Biomes.HAS_LAND_VOLCANO).orElseThrow(),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                UniformInt.of(45, 64)
        ));
    }

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Tropicraft.location(name));
    }
}
