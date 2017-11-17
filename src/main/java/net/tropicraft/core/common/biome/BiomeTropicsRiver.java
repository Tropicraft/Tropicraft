package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityPiranha;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityRiverSardine;
import net.tropicraft.core.registry.BlockRegistry;


public class BiomeTropicsRiver extends BiomeTropicraft {

	public BiomeTropicsRiver(BiomeProperties props) {
		super(props);
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityPiranha.class, 20, 1, 12));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityRiverSardine.class, 20, 1, 15));
        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}

}
