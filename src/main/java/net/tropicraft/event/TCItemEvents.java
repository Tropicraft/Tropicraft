package net.tropicraft.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TCItemEvents {

	@SubscribeEvent
	public void handleBucketFillEvent(FillBucketEvent event) {
		ItemStack iHazBucket = new ItemStack(TCItemRegistry.bucketTropicsWater);

		World world = event.world;

		int x = event.target.blockX;
		int y = event.target.blockY;
		int z = event.target.blockZ;
		int meta = world.getBlockMetadata(x, y, z);

		Fluid fluid = FluidRegistry.lookupFluidForBlock(world.getBlock(x, y, z));

		if (fluid != null) {
			if (fluid == TCFluidRegistry.tropicsWater && meta == 0) {
				TCItemRegistry.bucketTropicsWater.fill(iHazBucket, new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME), true);

				world.setBlockToAir(x, y, z);

				event.result = iHazBucket;
				event.setResult(Result.ALLOW);
			}
		}
	}
	
	@SubscribeEvent
	public void changeTropicsWaterFogDensity(EntityViewRenderEvent.FogDensity event) {
	    int x = MathHelper.floor_double(event.entity.posX);
	    int y = MathHelper.ceiling_double_int(event.entity.posY + event.entity.height - 0.5F);
	    int z = MathHelper.floor_double(event.entity.posZ);
	    
	  //  System.out.println("hi");
	    
	    if (event.block.getMaterial().isLiquid()) {
	        event.density = 0.0005F;
	        event.setCanceled(true);
	       // System.out.println("3hi");
	    }
	}
}
