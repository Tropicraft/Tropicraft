package extendedrenderer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
    public World mainWorld;
    private int entityId = 0;

    public ExtendedRenderer mod;

    public CommonProxy()
    {
    }

    public void preInit(ExtendedRenderer pMod)
    {
    	
    }
    
    public void postInit(ExtendedRenderer pMod)
    {
    	
    }
    
    public void init(ExtendedRenderer pMod)
    {
        mod = pMod;
        
    	
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

}
