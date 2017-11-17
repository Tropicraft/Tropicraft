package net.tropicraft.core.common.block;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockDrinkMixer extends BlockTropicraft implements
ITileEntityProvider {

	@Nonnull
	public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;

	public BlockDrinkMixer() {
		super(Material.ROCK);
		this.setHardness(2.0F);
		this.setResistance(30F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	protected @Nonnull BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public boolean isTopSolid(@Nonnull IBlockState state) {
		return false;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(@Nonnull IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(@Nonnull IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
		return TileEntityFactory.getDrinkMixerTE();
	}

	@Override
	public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer entityPlayer, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}

		ItemStack stack = entityPlayer.getHeldItemMainhand();

		TileEntityDrinkMixer mixer = (TileEntityDrinkMixer)world.getTileEntity(pos);
		if (mixer == null) {
			return false;
		}

		if (mixer.isDoneMixing()) {
			mixer.retrieveResult(entityPlayer);
			return true;
		}

		if (stack.isEmpty()) {
			mixer.emptyMixer(entityPlayer);
			return true;
		}

		ItemStack ingredientStack = stack.copy();
		ingredientStack.setCount(1);

		if (mixer.addToMixer(ingredientStack)) {
			entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
		}

		if (stack.getItem() == ItemRegistry.bambooMug && mixer.canMix()) {
			mixer.startMixing();
			entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);

			Drink craftedDrink = MixerRecipes.getDrink(mixer.ingredients);
			Drink pinaColada = Drink.pinaColada;

			if (craftedDrink != null && craftedDrink.drinkId == pinaColada.drinkId) {
				// TODO advancements entityPlayer.addStat(AchievementRegistry.craftPinaColada);
			}
		}

		return true;    	
	}
	
	@Override
	public @Nonnull IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, @Nonnull EntityLivingBase placer, @Nonnull EnumHand hand) {
		IBlockState ret = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
		return ret.withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public @Nonnull IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public int getMetaFromState(@Nonnull IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

}
