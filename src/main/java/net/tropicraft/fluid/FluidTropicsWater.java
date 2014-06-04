package net.tropicraft.fluid;

import net.minecraftforge.fluids.Fluid;
import net.tropicraft.block.BlockTropicsWater;

public class FluidTropicsWater extends Fluid {

	public FluidTropicsWater(String fluidName) {
		super(fluidName);
		this.setIcons(BlockTropicsWater.stillIcon, BlockTropicsWater.flowingIcon);
	}

}
