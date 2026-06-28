package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CoolingLavaBlockEntity extends BlockEntity {

    private int coolingTime = 0;

    public CoolingLavaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static void coolingLavaTick(Level level, BlockPos pos, BlockState state, CoolingLavaBlockEntity lava) {
        lava.tick(level,pos,state);
    }

    private void tick(Level tickLevel,BlockPos pos, BlockState state){
        if (coolingTime < 200) coolingTime++;
        else if (!tickLevel.isClientSide)
            level.setBlock(pos, Blocks.AIR.defaultBlockState(),3);
    }


}
