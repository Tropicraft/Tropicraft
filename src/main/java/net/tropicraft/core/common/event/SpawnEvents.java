package net.tropicraft.core.common.event;

import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.dimension.WorldProviderTropicraft;
import net.tropicraft.core.common.spawning.TropiWorldEntitySpawner;

public class SpawnEvents {

    private TropiWorldEntitySpawner entitySpawner = new TropiWorldEntitySpawner();

    @SubscribeEvent
    public void canSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getWorld().provider instanceof WorldProviderTropicraft && !event.isSpawner()) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void tickServer(TickEvent.ServerTickEvent event) {
        WorldServer world = DimensionManager.getWorld(TropicraftWorldUtils.TROPICS_DIMENSION_ID);
        if (world != null) {
            if (world.getGameRules().getBoolean("doMobSpawning"))
            {
                boolean spawnHostileMobs = true;
                boolean spawnPeacefulMobs = true;
                entitySpawner.findChunksForSpawning(world, spawnHostileMobs, spawnPeacefulMobs, world.getWorldInfo().getWorldTotalTime() % 400L == 0L);
            }
        }
    }

}
