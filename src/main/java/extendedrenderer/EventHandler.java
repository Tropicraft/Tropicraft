package extendedrenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.ParticleRegistry;

public class EventHandler {

	
	public long lastWorldTime;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
    public void worldRender(RenderWorldLastEvent event)
    {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.theWorld != null && mc.theWorld.getWorldInfo().getWorldTime() != lastWorldTime)
        {
            lastWorldTime = mc.theWorld.getWorldInfo().getWorldTime();

            if (!isPaused())
            {
                ExtendedRenderer.rotEffRenderer.updateEffects();
            }
        }

        //Rotating particles hook
        ExtendedRenderer.rotEffRenderer.renderParticles((Entity)mc.renderViewEntity, (float)event.partialTicks);
    }
	
	@SideOnly(Side.CLIENT)
    public boolean isPaused() {
    	//if (FMLClientHandler.instance().getClient().getIntegratedServer() != null && FMLClientHandler.instance().getClient().getIntegratedServer().getServerListeningThread() != null && FMLClientHandler.instance().getClient().getIntegratedServer().getServerListeningThread().isGamePaused()) return true;
    	return false;
    }
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerIcons(TextureStitchEvent event) {
		ParticleRegistry.init(event);
	}

	
}
