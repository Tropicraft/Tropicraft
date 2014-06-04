package net.tropicraft.event;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TCItemEvents {

	@SubscribeEvent
	public void handleBucketFillEvent(FillBucketEvent event) {
		System.out.println("initial trigger");
		ItemStack iHazBucket = new ItemStack(TCItemRegistry.bucketTropicsWater);

		World world = event.world;

		int x = event.target.blockX;
		int y = event.target.blockY;
		int z = event.target.blockZ;
		int meta = world.getBlockMetadata(x, y, z);

		Fluid fluid = FluidRegistry.lookupFluidForBlock(world.getBlock(x, y, z));

		if (fluid != null) {
			if (fluid == TCFluidRegistry.tropicsWater && meta == 0) {
				System.out.println("filled with glee");
				TCItemRegistry.bucketTropicsWater.fill(iHazBucket, new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME), true);

				world.setBlockToAir(x, y, z);

				event.result = iHazBucket;
				event.setResult(Result.ALLOW);
			}
		}
	}
}
