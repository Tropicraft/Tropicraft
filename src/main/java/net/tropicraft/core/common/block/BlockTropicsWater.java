package net.tropicraft.core.common.block;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
		this.displacements.put(BlockRegistry.coral, false);
		this.displacements.put(BlockRegistry.bambooFence, false);
		this.displacements.put(BlockRegistry.chunkFence, false);
        this.displacements.put(BlockRegistry.mahoganyFence, false);
        this.displacements.put(BlockRegistry.palmFence, false);
        this.displacements.put(BlockRegistry.thatchFence, false);

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

	/** These 5 methods below are a copypasta from BlockLiquid to patch the issue of tropics water pulling your down too much **/

	@Override
	@Nonnull
	public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion)
	{
		return motion.add(this.getFlow(worldIn, pos, worldIn.getBlockState(pos)));
	}

	protected Vec3d getFlow(IBlockAccess worldIn, BlockPos pos, IBlockState state)
	{
		double d0 = 0.0D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		int i = this.getRenderedDepth(state);
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
		{
			blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
			int j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos));

			if (j < 0)
			{
				if (!worldIn.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement())
				{
					j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos.down()));

					if (j >= 0)
					{
						int k = j - (i - 8);
						d0 += (double)(enumfacing.getFrontOffsetX() * k);
						d1 += (double)(enumfacing.getFrontOffsetY() * k);
						d2 += (double)(enumfacing.getFrontOffsetZ() * k);
					}
				}
			}
			else if (j >= 0)
			{
				int l = j - i;
				d0 += (double)(enumfacing.getFrontOffsetX() * l);
				d1 += (double)(enumfacing.getFrontOffsetY() * l);
				d2 += (double)(enumfacing.getFrontOffsetZ() * l);
			}
		}

		Vec3d vec3d = new Vec3d(d0, d1, d2);

		if (((Integer)state.getValue(LEVEL)).intValue() >= 8)
		{
			for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
			{
				blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing1);

				if (this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos, enumfacing1) || this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos.up(), enumfacing1))
				{
					vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
					break;
				}
			}
		}

		blockpos$pooledmutableblockpos.release();
		return vec3d.normalize();
	}

	protected int getDepth(IBlockState state)
	{
		return state.getMaterial() == this.blockMaterial ? ((Integer)state.getValue(LEVEL)).intValue() : -1;
	}

	protected int getRenderedDepth(IBlockState state)
	{
		int i = this.getDepth(state);
		return i >= 8 ? 0 : i;
	}

	/**
	 * Checks if an additional {@code -6} vertical drag should be applied to the entity. See {#link
	 * net.minecraft.block.BlockLiquid#getFlow()}
	 */
	private boolean causesDownwardCurrent(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		Material material = iblockstate.getMaterial();

		if (material == this.blockMaterial)
		{
			return false;
		}
		else if (side == EnumFacing.UP)
		{
			return true;
		}
		else if (material == Material.ICE)
		{
			return false;
		}
		else
		{
			boolean flag = isExceptBlockForAttachWithPiston(block) || block instanceof BlockStairs;
			return !flag && iblockstate.getBlockFaceShape(worldIn, pos, side) == BlockFaceShape.SOLID;
		}
	}
}
