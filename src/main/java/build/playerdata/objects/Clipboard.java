package build.playerdata.objects;

import CoroUtil.util.CoroUtilFile;
import build.playerdata.IPlayerData;
import build.world.Build;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class Clipboard implements IPlayerData {

	public Build clipboardData;
	
	public Clipboard() {
		String path = CoroUtilFile.getSaveFolderPath() + "clipboard";
		File file = new File(path + ".schematic");
		if (file.exists()) {
			clipboardData = new Build(0, 0, 0, path, false);
		} else {
			clipboardData = new Build(0, 0, 0, "blank", true);
		}
		//clipboardData = new Build(0, 0, 0, "blank", true);
		clipboardData.newFormat = true;
	}
	
	@Override
	public void nbtLoad(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nbtSave(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

}
