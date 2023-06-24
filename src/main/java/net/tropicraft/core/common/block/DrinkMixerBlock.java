package net.tropicraft.core.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.List;

public class DrinkMixerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public DrinkMixerBlock(final Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TropicraftBlocks.DRINK_MIXER_ENTITY.get(), DrinkMixerBlockEntity::mixTick);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getMainHandItem();

        DrinkMixerBlockEntity mixer = (DrinkMixerBlockEntity) world.getBlockEntity(pos);
        if (mixer == null) {
            return InteractionResult.FAIL;
        }

        if (mixer.isDoneMixing()) {
            mixer.retrieveResult(player);
            return InteractionResult.CONSUME;
        }

        if (stack.isEmpty()) {
            mixer.emptyMixer(player);
            return InteractionResult.CONSUME;
        }

        ItemStack ingredientStack = stack.copy();
        ingredientStack.setCount(1);

        if (mixer.addToMixer(ingredientStack)) {
            if (!player.isCreative()) {
                player.getInventory().removeItem(player.getInventory().selected, 1);
            }
        }

        if (ingredientStack.getItem() == TropicraftItems.BAMBOO_MUG.get() && mixer.canMix()) {
            mixer.startMixing();
            if (!player.isCreative()) {
                player.getInventory().removeItem(player.getInventory().selected, 1);
            }

            Drink craftedDrink = MixerRecipes.getDrink(mixer.ingredients);
            Drink pinaColada = Drink.PINA_COLADA;

            if (craftedDrink != null && craftedDrink.drinkId == pinaColada.drinkId) {
                // TODO advancements entityPlayer.addStat(AchievementRegistry.craftPinaColada);
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState ret = super.getStateForPlacement(context);
        return ret.setValue(FACING, context.getPlayer().getDirection());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new DrinkMixerBlockEntity(TropicraftBlocks.DRINK_MIXER_ENTITY.get(), pos, state);
    }
}
