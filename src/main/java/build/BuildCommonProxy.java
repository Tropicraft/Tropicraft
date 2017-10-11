package build;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BuildCommonProxy implements IGuiHandler
{
    public BuildMod mod;

    public BuildCommonProxy()
    {
    }

    public void init(BuildMod pMod)
    {
        mod = pMod;
        //TickRegistry.registerTickHandler(new BuildServerTicks(), Side.SERVER);
        
        //pMod.itemEditTool = (ItemEditTool) (new ItemEditTool(5467, 1)).setUnlocalizedName("buildTool").setCreativeTab(CreativeTabs.tabMisc);
        //GameRegistry.registerItem(pMod.itemEditTool, "Build Tool");
        //LanguageRegistry.addName(pMod.itemEditTool, "Build Tool");
    }

    public void registerRenderInformation()
    {
    }

    public void registerTileEntitySpecialRenderer()
    {
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
}
