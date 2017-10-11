package build.playerdata;

import build.BuildMod;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class PlayerData {

	//this is the main class to handle binding a PlayerData object instance to a username
	//possible creation/implementation of interfaces ISavable & ISyncable
	//IPlayerData: implement on class you want to add, has nbt read and write overrides
	//ISyncable: handles sending the needed data to client, and maybe helps setup dummy client object for info
	
	//PlayerData -> list of PlayerDataObjects -> list of IPlayerData implemented classes
		
	//the method of lookup might require some caching to prevent hashmap lookup overkill
	
	public static HashMap<String, PlayerDataObject> playerData = new HashMap<String, PlayerDataObject>();
	
	public PlayerData() {
		
	}
	
	public static void initObjects(PlayerDataObject pdo) {
		//pdo.playerData.put("clipboard", new Clipboard());
		
		pdo.nbtLoad();
	}
	
	public static IPlayerData get(String username, String objectName) {
		return get(username).get(objectName);
	}
	
	public static PlayerDataObject get(String username) {
		if (!playerData.containsKey(username)) {
			NBTTagCompound nbt = tryLoadPlayerData(username);
			PlayerDataObject pd = new PlayerDataObject(username, nbt);
			initObjects(pd);
			playerData.put(username, pd);
		}
		return playerData.get(username);
	}
	
	public static NBTTagCompound tryLoadPlayerData(String username) {
		//init with data, if fail, init default blank
		
		NBTTagCompound playerNBT = new NBTTagCompound();
		
		boolean firstTimeInit = false;
		
		try {
			String fileURL = BuildMod.getWorldSaveFolderPath() + BuildMod.getWorldFolderName() + File.separator + "BMPlayerData" + File.separator + username + ".dat";
			
			if ((new File(fileURL)).exists()) {
				playerNBT = CompressedStreamTools.readCompressed(new FileInputStream(fileURL));
			} else {
				BuildMod.dbg("no saved data found for " + username);
				firstTimeInit = true;
			}
		} catch (Exception ex) {
			BuildMod.dbg("error trying to load data for " + username);
			firstTimeInit = true;
		}
		
		if (firstTimeInit) playerNBT.setBoolean("newPlayer", true); //it will be up to whatever ticks this stuff to unset this
		
		return playerNBT;
	}
}
