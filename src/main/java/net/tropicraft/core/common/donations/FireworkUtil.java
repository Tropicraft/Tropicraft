package net.tropicraft.core.common.donations;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireworkUtil {
    
    static final int CE_YELLOW = 0xF4C434;
    static final int CE_BLUE= 0x65B9DB;
    static final int CE_GREEN = 0xB1D299;
    static final int CE_RED = 0xEA5153;
    static final int CE_ORANGE = 0xF08747;
    static final int CE_PURPLE = 0x8160A6;
    static final int CE_NAVY = 0x394053;
    
    public enum Palette {
        
        FULL_RANDOM,
        PUERTO_RICO(new int[]{11743532, 15790320, 2437522, 0x6ac944, 0xf5c140, 0x0F9684}),
        COOL_EARTH(
                new int[] {CE_YELLOW, CE_BLUE, CE_GREEN},
                new int[] {CE_RED, CE_ORANGE, CE_PURPLE},
                new int[] {CE_YELLOW, CE_BLUE, CE_PURPLE},
                new int[] {CE_BLUE, CE_GREEN, CE_NAVY}
        ),
        ;
        
        private final int[][] palette;
        
        private Palette(int[]... palette) {
            this.palette = palette;
        }
        
        public int[][] getPalette() {
            return palette;
        }
    }

    private static final Random rand = new Random();

    public static @Nonnull FireworkRocketEntity getRandomFirework(@Nonnull World world) {
        return getRandomFirework(world, new BlockPos(0, 0, 0));
    }

    public static @Nonnull FireworkRocketEntity getRandomFirework(@Nonnull World world, @Nonnull BlockPos pos, int[]... palettes) {
        ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
        firework.setTag(new CompoundNBT());
        CompoundNBT expl = new CompoundNBT();
        expl.putBoolean("Flicker", true);
        expl.putBoolean("Trail", true);

        int[] colors;
        if (palettes.length == 0) {
            colors = new int[rand.nextInt(8) + 1];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = DyeColor.values()[rand.nextInt(16)].getColorValue();
            }
        } else {
            int[] palette = palettes[rand.nextInt(palettes.length)];
            colors = new int[palette.length];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = palette[rand.nextInt(palette.length)];
            }
        }
        expl.putIntArray("Colors", colors);
        byte type = (byte) (rand.nextInt(3) + 1);
        type = type == 3 ? 4 : type;
        expl.putByte("Type", type);

        ListNBT explosions = new ListNBT();
        explosions.add(expl);

        CompoundNBT fireworkTag = new CompoundNBT();
        fireworkTag.put("Explosions", explosions);
        fireworkTag.putByte("Flight", (byte) 1);
        firework.setTagInfo("Fireworks", fireworkTag);

        FireworkRocketEntity e = new FireworkRocketEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, firework);
        return e;
    }

    public static void spawnFirework(@Nonnull BlockPos block, World world, int[]... palettes) {
        spawnFirework(block, world, 0, palettes);
    }

    public static void spawnFirework(@Nonnull BlockPos pos, World world, int range, int[]... palettes) {
        BlockPos spawnPos = pos;

        // don't bother if there's no randomness at all
        if (range > 0) {
            spawnPos = new BlockPos(moveRandomly(spawnPos.getX(), range), spawnPos.getY(), moveRandomly(spawnPos.getZ(), range));
            BlockState bs = world.getBlockState(spawnPos);

            int tries = -1;
            while (!world.isAirBlock(new BlockPos(spawnPos)) && !bs.getMaterial().blocksMovement()) {
                tries++;
                if (tries > 100) {
                    return;
                }
            }
        }

        world.addEntity(getRandomFirework(world, spawnPos, palettes));
    }

    private static double moveRandomly(double base, double range) {
        return base + 0.5 + rand.nextDouble() * range - (range / 2);
    }
}
