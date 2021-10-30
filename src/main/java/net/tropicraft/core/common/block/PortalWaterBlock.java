package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class PortalWaterBlock extends FlowingFluidBlock {

    public PortalWaterBlock(Properties builder) {
        super(() -> Fluids.WATER, builder);
    }
    
    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        super.entityInside(state, worldIn, pos, entityIn);
        if (!worldIn.isClientSide && entityIn instanceof ServerPlayerEntity && entityIn.getPortalWaitTime() <= 0 && !entityIn.isPassenger() && !entityIn.isPassenger() && entityIn.canChangeDimensions()) {
            TropicraftDimension.teleportPlayer((ServerPlayerEntity) entityIn, TropicraftDimension.WORLD);
        }
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(2) == 0) {
            double d0 = pos.getX();
            double d1 = pos.getY();
            double d2 = pos.getZ();
            
            worldIn.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0 + 0.5D, d1, d2 + 0.5D, 0.0D, 0.04D, 0.0D);
            worldIn.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0D, 0.04D, 0.0D);
        }
    }
}
