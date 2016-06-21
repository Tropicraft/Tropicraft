package net.tropicraft.core.common.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.core.common.block.BlockTropicsWater;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class ItemEvents {

	@SubscribeEvent
	public void handleBucketFillEvent(FillBucketEvent event) {
		if (event.getEmptyBucket().getItem() != Items.BUCKET) return;
		if (event.getTarget() == null || event.getTarget().typeOfHit != RayTraceResult.Type.BLOCK) return;

		World world = event.getWorld();

		BlockPos pos = event.getTarget().getBlockPos();
		IBlockState iblockstate = event.getWorld().getBlockState(pos);

		if (!event.getWorld().isBlockModifiable(event.getEntityPlayer(), pos)) return;
		if (!event.getEntityPlayer().canPlayerEdit(pos.offset(event.getTarget().sideHit), event.getTarget().sideHit, event.getEmptyBucket())) return;

		if (iblockstate.getBlock() == BlockRegistry.tropicsWater && ((Integer)iblockstate.getValue(BlockTropicsWater.LEVEL)).intValue() == 0) {
			ItemStack iHazBucket = new ItemStack(ItemRegistry.tropicsWaterBucket);
			event.setResult(Result.ALLOW);
			event.setFilledBucket(iHazBucket);
			event.getWorld().setBlockToAir(pos);
		}
	}

}
