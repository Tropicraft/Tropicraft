package net.tropicraft.core.common.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.trees.Tree;
import net.minecraft.potion.Effects;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.Arrays;

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

    public static Block stairs(final BlockState baseState, final Material material, final MaterialColor color, final BlockRenderLayer renderLayer, final SoundType soundType) {
        // Need weird brackets here to override protected behavior
        return new StairsBlock(baseState, prop(material, color).sound(soundType)) {
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
        return new TropicraftLeavesBlock(prop(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT));
    }

    public static Block sapling(final Tree tree, final Block...validPlantBlocks) {
    	return new SaplingBlock(tree, prop(Material.PLANTS).hardnessAndResistance(0).tickRandomly().sound(SoundType.PLANT)) {
            protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
                final Block block = state.getBlock();
                if (validPlantBlocks == null || validPlantBlocks.length == 0) {
                    return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND;
                } else {
                    return Arrays.asList(validPlantBlocks).contains(block);
                }
            }
        };
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

    public static Block tropicraftPot(final Block block) {
        return BambooFlowerPotBlock.create(block, Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0).sound(SoundType.BAMBOO));
    }

    public static Block vanillaPot(final Block block) {
        return new FlowerPotBlock(block, Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0));
    }

    private static Block.Properties prop(final Material material) {
        return Block.Properties.create(material);
    }

    private static Block.Properties prop(final Material material, final MaterialColor color) {
        return Block.Properties.create(material, color);
    }
}
