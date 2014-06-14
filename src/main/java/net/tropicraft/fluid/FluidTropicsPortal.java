package net.tropicraft.fluid;

import net.minecraftforge.fluids.Fluid;
import net.tropicraft.block.BlockTropicsWater;

public class FluidTropicsPortal extends Fluid {

    public FluidTropicsPortal(String fluidName) {
        super(fluidName);
        this.setIcons(BlockTropicsWater.stillIcon, BlockTropicsWater.flowingIcon);
    }

}
