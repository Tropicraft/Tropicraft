package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.common.worldgen.TCGenUtils;
import net.tropicraft.core.registry.BlockRegistry;

// TODO this could be unified with BlockBundle easily
public class BlockTropicraftLog extends BlockLog implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftLogs> VARIANT = PropertyEnum.create("variant", TropicraftLogs.class);

	public BlockTropicraftLog() {
		super();
		this.disableStats();
		this.setHardness(2.0F);
		this.setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftLogs.MAHOGANY).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
	}

    @Override
    @Deprecated
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(VARIANT).getHardness();
    }

	/**
	 * Called when a user uses the creative pick block button on this block
	 *
	 * @param target The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
	 */
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(state.getBlock(), 1, ((TropicraftLogs)state.getValue(VARIANT)).getMeta());
	}

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
       if (((TropicraftLogs)state.getValue(VARIANT)) == TropicraftLogs.PALM) {
           spawnCoconuts(world, pos, random, 20);
       }
    }

	public static void spawnCoconuts(World world, BlockPos pos, Random random, int chance) {
	    if (world.getBlockState(pos.up()).getBlock() == BlockRegistry.leaves ||
	            world.getBlockState(pos.up(2)).getBlock() == BlockRegistry.leaves) {
	        Block coconut = BlockRegistry.coconut;

	        if (world.isAirBlock(pos.offset(EnumFacing.WEST)) && random.nextInt(chance) == 0) {
	            TCGenUtils.setBlock(world, pos.east(), coconut);
	        }

	        if (world.isAirBlock(pos.offset(EnumFacing.WEST)) && random.nextInt(chance) == 0) {
	            TCGenUtils.setBlock(world, pos.west(), coconut);
	        }

	        if (world.isAirBlock(pos.offset(EnumFacing.NORTH)) && random.nextInt(chance) == 0) {
	            TCGenUtils.setBlock(world, pos.north(), coconut);
	        }

	        if (world.isAirBlock(pos.offset(EnumFacing.SOUTH)) && random.nextInt(chance) == 0) {
	            TCGenUtils.setBlock(world, pos.south(), coconut);
	        }

	        if (world.isAirBlock(pos.offset(EnumFacing.DOWN)) && random.nextInt(chance) == 0) {
	            TCGenUtils.setBlock(world, pos.down(), coconut);
	        }
	    }
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LOG_AXIS, VARIANT);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < TropicraftLogs.values().length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, TropicraftLogs.byMetadata((meta & 3)));

		switch (meta & 12) {
		case 0:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
			break;
		case 4:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
			break;
		case 8:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
			break;
		default:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
		}

		return iblockstate;
	}


	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((TropicraftLogs)state.getValue(VARIANT)).getMeta();

		switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
		case X:
			i |= 4;
			break;
		case Z:
			i |= 8;
			break;
		case NONE:
			i |= 12;
		default:
			break;
		}

		return i;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return ((TropicraftLogs)state.getValue(VARIANT)).getMeta();
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this), 1, ((TropicraftLogs)state.getValue(VARIANT)).getMeta());
	}

	// ITropicraftBlock methods
	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftLogs) state.getValue(VARIANT)).getName();
	}

	@Override
	public IBlockColor getBlockColor() {
		return null;
	}

	@Override
	public IItemColor getItemColor() {
		return null;
	}

}
