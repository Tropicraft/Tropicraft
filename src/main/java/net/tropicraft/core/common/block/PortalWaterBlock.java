package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.function.Supplier;

public class PortalWaterBlock extends LiquidBlock {
    public PortalWaterBlock(Properties pProperties) {
        super(() -> Fluids.WATER, pProperties);
    }
}
