package net.tropicraft.registry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.tropicraft.fluid.FluidTropicsPortal;
import net.tropicraft.fluid.FluidTropicsWater;

public class TCFluidRegistry {

	public static final Fluid tropicsWater = new FluidTropicsWater("tropicsWater");
	public static final Fluid tropicsPortal = new FluidTropicsPortal("tropicsPortal");
	
	/**
	 * Initialization, called before TCBlockRegistry.init
	 */
	public static void init() {
		registerFluid(tropicsWater);
		registerFluid(tropicsPortal);
	}
	
	/**
	 * Initialization, called after TCBlockRegistry.init so the blocks are not null when
	 * matched up with the fluids
	 */
	public static void postInit() {
		tropicsWater.setBlock(TCBlockRegistry.tropicsWater).setUnlocalizedName(TCBlockRegistry.tropicsWater.getUnlocalizedName());
		tropicsPortal.setBlock(TCBlockRegistry.tropicsPortal).setUnlocalizedName(TCBlockRegistry.tropicsPortal.getUnlocalizedName());

		FluidRegistry.registerFluid(tropicsWater);
		FluidContainerRegistry.registerFluidContainer(tropicsWater, new ItemStack(TCItemRegistry.bucketTropicsWater), new ItemStack(Items.bucket));
	}
	
	private static void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
	}

}
