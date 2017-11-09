package net.tropicraft.core.common;

import net.tropicraft.core.common.build.BuildServerTicks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BuildEvents {

    @SubscribeEvent
    public void tickServer(TickEvent.ServerTickEvent event) {

        if (event.phase == TickEvent.Phase.START) {
            BuildServerTicks.onTickInGame();
        }

    }

}
