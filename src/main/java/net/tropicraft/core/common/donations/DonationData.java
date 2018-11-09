package net.tropicraft.core.common.donations;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class DonationData extends WorldSavedData {

    private int lastSeenId = 0;

    private int monumentsPlaced = 0;

    public DonationData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        lastSeenId = nbt.getInteger("lastSeenId");
        monumentsPlaced = nbt.getInteger("monumentsPlaced");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("lastSeenId", lastSeenId);
        compound.setInteger("monumentsPlaced", monumentsPlaced);
        return compound;
    }
    
    public int getLastSeenId() {
        return lastSeenId;
    }
    
    public void setLastSeenId(int id) {
        this.lastSeenId = id;
        markDirty();
    }

    public int getMonumentsPlaced() {
        return monumentsPlaced;
    }

    public void setMonumentsPlaced(int monumentsPlaced) {
        this.monumentsPlaced = monumentsPlaced;
    }

    public void resetData() {
        lastSeenId = 0;
        monumentsPlaced = 0;
    }
}
