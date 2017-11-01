package net.tropicraft.core.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerDataInstance {

    private EntityPlayer player;

    public boolean scubaFlippers;
    public boolean scubaChestgear;
    public boolean scubaHelmet;

    public int swimTimeCur = 0;
    public boolean receivedQuestReward = false;
    
    public PlayerDataInstance() {

    }
    
    public EntityPlayer getPlayer() {
        return player;
    }
    
    public PlayerDataInstance setPlayer(EntityPlayer player) {
        this.player = player;
        return this;
    }

    public void readNBT(NBTTagCompound nbt) {
        scubaFlippers = nbt.getBoolean("tcscubaFlippers");
        scubaChestgear = nbt.getBoolean("tcscubaChestgear");
        scubaHelmet = nbt.getBoolean("tcscubaHelmet");
        receivedQuestReward = nbt.getBoolean("receivedQuestReward");
        swimTimeCur = nbt.getInteger("swimTimeCur");
    }
    
    public void writeNBT(NBTTagCompound nbt) {
        nbt.setBoolean("tcscubaFlippers", scubaFlippers);
        nbt.setBoolean("tcscubaChestgear", scubaChestgear);
        nbt.setBoolean("tcscubaHelmet", scubaHelmet);
        nbt.setBoolean("receivedQuestReward", receivedQuestReward);
        nbt.setInteger("swimTimeCur", swimTimeCur);
    }
}
