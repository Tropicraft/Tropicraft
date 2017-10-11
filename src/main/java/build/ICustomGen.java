package build;

import build.world.BuildJob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface ICustomGen {
	/* Method used for injecting custom generation after any pass, first pass being setting air, when complete, it sends with pass value of -1 */
	public void genPassPre(World world, BuildJob parBuildJob, int parPass);
	
	public NBTTagCompound getInitNBTTileEntity();
}
