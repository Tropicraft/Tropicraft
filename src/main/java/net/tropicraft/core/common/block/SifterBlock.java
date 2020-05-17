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

public class SifterBlock extends Block implements ITileEntityProvider {

    public SifterBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        final ItemStack stack = player.getHeldItem(hand);

        final boolean isSandInHand = Block.getBlockFromItem(stack.getItem()).getMaterial(state) == Material.SAND;
        if (!isSandInHand) {
            return ActionResultType.PASS;
        }

        if (!world.isRemote) {
            final SifterTileEntity sifter = (SifterTileEntity) world.getTileEntity(pos);
            if (sifter != null && !stack.isEmpty() && !sifter.isSifting()) {
                // TODO use item tag
                if (isSandInHand) {
                    sifter.addItemToSifter(stack.split(1));
                    sifter.startSifting();
                    return ActionResultType.CONSUME;
                }
            }
        }

        return ActionResultType.SUCCESS;
    } // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D

    @Nullable
    @Override
    public TileEntity createNewTileEntity(final IBlockReader world) {
        return new SifterTileEntity();
    }
}
