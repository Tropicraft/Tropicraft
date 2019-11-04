package CoroUtil.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundNBT;

public interface ISerializableNBT {

	public void read(CompoundNBT nbt);
	
	public CompoundNBT write(CompoundNBT nbt);
	
}
