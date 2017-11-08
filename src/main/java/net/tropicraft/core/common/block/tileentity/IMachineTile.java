package net.tropicraft.core.common.block.tileentity;

import net.minecraft.util.EnumFacing;

public interface IMachineTile {
    
    boolean isActive();
    
    int getProgress();
    
    EnumFacing getFacing();

}
