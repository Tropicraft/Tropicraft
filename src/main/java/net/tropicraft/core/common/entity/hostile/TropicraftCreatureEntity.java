package net.tropicraft.core.common.entity.hostile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class TropicraftCreatureEntity extends CreatureEntity {

    public TropicraftCreatureEntity(EntityType<? extends CreatureEntity> p_i48575_1_, World p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    @Override
    public float getBlockPathWeight(BlockPos p_205022_1_, IWorldReader p_205022_2_) {
        return p_205022_2_.getBlockState(p_205022_1_.down()).getBlock() == Blocks.GRASS_BLOCK ? 10.0F : p_205022_2_.getBrightness(p_205022_1_) - 0.5F;
    }
}
