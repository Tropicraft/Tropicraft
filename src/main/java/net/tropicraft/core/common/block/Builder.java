package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class Builder {
    public static Block flower() {
        return new BlockFlower(prop(Material.PLANTS).sound(SoundType.PLANT));
    }

    public static Block sand(final MaterialColor color) {
        return new BlockTropicraftSand(prop(Material.SAND, color).hardnessAndResistance(0.5f));
    }

    private static Block.Properties prop(final Material material) {
        return Block.Properties.create(material);
    }

    private static Block.Properties prop(final Material material, final MaterialColor color) {
        return Block.Properties.create(material, color);
    }
}
