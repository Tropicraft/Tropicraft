package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockCoral extends BlockTropicraftEnumVariants<TropicraftCorals> implements ITropicraftBlock, net.minecraftforge.common.IPlantable {

	/** Brightness value of coral blocks during the day */
	private static final float BRIGHTNESS_DAY = 0.3F;

	/** Brightness value of coral blocks during the night */
	private static final float BRIGHTNESS_NIGHT = 0.6F;

	public BlockCoral() {
		super(Material.WATER, TropicraftCorals.class);
		setLightLevel(0.3F);
		setHardness(0.0F);
		setTickRandomly(true);
		this.setSoundType(SoundType.SAND);
	}
	
	@Override
	protected IProperty<?>[] getAdditionalProperties() {
	    return new IProperty[] { BlockFluidBase.LEVEL };
	}

	/**
	 * Indicate if a material is a normal solid opaque cube
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.XZ;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return (int) (15.0F * BRIGHTNESS_NIGHT);
	}

	/**
	 * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
	 */
	public boolean canBlockStay(World world, BlockPos pos) {
		return (world.getBlockState(pos).getMaterial().isLiquid() && 
				world.getBlockState(pos.up()).getMaterial().isLiquid()) &&
				canThisPlantGrowOnThisBlock(world.getBlockState(pos.down()).getBlock());
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && world.getBlockState(pos).getBlock() != this && 
				canThisPlantGrowOnThisBlock(world.getBlockState(pos.down()).getBlock()) && 
				world.getBlockState(pos).getMaterial() == Material.WATER && 
				world.getBlockState(pos.up()).getMaterial() == Material.WATER;
	}

	public boolean canThisPlantGrowOnThisBlock(Block b) {
		return b == Blocks.GRASS || b == Blocks.DIRT || b == Blocks.SAND || b == BlockRegistry.seaweed || b == BlockRegistry.sands; /* TODO: || b == TCBlockRegistry.purifiedSand || b == TCBlockRegistry.mineralSands;*/
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return net.minecraftforge.common.EnumPlantType.Water;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return this.getDefaultState();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		checkFlowerChange(world, pos, state);
		//System.out.println("Block update");
		/*if(!world.isRemote && world.getLoadedEntityList().size() < 200) {
			if(rand.nextInt(12) == 0) {
				EntityPiranha fish = new EntityPiranha(world);
				fish.setPosition(pos.getX(), pos.getY(), pos.getZ());
				world.spawnEntity(fish);
				fish.markAsLeader();
				int amt = rand.nextInt(12);
				for(int i =0 ; i < amt; i++) {
					EntityPiranha fishe = new EntityPiranha(fish);
					fishe.setPosition(pos.getX(), pos.getY(), pos.getZ());
					world.spawnEntity(fishe);
				}
			}
		}*/
	}

	protected void checkFlowerChange(World world, BlockPos pos, IBlockState state) {
		if (!canBlockStay(world, pos)) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}
}
