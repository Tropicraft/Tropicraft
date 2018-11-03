package net.tropicraft.core.common.donations;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class DonationData extends WorldSavedData {

    //public int lastIDReported = -1;
    private int lastSeenId = 0;

    public DonationData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        lastSeenId = nbt.getInteger("lastSeenId");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("lastSeenId", lastSeenId);
        return compound;
    }
    
    public int getLastSeenId() {
        return lastSeenId;
    }
    
    public void setLastSeenId(int id) {
        this.lastSeenId = id;
        markDirty();
    }

    public void resetData() {
        lastSeenId = 0;
    }
}
