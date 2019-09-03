package net.tropicraft.core.common.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public interface IMachineTile {
    
    boolean isActive();
    
    float getProgress(float partialTicks);
    
    Direction getDirection(BlockState state);

}
