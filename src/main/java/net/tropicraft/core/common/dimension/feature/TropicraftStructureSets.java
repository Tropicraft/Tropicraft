package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;

import java.util.List;

public final class TropicraftStructureSets {
    public final Holder<StructureSet> homeTrees;
    public final Holder<StructureSet> koaVillages;

    public TropicraftStructureSets(WorldgenDataConsumer<StructureSet> worldgen, TropicraftConfiguredStructures structures) {
        Register structureSets = new Register(worldgen);

        this.homeTrees = structureSets.register("home_trees", structures.homeTree, new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 1010101010));
        this.koaVillages = structureSets.register("koa_village", structures.koaVillage, new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.LINEAR, 1010101011));
    }

    static final class Register {
        private final WorldgenDataConsumer<StructureSet> worldgen;

        Register(WorldgenDataConsumer<StructureSet> worldgen) {
            this.worldgen = worldgen;
        }

        public Holder<StructureSet> register(String id, Holder<ConfiguredStructureFeature<?, ?>> structure, StructurePlacement placement) {
            return register(id, List.of(new StructureSet.StructureSelectionEntry(structure, 1)), placement);
        }

        public Holder<StructureSet> register(String id, List<StructureSet.StructureSelectionEntry> entries, StructurePlacement placement) {
            return worldgen.register(new ResourceLocation(Constants.MODID, id), new StructureSet(entries, placement));
        }
    }
}
