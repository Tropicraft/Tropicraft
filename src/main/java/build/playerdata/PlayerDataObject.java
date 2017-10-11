package build.playerdata;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public class PlayerDataObject {

	public String username;
	public NBTTagCompound nbtData = null;
	public static HashMap<String, IPlayerData> playerData = new HashMap<String, IPlayerData>();
	
	public PlayerDataObject(String parUsername) {
		nbtData = new NBTTagCompound();
		username = parUsername;
	}
	
	public PlayerDataObject(String parUsername, NBTTagCompound nbt) {
		this(parUsername);
		nbtData = nbt;
	}
	
	public IPlayerData get(String objectName) {
		return playerData.get(objectName);
	}
	
	public void nbtLoad() {
		
	}
	
	public void nbtSave() {
		
	}
	
}
