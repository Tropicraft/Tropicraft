package net.tropicraft.core.common.block.scuba;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraft;
import net.tropicraft.core.common.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;
import net.tropicraft.core.common.enums.BlockHardnessValues;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockAirCompressor extends BlockTropicraft implements ITileEntityProvider {

	@Nonnull
    public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;

    public BlockAirCompressor() {
		super(Material.ROCK);
		this.setHardness(2.0F);
		this.setResistance(BlockHardnessValues.CHUNK.resistance);
		//this.setBlockBounds(0, 0, 0, 1, 1.8F, 1);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }


    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

	@Override
	 public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}

		ItemStack stack = playerIn.getHeldItemMainhand();

		TileEntityAirCompressor mixer = (TileEntityAirCompressor)world.getTileEntity(pos);

		if (mixer.isDoneCompressing()) {
			mixer.ejectTank();
			return true;
		}

		if (stack.isEmpty()) {
			mixer.ejectTank();
			return true;
		}

		ItemStack ingredientStack = stack.copy();
		ingredientStack.setCount(1);

		if (mixer.addTank(ingredientStack)) {
			playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
		}

		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			TileEntityAirCompressor te = (TileEntityAirCompressor) world.getTileEntity(pos);
			te.ejectTank();
		}

		super.breakBlock(world, pos, state);
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World world) {
		return 4;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {

		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return TileEntityFactory.getAirCompressorTE();
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState ret = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return ret.withProperty(FACING, placer.getHorizontalFacing());
    }
	
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockRegistry.airCompressor);
    }
}
