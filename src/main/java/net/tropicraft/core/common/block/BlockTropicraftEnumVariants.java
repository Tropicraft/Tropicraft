package net.tropicraft.core.common.block;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.ITropicraftVariant;

public class BlockTropicraftEnumVariants<T extends Enum<T> & ITropicraftVariant> extends BlockTropicraft implements ITropicraftBlock {
    
    private final IProperty<T> property;
    private final BlockStateContainer blockState;
    
    private final T[] enumValues;
    
    protected BlockTropicraftEnumVariants(Material mat, Class<T> enumClass) {
        super(mat);
        this.property = PropertyEnum.create("variant", enumClass);
        this.blockState = createBlockState();
        this.enumValues = enumClass.getEnumConstants();

        this.setDefaultState(blockState.getBaseState());
    }
    
    protected IProperty<?>[] getAdditionalProperties() {
        return new IProperty[0];
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if (property == null) {
            return super.createBlockState();
        }
        return new BlockStateContainer(this, ArrayUtils.addAll(new IProperty[] { getProperty() }, getAdditionalProperties()));
    }
    
    @Override
    public BlockStateContainer getBlockState() {
        return blockState;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {        
        for (int i = 0; i < property.getAllowedValues().size(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(getProperty(), enumValues[meta % enumValues.length]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getVariant(state).getMeta();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    public IProperty<T> getProperty() {
        return property;
    }
    
    public T getVariant(IBlockState state) {
        return state.getValue(getProperty());
    }
    
    public IBlockState defaultForVariant(T variant) {
        return getDefaultState().withProperty(getProperty(), variant);
    }
    
    /* == ITropicraftBlock == */

    @Override
    public String getStateName(IBlockState state) {
        return getVariant(state).getName();
    }
}
