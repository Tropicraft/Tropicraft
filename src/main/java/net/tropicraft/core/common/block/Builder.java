package net.tropicraft.core.common.block;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.trees.Tree;
import net.minecraft.potion.Effects;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

public class Builder {
    
    public static Supplier<Block> block(final Block.Properties properties) {
        return () -> new Block(properties);
    }
    
    public static Supplier<Block> flower() {
        return () -> new FlowerBlock(Effects.REGENERATION, 0, prop(Material.PLANTS).sound(SoundType.PLANT));
    }

    public static Supplier<Block> sand(final MaterialColor color) {
        return sand(color, 0.5f, 0.5f);
    }

    public static Supplier<Block> sand(final MaterialColor color, final float hardness, final float resistance) {
        return () -> new BlockTropicraftSand(prop(Material.SAND, color).sound(SoundType.SAND).hardnessAndResistance(hardness, resistance));
    }

    public static Supplier<Block> bundle(final Block.Properties properties) {
        return () -> new BlockBundle(properties);
    }

    public static Supplier<Block> plank(final MaterialColor color) {
        return () -> new Block(prop(Material.WOOD, color).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD));
    }

    public static Supplier<Block> log(final MaterialColor topColor, final MaterialColor sideColor) {
        return () -> new LogBlock(topColor, prop(Material.WOOD, sideColor).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
    }

    public static Supplier<Block> stairs(final RegistryObject<Block> source, final Material material, final MaterialColor color, final BlockRenderLayer renderLayer, final SoundType soundType) {
        // Need weird brackets here to override protected behavior
        return () -> new StairsBlock(() -> source.get().getDefaultState(), prop(material, color).sound(soundType)) {
            @Override
            public BlockRenderLayer getRenderLayer() {
                return renderLayer;
            }
        };
    }

    public static Supplier<Block> slab(final Block.Properties properties) {
        return () -> new SlabBlock(properties);
    }

    public static Supplier<Block> leaves() {
        return () -> new TropicraftLeavesBlock(prop(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT));
    }

    @SafeVarargs
    public static Supplier<Block> sapling(final Tree tree, final Supplier<? extends Block>... validPlantBlocks) {
    	return () -> new SaplingBlock(tree, prop(Material.PLANTS).hardnessAndResistance(0).tickRandomly().sound(SoundType.PLANT)) {
            protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
                final Block block = state.getBlock();
                if (validPlantBlocks == null || validPlantBlocks.length == 0) {
                    return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND;
                } else {
                    return Arrays.stream(validPlantBlocks).map(Supplier::get).anyMatch(b -> b == block);
                }
            }
        };
    }

    public static Supplier<Block> fence(final Material material, final MaterialColor color) {
        return () -> new FenceBlock(prop(material, color));
    }

    public static Supplier<Block> fenceGate(final Material material, final MaterialColor color) {
        return () -> new FenceGateBlock(prop(material, color));
    }

    public static Supplier<Block> bongo(final BongoDrumBlock.Size bongoSize) {
        return () -> new BongoDrumBlock(bongoSize, prop(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
    }

    public static Supplier<FlowerPotBlock> tropicraftPot() {
        return () -> new FlowerPotBlock(null, () -> Blocks.AIR, Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0).sound(SoundType.BAMBOO));
    }

    public static Supplier<FlowerPotBlock> tropicraftPot(final Supplier<? extends Block> block) {
        return () -> new FlowerPotBlock(TropicraftBlocks.BAMBOO_FLOWER_POT, block, Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0).sound(SoundType.BAMBOO));
    }

    public static Supplier<FlowerPotBlock> vanillaPot(final Supplier<? extends Block> block) {
        return () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, block, Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0));
    }

    private static Block.Properties prop(final Material material) {
        return Block.Properties.create(material);
    }

    private static Block.Properties prop(final Material material, final MaterialColor color) {
        return Block.Properties.create(material, color);
    }
}
