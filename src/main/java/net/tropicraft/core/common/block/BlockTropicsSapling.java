package net.tropicraft.core.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftSaplings;
import net.tropicraft.core.common.worldgen.TCGenBase;
import net.tropicraft.core.common.worldgen.WorldGenCurvedPalms;
import net.tropicraft.core.common.worldgen.WorldGenFruitTrees;
import net.tropicraft.core.common.worldgen.WorldGenLargePalmTrees;
import net.tropicraft.core.common.worldgen.WorldGenNormalPalms;
import net.tropicraft.core.common.worldgen.WorldGenTallTree;
import net.tropicraft.core.common.worldgen.WorldGenTualang;

public class BlockTropicsSapling extends BlockBush implements ITropicraftBlock, IGrowable {

	public String[] names;

	public static final PropertyEnum<TropicraftSaplings> VARIANT = PropertyEnum.create("variant", TropicraftSaplings.class);
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

	public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	public BlockTropicsSapling(String[] names) {
		super(Material.PLANTS);
		this.setSoundType(SoundType.PLANT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftSaplings.PALM).withProperty(STAGE, Integer.valueOf(0)));
		this.names = names;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(STAGE, Integer.valueOf(meta >> 3))
				.withProperty(VARIANT, TropicraftSaplings.byMetadata(meta & 7));
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state) {
		return ((TropicraftSaplings)state.getValue(VARIANT)).getMetadata() & 7;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((TropicraftSaplings)state.getValue(VARIANT)).getMetadata();
		i = i | ((Integer)state.getValue(STAGE)).intValue() << 3;
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, STAGE, VARIANT);
	}

	/**
	 * is the block grass, dirt or farmland
	 */
	@Override
	protected boolean canSustainBush(IBlockState state) {
		Block block = state.getBlock();
		return block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.FARMLAND
				|| state.getMaterial() == Material.GRASS || state.getMaterial() == Material.SAND;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {
			super.updateTick(world, pos, state, rand);

			if (world.getLight(pos.up()) >= 9 && rand.nextInt(7) == 0) {
				grow(world, rand, pos, state);
			}
		}
	}

	@Override
	public String getStateName(IBlockState state) {
		return ((TropicraftSaplings) state.getValue(VARIANT)).getName();
	}

	/**
	 * Whether this IGrowable can grow
	 */
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return (double)worldIn.rand.nextFloat() < 0.45D;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		// Change flag (and back) to make it show up when generated
		TCGenBase.blockGenNotifyFlag = 3;

		WorldGenerator gen;		
		TropicraftSaplings variant;

		switch (variant = state.getValue(VARIANT)) {
		case PALM: 
		    int b = rand.nextInt(3);
            if (b == 0) {
                gen = new WorldGenLargePalmTrees(worldIn, rand);
            } else if (b == 1) {
                gen = new WorldGenCurvedPalms(worldIn, rand);
            } else if (b == 2) {
                gen = new WorldGenNormalPalms(worldIn, rand);
            } else {
                gen = null;
            }
            break;
        case MAHOGANY:
            gen = randomRainforestTreeGen(worldIn);
            break;
        default:
            gen = new WorldGenFruitTrees(worldIn, rand, variant.ordinal() - 2);
            break;
		}

		if (gen != null) {
			worldIn.setBlockToAir(pos);
			if (!gen.generate(worldIn, rand, pos)) {
				worldIn.setBlockState(pos, state.withProperty(BlockTropicsSapling.VARIANT, variant), 3);
			}
		}

		TCGenBase.blockGenNotifyFlag = TCGenBase.BLOCK_GEN_NOTIFY_FLAG_DEFAULT;
	}

	private WorldGenerator randomRainforestTreeGen(World world) {
		Random rand = new Random();
		int type = rand.nextInt(4);

		switch(type) {
		case 0:
			return new WorldGenTallTree(world, rand);
			//TODO:		case 1:
			//			return new WorldGenUpTree(world, rand);
		case 2:
			//return new WorldGenBentRainforestTree(world, rand, false);
		case 3:
			return new WorldGenTualang(world, rand, 18, 9);
		default:
			return new WorldGenTualang(world, rand, 25, 10);
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (TropicraftSaplings sapling : TropicraftSaplings.VALUES) {
			list.add(new ItemStack(itemIn, 1, sapling.getMetadata()));
		}
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
