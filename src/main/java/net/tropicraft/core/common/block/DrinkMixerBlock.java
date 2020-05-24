package net.tropicraft.core.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.item.TropicraftItems;

public class DrinkMixerBlock extends Block implements ITileEntityProvider {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public DrinkMixerBlock(final Properties properties) {
		super(properties);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".desc").applyTextStyle(TextFormatting.GRAY));
    }

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		}

		ItemStack stack = player.getHeldItemMainhand();

		DrinkMixerTileEntity mixer = (DrinkMixerTileEntity) world.getTileEntity(pos);
		if (mixer == null) {
			return ActionResultType.FAIL;
		}

		if (mixer.isDoneMixing()) {
			mixer.retrieveResult(player);
			return ActionResultType.CONSUME;
		}

		if (stack.isEmpty()) {
			mixer.emptyMixer(player);
			return ActionResultType.CONSUME;
		}

		ItemStack ingredientStack = stack.copy();
		ingredientStack.setCount(1);

		if (mixer.addToMixer(ingredientStack)) {
			if (!player.isCreative()) {
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
			}
		}

		if (ingredientStack.getItem() == TropicraftItems.BAMBOO_MUG.get() && mixer.canMix()) {
			mixer.startMixing();
			if (!player.isCreative()) {
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
			}

			Drink craftedDrink = MixerRecipes.getDrink(mixer.ingredients);
			Drink pinaColada = Drink.PINA_COLADA;

			if (craftedDrink != null && craftedDrink.drinkId == pinaColada.drinkId) {
				// TODO advancements entityPlayer.addStat(AchievementRegistry.craftPinaColada);
			}
		}

		return ActionResultType.CONSUME;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState ret = super.getStateForPlacement(context);
		return ret.with(FACING, context.getPlayer().getHorizontalFacing());
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new DrinkMixerTileEntity();
	}
}
