package build;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class BuildEventHandler {

	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
    public void worldRender(RenderWorldLastEvent event)
    {
		BuildClientTicks.i.worldRenderTick(event.partialTicks);
    }

	
}
