package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.worldgen.TCGenUtils;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTropicraftFence extends BlockFence {

	/** Class that this fence can connect to */
	private BlockFenceGate fenceGate;
	
	public BlockTropicraftFence(BlockFenceGate fenceGate, Material material, MapColor mapColor) {
		super(material, mapColor);
	}

	@Override
	public boolean canConnectTo(IBlockAccess world, BlockPos pos) {
		IBlockState state = TCGenUtils.getBlockState(world, pos);
		Block block = state.getBlock();
		if (block != this && block != BlockRegistry.bambooFenceGate && block != BlockRegistry.palmFenceGate) {
		    return block == Blocks.BARRIER ? false : ((!(block instanceof BlockFence) || state.getMaterial() != this.blockMaterial) && !(block instanceof BlockFenceGate) ? (state.getMaterial().isOpaque() && state.isFullCube() ? state.getMaterial() != Material.GOURD : false) : true);
		} else {
			return true;
		}
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}
}
