package net.tropicraft.core.common.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityPiranha;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityRiverSardine;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;
import net.tropicraft.core.registry.BlockRegistry;


public class BiomeGenTropicsRiver extends BiomeGenTropicraft {

	public BiomeGenTropicsRiver(BiomeProperties props) {
		super(props);
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityPiranha.class, 1, 3, 12));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityRiverSardine.class, 10, 10, 15));
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}

}
