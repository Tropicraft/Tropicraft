package build;

import build.config.BuildConfig;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import modconfig.ConfigMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(modid = "BuildMod", name = "Build Mod", version = "v1.0")

public class BuildMod
{

    @SidedProxy(clientSide = "build.BuildClientProxy", serverSide = "build.BuildCommonProxy")
    public static BuildCommonProxy proxy;
    
    public Configuration preInitConfig;
    
    public ItemEditTool itemEditTool;

	public static String eventChannelName = "buildmod";
	public static final FMLEventChannel eventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(eventChannelName);
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	ConfigMod.addConfigFile(event, "bm", new BuildConfig());
    	
    	eventChannel.register(new EventHandlerPacket());
    }
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        proxy.init(this);
        proxy.registerRenderInformation();
        
        MinecraftForge.EVENT_BUS.register(new BuildEventHandler());
        FMLCommonHandler.instance().bus().register(new EventHandlerFML());
    }

    @Mod.EventHandler
    public void modsLoaded(FMLPostInitializationEvent event)
    {
    	
    	//this is a test case to weed out duplicates in our attempted hash
    	//stop worrying about vanilla blocks, however they are a decent example
    	//enforce 100% unique unlocalized names for all mods you can control that in
    	//for the rest, try to use below
    	//implement into schematics as a mapping to use
    	//see if needs to be compressed
    	
    	HashMap<String, Integer> blockHash = new HashMap<String, Integer>();
    	List<String> fails = new ArrayList<String>();
    	
    	//System.out.println("TEST OUTPUT OF UNLOCALIZED NAMES! GO!");
        /*for (int i = Build.blockIDHighestVanilla+1; i < Block.blocksList.length; i++) {
        	Block block = Block.blocksList[i];
        	if (block != null) {
        		String hash;// = block.getClass().toString() + "|" + block.getUnlocalizedName() + "|" + Block.lightValue[i];
        		hash = block.getUnlocalizedName();
        		//System.out.println("ID: " + i + " - " + hash);
        		if (blockHash.containsKey(hash) && !block.getUnlocalizedName().equals("tile.ForgeFiller")) {
        			fails.add("ID: " + i + " vs " + blockHash.get(hash) + " - " + hash);
        		}
        		blockHash.put(hash, i);
        	}
        }
        
        
        System.out.println("BuildMod Name Hash Conflicts: " + fails.size());*/
        /*for (int i = 0; i < fails.size(); i++) {
        	System.out.println(fails.get(i));
        }*/
    }
    
    public static String lastWorldFolder = "";
    
    public static String getWorldFolderName() {
		World world = DimensionManager.getWorld(0);
		
		if (world != null) {
			lastWorldFolder = ((WorldServer)world).getChunkSaveLocation().getName();
			return lastWorldFolder + File.separator;
		}
		
		return lastWorldFolder + File.separator;
	}
	
	public static String getSaveFolderPath() {
    	if (MinecraftServer.getServer() == null || MinecraftServer.getServer().isSinglePlayer()) {
    		return getClientSidePath() + File.separator;
    	} else {
    		return new File(".").getAbsolutePath() + File.separator;
    	}
    	
    }
	
	public static String getWorldSaveFolderPath() {
    	if (MinecraftServer.getServer() == null || MinecraftServer.getServer().isSinglePlayer()) {
    		return getClientSidePath() + File.separator + "saves" + File.separator;
    	} else {
    		return new File(".").getAbsolutePath() + File.separator;
    	}
    	
    }
    
    @SideOnly(Side.CLIENT)
	public static String getClientSidePath() {
		return FMLClientHandler.instance().getClient().mcDataDir.getPath();
	}

	public static void dbg(String string) {
		System.out.println(string);
	}
    
}


