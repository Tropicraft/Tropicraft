package net.tropicraft.core.common.block;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftOreBlocks;

public class BlockTropicraftOreBlock extends BlockTropicraft implements ITropicraftBlock {

    public static final PropertyEnum<TropicraftOreBlocks> VARIANT = PropertyEnum.create("variant", TropicraftOreBlocks.class);
    public String[] names;
    
    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, VARIANT);
    }
    
    @Override
    public String getStateName(IBlockState state) {
        return ((TropicraftOreBlocks) state.getValue(VARIANT)).getName();
    }
	
	public BlockTropicraftOreBlock(String[] names) {
		super(Material.ROCK);
		this.names = names;
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftOreBlocks.ZIRCON));
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

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, TropicraftOreBlocks.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((TropicraftOreBlocks) state.getValue(VARIANT)).ordinal();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {VARIANT};
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
