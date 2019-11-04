package net.tropicraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

//@Mod.EventBusSubscriber(modid = Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandlerForge {

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        System.out.println("MAP!!!" + event.getMap().getBasePath());
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        System.out.println("SERVER STARTING!!!!!!!!!!");
    }

}
