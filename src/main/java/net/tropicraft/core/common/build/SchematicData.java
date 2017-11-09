package net.tropicraft.core.common.build;

import net.tropicraft.core.common.build.world.Build;
import net.minecraft.nbt.NBTTagCompound;

public interface SchematicData {
    void readFromNBT(NBTTagCompound par1NBTTagCompound, Build build);

    void writeToNBT(NBTTagCompound par1NBTTagCompound, Build build);
}
