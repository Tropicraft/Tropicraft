package net.tropicraft.core.common.block;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.trees.Tree;
import net.minecraft.potion.Effects;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

@SuppressWarnings("unused")
public class Builder {

    public static Supplier<Block> block(final Block.Properties properties) {
        return block(() -> properties);
    }
    
    public static Supplier<Block> block(final Supplier<Block.Properties> properties) {
        return block(Block::new, properties);
    }
    
    public static <T extends Block> Supplier<T> block(final Function<Block.Properties, T> ctor, final Block.Properties properties) {
        return block(ctor, () -> properties);
    }
    
    public static <T extends Block> Supplier<T> block(final Function<Block.Properties, T> ctor, final Supplier<Block.Properties> properties) {
        return () -> ctor.apply(properties.get());
    }
    
    public static Supplier<BlockTropicraftOre> ore(MaterialColor color) {
        return block(BlockTropicraftOre::new, prop(Material.ROCK, color).hardnessAndResistance(3.0F, 3.0F));
    }
    
    public static Supplier<BlockTropicraftOreBlock> oreBlock(MaterialColor color) {
        return block(BlockTropicraftOreBlock::new, prop(Material.IRON, color).sound(SoundType.METAL).hardnessAndResistance(5.0F, 6.0F));
    }
    
    public static Supplier<TropicsFlowerBlock> flower(TropicraftFlower type) {
        return block(p -> new TropicsFlowerBlock(type.getEffect(), type.getEffectDuration(), type.getShape(), p), lazyProp(Blocks.POPPY.delegate));
    }

    public static Supplier<BlockTropicraftSand> sand(final MaterialColor color) {
        return sand(color, 0.5f, 0.5f);
    }

    public static Supplier<BlockTropicraftSand> sand(final MaterialColor color, final float hardness, final float resistance) {
        return block(BlockTropicraftSand::new, prop(Material.SAND, color).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).hardnessAndResistance(hardness, resistance));
    }

    public static Supplier<RotatedPillarBlock> bundle(final Block.Properties properties) {
        return block(RotatedPillarBlock::new, properties);
    }

    public static Supplier<Block> plank(final MaterialColor color) {
        return block(prop(Material.WOOD, color).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD));
    }

    public static Supplier<LogBlock> log(final MaterialColor topColor, final MaterialColor sideColor) {
        return block(p -> new LogBlock(topColor, p), prop(Material.WOOD, sideColor).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
    }

    public static Supplier<StairsBlock> stairs(final RegistryObject<? extends Block> source) {
        return stairs(source, BlockRenderLayer.SOLID);
    }
    
    public static Supplier<StairsBlock> stairs(final RegistryObject<? extends Block> source, BlockRenderLayer layer) {
        return block(p -> new StairsBlock(source.lazyMap(Block::getDefaultState), p) {
            @Override
            public BlockRenderLayer getRenderLayer() {
                return layer;
            }
        }, lazyProp(source));
    }

    public static Supplier<SlabBlock> slab(final Supplier<? extends Block> source) {
        return block(SlabBlock::new, lazyProp(source));
    }

    public static Supplier<TropicraftLeavesBlock> leaves() {
        return block(TropicraftLeavesBlock::new, lazyProp(Blocks.OAK_LEAVES.delegate));
    }

    @SafeVarargs
    public static Supplier<SaplingBlock> sapling(final Tree tree, final Supplier<? extends Block>... validPlantBlocks) {
    	return block(p -> new SaplingBlock(tree, p) {
            protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
                final Block block = state.getBlock();
                if (validPlantBlocks == null || validPlantBlocks.length == 0) {
                    return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND;
                } else {
                    return Arrays.stream(validPlantBlocks).map(Supplier::get).anyMatch(b -> b == block);
                }
            }
        }, lazyProp(Blocks.OAK_SAPLING.delegate));
    }

    public static Supplier<FenceBlock> fence(final Supplier<? extends Block> source) {
        return block(FenceBlock::new, lazyProp(source));
    }

    public static Supplier<FenceGateBlock> fenceGate(final Supplier<? extends Block> source) {
        return block(FenceGateBlock::new, lazyProp(source));
    }

    public static Supplier<WallBlock> wall(final Supplier<? extends Block> source) {
        return block(WallBlock::new, lazyProp(source));
    }

    public static Supplier<BongoDrumBlock> bongo(final BongoDrumBlock.Size bongoSize) {
        return block(p -> new BongoDrumBlock(bongoSize, p), prop(Material.WOOD, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
    }
    
    public static Supplier<FlowerPotBlock> pot(final Supplier<FlowerPotBlock> emptyPot, final Supplier<? extends Block> flower, final Supplier<Block.Properties> properties) {
        return block(p -> new FlowerPotBlock(emptyPot, flower, p), properties);
    }

    public static Supplier<FlowerPotBlock> tropicraftPot() {
        return pot(null, Blocks.AIR.delegate, lazyProp(Material.MISCELLANEOUS).then(p -> p.hardnessAndResistance(0).sound(SoundType.BAMBOO)));
    }

    public static Supplier<FlowerPotBlock> tropicraftPot(final Supplier<? extends Block> block) {
        return pot(TropicraftBlocks.BAMBOO_FLOWER_POT, block, lazyProp(Material.MISCELLANEOUS).then(p -> p.hardnessAndResistance(0).sound(SoundType.BAMBOO)));
    }

    public static Supplier<FlowerPotBlock> vanillaPot(final Supplier<? extends Block> block) {
        return pot(() -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), block, lazyProp(Blocks.FLOWER_POT.delegate));
    }

    private static Block.Properties prop(final Material material) {
        return Block.Properties.create(material);
    }

    private static Block.Properties prop(final Material material, final MaterialColor color) {
        return Block.Properties.create(material, color);
    }
    
    interface ComposableSupplier<T> extends Supplier<T> {
        
        default <R> ComposableSupplier<R> then(Function<T, R> func) {
            return () -> func.apply(get());
        }
    }

    private static ComposableSupplier<Block.Properties> lazyProp(final Material material) {
        return () -> prop(material);
    }

    private static ComposableSupplier<Block.Properties> lazyProp(final Material material, final MaterialColor color) {
        return () -> prop(material, color);
    }
    
    private static ComposableSupplier<Block.Properties> lazyProp(final Supplier<? extends Block> source) {
        return () -> {
            Objects.requireNonNull(source.get(), "Must register source block before using it");
            return Block.Properties.from(source.get());
        };
    }
}
