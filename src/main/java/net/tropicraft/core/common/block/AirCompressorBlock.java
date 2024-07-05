package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public final class AirCompressorBlock extends BaseEntityBlock {
    public static final MapCodec<AirCompressorBlock> CODEC = simpleCodec(AirCompressorBlock::new);

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public AirCompressorBlock(Block.Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<AirCompressorBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TropicraftBlocks.AIR_COMPRESSOR_ENTITY.get(), AirCompressorBlockEntity::compressTick);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(level.getBlockEntity(pos) instanceof AirCompressorBlockEntity compressor)) {
            return InteractionResult.FAIL;
        }

        if (compressor.isDoneCompressing()) {
            compressor.ejectTank();
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (!(level.getBlockEntity(pos) instanceof AirCompressorBlockEntity compressor)) {
            return ItemInteractionResult.FAIL;
        }

        if (compressor.isDoneCompressing()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack ingredientStack = stack.copyWithCount(1);

        if (compressor.addTank(ingredientStack)) {
            player.getInventory().removeItem(player.getInventory().selected, 1);
        }

        return ItemInteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide) {
            AirCompressorBlockEntity te = (AirCompressorBlockEntity) world.getBlockEntity(pos);
            te.ejectTank();
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p, BlockState s) {
        return new AirCompressorBlockEntity(TropicraftBlocks.AIR_COMPRESSOR_ENTITY.get(), p, s);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState ret = super.getStateForPlacement(context);
        return ret.setValue(FACING, context.getHorizontalDirection());
    }
}
