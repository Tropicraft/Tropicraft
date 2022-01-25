package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class PortalWaterBlock extends LiquidBlock {

    public PortalWaterBlock(Properties builder) {
        super(() -> Fluids.WATER, builder);
    }
    
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        super.entityInside(state, worldIn, pos, entityIn);
        if (!worldIn.isClientSide && entityIn instanceof ServerPlayer && entityIn.getPortalWaitTime() <= 0 && !entityIn.isPassenger() && !entityIn.isPassenger() && entityIn.canChangeDimensions()) {
            TropicraftDimension.teleportPlayer((ServerPlayer) entityIn, TropicraftDimension.WORLD, true);
        }
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(2) == 0) {
            double d0 = pos.getX();
            double d1 = pos.getY();
            double d2 = pos.getZ();
            
            worldIn.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0 + 0.5D, d1, d2 + 0.5D, 0.0D, 0.04D, 0.0D);
            worldIn.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0D, 0.04D, 0.0D);
        }
    }
}
