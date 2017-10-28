package net.tropicraft.core.registry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.tropicraft.Info;
import net.tropicraft.Names;
import net.tropicraft.core.common.fluid.FluidTropicsPortal;
import net.tropicraft.core.common.fluid.FluidTropicsWater;

public class FluidRegistry {

	public static final Fluid tropicsWater = new FluidTropicsWater(TropicraftRegistry.getNamePrefixed(Names.TROPICS_WATER));
	public static final Fluid tropicsPortal = new FluidTropicsPortal(TropicraftRegistry.getNamePrefixed(Names.TROPICS_PORTAL));

	public static void preInit() {
		registerFluid(tropicsWater);
		registerFluid(tropicsPortal);
	}

	public static void init() {

	}

	/**
	 * Initialization, called after TCBlockRegistry.init so the blocks are not null when
	 * matched up with the fluids
	 */
	public static void postInit() {
		tropicsWater.setBlock(BlockRegistry.tropicsWater);
		tropicsPortal.setBlock(BlockRegistry.tropicsPortal);

		FluidRegistry.registerFluid(tropicsWater);
		FluidContainerRegistry.registerFluidContainer(tropicsWater, new ItemStack(ItemRegistry.tropicsWaterBucket));
	}

	private static void registerFluid(Fluid fluid) {
		net.minecraftforge.fluids.FluidRegistry.registerFluid(fluid);
	}
}
