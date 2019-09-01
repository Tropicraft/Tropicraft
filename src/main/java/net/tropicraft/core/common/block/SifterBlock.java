package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isRemote) {
            final ItemStack stack = player.getHeldItem(hand);

            final SifterTileEntity sifter = (SifterTileEntity) world.getTileEntity(pos);

            if (sifter != null && !stack.isEmpty() && !sifter.isSifting()) {
                // TODO use item tag
                if (Block.getBlockFromItem(stack.getItem()).getMaterial(state) == Material.SAND) {
                    sifter.addItemToSifter(stack.split(1));
                    sifter.startSifting();
                }
            }
        }

        return true;
    } // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(final IBlockReader world) {
        return new SifterTileEntity();
    }
}
