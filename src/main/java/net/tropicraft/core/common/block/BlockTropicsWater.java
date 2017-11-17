package net.tropicraft.core.common.block;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.CreateFluidSourceEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTropicsWater extends BlockFluidClassic {

	public BlockTropicsWater(Fluid fluid, Material material) {
		super(fluid, material);
		this.lightOpacity = 0;
		this.setCreativeTab(null);
		this.displacements.put(BlockRegistry.coral, Boolean.valueOf(false));
		MinecraftForge.EVENT_BUS.register(this);
		//TODO: this.displacements.put(BlockRegistry.bambooFence, Boolean.valueOf(false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}
	
	@SubscribeEvent
	public void onCreateFluidSource(CreateFluidSourceEvent event) {
	    if (event.getState().getBlock() == this) {
	        event.setResult(Result.ALLOW);
	    }
	}
	
	@Override
	public int getQuantaValue(IBlockAccess world, BlockPos pos) {
		int ret = super.getQuantaValue(world, pos);
		IBlockState state = world.getBlockState(pos);
		return Optional.ofNullable((Integer) state.getProperties().get(BlockFluidBase.LEVEL))
				.map(i -> quantaPerBlock - i)
				.orElse(ret);
	}
}
