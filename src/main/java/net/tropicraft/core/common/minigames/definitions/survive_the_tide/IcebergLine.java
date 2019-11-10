package net.tropicraft.core.common.minigames.definitions.survive_the_tide;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class IcebergLine
{
    private final BlockPos start, end;

    private final int count;
    private final int intervalX;
    private final int intervalZ;

    private Random rand;

    public IcebergLine(BlockPos start, BlockPos end, int distBetweenEach) {
        this.rand = new Random();

        this.start = start;
        this.end = end;

        int diffX = this.start.getX() - this.end.getX();
        int diffZ = this.start.getZ() - this.end.getZ();

        this.count = Math.max(Math.abs(diffX), Math.abs(diffZ)) / distBetweenEach;

        this.intervalX = diffX / (count + 1);
        this.intervalZ = diffZ / (count + 1);
    }

    public void generate(World world, int waterLevel) {
        for (int i = 1; i <= count; i++) {
            int offsetX = getRandOffset(4);
            int offsetZ = getRandOffset(4);

            BlockPos pos = new BlockPos(this.start.getX() - (i * intervalX) + offsetX, waterLevel, this.start.getZ() - (i * intervalZ) + offsetZ);

            world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState(), 2);
        }

        world.setBlockState(this.start.add(getRandOffset(6), waterLevel, getRandOffset(6)), Blocks.ICE.getDefaultState(), 2);
        world.setBlockState(this.end.add(getRandOffset(6), waterLevel, getRandOffset(6)), Blocks.ICE.getDefaultState(), 2);
    }

    private int getRandOffset(int radius) {
        return rand.nextInt(radius) * (rand.nextBoolean() ? -1 : 1);
    }
}
