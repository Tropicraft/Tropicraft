package net.tropicraft.core.common.donations;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class FireworkUtil {

    private static final Random rand = new Random();

    public static @Nonnull EntityFireworkRocket getRandomFirework(@Nonnull World world) {
        return getRandomFirework(world, new BlockPos(0, 0, 0));
    }

    public static @Nonnull EntityFireworkRocket getRandomFirework(@Nonnull World world, @Nonnull BlockPos pos) {
        ItemStack firework = new ItemStack(Items.FIREWORKS);
        firework.setTagCompound(new NBTTagCompound());
        NBTTagCompound expl = new NBTTagCompound();
        expl.setBoolean("Flicker", true);
        expl.setBoolean("Trail", true);

        int[] colors = new int[rand.nextInt(8) + 1];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = ItemDye.DYE_COLORS[rand.nextInt(16)];
        }
        expl.setIntArray("Colors", colors);
        byte type = (byte) (rand.nextInt(3) + 1);
        type = type == 3 ? 4 : type;
        expl.setByte("Type", type);

        NBTTagList explosions = new NBTTagList();
        explosions.appendTag(expl);

        NBTTagCompound fireworkTag = new NBTTagCompound();
        fireworkTag.setTag("Explosions", explosions);
        fireworkTag.setByte("Flight", (byte) 1);
        firework.setTagInfo("Fireworks", fireworkTag);

        EntityFireworkRocket e = new EntityFireworkRocket(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, firework);
        return e;
    }

    public static @Nonnull EntityFireworkRocket getRandomPRFirework(@Nonnull World world, @Nonnull BlockPos pos) {
        ItemStack firework = new ItemStack(Items.FIREWORKS);
        firework.setTagCompound(new NBTTagCompound());
        NBTTagCompound expl = new NBTTagCompound();
        expl.setBoolean("Flicker", true);
        expl.setBoolean("Trail", true);

        int[] colors = new int[rand.nextInt(8) + 1];
        int[] PR_COLORS = new int[] {11743532, 15790320, 2437522};
        for (int i = 0; i < colors.length; i++) {
            colors[i] = PR_COLORS[rand.nextInt(PR_COLORS.length)];
        }
        expl.setIntArray("Colors", colors);
        byte type = (byte) (rand.nextInt(3) + 1);
        type = type == 3 ? 4 : type;
        expl.setByte("Type", type);

        NBTTagList explosions = new NBTTagList();
        explosions.appendTag(expl);

        NBTTagCompound fireworkTag = new NBTTagCompound();
        fireworkTag.setTag("Explosions", explosions);
        fireworkTag.setByte("Flight", (byte) 1);
        firework.setTagInfo("Fireworks", fireworkTag);

        EntityFireworkRocket e = new EntityFireworkRocket(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, firework);
        return e;
    }

    public static void spawnFirework(@Nonnull BlockPos block, int dimID) {
        spawnFirework(block, dimID, 0);
    }

    public static void spawnFirework(@Nonnull BlockPos pos, int dimID, int range) {
        World world = DimensionManager.getWorld(dimID);
        BlockPos spawnPos = pos;

        // don't bother if there's no randomness at all
        if (range > 0) {
            spawnPos = new BlockPos(moveRandomly(spawnPos.getX(), range), spawnPos.getY(), moveRandomly(spawnPos.getZ(), range));
            IBlockState bs = world.getBlockState(spawnPos);

            int tries = -1;
            while (!world.isAirBlock(new BlockPos(spawnPos)) && !bs.getBlock().isReplaceable(world, spawnPos)) {
                tries++;
                if (tries > 100) {
                    return;
                }
            }
        }

        world.spawnEntity(getRandomPRFirework(world, spawnPos));
    }

    private static double moveRandomly(double base, double range) {
        return base + 0.5 + rand.nextDouble() * range - (range / 2);
    }
}
