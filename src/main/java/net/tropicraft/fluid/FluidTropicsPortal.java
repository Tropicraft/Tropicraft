package net.tropicraft.fluid;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.block.BlockTropicsWater;

public class FluidTropicsPortal extends Fluid {

    public FluidTropicsPortal(String fluidName) {
        super(fluidName);
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
        	this.setIcons(BlockTropicsWater.stillIcon, BlockTropicsWater.flowingIcon);
        }
    }

}
