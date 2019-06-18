package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            registerItemDefault(event, CHUNK, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, AZURITE_ORE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, EUDIALYTE_ORE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, ZIRCON_ORE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, MANGANESE_ORE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, SHAKA_ORE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, AZURITE_BLOCK, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, EUDIALYTE_BLOCK, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, ZIRCON_BLOCK, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, ACAI_VINE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, ANEMONE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, BROMELIAD, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, CANNA, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, COMMELINA_DIFFUSA, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, CROCOSMIA, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, CROTON, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, DRACAENA, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, FERN, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, FOLIAGE, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, MAGIC_MUSHROOM, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, ORANGE_ANTHURIUM, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, ORCHID, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, PATHOS, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, RED_ANTHURIUM, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, PURIFIED_SAND, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, CORAL_SAND, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, FOAMY_SAND, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, VOLCANIC_SAND, Tropicraft.TROPICRAFT_ITEM_GROUP);
            registerItemDefault(event, MINERAL_SAND, Tropicraft.TROPICRAFT_ITEM_GROUP);
        }

        private static void registerBlockDefault(final RegistryEvent.Register<Block> event, final String name, final Block block) {
            event.getRegistry().register(block.setRegistryName(new ResourceLocation(Constants.MODID, name)));
        }

        private static Item registerItemDefault(final RegistryEvent.Register<Item> event, final Block block, final ItemGroup itemGroup) {
            final Item itemBlock = new BlockItem(block, new Item.Properties().group(itemGroup)).setRegistryName(block.getRegistryName());
            event.getRegistry().register(itemBlock);
            return itemBlock;
        }
    }
}
