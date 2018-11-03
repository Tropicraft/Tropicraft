package net.tropicraft.core.common.block;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.tropicraft.core.common.worldgen.TCGenUtils;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockTropicraftFence extends BlockFence {
    
    public enum WaterState implements IStringSerializable {
        UNDER,
        SURFACE,
        NONE;
        
        @Override
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
    
    public static final IProperty<WaterState> WATER = PropertyEnum.create("water", WaterState.class);
    
    private final BlockFenceGate gate;
	
	public BlockTropicraftFence(BlockFenceGate fenceGate, Material material, MapColor mapColor) {
		super(material, mapColor);
		this.useNeighborBrightness = true;
		this.setDefaultState(getDefaultState().withProperty(WATER, WaterState.NONE));
		this.gate = fenceGate;
	}
	
	@Override
	protected @Nonnull BlockStateContainer createBlockState() {
	    IProperty<?>[] superProps = super.createBlockState().getProperties().toArray(new IProperty<?>[4]);
	    return new BlockStateContainer(this, ArrayUtils.addAll(superProps, WATER, BlockFluidBase.LEVEL));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
	    return state.getValue(WATER).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
	    return getDefaultState().withProperty(WATER, WaterState.values()[meta % WaterState.values().length]);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
	    super.onBlockAdded(worldIn, pos, state);
	    updateWaterState(state, worldIn, pos);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
	    return getWaterState(super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand), world, pos);
	}

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        updateWaterState(state, worldIn, pos);
    }

    protected final IBlockState getWaterState(IBlockState state, World world, BlockPos pos) {
        WaterState water = WaterState.NONE;
        IBlockState up = world.getBlockState(pos.up());
        Set<IBlockState> neighbors = new HashSet<>();
        for (EnumFacing dir : EnumFacing.HORIZONTALS) {
            neighbors.add(world.getBlockState(pos.offset(dir)));
        }
        if (neighbors.stream().anyMatch(s -> (s.getBlock() == this && s.getValue(WATER) != WaterState.NONE) || (s.getBlock() == BlockRegistry.tropicsWater && s.getValue(BlockFluidBase.LEVEL) == 0))) {
            water = WaterState.UNDER;
        }
        // Don't allow "chained" waterlogging
        if (neighbors.stream().allMatch(s -> s.getBlock() != BlockRegistry.tropicsWater || s.getValue(BlockFluidBase.LEVEL) != 0)) {
            water = WaterState.NONE;
        }
        if (water == WaterState.UNDER && up.getBlock() != BlockRegistry.tropicsWater && (up.getBlock() != this || up.getValue(WATER) == WaterState.NONE)) {
            water = WaterState.SURFACE;
        }
        return state.withProperty(WATER, water).withProperty(BlockFluidBase.LEVEL, water == WaterState.NONE ? 15 : 0);
    }

    protected final void updateWaterState(IBlockState state, World world, BlockPos pos) {
        IBlockState newState = getWaterState(state, world, pos);
        if (state.getValue(WATER) != newState.getValue(WATER)) {
            world.setBlockState(pos, newState);
        }
    }

    @Override
	public @Nonnull Material getMaterial(IBlockState state) {
	    return state.getValue(WATER) == WaterState.NONE ? super.getMaterial(state) : Material.WATER;
	}
    
    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
    }
	
	@Override
	@Deprecated
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
	    return this.gate.getBlockHardness(blockState, worldIn, pos);
	}

	@Override
	public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
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
