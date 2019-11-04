package CoroUtil.util;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CoroUtilFile {
	public static String lastWorldFolder = "";
    
	public static CompoundNBT getExtraWorldNBT(String fileName) {
		CompoundNBT data = new CompoundNBT();
		//try load
		
		String saveFolder = getWorldSaveFolderPath() + getWorldFolderName();
		
		if ((new File(saveFolder + fileName)).exists()) {
			try {
				data = CompressedStreamTools.readCompressed(new FileInputStream(saveFolder + fileName));
			} catch (Exception ex) {
				System.out.println("CoroUtilFile: getExtraWorldNBT: Error loading " + saveFolder + fileName);
			}
			
			//NBTTagList var14 = gameData.getList("playerData");
		}
		
		return data;
	}
	
	public static void setExtraWorldNBT(String fileName, CompoundNBT data) {
		try {
    		
    		String saveFolder = getWorldSaveFolderPath() + getWorldFolderName();
    		
    		//Write out to file
    		FileOutputStream fos = new FileOutputStream(saveFolder + fileName);
	    	CompressedStreamTools.writeCompressed(data, fos);
	    	fos.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//this must be used while server is active
    public static String getWorldFolderName() {
		World world = getWorld(0);
		
		if (world != null) {
			lastWorldFolder = ((ServerWorld)world).getSaveHandler().getWorldDirectory().getPath();
			return lastWorldFolder + File.separator;
		}
		
		return lastWorldFolder + File.separator;
	}

	public static ServerWorld getWorld(int dimID) {
		return DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), DimensionType.getById(dimID), true, true);
	}
	
	public static String getSaveFolderPath() {
    	if (ServerLifecycleHooks.getCurrentServer() == null || ServerLifecycleHooks.getCurrentServer().isSinglePlayer()) {
    		return getClientSidePath() + File.separator;
    	} else {
    		return new File(".").getAbsolutePath() + File.separator;
    	}
    	
    }
	
	public static String getMinecraftSaveFolderPath() {
    	if (ServerLifecycleHooks.getCurrentServer() == null || ServerLifecycleHooks.getCurrentServer().isSinglePlayer()) {
    		return getClientSidePath() + File.separator + "config" + File.separator;
    	} else {
    		return new File(".").getAbsolutePath() + File.separator + "config" + File.separator;
    	}
    }
	
	public static String getWorldSaveFolderPath() {
    	if (ServerLifecycleHooks.getCurrentServer() == null || ServerLifecycleHooks.getCurrentServer().isSinglePlayer()) {
    		return getClientSidePath() + File.separator + "saves" + File.separator;
    	} else {
    		return new File(".").getAbsolutePath() + File.separator;
    	}
    }
    
    @OnlyIn(Dist.CLIENT)
	public static String getClientSidePath() {
		return Minecraft.getInstance().gameDir/*getAppDir("minecraft")*/.getPath();
	}
    
    public static void writeCoords(String name, BlockCoord coords, CompoundNBT nbt) {
    	nbt.putInt(name + "X", coords.posX);
    	nbt.putInt(name + "Y", coords.posY);
    	nbt.putInt(name + "Z", coords.posZ);
    }
    
    public static BlockCoord readCoords(String name, CompoundNBT nbt) {
    	if (nbt.contains(name + "X")) {
    		return new BlockCoord(nbt.getInt(name + "X"), nbt.getInt(name + "Y"), nbt.getInt(name + "Z"));
    	} else {
    		return null;
    	}
    }

	//TODO: 1.14 uncomment
    /*@OnlyIn(Dist.CLIENT)
    public static String getContentsFromResourceLocation(ResourceLocation resourceLocation) {
		try {
			IResourceManager resourceManager = Minecraft.getInstance().gameRenderer.resourceManager;
			IResource iresource = resourceManager.getResource(resourceLocation);
			String contents = IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8);
			return contents;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}*/
}
