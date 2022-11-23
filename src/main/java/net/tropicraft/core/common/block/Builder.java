package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

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
    
    public static Supplier<Block> ore(MaterialColor color) {
        return block(prop(Material.STONE, color).strength(3.0F, 3.0F));
    }
    
    public static Supplier<Block> oreBlock(MaterialColor color) {
        return block(prop(Material.METAL, color).sound(SoundType.METAL).strength(5.0F, 6.0F));
    }
    
    public static Supplier<TropicsFlowerBlock> flower(TropicraftFlower type) {
        return block(p -> new TropicsFlowerBlock(type.getEffect(), type.getEffectDuration(), type.getShape(), p), lazyProp(Blocks.POPPY.delegate));
    }

    public static Supplier<BlockTropicraftSand> sand(final MaterialColor color) {
        return sand(color, 0.5f, 0.5f);
    }

    public static Supplier<BlockTropicraftSand> sand(final MaterialColor color, final float hardness, final float resistance) {
        return sand(BlockTropicraftSand::new, color, hardness, resistance);
    }

    public static Supplier<VolcanicSandBlock> volcanicSand(final MaterialColor color) {
        return sand(VolcanicSandBlock::new, color, 0.5f, 0.5f);
    }

    public static <T extends BlockTropicraftSand> Supplier<T> sand(Function<Block.Properties, T> ctor, final MaterialColor color, final float hardness, final float resistance) {
        return block(ctor, prop(Material.SAND, color).sound(SoundType.SAND).strength(hardness, resistance));
    }

    public static Supplier<MudBlock> mud() {
        BlockBehaviour.Properties properties = Block.Properties.copy(Blocks.DIRT).speedFactor(0.5F)
                .isValidSpawn((s, w, p, e) -> true).isRedstoneConductor((s, w, p) -> true)
                .isViewBlocking((s, w, p) -> true).isSuffocating((s, w, p) -> true);
        return block(MudBlock::new, properties);
    }

    public static Supplier<RotatedPillarBlock> bundle(final Block.Properties properties) {
        return block(RotatedPillarBlock::new, properties);
    }

    public static Supplier<Block> plank(final MaterialColor color) {
        return block(prop(Material.WOOD, color).strength(2.0F, 3.0F).sound(SoundType.WOOD));
    }

    public static Supplier<RotatedPillarBlock> log(final MaterialColor topColor, final MaterialColor sideColor) {
        return block(RotatedPillarBlock::new, logProperties(topColor, sideColor));
    }

    public static Supplier<RotatedPillarBlock> log(final MaterialColor topColor, final MaterialColor sideColor, Supplier<RegistryObject<RotatedPillarBlock>> stripped) {
        return block(properties -> new TropicraftLogBlock(properties, () -> stripped.get().get()), logProperties(topColor, sideColor));
    }

    private static BlockBehaviour.Properties logProperties(MaterialColor topColor, MaterialColor sideColor) {
        return prop(Material.WOOD, state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : sideColor).strength(2.0F).sound(SoundType.WOOD);
    }

    public static Supplier<RotatedPillarBlock> wood(final MaterialColor color) {
        return block(RotatedPillarBlock::new, woodProperties(color));
    }

    public static Supplier<RotatedPillarBlock> wood(final MaterialColor color, Supplier<RegistryObject<RotatedPillarBlock>> stripped) {
        return block(properties -> new TropicraftLogBlock(properties, () -> stripped.get().get()), woodProperties(color));
    }

    private static BlockBehaviour.Properties woodProperties(MaterialColor color) {
        return prop(Material.WOOD, color).strength(2.0F).sound(SoundType.WOOD);
    }

    public static Supplier<StairBlock> stairs(final RegistryObject<? extends Block> source) {
        return block(p -> new StairBlock(source.lazyMap(Block::defaultBlockState), p), lazyProp(source));
    }

    public static Supplier<SlabBlock> slab(final Supplier<? extends Block> source) {
        return block(SlabBlock::new, lazyProp(source));
    }

    public static Supplier<LeavesBlock> leaves(boolean decay) {
        return block(decay ? LeavesBlock::new : TropicraftLeavesBlock::new, lazyProp(Blocks.OAK_LEAVES.delegate));
    }

    public static Supplier<LeavesBlock> mangroveLeaves(Supplier<RegistryObject<PropaguleBlock>> propagule) {
        return block(properties -> new MangroveLeavesBlock(properties.randomTicks(), () -> propagule.get().get()), lazyProp(Blocks.OAK_LEAVES.delegate));
    }

    public static Supplier<Block> mangroveRoots() {
        return () -> new MangroveRootsBlock(
                Block.Properties.of(Material.WOOD)
                        .strength(2.0f)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .isRedstoneConductor((state, world, pos) -> false)
                        .hasPostProcess((state, world, pos) -> true)
        );
    }

    @SafeVarargs
    public static Supplier<SaplingBlock> sapling(final AbstractTreeGrower tree, final Supplier<? extends Block>... validPlantBlocks) {
        return block(p -> new SaplingBlock(tree, p) {
            @Override
            public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
                if (validPlantBlocks == null || validPlantBlocks.length == 0) {
                    return super.canSurvive(pState, pLevel, pPos);
                } else {
                    BlockPos blockpos = pPos.below();
                    return this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);
                }
            }

            @Override
            protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
                final Block block = state.getBlock();
                if (validPlantBlocks == null || validPlantBlocks.length == 0) {
                    return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND;
                } else {
                    return Arrays.stream(validPlantBlocks).map(Supplier::get).anyMatch(b -> b == block);
                }
            }
        }, lazyProp(Blocks.OAK_SAPLING.delegate));
    }

    public static Supplier<SaplingBlock> waterloggableSapling(final AbstractTreeGrower tree) {
        return block(p -> new WaterloggableSaplingBlock(tree, p), lazyProp(Blocks.OAK_SAPLING.delegate));
    }

    public static Supplier<PropaguleBlock> propagule(final AbstractTreeGrower tree) {
        return block(p -> new PropaguleBlock(tree, p), lazyProp(Blocks.OAK_SAPLING.delegate));
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
        return block(p -> new BongoDrumBlock(bongoSize, p), woodProperties(MaterialColor.TERRACOTTA_WHITE));
    }
    
    public static Supplier<FlowerPotBlock> pot(final Supplier<FlowerPotBlock> emptyPot, final Supplier<? extends Block> flower, final Supplier<Block.Properties> properties) {
        return block(p -> new FlowerPotBlock(emptyPot, flower, p), properties);
    }

    public static Supplier<FlowerPotBlock> tropicraftPot() {
        return pot(null, Blocks.AIR.delegate, lazyProp(Material.DECORATION).then(p -> p.strength(0.2F, 5.0F).sound(SoundType.BAMBOO)));
    }

    public static Supplier<FlowerPotBlock> tropicraftPot(final Supplier<? extends Block> block) {
        return pot(TropicraftBlocks.BAMBOO_FLOWER_POT, block, lazyProp(Material.DECORATION).then(p -> p.strength(0.2F, 5.0F).sound(SoundType.BAMBOO)));
    }

    public static Supplier<FlowerPotBlock> vanillaPot(final Supplier<? extends Block> block) {
        return pot(() -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), block, lazyProp(Blocks.FLOWER_POT.delegate));
    }

    private static Block.Properties prop(final Material material) {
        return Block.Properties.of(material);
    }

    private static Block.Properties prop(final Material material, final MaterialColor color) {
        return Block.Properties.of(material, color);
    }

    private static Block.Properties prop(final Material material, final Function<BlockState, MaterialColor> color) {
        return Block.Properties.of(material, color);
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
            return Block.Properties.copy(source.get());
        };
    }
}
