package net.tropicraft.core.common.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityPiranha;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityRiverSardine;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;


public class BiomeGenTropicsRiver extends BiomeGenTropicraft {

	public BiomeGenTropicsRiver(BiomeProperties props) {
		super(props);
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();

        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityPiranha.class, 1, 3, 12));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityRiverSardine.class, 10, 10, 15));
	}

}
