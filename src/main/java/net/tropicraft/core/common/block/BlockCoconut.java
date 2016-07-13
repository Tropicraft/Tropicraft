package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCoconut extends BlockTropicraft {

	protected static final AxisAlignedBB COCONUT_AABB = new AxisAlignedBB(0.275D, 0.225D, 0.275D, 0.725D, 0.775, 0.725);
	
	public BlockCoconut() {
		super(Material.GOURD);
		this.setHardness(2.0F);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return COCONUT_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
		return COCONUT_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * NOTE: Behavior overridden in BlockEvents
	 */
	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

}
