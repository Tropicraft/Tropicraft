package net.tropicraft.core.common.biome;

import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropics;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrog;

public class BiomeTropics extends BiomeTropicraft {

	public BiomeTropics(BiomeProperties props) {
		super(props);
		this.decorator = new BiomeDecoratorTropics();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityTreeFrog.class, 25, 2, 5));
	}

}
