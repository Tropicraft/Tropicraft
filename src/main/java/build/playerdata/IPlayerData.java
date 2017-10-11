package build.playerdata;

import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerData {

	public void nbtLoad(NBTTagCompound nbt);
	
	public void nbtSave(NBTTagCompound nbt);
	
}
