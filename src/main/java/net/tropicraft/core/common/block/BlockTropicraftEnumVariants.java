package net.tropicraft.core.common.block;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.ITropicraftVariant;

public class BlockTropicraftEnumVariants<T extends Enum<T> & ITropicraftVariant> extends BlockTropicraft implements ITropicraftBlock {
    
    private final IProperty<T> property;
    private final BlockStateContainer blockState;
    
    private final Int2ObjectMap<T> byMeta = new Int2ObjectArrayMap<>();
    private final T[] variants;
    
    protected BlockTropicraftEnumVariants(Material mat, Class<T> enumClass) {
        this(mat, enumClass, enumClass.getEnumConstants());
    }
    
    protected BlockTropicraftEnumVariants(Material mat, Class<T> enumClass, T[] variants) {
        super(mat);
        Preconditions.checkNotNull(variants);
        Preconditions.checkArgument(variants.length > 0, "Must supply at least one variant.");
        this.variants = variants;
        this.property = PropertyEnum.create("variant", enumClass, Lists.newArrayList(variants));
        this.blockState = createBlockState();
        for (T variant : enumClass.getEnumConstants()) {
            byMeta.put(variant.getMeta(), variant);
        }

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
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {        
        for (int i = 0; i < property.getAllowedValues().size(); i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        T variant = byMeta.get(meta);
        IBlockState ret = this.getDefaultState();
        if (variant != null) {
            ret = ret.withProperty(getProperty(), variant);
        }
        return ret;
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
    
    public IBlockState randomVariant(Random rand) {
        return defaultForVariant(variants[rand.nextInt(variants.length)]);
    }
    
    /* == ITropicraftBlock == */

    @Override
    public String getStateName(IBlockState state) {
        return getVariant(state).getName();
    }
}
