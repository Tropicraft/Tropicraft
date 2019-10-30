package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

public class PortalWaterBlock extends FlowingFluidBlock {

    public PortalWaterBlock(Properties builder) {
        super(() -> Fluids.WATER, builder);
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityCollision(state, worldIn, pos, entityIn);
        if (!worldIn.isRemote && entityIn instanceof ServerPlayerEntity && entityIn.timeUntilPortal <= 0 && !entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.isNonBoss()) {
            TropicraftWorldUtils.teleportPlayer((ServerPlayerEntity) entityIn, TropicraftWorldUtils.TROPICS_DIMENSION);
        }
    }
    
    @Override
    public boolean reactWithNeighbors(World worldIn, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(2) == 0) {
            double d0 = (double)pos.getX();
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ();
            
            worldIn.addOptionalParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0 + 0.5D, d1, d2 + 0.5D, 0.0D, 0.04D, 0.0D);
            worldIn.addOptionalParticle(ParticleTypes.BUBBLE_COLUMN_UP, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0D, 0.04D, 0.0D);
        }
    }
}
