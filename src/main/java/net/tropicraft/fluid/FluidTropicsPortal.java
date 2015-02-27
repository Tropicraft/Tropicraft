package net.tropicraft.fluid;

import net.minecraftforge.fluids.Fluid;
import net.tropicraft.block.BlockTropicsWater;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class FluidTropicsPortal extends Fluid {

    public FluidTropicsPortal(String fluidName) {
        super(fluidName);
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
        	this.setIcons(BlockTropicsWater.stillIcon, BlockTropicsWater.flowingIcon);
        }
    }

}
