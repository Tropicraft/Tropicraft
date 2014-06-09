package extendedrenderer;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.entity.EntityRotFX;
import extendedrenderer.particle.entity.EntityTexBiomeColorFX;
import extendedrenderer.particle.entity.EntityTexFX;
import extendedrenderer.render.RenderNull;
import extendedrenderer.render.RotatingEffectRenderer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    public static Minecraft mc;

    public ClientProxy()
    {
        mc = FMLClientHandler.instance().getClient();
    }
    
    @Override
    public void preInit(ExtendedRenderer pMod)
    {
    	super.preInit(pMod);
    }
    
    @Override
    public void postInit(ExtendedRenderer pMod)
    {
    	super.postInit(pMod);
    	ExtendedRenderer.rotEffRenderer = new RotatingEffectRenderer(mc.theWorld, mc.renderEngine);
    }

    @Override
    public void init(ExtendedRenderer pMod)
    {
        super.init(pMod);
        //rr.registerEntityRenderingHandler(StormCluster.class, new RenderNull());
        RenderingRegistry.registerEntityRenderingHandler(EntityTexFX.class, new RenderNull());
        RenderingRegistry.registerEntityRenderingHandler(EntityTexBiomeColorFX.class, new RenderNull());
        RenderingRegistry.registerEntityRenderingHandler(EntityRotFX.class, new RenderNull());
        //rr.registerEntityRenderingHandler(EntityFallingRainFX.class, new RenderNull());
        //rr.registerEntityRenderingHandler(EntityWaterfallFX.class, new RenderNull());
        //rr.registerEntityRenderingHandler(EntitySnowFX.class, new RenderNull());
    }
    
}
