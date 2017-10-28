package net.tropicraft.core.common.enums;

import java.util.Random;

import net.minecraft.block.material.MapColor;

public enum TropicraftSands implements ITropicraftVariant {
    PURIFIED(0, MapColor.SAND),
    CORAL(1, MapColor.PINK),
    FOAMY(2, MapColor.GREEN),
    VOLCANIC(3, MapColor.BLACK),
    MINERAL(4, MapColor.SAND);

    private final int meta;
    private final MapColor color;
    private static final TropicraftSands[] META_LOOKUP = new TropicraftSands[values().length];
    public static final TropicraftSands VALUES[] = values();

    private TropicraftSands(int meta, MapColor color) {
        this.meta = meta;
        this.color = color;
    }

    @Override
    public int getMeta() {
        return this.meta;
    }
    
    public MapColor getColor() {
        return color;
    }

    public static TropicraftSands getRandomSand(Random rand) {
        int meta = rand.nextInt(META_LOOKUP.length);

        return META_LOOKUP[meta];
    }

    public static TropicraftSands byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    @Override
    public String getTypeName() {
        return "sand";
    }

    // Set META_LOOKUP table
    static {
        for (TropicraftSands sand : VALUES) {
            META_LOOKUP[sand.getMeta()] = sand;
        }
    }
}
