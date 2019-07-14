package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;

public class TropicraftBlocks {
    public static final Block CHUNK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(2, 30));
    public static final Block AZURITE_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block EUDIALYTE_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block MANGANESE_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block SHAKA_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block ZIRCON_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));

    public static final Block AZURITE_BLOCK = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block EUDIALYTE_BLOCK = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block ZIRCON_BLOCK = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));

    public static final Block ACAI_VINE = Builder.flower();
    public static final Block ANEMONE = Builder.flower();
    public static final Block BROMELIAD = Builder.flower();
    public static final Block CANNA = Builder.flower();
    public static final Block COMMELINA_DIFFUSA = Builder.flower();
    public static final Block CROCOSMIA = Builder.flower();
    public static final Block CROTON = Builder.flower();
    public static final Block DRACAENA = Builder.flower();
    public static final Block FERN = Builder.flower();
    public static final Block FOLIAGE = Builder.flower();
    public static final Block MAGIC_MUSHROOM = Builder.flower();
    public static final Block ORANGE_ANTHURIUM = Builder.flower();
    public static final Block ORCHID = Builder.flower();
    public static final Block PATHOS = Builder.flower();
    public static final Block RED_ANTHURIUM = Builder.flower();

    public static final Block PURIFIED_SAND = Builder.sand(MaterialColor.SAND);
    public static final Block CORAL_SAND = Builder.sand(MaterialColor.PINK);
    public static final Block FOAMY_SAND = Builder.sand(MaterialColor.GREEN);
    public static final Block VOLCANIC_SAND = Builder.sand(MaterialColor.BLACK);
    public static final Block MINERAL_SAND = Builder.sand(MaterialColor.SAND);

    public static final Block BAMBOO_BUNDLE = Builder.bundle(Block.Properties.create(Material.WOOD).sound(SoundType.PLANT));
    public static final Block THATCH_BUNDLE = Builder.bundle(Block.Properties.create(Material.WOOD).sound(SoundType.PLANT));

    public static final Block MAHOGANY_PLANKS = Builder.plank(MaterialColor.BROWN);
    public static final Block PALM_PLANKS = Builder.plank(MaterialColor.WOOD);
    public static final Block MAHOGANY_LOG = Builder.log(MaterialColor.WOOD, MaterialColor.BROWN);
    public static final Block PALM_LOG = Builder.log(MaterialColor.WOOD, MaterialColor.BROWN);

    public static final Block PALM_STAIRS = Builder.stairs(PALM_PLANKS.getDefaultState(), Material.WOOD, MaterialColor.BROWN, BlockRenderLayer.SOLID);
    public static final Block MAHOGANY_STAIRS = Builder.stairs(MAHOGANY_PLANKS.getDefaultState(), Material.WOOD, MaterialColor.BROWN, BlockRenderLayer.SOLID);
    public static final Block THATCH_STAIRS = Builder.stairs(THATCH_BUNDLE.getDefaultState(), Material.WOOD, MaterialColor.YELLOW, BlockRenderLayer.SOLID);
    public static final Block THATCH_STAIRS_FUZZY = Builder.stairs(THATCH_BUNDLE.getDefaultState(), Material.WOOD, MaterialColor.YELLOW, BlockRenderLayer.CUTOUT_MIPPED);
    public static final Block BAMBOO_STAIRS = Builder.stairs(BAMBOO_BUNDLE.getDefaultState(), Material.BAMBOO, MaterialColor.YELLOW, BlockRenderLayer.SOLID);
    public static final Block CHUNK_STAIRS = Builder.stairs(CHUNK.getDefaultState(), Material.ROCK, MaterialColor.BLACK, BlockRenderLayer.SOLID);

    public static final Block COCONUT = new CoconutBlock(Block.Properties.create(Material.GOURD).hardnessAndResistance(2.0f));

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            registerBlockDefault(event, "chunk", CHUNK);
            // TODO oredict these
            // TODO datafix these
            registerBlockDefault(event, "azurite_ore", AZURITE_ORE);
            registerBlockDefault(event, "eudialyte_ore", EUDIALYTE_ORE);
            registerBlockDefault(event, "manganese_ore", MANGANESE_ORE);
            registerBlockDefault(event, "shaka_ore", SHAKA_ORE);
            registerBlockDefault(event, "zircon_ore", ZIRCON_ORE);
            registerBlockDefault(event, "azurite_block", AZURITE_BLOCK);
            registerBlockDefault(event, "eudialyte_block", EUDIALYTE_BLOCK);
            registerBlockDefault(event, "zircon_block", ZIRCON_BLOCK);
            registerBlockDefault(event, "acai_vine", ACAI_VINE);
            registerBlockDefault(event, "anemone", ANEMONE);
            registerBlockDefault(event, "bromeliad", BROMELIAD);
            registerBlockDefault(event, "canna", CANNA);
            registerBlockDefault(event, "commelina_diffusa", COMMELINA_DIFFUSA);
            registerBlockDefault(event, "crocosmia", CROCOSMIA);
            registerBlockDefault(event, "croton", CROTON);
            registerBlockDefault(event, "dracaena", DRACAENA);
            registerBlockDefault(event, "fern", FERN);
            registerBlockDefault(event, "foliage", FOLIAGE);
            registerBlockDefault(event, "magic_mushroom", MAGIC_MUSHROOM);
            registerBlockDefault(event, "orange_anthurium", ORANGE_ANTHURIUM);
            registerBlockDefault(event, "orchid", ORCHID);
            registerBlockDefault(event, "pathos", PATHOS);
            registerBlockDefault(event, "red_anthurium", RED_ANTHURIUM);
            registerBlockDefault(event, "purified_sand", PURIFIED_SAND);
            registerBlockDefault(event, "coral_sand", CORAL_SAND);
            registerBlockDefault(event, "foamy_sand", FOAMY_SAND);
            registerBlockDefault(event, "volcanic_sand", VOLCANIC_SAND);
            registerBlockDefault(event, "mineral_sand", MINERAL_SAND);
            registerBlockDefault(event, "bamboo_bundle", BAMBOO_BUNDLE);
            registerBlockDefault(event, "thatch_bundle", THATCH_BUNDLE);
            registerBlockDefault(event, "mahogany_planks", MAHOGANY_PLANKS);
            registerBlockDefault(event, "palm_planks", PALM_PLANKS);
            registerBlockDefault(event, "mahogany_log", MAHOGANY_LOG);
            registerBlockDefault(event, "palm_log", PALM_LOG);
            registerBlockDefault(event, "palm_stairs", PALM_STAIRS);
            registerBlockDefault(event, "mahogany_stairs", MAHOGANY_STAIRS);
            registerBlockDefault(event, "thatch_stairs", THATCH_STAIRS);
            registerBlockDefault(event, "thatch_stairs_fuzzy", THATCH_STAIRS_FUZZY);
            registerBlockDefault(event, "bamboo_stairs", BAMBOO_STAIRS);
            registerBlockDefault(event, "chunk_stairs", CHUNK_STAIRS);
            registerBlockDefault(event, "coconut", COCONUT);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            registerItemDefault(event, CHUNK);
            registerItemDefault(event, AZURITE_ORE);
            registerItemDefault(event, EUDIALYTE_ORE);
            registerItemDefault(event, ZIRCON_ORE);
            registerItemDefault(event, MANGANESE_ORE);
            registerItemDefault(event, SHAKA_ORE);
            registerItemDefault(event, AZURITE_BLOCK);
            registerItemDefault(event, EUDIALYTE_BLOCK);
            registerItemDefault(event, ZIRCON_BLOCK);
            registerItemDefault(event, ACAI_VINE);
            registerItemDefault(event, ANEMONE);
            registerItemDefault(event, BROMELIAD);
            registerItemDefault(event, CANNA);
            registerItemDefault(event, COMMELINA_DIFFUSA);
            registerItemDefault(event, CROCOSMIA);
            registerItemDefault(event, CROTON);
            registerItemDefault(event, DRACAENA);
            registerItemDefault(event, FERN);
            registerItemDefault(event, FOLIAGE);
            registerItemDefault(event, MAGIC_MUSHROOM);
            registerItemDefault(event, ORANGE_ANTHURIUM);
            registerItemDefault(event, ORCHID);
            registerItemDefault(event, PATHOS);
            registerItemDefault(event, RED_ANTHURIUM);
            registerItemDefault(event, PURIFIED_SAND);
            registerItemDefault(event, CORAL_SAND);
            registerItemDefault(event, FOAMY_SAND);
            registerItemDefault(event, VOLCANIC_SAND);
            registerItemDefault(event, MINERAL_SAND);
            registerItemDefault(event, BAMBOO_BUNDLE);
            registerItemDefault(event, THATCH_BUNDLE);
            registerItemDefault(event, MAHOGANY_PLANKS);
            registerItemDefault(event, PALM_PLANKS);
            registerItemDefault(event, MAHOGANY_LOG);
            registerItemDefault(event, PALM_LOG);
            registerItemDefault(event, PALM_STAIRS);
            registerItemDefault(event, MAHOGANY_STAIRS);
            registerItemDefault(event, THATCH_STAIRS);
            registerItemDefault(event, THATCH_STAIRS_FUZZY);
            registerItemDefault(event, BAMBOO_STAIRS);
            registerItemDefault(event, CHUNK_STAIRS);
            registerItemDefault(event, COCONUT);
        }

        private static void registerBlockDefault(final RegistryEvent.Register<Block> event, final String name, final Block block) {
            event.getRegistry().register(block.setRegistryName(new ResourceLocation(Constants.MODID, name)));
        }

        private static Item registerItemDefault(final RegistryEvent.Register<Item> event, final Block block) {
            return registerItem(event, block, Tropicraft.TROPICRAFT_ITEM_GROUP);
        }

        private static Item registerItem(final RegistryEvent.Register<Item> event, final Block block, final ItemGroup itemGroup) {
            final Item itemBlock = new BlockItem(block, new Item.Properties().group(itemGroup)).setRegistryName(block.getRegistryName());
            event.getRegistry().register(itemBlock);
            return itemBlock;
        }
    }
}
