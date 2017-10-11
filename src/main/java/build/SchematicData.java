package build;

import build.world.Build;
import net.minecraft.nbt.NBTTagCompound;

public interface SchematicData {
	void readFromNBT(NBTTagCompound par1NBTTagCompound, Build build);
	void writeToNBT(NBTTagCompound par1NBTTagCompound, Build build);
}
