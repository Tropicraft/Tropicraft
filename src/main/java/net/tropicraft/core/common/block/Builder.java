package net.tropicraft.core.common.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.potion.Effects;

public class Builder {
    public static Block flower() {
        return new FlowerBlock(Effects.REGENERATION, 0, prop(Material.PLANTS).sound(SoundType.PLANT));
    }

    public static Block sand(final MaterialColor color) {
        return new BlockTropicraftSand(prop(Material.SAND, color).hardnessAndResistance(0.5f));
    }

    public static Block bundle(final Block.Properties properties) {
        return new BlockBundle(properties);
    }

    public static Block plank(final MaterialColor color) {
        return new Block(prop(Material.WOOD, color).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD));
    }

    public static Block log(final MaterialColor topColor, final MaterialColor sideColor) {
        return new LogBlock(topColor, prop(Material.WOOD, sideColor).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
    }

    public static Block stairs(final BlockState baseState, final Material material, final MaterialColor color) {
        // Need weird brackets here to override protected behavior
        return new StairsBlock(baseState, prop(material, color)) {};
    }

    private static Block.Properties prop(final Material material) {
        return Block.Properties.create(material);
    }

    private static Block.Properties prop(final Material material, final MaterialColor color) {
        return Block.Properties.create(material, color);
    }
}
