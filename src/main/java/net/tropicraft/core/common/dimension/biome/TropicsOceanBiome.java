package net.tropicraft.core.common.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.tropicraft.core.common.dimension.config.TropicsBuilderConfigs;

public class TropicsOceanBiome extends TropicraftBiome {
    public TropicsOceanBiome() {
        super(new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, TropicsBuilderConfigs.PURIFIED_SAND_CONFIG)
                .precipitation(RainType.RAIN)
                .category(Category.OCEAN)
                .depth(-1.0F)
                .scale(0.4F)
                .temperature(1.5F)
                .downfall(1.25F)
                .parent(null)
        );

        DefaultBiomeFeatures.addOceanCarvers(this);

//        this.addSpawn(new SpawnListEntry(EntitySeahorse.class, 6, 6, 12));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityMarlin.class, 10, 1, 4));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityManOWar.class, 2, 1, 1));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityStarfish.class, 4, 1, 4));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaUrchin.class, 4, 1, 4));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityDolphin.class, 3, 4, 7));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySeaTurtle.class, 6, 3, 8));
//        this.spawnableMonsterList.add(new SpawnListEntry(EntityFailgull.class, 30, 5, 15));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityTropicalFish.class, 20, 1, 1));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityEagleRay.class, 6, 2, 4));
//        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityShark.class, 3, 1, 3));
    }
}
