package net.tropicraft.core.registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.config.TropicsConfigs;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.enums.TropicraftLogs;

@Mod.EventBusSubscriber
public class CraftingRegistry {

    @SubscribeEvent
    public static void init(RegistryEvent.Register<IRecipe> event) {
        addRecipes(event.getRegistry());
    }

    private static void addRecipes(IForgeRegistry<IRecipe> registry) {
        // Thatch bundle (4 shoots make 2 bundles)
        createRecipe(true, new ItemStack(BlockRegistry.bundles, 2, 0), new Object[] {
                "XX", "XX",
                'X', "sugarcane"
        });

        // And back
        createRecipe(true, new ItemStack(Items.REEDS, 4), new Object[] {
                "XX", "XX",
                'X', new ItemStack(BlockRegistry.bundles, 1, 1)
        });

        // Bamboo bundle (4 shoots make 2 bundles)
        createRecipe(true, new ItemStack(BlockRegistry.bundles, 2, 1), new Object[] {
                "XX", "XX",
                'X', ItemRegistry.bambooShoot
        });

        // And back
        createRecipe(true, new ItemStack(ItemRegistry.bambooShoot, 4), new Object[] {
                "XX", "XX",
                'X', new ItemStack(BlockRegistry.bundles, 1, 0)
        });

        // Thatch stairs - thatch bundles
        createRecipe(true, new ItemStack(BlockRegistry.thatchStairs, 4), new Object[] {
                "X  ", "XX ", "XXX",
                'X', new ItemStack(BlockRegistry.bundles, 1, 0)
        });

        // Bamboo stairs - bamboo bundles
        createRecipe(true, new ItemStack(BlockRegistry.bambooStairs, 4), new Object[] {
                "X  ", "XX ", "XXX",
                'X', new ItemStack(BlockRegistry.bundles, 1, 1)
        });

        // Chunk stairs
        createRecipe(true, new ItemStack(BlockRegistry.chunkStairs, 4), new Object[] {
                "X  ", "XX ", "XXX",
                'X', BlockRegistry.chunk
        });

        // Bamboo stick
        createRecipe(true, new ItemStack(ItemRegistry.bambooStick, 4), new Object[] {
                "#", "#",
                '#', ItemRegistry.bambooShoot
        });

        // Zircon pickaxe
        createRecipe(true, new ItemStack(ItemRegistry.pickaxeZircon), new Object[] {
                "XXX", " I ", " I ",
                'X', new ItemStack(ItemRegistry.zircon, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Zircon axe
        createRecipe(true, new ItemStack(ItemRegistry.axeZircon), new Object[] {
                "XX", "XI", " I",
                'X', new ItemStack(ItemRegistry.zircon, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Zircon hoe
        createRecipe(true, new ItemStack(ItemRegistry.hoeZircon), new Object[] {
                "XX", " I", " I",
                'X', new ItemStack(ItemRegistry.zircon, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Zircon sword
        createRecipe(true, new ItemStack(ItemRegistry.swordZircon), new Object[] {
                "X", "X", "I",
                'X', new ItemStack(ItemRegistry.zircon, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Zircon shovel
        createRecipe(true, new ItemStack(ItemRegistry.shovelZircon), new Object[] {
                "X", "I", "I",
                'X', new ItemStack(ItemRegistry.zircon, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Eudialyte pickaxe
        createRecipe(true, new ItemStack(ItemRegistry.pickaxeEudialyte), new Object[] {
                "XXX", " I ", " I ",
                'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Eudialyte axe
        createRecipe(true, new ItemStack(ItemRegistry.axeEudialyte), new Object[] {
                "XX", "XI", " I",
                'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Eudialyte hoe
        createRecipe(true, new ItemStack(ItemRegistry.hoeEudialyte), new Object[] {
                "XX", " I", " I",
                'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Eudialyte sword
        createRecipe(true, new ItemStack(ItemRegistry.swordEudialyte), new Object[] {
                "X", "X", "I",
                'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Eudialyte shovel
        createRecipe(true, new ItemStack(ItemRegistry.shovelEudialyte), new Object[] {
                "X", "I", "I",
                'X', new ItemStack(ItemRegistry.eudialyte, 1, 0),
                'I', ItemRegistry.bambooStick
        });

        // Bamboo chest
        createRecipe(true, new ItemStack(BlockRegistry.bambooChest, 1), new Object[] {
                "XXX", "X X", "XXX",
                'X', ItemRegistry.bambooShoot
        });

        // Scale helmet
        createRecipe(true, new ItemStack(ItemRegistry.scaleHelmet, 1), new Object[]{
                "XXX", "X X",
                'X', ItemRegistry.scale
        });

        // Scale chestplate
        createRecipe(true, new ItemStack(ItemRegistry.scaleChestplate, 1), new Object[]{
                "X X", "XXX", "XXX",
                'X', ItemRegistry.scale
        });

        // Scale leggings
        createRecipe(true, new ItemStack(ItemRegistry.scaleLeggings, 1), new Object[]{
                "XXX", "X X", "X X",
                'X', ItemRegistry.scale
        });

        // Scale boots
        createRecipe(true, new ItemStack(ItemRegistry.scaleBoots, 1), new Object[]{
                "X X", "X X",
                'X', ItemRegistry.scale
        });

        // Bamboo mug
        createRecipe(true, new ItemStack(ItemRegistry.bambooMug, 1), new Object[]{
                "X X", "X X", "XXX",
                'X', ItemRegistry.bambooShoot
        });

        // Tiki torch (coal) - diagonal recipe
        createRecipe(true, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
                "  Y", " X ", "X  ",
                'Y', Items.COAL,
                'X', ItemRegistry.bambooStick
        });

        // Tiki torch (coal) - vertical recipe
        createRecipe(true, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
                " Y ", " X ", " X ",
                'Y', Items.COAL,
                'X', ItemRegistry.bambooStick
        });

        // Tiki torch (charcoal) - diagonal recipe
        createRecipe(false, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
                "  Y", " X ", "X  ",
                'Y', new ItemStack(Items.COAL, 1, 1),
                'X', ItemRegistry.bambooStick
        });

        // Tiki torch (charcoal) - vertical recipe
        createRecipe(false, new ItemStack(BlockRegistry.tikiTorch, 2, 2), new Object[]{
                " Y ", " X ", " X ",
                'Y', new ItemStack(Items.COAL, 1, 1),
                'X', ItemRegistry.bambooStick
        });

        // Pina Colada
        createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
                ItemRegistry.coconutChunk,
                new ItemStack(BlockRegistry.pineapple),
                ItemRegistry.bambooMug
        });

        // Pina Colada
        createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
                ItemRegistry.coconutChunk,
                ItemRegistry.pineappleCubes,
                ItemRegistry.bambooMug
        });

        // Pina Colada
        createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
                new ItemStack(BlockRegistry.coconut),
                ItemRegistry.pineappleCubes,
                ItemRegistry.bambooMug
        });

        // Pina Colada
        createShapelessRecipe(true, MixerRecipes.getItemStack(Drink.pinaColada), new Object[]{
                new ItemStack(BlockRegistry.coconut),
                new ItemStack(BlockRegistry.pineapple),
                ItemRegistry.bambooMug
        });

        // Flower Pot
        createRecipe(true, new ItemStack(ItemRegistry.flowerPot), new Object[] {
                "# #", " # ", '#', ItemRegistry.bambooShoot
        });

        // Bamboo Door
        createRecipe(true, new ItemStack(ItemRegistry.bambooDoor), new Object[]{
                "XX", "YY", "XX", 'X', new ItemStack(BlockRegistry.bundles, 1, 1),
                'Y', new ItemStack(BlockRegistry.bundles, 1, 0)
        });

        // Coffee bean -> berry
        createShapelessRecipe(true, new ItemStack(ItemRegistry.coffeeBeans), new Object[]{
                new ItemStack(ItemRegistry.coffeeBeans, 1, 2)
        });

        // planks -> logs
        int mahogany_meta = TropicraftLogs.MAHOGANY.getMeta();
        int palm_meta = TropicraftLogs.PALM.getMeta();

        createShapelessRecipe(true, new ItemStack(BlockRegistry.planks, 4, palm_meta), new Object[] {
                new ItemStack(BlockRegistry.logs, 1, palm_meta)
        });

        createShapelessRecipe(true, new ItemStack(BlockRegistry.planks, 4, mahogany_meta), new Object[] {
                new ItemStack(BlockRegistry.logs, 1, mahogany_meta)
        });

        // Bamboo slabs
        createRecipe(true, new ItemStack(BlockRegistry.slabs, 6, 0), new Object[] {
                "XXX",
                'X', new ItemStack(BlockRegistry.bundles, 1, 1)
        });

        // Thatch slabs
        createRecipe(true, new ItemStack(BlockRegistry.slabs, 6, 1), new Object[] {
                "XXX",
                'X', new ItemStack(BlockRegistry.bundles, 1, 0)
        });

        // Chunk slabs
        createRecipe(true, new ItemStack(BlockRegistry.slabs, 6, 2), new Object[] {
                "XXX",
                'X', BlockRegistry.chunk
        });

        // Palm slabs
        createRecipe(true, new ItemStack(BlockRegistry.slabs, 6, 3), new Object[] {
                "XXX",
                'X', new ItemStack(BlockRegistry.planks, 1, 1)
        });

        // Small bongo
        createRecipe(true, new ItemStack(BlockRegistry.bongo, 1, 0), new Object[] {
            "Y", "X", "X",
            'X', new ItemStack(BlockRegistry.planks, 1, 0),
            'Y', new ItemStack(ItemRegistry.iguanaLeather)
        });

        // Medium bongo
        createRecipe(true, new ItemStack(BlockRegistry.bongo, 1, 1), new Object[] {
            "YY", "XX", "XX",
            'X', new ItemStack(BlockRegistry.planks, 1, 0),
            'Y', new ItemStack(ItemRegistry.iguanaLeather)
        });

        // Large bongo
        createRecipe(true, new ItemStack(BlockRegistry.bongo, 1, 2), new Object[] {
            "YYY", "XXX", "XXX",
            'X', new ItemStack(BlockRegistry.planks, 1, 0),
            'Y', new ItemStack(ItemRegistry.iguanaLeather)
        });

        // Bamboo Fence
        createRecipe(true, new ItemStack(BlockRegistry.bambooFence, 2), new Object[]{
            "XXX", "XXX",
            'X', ItemRegistry.bambooStick
        });

        // Bamboo Fence Gate
        createRecipe(true, new ItemStack(BlockRegistry.bambooFenceGate, 1), new Object[]{
            "XIX", "XIX",
            'X', ItemRegistry.bambooStick,
            'I', new ItemStack(BlockRegistry.bundles, 1, 1)
        });

        // Thatch Fence
        createRecipe(true, new ItemStack(BlockRegistry.thatchFence, 2), new Object[]{
            "XXX", "XXX",
            'X', new ItemStack(BlockRegistry.bundles, 1, 0)
        });

        // Thatch Fence Gate
        createRecipe(true, new ItemStack(BlockRegistry.thatchFenceGate, 1), new Object[]{
            "XIX", "XIX",
            'X', ItemRegistry.bambooStick,
            'I', new ItemStack(BlockRegistry.bundles, 1, 0)
        });

        // Chunk Fence
        createRecipe(true, new ItemStack(BlockRegistry.chunkFence, 2), new Object[]{
            "XXX", "XXX",
            'X', BlockRegistry.chunk
        });

        // Chunk Fence Gate
        createRecipe(true, new ItemStack(BlockRegistry.chunkFenceGate, 1), new Object[]{
            "XIX", "XIX",
            'X', ItemRegistry.bambooStick,
            'I', BlockRegistry.chunk
        });

        // Palm Fence
        createRecipe(true, new ItemStack(BlockRegistry.palmFence, 2), new Object[]{
            "XXX", "XXX",
            'X', new ItemStack(BlockRegistry.planks, 1, 1)
        });

        // Palm Fence Gate
        createRecipe(true, new ItemStack(BlockRegistry.palmFenceGate, 1), new Object[]{
            "XIX", "XIX",
            'X', Items.STICK,
            'I', new ItemStack(BlockRegistry.planks, 1, 1)
        });

        // Mahogany Fence
        createRecipe(true, new ItemStack(BlockRegistry.mahoganyFence, 2), new Object[]{
            "XXX", "XXX",
            'X', new ItemStack(BlockRegistry.planks, 1, 0)
        });

        // Mahogany Fence Gate
        createRecipe(true, new ItemStack(BlockRegistry.mahoganyFenceGate, 1), new Object[]{
            "XIX", "XIX",
            'X', Items.STICK,
            'I', new ItemStack(BlockRegistry.planks, 1, 0)
        });
        
        // Bamboo fishing rod
        createRecipe(true, new ItemStack(ItemRegistry.fishingRod), new Object[]{
    			"  I",
    			" IX",
    			"I X",

    			'X', Items.STRING,
    			
    			'I', ItemRegistry.bambooStick
    		});

        // Sifter
        Block[] plankBlocks = new Block[] {Blocks.PLANKS, BlockRegistry.planks};
        for (Block plankBlock : plankBlocks) {
            createRecipe(true, new ItemStack(BlockRegistry.sifter), new Object[] {
                    "XXX", "XIX", "XXX",
                    'X', plankBlock,
                    'I', Blocks.GLASS
                });

            createRecipe(true, new ItemStack(BlockRegistry.sifter), new Object[] {
                    "XXX", "XIX", "XXX",
                    'X', plankBlock,
                    'I', Blocks.GLASS_PANE
                });
        }

        // Fishing net
        createRecipe(true, new ItemStack(ItemRegistry.fishingNet), new Object[] {
                "  X", " XI", "XII",
                'X', ItemRegistry.bambooStick,
                'I', Items.STRING
            });

        // List of wool colors to use for chair/float/umbrella
        for (EnumDyeColor dye : EnumDyeColor.values()) {
            createRecipe(true, new ItemStack(ItemRegistry.chair, 1, dye.getDyeDamage()), new Object[] {
                "XIX", "XIX", "XIX",
                'X', ItemRegistry.bambooStick,
                'I', new ItemStack(Blocks.WOOL, 1, dye.getMetadata())
            });

            createRecipe(true, new ItemStack(ItemRegistry.umbrella, 1, dye.getDyeDamage()), new Object[] {
                "XXX", " I ", " I ",
                'X', new ItemStack(Blocks.WOOL, 1, dye.getMetadata()),
                'I', ItemRegistry.bambooStick
            });
        }

        // Spear
        createRecipe(true, new ItemStack(ItemRegistry.bambooSpear, 1), new Object[] {
                "X ", " X",
                'X', ItemRegistry.bambooStick
            });

        // Dagger
        createRecipe(true, new ItemStack(ItemRegistry.dagger), new Object[] {
                "X", "X", "I",
                'X', BlockRegistry.chunk,
                'I', new ItemStack(BlockRegistry.planks, 1, 1)    // Palm planks
            });

        // Portal Enchanter
        createRecipe(true, new ItemStack(ItemRegistry.portalEnchanter, 1), new Object[] {
                "%@#", "#@%", " @ ", '@', ItemRegistry.bambooStick, '#',
                new ItemStack(ItemRegistry.azurite),
                '%', new ItemStack(ItemRegistry.zircon)
            });

        // Portal Enchanter
        createRecipe(true, new ItemStack(ItemRegistry.portalEnchanter, 1), new Object[] {
            "#@%", "%@#", " @ ", '@', ItemRegistry.bambooStick,
            '#', new ItemStack(ItemRegistry.azurite),
            '%', new ItemStack(ItemRegistry.zircon)
        });

        // Water wand
        createRecipe(true, new ItemStack(ItemRegistry.waterWand), new Object[] {
                "  X", " Y ", "Y  ", 'X', new ItemStack(ItemRegistry.azurite), 'Y', Items.GOLD_INGOT
            });
        
        // Tropi Item Frame
        createRecipe(true, new ItemStack(ItemRegistry.bambooItemFrame, 1), new Object[] {
                "###", "#X#", "###", '#', ItemRegistry.bambooShoot, 'X', Items.LEATHER
            });

        // Coconut bomb
        createRecipe(true, new ItemStack(ItemRegistry.coconutBomb, 1), new Object[] {
                " X ", "XYX", " X ",
                'X', "gunpowder",
                'Y', BlockRegistry.coconut
            });

        // Fertilizer
        createRecipe(true, new ItemStack(ItemRegistry.fertilizer, 3), new Object[]{
                "XI",
                'X', new ItemStack(BlockRegistry.flowers, 1, 7),
                'I', new ItemStack(BlockRegistry.flowers, 1, 10)
            });

        // Flower dyes
        createShapelessRecipe(true, new ItemStack(Items.DYE, 4, 5), new Object[]{
                new ItemStack(BlockRegistry.iris)
            });

        createShapelessRecipe(true, new ItemStack(Items.DYE, 2, 1), new Object[]{
            new ItemStack(BlockRegistry.flowers, 1, 6)//r antherium
        });

        createShapelessRecipe(true, new ItemStack(Items.DYE, 2, 14), new Object[]{
            new ItemStack(BlockRegistry.flowers, 1, 5)//o antherium
        });

        createShapelessRecipe(true, new ItemStack(Items.DYE, 2, 12), new Object[]{
            new ItemStack(BlockRegistry.flowers, 1, 0)//fern
        });

        createShapelessRecipe(true, new ItemStack(Items.DYE, 2, 2), new Object[]{
            new ItemStack(BlockRegistry.flowers, 1, 12)//c. diffusa
        });

        createShapelessRecipe(true, new ItemStack(Items.DYE, 2, 11), new Object[]{
            new ItemStack(BlockRegistry.flowers, 1, 3) //canna
        });

        createRecipe(true, new ItemStack(ItemRegistry.encyclopedia, 1), new Object[]{
                "###", "#$#", "###",
                '#', ItemRegistry.bambooShoot,
                '$', Items.BOOK
            });

        // Drink mixer
        createRecipe(true, new ItemStack(BlockRegistry.drinkMixer), new Object[] {
                "XXX", "XYX", "XXX",
                'X', BlockRegistry.chunk,
                'Y', ItemRegistry.bambooMug
            });

        // Air compressor
        createRecipe(true, new ItemStack(BlockRegistry.airCompressor), new Object[] {
                "XXX", "XYX", "XXX",
                'X', BlockRegistry.chunk,
                'Y', ItemRegistry.azurite
            });

        // Pink scuba tank
        createRecipe(true, new ItemStack(ItemRegistry.pinkScubaTank), new Object[] {
                "Y", "X", "X",
                'X', "blockGlassPink",
                'Y', Blocks.LEVER
        });   

        // Yellow scuba tank
        createRecipe(true, new ItemStack(ItemRegistry.yellowScubaTank), new Object[] {
                "Y", "X", "X",
                'X', "blockGlassYellow",
                'Y', Blocks.LEVER
        });   

        // Pink pony bottle
            createRecipe(true, new ItemStack(ItemRegistry.pinkPonyBottle), new Object[] {
                    "Y", "X",
                    'X', "blockGlassPink",
                    'Y', Blocks.LEVER
            });   

        // Yellow pony bottle
            createRecipe(true, new ItemStack(ItemRegistry.yellowPonyBottle), new Object[] {
                    "Y", "X",
                    'X', "blockGlassYellow",
                    'Y', Blocks.LEVER
            });

        // Yellow scuba flippers
            createRecipe(true, new ItemStack(ItemRegistry.yellowFlippers), new Object[] {
                "XX", "YY", "XX",
                'X', "dyeYellow",
                'Y', ItemRegistry.zircon
            });

        // Pink scuba flippers
            createRecipe(true, new ItemStack(ItemRegistry.pinkFlippers), new Object[] {
                "XX", "YY", "XX",
                'X', "dyePink",
                'Y', ItemRegistry.zircon
            });

        // Yellow weight belt
            createRecipe(true, new ItemStack(ItemRegistry.yellowWeightBelt), new Object[] {
                    "XYX",
                    'X', BlockRegistry.chunk,
                    'Y', "dyeYellow"
                });

        // Pink weight belt
            createRecipe(true, new ItemStack(ItemRegistry.pinkWeightBelt), new Object[] {
                "XYX",
                'X', BlockRegistry.chunk,
                'Y', "dyePink"
            });

        // Dive Computer
        createRecipe(true, new ItemStack(ItemRegistry.diveComputer), new Object[] {
                "XYX", "XXX",
                'X', BlockRegistry.chunk,
                'Y', ItemRegistry.azurite
            });

        // Pink BCD
            createRecipe(true, new ItemStack(ItemRegistry.pinkBCD), new Object[] {
                "X X", "YXY", "XXX",
                'X', ItemRegistry.zircon,
                'Y', "dyePink"
            });

        // Yellow BCD
            createRecipe(true, new ItemStack(ItemRegistry.yellowBCD), new Object[] {
                "X X", "YXY", "XXX",
                'X', ItemRegistry.zircon,
                'Y', "dyeYellow"
            });

        // Pink Regulator
            createRecipe(true, new ItemStack(ItemRegistry.pinkRegulator), new Object[] {
                " X ", "XYX", " X ",
                'X', BlockRegistry.chunk,
                'Y', "dyePink"
            });

        // Yellow Regulator
            createRecipe(true, new ItemStack(ItemRegistry.yellowRegulator), new Object[] {
                " X ", "XYX", " X ",
                'X', BlockRegistry.chunk,
                'Y', "dyeYellow"
            });

        // Pink Scuba Goggles
        createRecipe(true, new ItemStack(ItemRegistry.pinkScubaGoggles), new Object[] {
            "YYY", "X X", " Z ",
            'X', "blockGlass",
            'Y', ItemRegistry.zircon,
            'Z', ItemRegistry.pinkRegulator
        });

        // Yellow Scuba Goggles
        createRecipe(true, new ItemStack(ItemRegistry.yellowScubaGoggles), new Object[] {
            "YYY", "X X", " Z ",
            'X', Blocks.GLASS,
            'Y', ItemRegistry.zircon,
            'Z', ItemRegistry.yellowRegulator
        });

        // Pink Scuba Chestplate Gear
        createRecipe(true, new ItemStack(ItemRegistry.pinkChestplateGear), new Object[] {
                "Y Y", "YXY", "YZY",
                'X', ItemRegistry.pinkBCD,
                'Y', ItemRegistry.zircon,
                'Z', ItemRegistry.pinkWeightBelt
            });

        // Yellow Scuba Chestplate Gear
        createRecipe(true, new ItemStack(ItemRegistry.yellowChestplateGear), new Object[] {
                "Y Y", "YXY", "YZY",
                'X', ItemRegistry.yellowBCD,
                'Y', ItemRegistry.zircon,
                'Z', ItemRegistry.yellowWeightBelt
            });

        createFullSingleBlockRecipe(BlockRegistry.oreBlock, ItemRegistry.azurite, 0);
        createFullSingleBlockRecipe(BlockRegistry.oreBlock, ItemRegistry.eudialyte, 1);
        createFullSingleBlockRecipe(BlockRegistry.oreBlock, ItemRegistry.zircon, 2);

        createOreRecipe(ItemRegistry.azurite, 0);
        createOreRecipe(ItemRegistry.eudialyte, 1);
        createOreRecipe(ItemRegistry.zircon, 2);

        GameRegistry.addSmelting(ItemRegistry.frogLeg, new ItemStack(ItemRegistry.cookedFrogLeg), 0.35F);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.coffeeBeans, 1, 0), new ItemStack(ItemRegistry.coffeeBeans, 1, 1), 0.35F);
        GameRegistry.addSmelting(BlockRegistry.sands, new ItemStack(Blocks.GLASS), 4);
        GameRegistry.addSmelting(ItemRegistry.freshMarlin, new ItemStack(ItemRegistry.searedMarlin), 6);
        GameRegistry.addSmelting((BlockRegistry.logs), new ItemStack(Items.COAL, 1, 1), 3); // metadata 1 = charcoal
        
        // Custom fuel burn times!
        GameRegistry.registerFuelHandler(new IFuelHandler() {
            @Override
            public int getBurnTime(ItemStack fuel) {
                // Palm slabs
                if (fuel.getItem() != null && fuel.getItem() instanceof ItemBlock
                        && Block.getBlockFromItem(fuel.getItem()) == BlockRegistry.slabs && fuel.getItemDamage() == 3) {
                    return 150;
                }
                return 0;
            }
        });
    }

    private static void createFullSingleBlockRecipe(Block out, Item ingredient, int blockDmg) {
        createRecipe(true, new ItemStack(out, 1, blockDmg), new Object[] {
                "%%%", "%%%", "%%%",
                '%', ingredient
        });
    }

    private static void createOreRecipe(Item oreItem, int blockDmg) {
        createShapelessRecipe(true, new ItemStack(oreItem, 9), new Object[] {
                new ItemStack(BlockRegistry.oreBlock, 1, blockDmg)
        });
    }

    @SideOnly(Side.CLIENT)
    public static void addToEncyclopedia(ItemStack itemstack, Object obj[]) {
        Tropicraft.encyclopedia.includeRecipe(itemstack, obj);
    }

    public static void createRecipe(boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
        if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            addToEncyclopedia(itemstack, obj);
        }

        addShapedRecipe(itemstack, obj);
    }

    public static void createShapelessRecipe(boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
        //cannot add shapeless recipes to the encyclopedia yet
        /*if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			addToEncyclopedia(itemstack, obj);
		}*/
        addShapelessRecipe(itemstack, obj);
    }
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static File RECIPE_DIR = null;
	private static final Set<String> USED_OD_NAMES = new TreeSet<>();

	private static void setupDir() {
		if (RECIPE_DIR == null) {
			RECIPE_DIR = TropicsConfigs.getConfig().getConfigFile().toPath().resolve("../recipes/").toFile();
		}

		if (!RECIPE_DIR.exists()) {
			RECIPE_DIR.mkdir();
		}
	}

	private static void addShapedRecipe(ItemStack result, Object... components) {
		setupDir();

		// GameRegistry.addShapedRecipe(result, components);

		Map<String, Object> json = new HashMap<>();

		List<String> pattern = new ArrayList<>();
		int i = 0;
		while (i < components.length && components[i] instanceof String) {
			pattern.add((String) components[i]);
			i++;
		}
		json.put("pattern", pattern);

		boolean isOreDict = false;
		Map<String, Map<String, Object>> key = new HashMap<>();
		Character curKey = null;
		for (; i < components.length; i++) {
			Object o = components[i];
			if (o instanceof Character) {
				if (curKey != null)
					throw new IllegalArgumentException("Provided two char keys in a row");
				curKey = (Character) o;
			} else {
				if (curKey == null)
					throw new IllegalArgumentException("Providing object without a char key");
				if (o instanceof String)
					isOreDict = true;
				key.put(Character.toString(curKey), serializeItem(o));
				curKey = null;
			}
		}
		json.put("key", key);
		json.put("type", isOreDict ? "forge:ore_shaped" : "minecraft:crafting_shaped");
		json.put("result", serializeItem(result));

		// names the json the same name as the output's registry name
		// repeatedly adds _alt if a file already exists
		// janky I know but it works
		String suffix = result.getItem().getHasSubtypes() ? "_" + result.getItemDamage() : "";
		File f = new File(RECIPE_DIR, result.getItem().getRegistryName().getResourcePath() + suffix + ".json");

		while (f.exists()) {
			suffix += "_alt";
			f = new File(RECIPE_DIR, result.getItem().getRegistryName().getResourcePath() + suffix + ".json");
		}

		try (FileWriter w = new FileWriter(f)) {
			GSON.toJson(json, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addShapelessRecipe(ItemStack result, Object... components)
	{
		setupDir();

		// addShapelessRecipe(result, components);

		Map<String, Object> json = new HashMap<>();

		boolean isOreDict = false;
		List<Map<String, Object>> ingredients = new ArrayList<>();
		for (Object o : components) {
			if (o instanceof String)
				isOreDict = true;
			ingredients.add(serializeItem(o));
		}
		json.put("ingredients", ingredients);
		json.put("type", isOreDict ? "forge:ore_shapeless" : "minecraft:crafting_shapeless");
		json.put("result", serializeItem(result));

		// names the json the same name as the output's registry name
		// repeatedly adds _alt if a file already exists
		// janky I know but it works
		String suffix = result.getItem().getHasSubtypes() ? "_" + result.getItemDamage() : "";
		File f = new File(RECIPE_DIR, result.getItem().getRegistryName().getResourcePath() + suffix + ".json");

		while (f.exists()) {
			suffix += "_alt";
			f = new File(RECIPE_DIR, result.getItem().getRegistryName().getResourcePath() + suffix + ".json");
		}


		try (FileWriter w = new FileWriter(f)) {
			GSON.toJson(json, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Object> serializeItem(Object thing) {
		if (thing instanceof Item) {
			return serializeItem(new ItemStack((Item) thing));
		}
		if (thing instanceof Block) {
			return serializeItem(new ItemStack((Block) thing));
		}
		if (thing instanceof ItemStack) {
			ItemStack stack = (ItemStack) thing;
			Map<String, Object> ret = new HashMap<>();
			ret.put("item", stack.getItem().getRegistryName().toString());
			if (stack.getItem().getHasSubtypes() || stack.getItemDamage() != 0) {
				ret.put("data", stack.getItemDamage());
			}
			if (stack.getCount() > 1) {
				ret.put("count", stack.getCount());
			}
			
			if (stack.hasTagCompound()) {
				ret.put("type", "minecraft:item_nbt");
				ret.put("nbt", stack.getTagCompound().toString());
			}

			return ret;
		}
		if (thing instanceof String) {
			Map<String, Object> ret = new HashMap<>();
			USED_OD_NAMES.add((String) thing);
			ret.put("item", "#" + ((String) thing).toUpperCase(Locale.ROOT));
			return ret;
		}

		throw new IllegalArgumentException("Not a block, item, stack, or od name");
	}

	// Call this after you are done generating
	private static void generateConstants() {
		List<Map<String, Object>> json = new ArrayList<>();
		for (String s : USED_OD_NAMES) {
			Map<String, Object> entry = new HashMap<>();
			entry.put("name", s.toUpperCase(Locale.ROOT));
			entry.put("ingredient", ImmutableMap.of("type", "forge:ore_dict", "ore", s));
			json.add(entry);
		}

		try (FileWriter w = new FileWriter(new File(RECIPE_DIR, "_constants.json"))) {
			GSON.toJson(json, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
