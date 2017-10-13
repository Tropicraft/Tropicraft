package net.tropicraft.core.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ExtendedPlayerStorage implements IStorage<PlayerDataInstance> {

    @Override
    public NBTBase writeNBT(Capability<PlayerDataInstance> capability, PlayerDataInstance instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        instance.writeNBT(nbt);
        return nbt;
    }

    @Override
    public void readNBT(Capability<PlayerDataInstance> capability, PlayerDataInstance instance, EnumFacing side,
            NBTBase nbt) {
        if (!(instance instanceof PlayerDataInstance) || !(nbt instanceof NBTTagCompound)) {
            return;
        }
        
        NBTTagCompound extTagCompound = (NBTTagCompound)nbt;
        instance.readNBT(extTagCompound);
    }

}
