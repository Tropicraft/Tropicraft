package net.tropicraft.event;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.tropicraft.util.TropicraftWorldUtils;
import CoroUtil.forge.CoroAI;
import CoroUtil.world.WorldDirector;
import CoroUtil.world.WorldDirectorManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TCMiscEvents {

    @SubscribeEvent
	public void worldLoad(Load event) {
		if (!event.world.isRemote) {
			if (((WorldServer)event.world).provider.dimensionId == TropicraftWorldUtils.TROPICS_DIMENSION_ID) {
				if (WorldDirectorManager.instance().getWorldDirector(CoroAI.modID, event.world) == null) {
					WorldDirectorManager.instance().registerWorldDirector(new WorldDirector(), CoroAI.modID, event.world);
				}
			}
		}
	}
}
