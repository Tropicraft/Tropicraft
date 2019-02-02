package net.tropicraft.core.common.donations;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class DonationData extends WorldSavedData {

    //now uses completedAt because tiltify id ordering unreliable
    private long lastSeenDate = 0;
    private int lastSeenId = 0;

    private int monumentsPlaced = 0;

    public DonationData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        lastSeenDate = nbt.getLong("lastSeenDate");
        lastSeenId = nbt.getInteger("lastSeenId");
        monumentsPlaced = nbt.getInteger("monumentsPlaced");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setLong("lastSeenDate", lastSeenDate);
        compound.setInteger("lastSeenId", lastSeenId);
        compound.setInteger("monumentsPlaced", monumentsPlaced);
        return compound;
    }
    
    public long getLastSeenDate() {
        return lastSeenDate;
    }
    
    public void setLastSeenDate(long id) {
        this.lastSeenDate = id;
        markDirty();
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
        lastSeenDate = 0;
        lastSeenId = 0;
        monumentsPlaced = 0;
    }
}
