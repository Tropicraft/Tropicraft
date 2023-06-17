package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

import javax.annotation.Nullable;

public class SifterBlock extends BaseEntityBlock {

    public SifterBlock(final Properties properties) {
        super(properties);
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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        final ItemStack stack = player.getItemInHand(hand);

        // TODO use item tag
        final boolean isSandInHand = Block.byItem(stack.getItem()).defaultBlockState().getMaterial() == Material.SAND;
        if (!isSandInHand) {
            return InteractionResult.PASS;
        }

        if (!world.isClientSide) {
            final SifterBlockEntity sifter = (SifterBlockEntity) world.getBlockEntity(pos);
            if (sifter != null && !stack.isEmpty() && !sifter.isSifting()) {
                final ItemStack addItem;
                if (!player.isCreative()) {
                    addItem = stack.split(1);
                } else {
                    addItem = stack.copy().split(1);
                }
                sifter.addItemToSifter(addItem);

                sifter.startSifting();
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.SUCCESS;
    } // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D
}
