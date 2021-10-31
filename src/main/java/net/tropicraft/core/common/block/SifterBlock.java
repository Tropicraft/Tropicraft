package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.block.tileentity.VolcanoTileEntity;

public class SifterBlock extends Block implements EntityBlock {

    public SifterBlock(final Properties properties) {
        super(properties);
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
            final SifterTileEntity sifter = (SifterTileEntity) world.getBlockEntity(pos);
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SifterTileEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == TropicraftTileEntityTypes.SIFTER.get() ? (world1, pos, state1, be) -> SifterTileEntity.tick(world1, pos, state1, (SifterTileEntity) be) : null;
    }
}
