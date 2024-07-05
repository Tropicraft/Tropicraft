package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IMachineBlock {

    boolean isActive();

    float getProgress(float partialTicks);

    Direction getDirection(BlockState state);
}
