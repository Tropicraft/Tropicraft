package net.tropicraft.core.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockCoffeeBush extends BlockCrops implements ITropicraftBlock {
	
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);
    
    /** Number of bushes high this plant can grow */
	public static final int MAX_HEIGHT = 3;

	/** The growth rate when this plant is fertile */
	public static final int GROWTH_RATE_FERTILE = 10;

	/** The growth rate when this plant is infertile */
	public static final int GROWTH_RATE_INFERTILE = 20;
	
	public BlockCoffeeBush() {
        this.setHardness(0.15F);
        this.setLightOpacity(2);
	}
    
    @Override
    protected PropertyInteger getAgeProperty() {
    	return AGE;
	}
    
    @Override
    public int getMaxAge() {
    	return 6;
    }
    
    // TODO should this drop beans?
    @Override
    protected Item getSeed() {
    	return ItemRegistry.coffeeBeans;
    }
    
    @Override
    protected Item getCrop() {
    	return null;
    }
    
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
    	return EnumPlantType.Crop;
    }

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (state.getValue(AGE) == getMaxAge()) {
	        worldIn.setBlockState(pos, state.withProperty(AGE, 0));
	        ItemStack stack = new ItemStack(ItemRegistry.coffeeBeans, 1, 2);
	        spawnAsEntity(worldIn, pos, stack);
	        return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		// Try to grow up
        if (worldIn.isAirBlock(pos.up())) {
            int height;
            BlockPos ground = pos;
            for (height = 1; worldIn.getBlockState(ground = ground.down()).getBlock() == this; ++height);

            if (height < MAX_HEIGHT) {// && worldIn.rand.nextInt(worldIn.getBlockState(ground).getBlock().isFertile(worldIn, ground) ? GROWTH_RATE_FERTILE : GROWTH_RATE_INFERTILE) == 0) {
                worldIn.setBlockState(pos.up(), getDefaultState());
            }
        }
        
        super.updateTick(worldIn, pos, state, rand);
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return super.canSustainPlant(state, world, pos, direction, plantable) || state.getBlock() == this;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		if (state.getValue(AGE) == getMaxAge()) {
			ret.add(new ItemStack(ItemRegistry.coffeeBeans, 1, 2));
		}
		return ret;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public String getStateName(IBlockState state) {
		return state.getValue(AGE).toString();
	}

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] { AGE };
	}
}
