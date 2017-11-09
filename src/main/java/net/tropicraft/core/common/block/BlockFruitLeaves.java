package net.tropicraft.core.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftFruitLeaves;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

//TODO unify under BlockTropicraftEnumVariants somehow
public class BlockFruitLeaves extends BlockLeaves implements ITropicraftBlock {

	public static final PropertyEnum<TropicraftFruitLeaves> VARIANT = PropertyEnum.create("variant", TropicraftFruitLeaves.class);

	public BlockFruitLeaves() {
		super();
		setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false).withProperty(VARIANT, TropicraftFruitLeaves.GRAPEFRUIT));
	}

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        // ignore decay
    }

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {	
		for (TropicraftFruitLeaves leaf : TropicraftFruitLeaves.VALUES) {
			list.add(new ItemStack(item, 1, leaf.getMeta()));
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(BlockRegistry.saplings);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, TropicraftFruitLeaves.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(VARIANT).getMeta();
		return i;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return Blocks.LEAVES.getBlockLayer();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return Blocks.LEAVES.isOpaqueCube(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(3) != 0 ? 0 : 1;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		int chance = this.getSaplingDropChance(state);

		if (fortune > 0)
		{
			chance -= 2 << fortune;
			if (chance < 10) chance = 10;
		}

		if (rand.nextInt(chance) == 0) {
			int meta = (this.getMetaFromState(state) & 3) + 2;
			ret.add(new ItemStack(getItemDropped(state, rand, fortune), 1, meta));
		}

		chance = 200;
		if (fortune > 0)
		{
			chance -= 10 << fortune;
			if (chance < 40) chance = 40;
		}

		this.captureDrops(true);

		if (world instanceof World) {
			int treeType = this.getMetaFromState(state) & 3;
			if (treeType == 0) {
				ret.add(new ItemStack(ItemRegistry.grapefruit));
			} else if (treeType == 1) {
				ret.add(new ItemStack(ItemRegistry.lemon));
			} else if (treeType == 2) {
				ret.add(new ItemStack(ItemRegistry.lime));
			} else {
				ret.add(new ItemStack(ItemRegistry.orange));
			}
		}

		ret.addAll(this.captureDrops(false));
		return ret;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE, VARIANT);
	}

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftFruitLeaves) state.getValue(VARIANT)).getName();
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world,
			BlockPos pos, int fortune) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		IBlockState state = world.getBlockState(pos);

		if (state.getBlock() == this) {
			int meta = this.getMetaFromState(state);
			items.add(new ItemStack(this, 1, meta));
		}

		return items;
	}

	@Override
	public EnumType getWoodType(int meta) {
		return null;
	}
}
