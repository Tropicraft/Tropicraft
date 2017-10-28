package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTikiTorch extends BlockTropicraft implements ITropicraftBlock {

	public static enum TorchSection implements IStringSerializable {
		UPPER, MIDDLE, LOWER;

		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
		@Override
		public String toString() {
			return this.getName();
		}
	};

	public static final PropertyEnum<TorchSection> SECTION = PropertyEnum.create("section", TorchSection.class);

	protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.999999D, 0.6000000238418579D);
	protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);

	public BlockTikiTorch() {
		super(Material.CIRCUITS);
		this.setTickRandomly(true);
		this.setCreativeTab(null);

		this.lightValue = (int)(15.0F);

		this.setDefaultState(this.blockState.getBaseState().withProperty(SECTION, TorchSection.UPPER));
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		TorchSection section = (TorchSection)state.getValue(SECTION);

		if (section == TorchSection.UPPER) {
			return TOP_AABB;
		} else {
			return BASE_AABB;
		}
	}


	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return canPlaceTikiTorchOn(world, pos.down()) && world.isAirBlock(pos.up()) && world.isAirBlock(pos.up(2)) ;
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(BlockRegistry.tikiTorch);
	}

	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
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

	private boolean canPlaceTikiTorchOn(World world, BlockPos pos) {
		if (world.isBlockNormalCube(pos, false)) {
			return true;
		} else {
			IBlockState state = world.getBlockState(pos);
			// Can't place on air
			if (world.isAirBlock(pos)) {
				return false;
			}

			// If trying to place on a tiki torch already
			if (state.getBlock() == this &&
					(((TorchSection)world.getBlockState(pos).getValue(SECTION)) == TorchSection.MIDDLE ||
					((TorchSection)world.getBlockState(pos).getValue(SECTION)) == TorchSection.LOWER)) {
				return true;
			}

			return state.getBlock().canPlaceTorchOnTop(state, world, pos);
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if (state.getBlock() != this) return null;

		if (state.getValue(SECTION) == TorchSection.UPPER || state.getValue(SECTION) == TorchSection.MIDDLE) {
			return null;
		} else {
			return Item.getItemFromBlock(this);
		}
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(SECTION) != TorchSection.LOWER ? 0 : 2;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		this.onNeighborChangeInternal(worldIn, pos, state);
	}

	// Taken from BlockTorch
	protected boolean onNeighborChangeInternal(World world, BlockPos pos, IBlockState state) {
		if (!this.checkForDrop(world, pos, state)) {
			return true;
		}

		if (!world.isRemote && state.getValue(SECTION) == TorchSection.LOWER && !canPlaceTikiTorchOn(world, pos.down())) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
		return false;
	}

	// Taken from BlockTorch
	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getValue(SECTION) != TorchSection.LOWER || (state.getBlock() == this && this.canPlaceAt(worldIn, pos))) {
			return true;
		} else {
			if (worldIn.getBlockState(pos).getBlock() == this) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TorchSection section = state.getValue(SECTION);

		if (section == TorchSection.UPPER) return;

		// Only place top block if it's on a fence
		IBlockState stateBelow = worldIn.getBlockState(pos.down());
		if (stateBelow.getBlock() instanceof BlockFence ||
				stateBelow.getBlock() instanceof BlockWall) {
			worldIn.setBlockState(pos, this.getDefaultState().withProperty(SECTION, TorchSection.UPPER), 2);
		} else {
			worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(SECTION, TorchSection.MIDDLE), 2);
			worldIn.setBlockState(pos.up(2), this.getDefaultState().withProperty(SECTION, TorchSection.UPPER), 2);	
		}
	}

	// Taken from BlockTorch
	private boolean canPlaceAt(World worldIn, BlockPos pos) {
		return this.canPlaceTikiTorchOn(worldIn, pos.down());
	}
	
	@Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(world, pos, state, player);
        if (!world.isRemote) {
            switch (state.getValue(SECTION)) {
            case MIDDLE:
                dropBlockAsItem(world, pos.down(), world.getBlockState(pos.down()), 0);
                break;
            case UPPER:
                dropBlockAsItem(world, pos.down(2), world.getBlockState(pos.down(2)), 0);
                break;
            default:
                break;
            }
        }
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
	    super.breakBlock(world, pos, state);
        if (!world.isRemote) {
            TorchSection section = state.getValue(SECTION);
            if (section == TorchSection.LOWER) {
                if (world.getBlockState(pos.up()).getBlock() == this) {
                    world.setBlockToAir(pos.up());
                }

                if (world.getBlockState(pos.up(2)).getBlock() == this) {
                    world.setBlockToAir(pos.up(2));
                }
            } else if (section == TorchSection.MIDDLE){
                if (world.getBlockState(pos.down()).getBlock() == this) {
                    world.setBlockToAir(pos.down());
                }

                if (world.getBlockState(pos.up()).getBlock() == this) {
                    world.setBlockToAir(pos.up());
                }
            } else {

                if (world.getBlockState(pos.down()).getBlock() == this) {
                    world.setBlockToAir(pos.down());
                }

                if (world.getBlockState(pos.down(2)).getBlock() == this) {
                    world.setBlockToAir(pos.down(2));
                }
            }
        }
	}

	/**
	 * Called upon the block being destroyed by an explosion
	 */
	@Override
	public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		onBlockDestroyedByPlayer(world, pos, world.getBlockState(pos));
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		boolean isTop = (TorchSection)state.getValue(SECTION) == TorchSection.UPPER;
		if (isTop) {
			double d = (float) pos.getX() + 0.5F;
			double d1 = (float) pos.getY() + 0.7F;
			double d2 = (float) pos.getZ() + 0.5F;

			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d1, d2, 0.0D, 0.0D, 0.0D);
			world.spawnParticle(EnumParticleTypes.FLAME, d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean isTop = (TorchSection)state.getValue(SECTION) == TorchSection.UPPER;
		if (isTop) {
			return super.getLightValue(state, world, pos);
		} else {
			return 0;
		}
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SECTION);
	}

	@Override
	public String getStateName(IBlockState state) {
		return "tiki_torch_" + ((TorchSection) state.getValue(SECTION)).getName();
	}

	@Override
	public IBlockColor getBlockColor() {
		return null;
	}

	@Override
	public IItemColor getItemColor() {
		return null;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		TorchSection section = null;
		switch(meta) {
		case 0:
			section = TorchSection.UPPER;
			break;
		case 1:
			section = TorchSection.MIDDLE;
			break;
		case 2:
			section = TorchSection.LOWER;
			break;
		default:
			section = TorchSection.UPPER;
			break;
		}

		return this.getDefaultState().withProperty(SECTION, section);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((TorchSection) state.getValue(SECTION)).ordinal();
	}

}
