package build;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class EventHandlerFML {
	
	@SubscribeEvent
	public void tickServer(ServerTickEvent event) {
		
		if (event.phase == Phase.START) {
			BuildServerTicks.onTickInGame();
		}
		
	}
	
	@SubscribeEvent
	public void tickRender(RenderTickEvent event) {
		if (FMLClientHandler.instance().getClient().thePlayer != null) {
			if (event.phase == Phase.END) {
				BuildClientTicks.i.onRenderTick();
			}
		}
	}
	
	@SubscribeEvent
	public void tickClient(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			BuildClientTicks.i.onTickInGame();
		}
	}
}
