package net.tropicraft.core.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ExtendedWorldStorage implements IStorage<WorldDataInstance> {

    @Override
    public NBTBase writeNBT(Capability<WorldDataInstance> capability, WorldDataInstance instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        instance.writeNBT(nbt);
        return nbt;
    }

    @Override
    public void readNBT(Capability<WorldDataInstance> capability, WorldDataInstance instance, EnumFacing side,
            NBTBase nbt) {
        if (!(instance instanceof WorldDataInstance) || !(nbt instanceof NBTTagCompound)) {
            return;
        }
        
        NBTTagCompound extTagCompound = (NBTTagCompound)nbt;
        instance.readNBT(extTagCompound);
    }

}
