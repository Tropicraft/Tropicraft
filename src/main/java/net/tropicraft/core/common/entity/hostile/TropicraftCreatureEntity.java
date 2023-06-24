package net.tropicraft.core.common.entity.hostile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

public class TropicraftCreatureEntity extends PathfinderMob {

    public TropicraftCreatureEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK ? 10.0F : worldIn.getPathfindingCostFromLightLevels(pos);
    }
}
