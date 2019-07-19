package net.tropicraft.core.common.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.potion.Effects;
import net.minecraft.util.BlockRenderLayer;

public class Builder {
    public static Block flower() {
        return new FlowerBlock(Effects.REGENERATION, 0, prop(Material.PLANTS).sound(SoundType.PLANT));
    }

    public static Block sand(final MaterialColor color) {
        return sand(color, 0.5f, 0.5f);
    }

    public static Block sand(final MaterialColor color, final float hardness, final float resistance) {
        return new BlockTropicraftSand(prop(Material.SAND, color).sound(SoundType.SAND).hardnessAndResistance(hardness, resistance));
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

    public static Block stairs(final BlockState baseState, final Material material, final MaterialColor color, final BlockRenderLayer renderLayer) {
        // Need weird brackets here to override protected behavior
        return new StairsBlock(baseState, prop(material, color)) {
            @Override
            public BlockRenderLayer getRenderLayer() {
                return renderLayer;
            }
        };
    }

    public static Block slab(final Block.Properties properties) {
        return new SlabBlock(properties);
    }

    public static Block leaves() {
        return new LeavesBlock(prop(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT));
    }

    public static Block fence(final Material material, final MaterialColor color) {
        return new FenceBlock(prop(material, color));
    }

    public static Block fenceGate(final Material material, final MaterialColor color) {
        return new FenceGateBlock(prop(material, color));
    }

    public static Block bongo(final BongoDrumBlock.Size bongoSize) {
        return new BongoDrumBlock(bongoSize, prop(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
    }

    private static Block.Properties prop(final Material material) {
        return Block.Properties.create(material);
    }

    private static Block.Properties prop(final Material material, final MaterialColor color) {
        return Block.Properties.create(material, color);
    }
}
