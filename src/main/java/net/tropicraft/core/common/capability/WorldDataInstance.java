package net.tropicraft.core.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class WorldDataInstance {

    private World world;

    public int test = 0;

    public WorldDataInstance() {

    }

    public World getWorld() {
        return world;
    }

    public WorldDataInstance setWorld(World world) {
        this.world = world;
        return this;
    }

    public void readNBT(NBTTagCompound nbt) {
        test = nbt.getInteger("test");
    }
    
    public void writeNBT(NBTTagCompound nbt) {
        nbt.setInteger("test", test);
    }
}
