package net.tropicraft.core.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTropicraftLog extends BlockLog implements ITropicraftBlock {

    public static final PropertyEnum<TropicraftLogs> VARIANT = PropertyEnum.create("variant", TropicraftLogs.class);
    public String[] names;
	
	public BlockTropicraftLog(String[] logNames) {
		super();
		this.names = logNames;
		this.disableStats();
		this.setHardness(2.0F);
		this.setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftLogs.MAHOGANY).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
	}
	
	public static void spawnCoconuts(World world, BlockPos pos, Random random, int chance) {
		if (world.getBlockState(pos.up()).getBlock() == BlockRegistry.leaves ||
				world.getBlockState(pos.up(2)).getBlock() == BlockRegistry.leaves) {
// TODO		if (world.isAirBlock(pos.offset(EnumFacing.WEST)) && random.nextInt(chance) == 0) {
//				world.setBlock(i + 1, j, k, BlockRegistry.coconut);
//				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
//
//			}
//			if (world.isAirBlock(pos.offset(EnumFacing.WEST)) && random.nextInt(chance) == 0) {
//				world.setBlock(i - 1, j, k, BlockRegistry.coconut);
//				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
//
//
//			}
//			if (world.isAirBlock(i, j, k - 1) && random.nextInt(chance) == 0) {
//				world.setBlock(i, j, k - 1, BlockRegistry.coconut);
//				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
//
//
//			}
//			if (world.isAirBlock(i, j, k + 1) && random.nextInt(chance) == 0) {
//				world.setBlock(i, j, k + 1, BlockRegistry.coconut);
//				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
//			}
//
//			if (world.isAirBlock(i, j - 1, k) && random.nextInt(chance) == 0) {
//				world.setBlock(i, j - 1, k, BlockRegistry.coconut);
//				world.setBlockMetadataWithNotify(i, j, k, 0, 3);
//			}
		} 

	}

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, new IProperty[] { LOG_AXIS, VARIANT });
    }
    
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {        
        for (int i = 0; i < names.length; i++) {
        	list.add(new ItemStack(item, 1, i));
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
        i = i | ((TropicraftLogs)state.getValue(VARIANT)).getMetadata();

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
        return ((TropicraftLogs)state.getValue(VARIANT)).getMetadata();
    }
    
    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((TropicraftLogs)state.getValue(VARIANT)).getMetadata());
    }
    
    // ITropicraftBlock methods
    @Override
    public String getStateName(IBlockState state) {
        return ((TropicraftLogs) state.getValue(VARIANT)).getName();
    }
    
	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {VARIANT, LOG_AXIS};
	}

}
