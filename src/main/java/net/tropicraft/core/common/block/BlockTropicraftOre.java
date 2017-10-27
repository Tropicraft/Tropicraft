package net.tropicraft.core.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftOreBlocks;
import net.tropicraft.core.registry.ItemRegistry;

// TODO unify this with BlockTropicraftOreBlock
public class BlockTropicraftOre extends BlockTropicraft implements ITropicraftBlock {
    
    public static final IProperty<TropicraftOreBlocks> VARIANT = PropertyEnum.create("variant", TropicraftOreBlocks.class);

	public BlockTropicraftOre() {
		super(Material.ROCK);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
	    return new BlockStateContainer(this, VARIANT);
	}
    
    @Override
    public String getStateName(IBlockState state) {
        return ((TropicraftOreBlocks) state.getValue(VARIANT)).getName();
    }
    
    @Override
    public IProperty<?>[] getProperties() {
        return new IProperty[]{ VARIANT };
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, TropicraftOreBlocks.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((TropicraftOreBlocks) state.getValue(VARIANT)).ordinal();
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {        
        for (int i = 0; i < TropicraftOreBlocks.VALUES.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        switch (state.getValue(VARIANT)) {
        case AZURITE:
            return ItemRegistry.azurite;
        case EUDIALYTE:
            return ItemRegistry.eudialyte;
        case ZIRCON:
            return ItemRegistry.zircon;
        }
        return null;
    }

	// TODO this is a lazy impl
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        if (state.getValue(VARIANT) == TropicraftOreBlocks.EUDIALYTE) {
            return 1 + random.nextInt(4 + fortune);
        } else {
            return 1 + random.nextInt(1 + fortune);
        }
    }
	
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, 2, 7);
    }
}
