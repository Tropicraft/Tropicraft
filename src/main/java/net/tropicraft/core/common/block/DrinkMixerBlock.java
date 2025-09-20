package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;

public final class DrinkMixerBlock extends BaseEntityBlock {
    public static final MapCodec<DrinkMixerBlock> CODEC = simpleCodec(DrinkMixerBlock::new);

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(level.getBlockEntity(pos) instanceof DrinkMixerBlockEntity mixer)) {
            return InteractionResult.FAIL;
        }

        if (mixer.isDoneMixing()) {
            mixer.retrieveResult(player);
        } else {
            mixer.emptyMixer(player);
        }

        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        if (!(level.getBlockEntity(pos) instanceof DrinkMixerBlockEntity mixer)) {
            return InteractionResult.FAIL;
        }

        if (mixer.isDoneMixing()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (stack.is(TropicraftItems.BAMBOO_MUG)) {
            if (mixer.tryStartMixing()) {
                stack.consume(1, player);
                return InteractionResult.SUCCESS_SERVER;
            }
        } else if (mixer.tryTransferToMixer(level, stack, player)) {
            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DrinkMixerBlockEntity(TropicraftBlocks.DRINK_MIXER_ENTITY.get(), pos, state);
    }
}
