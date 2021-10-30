package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class SifterBlock extends Block implements ITileEntityProvider {

    public SifterBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        final ItemStack stack = player.getItemInHand(hand);

        // TODO use item tag
        final boolean isSandInHand = Block.byItem(stack.getItem()).defaultBlockState().getMaterial() == Material.SAND;
        if (!isSandInHand) {
            return ActionResultType.PASS;
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
                return ActionResultType.CONSUME;
            }
        }

        return ActionResultType.SUCCESS;
    } // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D

    @Nullable
    @Override
    public TileEntity newBlockEntity(final IBlockReader world) {
        return new SifterTileEntity();
    }
}
