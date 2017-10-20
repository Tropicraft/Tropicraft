package net.tropicraft.core.common.biome;

import net.tropicraft.core.common.biome.decorators.BiomeDecoratorTropicsOcean;
import net.tropicraft.core.common.entity.passive.EntityFailgull;
import net.tropicraft.core.common.entity.underdasea.EntityManOWar;
import net.tropicraft.core.common.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.core.common.entity.underdasea.EntitySeaUrchin;
import net.tropicraft.core.common.entity.underdasea.EntityStarfish;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityDolphin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityEagleRay;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntitySeahorse;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityShark;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicalFish;
import net.tropicraft.core.registry.BlockRegistry;

public class BiomeGenTropicsOcean extends BiomeGenTropicraft {

	public BiomeGenTropicsOcean(BiomeProperties bgprop) {
		super(bgprop);
		this.theBiomeDecorator = new BiomeDecoratorTropicsOcean();

        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeahorse.class, 6, 6, 12));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityMarlin.class, 10, 1, 4));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityManOWar.class, 2, 1, 1));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityStarfish.class, 4, 1, 4));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaUrchin.class, 4, 1, 4));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityDolphin.class, 3, 4, 7));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityShark.class, 1, 1, 3));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaTurtle.class, 6, 3, 8));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityFailgull.class, 30, 5, 15));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityTropicalFish.class, 20, 20, 40));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityEagleRay.class, 6, 2, 4));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityShark.class, 3, 1, 2));

        this.topBlock = this.fillerBlock = BlockRegistry.sands.getDefaultState();
	}

}
