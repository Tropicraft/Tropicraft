package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

import javax.annotation.Nullable;

public class SifterBlock extends BaseEntityBlock {
    public static final MapCodec<SifterBlock> CODEC = simpleCodec(SifterBlock::new);

    public SifterBlock(final Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<SifterBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SifterBlockEntity(TropicraftBlocks.SIFTER_ENTITY.get(), pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TropicraftBlocks.SIFTER_ENTITY.get(), SifterBlockEntity::siftTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(final ItemStack stack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hitResult) {
        if (!stack.is(ItemTags.SAND)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof SifterBlockEntity sifter && !stack.isEmpty() && !sifter.isSifting()) {
            final ItemStack addItem = stack.consumeAndReturn(1, player);
            sifter.addItemToSifter(addItem);

            sifter.startSifting();
            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.SUCCESS;
    } // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D
}
