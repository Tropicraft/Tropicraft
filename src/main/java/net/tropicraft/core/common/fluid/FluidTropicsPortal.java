package net.tropicraft.core.common.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.Info;

public class FluidTropicsPortal extends Fluid {

    public FluidTropicsPortal(String fluidName) {
		super(fluidName, new ResourceLocation(Info.MODID + ":blocks/tropics_portal_still"),
				new ResourceLocation(Info.MODID + ":blocks/tropics_portal_flowing"));
    }

}
