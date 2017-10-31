package net.tropicraft.core.common.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCGenUtils {
    public static BlockPos getBlockPos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }
    
    public static BlockPos getBlockPos(int[] xyz) {
        return new BlockPos(xyz[0], xyz[1], xyz[2]);
    }
    
    public static Block getBlock(IBlockAccess world, int x, int y, int z) {
        return getBlock(world, getBlockPos(x, y, z));
    }
    
    public static Block getBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }
    
    public static IBlockState getBlockState(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos);
    }
    
    public static IBlockState getBlockState(IBlockAccess world, int x, int y, int z) {
        return getBlockState(world, getBlockPos(x, y, z));
    }
    
    public static boolean isAirBlock(IBlockAccess world, int x, int y, int z) {
        return isAirBlock(world, getBlockPos(x, y, z));
    }
    
    public static boolean isAirBlock(IBlockAccess world, BlockPos pos) {
        return world.isAirBlock(pos);
    }
    
    public static boolean setBlock(World world, BlockPos pos, Block block) {
        return world.setBlockState(pos, block.getDefaultState());
    }
    
    public static boolean setBlock(World world, int x, int y, int z, Block block) {
        return setBlock(world, new BlockPos(x, y, z), block);
    }
    
    public static boolean setBlockState(World world, BlockPos pos, IBlockState state, int flags) {
        return world.setBlockState(pos, state, flags);
    }
    
    public static boolean setBlockState(World world, BlockPos pos, IBlockState state) {
        return world.setBlockState(pos, state);
    }
    
    public static boolean setBlockState(World world, int x, int y, int z, IBlockState state) {
        return setBlockState(world, new BlockPos(x, y, z), state);
    }
    
    public static boolean setBlockState(World world, int x, int y, int z, IBlockState state, int flags) {
        return world.setBlockState(new BlockPos(x, y, z), state, flags);
    }
    
    public static Material getMaterial(IBlockAccess world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x, y, z)).getMaterial();
    }
    
    public static Material getMaterial(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial();
    }
    
    /**
     * Returns whether the given block is the same as any of the other blocks.
     * @param block1 Provided block
     * @param blocks Array of other blocks
     * @return
     */
    public static boolean isBlockInList(Block block1, Block...blocks) {
        for (Block b : blocks) {
            if (block1 == b) {
                return true;
            }
        }
        return false;
    }
}
