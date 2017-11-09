package net.tropicraft.core.common.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.BlockHardnessValues;
import net.tropicraft.core.common.enums.TropicraftSlabs;
import net.tropicraft.core.registry.BlockRegistry;

// TODO unify under BlockTropicraftEnumVariants somehow
public class BlockTropicraftSlab extends BlockSlab implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftSlabs> VARIANT = PropertyEnum.<TropicraftSlabs>create("variant", TropicraftSlabs.class);
	public boolean isDoubleSlab;

	public BlockTropicraftSlab(Material material, boolean isDoubleSlab) {
		super(material);
		this.isDoubleSlab = isDoubleSlab;

		IBlockState iblockstate = this.blockState.getBaseState();

		if (!this.isDouble()) {
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}

		this.setDefaultState(iblockstate.withProperty(VARIANT, TropicraftSlabs.BAMBOO));

		this.useNeighborBrightness = true;
	}

    @Override
    @Deprecated
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(this.getVariantProperty()).getHardness();
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
	@Override
    public MapColor getMapColor(IBlockState state) {
        return ((TropicraftSlabs)state.getValue(VARIANT)).getMapColor();
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockRegistry.slabs);
    }
    
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(BlockRegistry.slabs, 1, ((TropicraftSlabs)state.getValue(VARIANT)).getMeta());
    }

	@Override
	public boolean isDouble() {
		return this.isDoubleSlab;
	}

    /**
     * Returns the slab block name with the type associated with it
     */
	@Override
    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName() + "." + TropicraftSlabs.byMetadata(meta).getName();
    }

	@Override
	public IProperty<TropicraftSlabs> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return TropicraftSlabs.byMetadata(stack.getMetadata() & 7);
	}
	
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        if (itemIn != Item.getItemFromBlock(BlockRegistry.doubleSlabs)) {
            for (TropicraftSlabs slab : TropicraftSlabs.VALUES) {
                list.add(new ItemStack(itemIn, 1, slab.getMeta()));
            }
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
	@Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, TropicraftSlabs.byMetadata(meta & 7));

        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockstate;
    }
	
    /**
     * Convert the BlockState into the correct metadata value
     */
	@Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | ((TropicraftSlabs)state.getValue(VARIANT)).getMeta();

        if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }
	
	@Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, VARIANT): new BlockStateContainer(this, HALF, VARIANT);
    }
	
    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
	@Override
    public int damageDropped(IBlockState state) {
        return ((TropicraftSlabs)state.getValue(VARIANT)).getMeta();
    }

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftSlabs) state.getValue(VARIANT)).getName();
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
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
		IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        //IBlockState ret = this.isDouble() ? iblockstate : (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.TOP));
        
        if (isDouble()) {
        	return iblockstate;
        } else {
        	if (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D)) {
        		return iblockstate;
        	} else {
        		return iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.TOP);
        	}
        }
        
//        System.out.println(ret);
//        return ret;
    }
}
