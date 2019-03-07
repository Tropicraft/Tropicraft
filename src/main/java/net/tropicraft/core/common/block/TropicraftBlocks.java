package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

public class TropicraftBlocks {
    public static final Block chunkBlock = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(2, 30));
    public static final Block azuriteOre = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block eudialyteOre = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block manganeseOre = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block shakaOre = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block zirconOre = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));

    public static final Block azuriteBlock = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block eudialyteBlock = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block zirconBlock = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            registerBlockDefault(event, "chunk", chunkBlock);
            // TODO oredict these
            // TODO datafix these
            registerBlockDefault(event, "azurite_ore", azuriteOre);
            registerBlockDefault(event, "eudialyte_ore", eudialyteOre);
            registerBlockDefault(event, "manganese_ore", manganeseOre);
            registerBlockDefault(event, "shaka_ore", shakaOre);
            registerBlockDefault(event, "zircon_ore", zirconOre);
            registerBlockDefault(event, "azurite_block", azuriteBlock);
            registerBlockDefault(event, "eudialyte_block", eudialyteBlock);
            registerBlockDefault(event, "zircon_block", zirconBlock);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            registerItemDefault(event, chunkBlock);
            registerItemDefault(event, azuriteOre);
            registerItemDefault(event, eudialyteOre);
            registerItemDefault(event, manganeseOre);
            registerItemDefault(event, shakaOre);
            registerItemDefault(event, zirconOre);
            registerItemDefault(event, azuriteBlock);
            registerItemDefault(event, eudialyteBlock);
            registerItemDefault(event, zirconBlock);
        }

        private static void registerBlockDefault(final RegistryEvent.Register<Block> event, final String name, final Block block) {
            event.getRegistry().register(block.setRegistryName(new ResourceLocation(Constants.MODID, name)));
        }

        private static Item registerItemDefault(final RegistryEvent.Register<Item> event, final Block block) {
            final Item itemBlock = new ItemBlock(block, new Item.Properties()).setRegistryName(block.getRegistryName());
            event.getRegistry().register(itemBlock);
            return itemBlock;
        }
    }
}
