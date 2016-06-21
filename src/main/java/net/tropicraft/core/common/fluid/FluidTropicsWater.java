package net.tropicraft.core.common.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.Info;

public class FluidTropicsWater extends Fluid {

	public FluidTropicsWater(String fluidName) {
		super(fluidName, new ResourceLocation(Info.MODID + ":blocks/tropics_water_still"),
				new ResourceLocation(Info.MODID + ":blocks/tropics_water_flowing"));
	}

}
