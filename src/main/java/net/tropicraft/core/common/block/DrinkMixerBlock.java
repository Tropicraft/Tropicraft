package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.drinks.TropicraftDrinks;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.List;

public final class DrinkMixerBlock extends BaseEntityBlock {
    public static final MapCodec<DrinkMixerBlock> CODEC = simpleCodec(DrinkMixerBlock::new);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public DrinkMixerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<DrinkMixerBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TropicraftBlocks.DRINK_MIXER_ENTITY.get(), DrinkMixerBlockEntity::mixTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(level.getBlockEntity(pos) instanceof DrinkMixerBlockEntity mixer)) {
            return InteractionResult.FAIL;
        }

        if (mixer.isDoneMixing()) {
            mixer.retrieveResult(player);
            return InteractionResult.CONSUME;
        }

        mixer.emptyMixer(player);
        return InteractionResult.CONSUME;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (!(level.getBlockEntity(pos) instanceof DrinkMixerBlockEntity mixer)) {
            return ItemInteractionResult.FAIL;
        }

        if (mixer.isDoneMixing()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack ingredientStack = stack.copyWithCount(1);

        if (mixer.addToMixer(ingredientStack)) {
            if (!player.isCreative()) {
                player.getInventory().removeItem(player.getInventory().selected, 1);
            }
        }

        if (ingredientStack.is(TropicraftItems.BAMBOO_MUG) && mixer.canMix()) {
            mixer.startMixing();
            if (!player.isCreative()) {
                player.getInventory().removeItem(player.getInventory().selected, 1);
            }

            ResourceKey<Drink> craftedDrink = MixerRecipes.getDrink(mixer.ingredients);

            if (craftedDrink != null && craftedDrink == TropicraftDrinks.PINA_COLADA) {
                // TODO advancements entityPlayer.addStat(AchievementRegistry.craftPinaColada);
            }
        }

        return ItemInteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState ret = super.getStateForPlacement(context);
        return ret.setValue(FACING, context.getPlayer().getDirection());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DrinkMixerBlockEntity(TropicraftBlocks.DRINK_MIXER_ENTITY.get(), pos, state);
    }
}
