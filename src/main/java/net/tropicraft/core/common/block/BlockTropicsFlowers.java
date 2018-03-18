package net.tropicraft.core.common.block;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftFlowers;

//TODO unify under BlockTropicraftEnumVariants somehow
public class BlockTropicsFlowers extends BlockBush implements ITropicraftBlock {

    public static final PropertyEnum<TropicraftFlowers> VARIANT = PropertyEnum.create("variant", TropicraftFlowers.class);

    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, VARIANT);
    }
    
    @Override
    public String getStateName(IBlockState state) {
        return ((TropicraftFlowers) state.getValue(VARIANT)).toString();
    }
	
	public BlockTropicsFlowers() {
		super(Material.PLANTS, MapColor.GREEN);
		setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftFlowers.COMMELINA_DIFFUSA));
	}
	
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {        
        for (TropicraftFlowers variant : TropicraftFlowers.VALUES) {
        	list.add(new ItemStack(this, 1, variant.getMeta()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, TropicraftFlowers.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((TropicraftFlowers) state.getValue(VARIANT)).getMeta();
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return state.getValue(VARIANT).getBounds();
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
