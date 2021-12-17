package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class PortalWaterBlock extends LiquidBlock {
    public PortalWaterBlock(Properties pProperties) {
        super(() -> Fluids.WATER, pProperties);
    }
}
