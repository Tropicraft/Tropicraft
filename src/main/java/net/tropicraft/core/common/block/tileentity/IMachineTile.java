package net.tropicraft.core.common.block.tileentity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;

public interface IMachineTile {
    
    boolean isActive();
    
    float getProgress(float partialTicks);
    
    Direction getDirection(BlockState state);

}
