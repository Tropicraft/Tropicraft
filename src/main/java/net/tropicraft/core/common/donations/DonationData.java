package net.tropicraft.core.common.donations;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class DonationData extends WorldSavedData {

    //public int lastIDReported = -1;
    public long lastDateReported = -1;

    public DonationData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        lastDateReported = nbt.getLong("lastDateReported");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setLong("lastDateReported", lastDateReported);
        return compound;
    }

    public void resetData() {
        lastDateReported = -1;
    }
}
