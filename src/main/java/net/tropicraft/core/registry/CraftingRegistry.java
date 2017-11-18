package net.tropicraft.core.registry;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.enums.AshenMasks;
import net.tropicraft.core.common.enums.TropicraftCorals;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.common.enums.TropicraftShells;

public class CraftingRegistry {

    public static void preInit() {
        addRecipes();
    }

    private static void addRecipes() {
        // Thatch bundle (4 shoots make 2 bundles)
        createRecipe(true, new ItemStack(BlockRegistry.bundles, 2, 0), new Object[] {
                "XX", "XX",
                'X', Items.REEDS
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

        // Palm planks -> crafting table
        createRecipe(true, new ItemStack(Blocks.CRAFTING_TABLE), new Object[] {
                "XX", "XX",
                'X', new ItemStack(BlockRegistry.planks, 1, 1)
        });

        // Mahogany planks -> crafting table
        createRecipe(true, new ItemStack(Blocks.CRAFTING_TABLE), new Object[] {
                "XX", "XX",
                'X', new ItemStack(BlockRegistry.planks, 1, 0)
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
                'X', Items.GUNPOWDER,
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

        // Color damage values found here https://minecraft.gamepedia.com/Glass
        List<ItemStack> pinkDyes = OreDictionary.getOres("dyePink");
        List<ItemStack> yellowDyes = OreDictionary.getOres("dyeYellow");

        // Pink scuba tank
        for (ItemStack pinkGlass : OreDictionary.getOres("blockGlassPink")) {
            createRecipe(true, new ItemStack(ItemRegistry.pinkScubaTank), new Object[] {
                    "Y", "X", "X",
                    'X', pinkGlass,
                    'Y', Blocks.LEVER
            });   
        }

        // Yellow scuba tank
        for (ItemStack yellowGlass : OreDictionary.getOres("blockGlassYellow")) {
            createRecipe(true, new ItemStack(ItemRegistry.yellowScubaTank), new Object[] {
                    "Y", "X", "X",
                    'X', yellowGlass,
                    'Y', Blocks.LEVER
            });   
        }

        // Pink pony bottle
        for (ItemStack pinkGlass : OreDictionary.getOres("blockGlassPink")) {
            createRecipe(true, new ItemStack(ItemRegistry.pinkPonyBottle), new Object[] {
                    "Y", "X",
                    'X', pinkGlass,
                    'Y', Blocks.LEVER
            });   
        }

        // Yellow pony bottle
        for (ItemStack yellowGlass : OreDictionary.getOres("blockGlassYellow")) {
            createRecipe(true, new ItemStack(ItemRegistry.yellowPonyBottle), new Object[] {
                    "Y", "X",
                    'X', yellowGlass,
                    'Y', Blocks.LEVER
            });
        }

        // Yellow scuba flippers
        for (ItemStack yellowDye : yellowDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.yellowFlippers), new Object[] {
                "XX", "YY", "XX",
                'X', yellowDye,
                'Y', ItemRegistry.zircon
            });
        }

        // Pink scuba flippers
        for (ItemStack dye : pinkDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.pinkFlippers), new Object[] {
                "XX", "YY", "XX",
                'X', dye,
                'Y', ItemRegistry.zircon
            });
        }

        // Yellow weight belt
        for (ItemStack dye : yellowDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.yellowWeightBelt), new Object[] {
                    "XYX",
                    'X', BlockRegistry.chunk,
                    'Y', dye
                });
        }

        // Pink weight belt
        for (ItemStack dye : pinkDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.pinkWeightBelt), new Object[] {
                "XYX",
                'X', BlockRegistry.chunk,
                'Y', dye
            });
        }

        // Dive Computer
        createRecipe(true, new ItemStack(ItemRegistry.diveComputer), new Object[] {
                "XYX", "XXX",
                'X', BlockRegistry.chunk,
                'Y', ItemRegistry.azurite
            });

        // Pink BCD
        for (ItemStack dye : pinkDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.pinkBCD), new Object[] {
                "X X", "YXY", "XXX",
                'X', ItemRegistry.zircon,
                'Y', dye
            });
        }

        // Yellow BCD
        for (ItemStack dye : yellowDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.yellowBCD), new Object[] {
                "X X", "YXY", "XXX",
                'X', ItemRegistry.zircon,
                'Y', dye
            });
        }

        // Pink Regulator
        for (ItemStack dye : pinkDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.pinkRegulator), new Object[] {
                " X ", "XYX", " X ",
                'X', BlockRegistry.chunk,
                'Y', dye
            });
        }

        // Yellow Regulator
        for (ItemStack dye : yellowDyes) {
            createRecipe(true, new ItemStack(ItemRegistry.yellowRegulator), new Object[] {
                " X ", "XYX", " X ",
                'X', BlockRegistry.chunk,
                'Y', dye
            });
        }

        // Pink Scuba Goggles
        createRecipe(true, new ItemStack(ItemRegistry.pinkScubaGoggles), new Object[] {
            "YYY", "X X", " Z ",
            'X', Blocks.GLASS,
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

        // Bamboo ladder
        createRecipe(true, new ItemStack(BlockRegistry.bambooLadder), new Object[] {
                "X X", "XXX", "X X",
                'X', ItemRegistry.bambooStick
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

	/**
	 * Items that should be recognized by the encyclopedia are added here.
	 * The names given MUST match the page names in the encyclopedia text file,
	 * and duplicates here are ok - multiple items can be associated with 1 page.
	 * Ordering doesn't matter, as the page order is determined by the text file
	 * 
	 * Note: Items with metadata values must be added individually (use a loop
	 * if possible)
	 */
	public static void addItemsToEncyclopedia() {
		Tropicraft.encyclopedia.includeItem("acaivine", new ItemStack(BlockRegistry.flowers, 1, 9));
		Tropicraft.encyclopedia.includeItem("anemone", new ItemStack(BlockRegistry.flowers, 1, 4));
		Tropicraft.encyclopedia.includeItem("anthuriumo", new ItemStack(BlockRegistry.flowers, 1, 5));
		Tropicraft.encyclopedia.includeItem("anthuriumr", new ItemStack(BlockRegistry.flowers, 1, 6));

		for (int i = 0; i < AshenMasks.VALUES.length; i++) {
            Tropicraft.encyclopedia.includeItem("ashenmask", new ItemStack(ItemRegistry.maskMap.get(AshenMasks.VALUES[i]), 1, i));
        }

        Tropicraft.encyclopedia.includeItem("azurite", new ItemStack(ItemRegistry.azurite));
        Tropicraft.encyclopedia.includeItem("bamboo", new ItemStack(ItemRegistry.bambooShoot));
        Tropicraft.encyclopedia.includeItem("bamboomug", new ItemStack(ItemRegistry.bambooMug));
        Tropicraft.encyclopedia.includeItem("bambooblock", new ItemStack(BlockRegistry.bundles, 1, 1));
        Tropicraft.encyclopedia.includeItem("bamboochest", new ItemStack(BlockRegistry.bambooChest));
        Tropicraft.encyclopedia.includeItem("bamboodoor", new ItemStack(ItemRegistry.bambooDoor));
        Tropicraft.encyclopedia.includeItem("bamboofence", new ItemStack(BlockRegistry.bambooFence));
        Tropicraft.encyclopedia.includeItem("bamboofencegate", new ItemStack(BlockRegistry.bambooFenceGate));
        Tropicraft.encyclopedia.includeItem("bambooladder", new ItemStack(BlockRegistry.bambooLadder));
        Tropicraft.encyclopedia.includeItem("bamboospear", new ItemStack(ItemRegistry.bambooSpear));
        Tropicraft.encyclopedia.includeItem("bamboostick", new ItemStack(ItemRegistry.bambooStick));

        for (int i = 0; i < 5; i++) {
            Tropicraft.encyclopedia.includeItem("beachchair", new ItemStack(ItemRegistry.chair, 1, i));
            //TODO  Tropicraft.encyclopedia.includeItem("beachfloat", new ItemStack(TCItemRegistry.beachFloat, 1, i));
            /*
             * beachfloat.title = Beach Float
beachfloat.desc  = These uncontrollable floats allow the gentle currents of the tropics to move you. They come in five different colors.
             */
            Tropicraft.encyclopedia.includeItem("beachumbrella", new ItemStack(ItemRegistry.umbrella, 1, i));
        }

        Tropicraft.encyclopedia.includeItem("blackcoffee", MixerRecipes.getItemStack(Drink.blackCoffee));
        Tropicraft.encyclopedia.includeItem("blacksand", new ItemStack(BlockRegistry.sands, 1, 3));
        //TODO Tropicraft.encyclopedia.includeItem("blowgun", new ItemStack(ItemRegistry.blowGun, 1, 0));
        Tropicraft.encyclopedia.includeItem("bromeliad", new ItemStack(BlockRegistry.flowers, 1, 14));
        Tropicraft.encyclopedia.includeItem("caipirinha", MixerRecipes.getItemStack(Drink.caipirinha));
        Tropicraft.encyclopedia.includeItem("canna", new ItemStack(BlockRegistry.flowers, 1, 3));
        Tropicraft.encyclopedia.includeItem("chunkohead", new ItemStack(BlockRegistry.chunk));
        Tropicraft.encyclopedia.includeItem("chunkfence", new ItemStack(BlockRegistry.chunkFence));
        Tropicraft.encyclopedia.includeItem("chunkfencegate", new ItemStack(BlockRegistry.chunkFenceGate));
        Tropicraft.encyclopedia.includeItem("coconut", new ItemStack(BlockRegistry.coconut));
        Tropicraft.encyclopedia.includeItem("coconutchunks", new ItemStack(ItemRegistry.coconutChunk));
        Tropicraft.encyclopedia.includeItem("coconutbomb", new ItemStack(ItemRegistry.coconutBomb));
        Tropicraft.encyclopedia.includeItem("coffeebean", new ItemStack(ItemRegistry.coffeeBeans, 1, 0));
        Tropicraft.encyclopedia.includeItem("commelina", new ItemStack(BlockRegistry.flowers, 1, 0));

        for (int i = 0; i < TropicraftCorals.VALUES.length; i++) {
            Tropicraft.encyclopedia.includeItem("coral", new ItemStack(BlockRegistry.coral, 1, i));
        }

        Tropicraft.encyclopedia.includeItem("coralsand", new ItemStack(BlockRegistry.sands, 1, 1));
        Tropicraft.encyclopedia.includeItem("crocosmia", new ItemStack(BlockRegistry.flowers, 1, 1));
        Tropicraft.encyclopedia.includeItem("croton", new ItemStack(BlockRegistry.flowers, 1, 10));
        //		TODO for (int i = 0; i < ItemCurare.effectNames.length; i++) {
        //			Tropicraft.encyclopedia.includeItem("curare", new ItemStack(TCItemRegistry.curare, 1, i));
        //		}
        //TODO Tropicraft.encyclopedia.includeItem("curarebowl", new ItemStack(BlockRegistry.curareBowl));
        Tropicraft.encyclopedia.includeItem("dagger", new ItemStack(ItemRegistry.dagger));
        //		TODO for (int i = 0; i < ItemCurare.effectNames.length; i++) {
        //			Tropicraft.encyclopedia.includeItem("dart", new ItemStack(TCItemRegistry.dart, 1, i));
        //		}
        Tropicraft.encyclopedia.includeItem("dracaena", new ItemStack(BlockRegistry.flowers, 1, 11));
        Tropicraft.encyclopedia.includeItem("easternisles", new ItemStack(ItemRegistry.recordEasternIsles));
        //  Tropicraft.encyclopedia.includeItem("enchantwand", new ItemStack(TCItemRegistry.enchantWand));
        Tropicraft.encyclopedia.includeItem("encyclopedia", new ItemStack(ItemRegistry.encyclopedia));
        Tropicraft.encyclopedia.includeItem("eudialyte", new ItemStack(ItemRegistry.eudialyte));
        Tropicraft.encyclopedia.includeItem("fern", new ItemStack(BlockRegistry.flowers, 1, 12));
        Tropicraft.encyclopedia.includeItem("fertilizer", new ItemStack(ItemRegistry.fertilizer));
        Tropicraft.encyclopedia.includeItem("fireboots", new ItemStack(ItemRegistry.fireBoots));
        Tropicraft.encyclopedia.includeItem("firechestplate", new ItemStack(ItemRegistry.fireChestplate));
        Tropicraft.encyclopedia.includeItem("firehelm", new ItemStack(ItemRegistry.fireHelmet));
        Tropicraft.encyclopedia.includeItem("fireleggings", new ItemStack(ItemRegistry.fireLeggings));
        //	Tropicraft.encyclopedia.includeItem("firestaff", new ItemStack(TCItemRegistry.staffFire));
        /*
         * firestaff.title = Fire Staff
firestaff.desc = A mystical weapon usually wielded by a Koa Shaman which allows the holder to shoot fireballs that resemble meteors from the Catacombs
         */
        Tropicraft.encyclopedia.includeItem("fishbucket", new ItemStack(ItemRegistry.fishBucket));
        Tropicraft.encyclopedia.includeItem("fishingnet", new ItemStack(ItemRegistry.fishingNet));
        //TODO Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(ItemRegistry.flippers));
        //TODO Tropicraft.encyclopedia.includeItem("flippers", new ItemStack(Items.LEATHER));
        Tropicraft.encyclopedia.includeItem("flowerpot", new ItemStack(ItemRegistry.flowerPot));
        Tropicraft.encyclopedia.includeItem("froglegs", new ItemStack(ItemRegistry.frogLeg));
        Tropicraft.encyclopedia.includeItem("froglegscooked", new ItemStack(ItemRegistry.cookedFrogLeg));
        Tropicraft.encyclopedia.includeItem("frogskin", new ItemStack(ItemRegistry.poisonFrogSkin));
        Tropicraft.encyclopedia.includeItem("froxconch", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.FROX.getMeta()));
        Tropicraft.encyclopedia.includeItem("grapefruit", new ItemStack(ItemRegistry.grapefruit));
        Tropicraft.encyclopedia.includeItem("grapefruitsapling", new ItemStack(BlockRegistry.saplings, 1, 1));
        Tropicraft.encyclopedia.includeItem("greensand", new ItemStack(BlockRegistry.sands, 1, 2));
        //Tropicraft.encyclopedia.includeItem("icestaff", new ItemStack(TCItemRegistry.staffIce));
        Tropicraft.encyclopedia.includeItem("iggyscale", new ItemStack(ItemRegistry.scale));
        Tropicraft.encyclopedia.includeItem("iris", new ItemStack(BlockRegistry.iris));
        //  Tropicraft.encyclopedia.includeItem("journalpage", new ItemStack(TCItemRegistry.journalPage));
        Tropicraft.encyclopedia.includeItem("kapok", new ItemStack(BlockRegistry.leaves, 1, 2));
        //TODO Tropicraft.encyclopedia.includeItem("koachest", new ItemStack(BlockRegistry.koaChest));
        //TODO Tropicraft.encyclopedia.includeItem("leafball", new ItemStack(ItemRegistry.leafBall));
        //Tropicraft.encyclopedia.includeItem("leather", new ItemStack(Items.LEATHER));
        Tropicraft.encyclopedia.includeItem("lemon", new ItemStack(ItemRegistry.lemon));
        Tropicraft.encyclopedia.includeItem("lemonade", MixerRecipes.getItemStack(Drink.lemonade));
        Tropicraft.encyclopedia.includeItem("lemonsapling", new ItemStack(BlockRegistry.saplings, 1, 2));
        Tropicraft.encyclopedia.includeItem("lime", new ItemStack(ItemRegistry.lime));
        Tropicraft.encyclopedia.includeItem("limeade", MixerRecipes.getItemStack(Drink.limeade));
        Tropicraft.encyclopedia.includeItem("limesapling", new ItemStack(BlockRegistry.saplings, 1, 4));
        Tropicraft.encyclopedia.includeItem("lowtide", new ItemStack(ItemRegistry.recordLowTide));
        Tropicraft.encyclopedia.includeItem("magicmushroom", new ItemStack(BlockRegistry.flowers, 1, 7));
        Tropicraft.encyclopedia.includeItem("mahogany", new ItemStack(BlockRegistry.logs, 1, 1));
        Tropicraft.encyclopedia.includeItem("mahoganyfence", new ItemStack(BlockRegistry.mahoganyFence));
        Tropicraft.encyclopedia.includeItem("mahoganyfencegate", new ItemStack(BlockRegistry.mahoganyFenceGate));
        Tropicraft.encyclopedia.includeItem("marlinmeat", new ItemStack(ItemRegistry.freshMarlin));
        Tropicraft.encyclopedia.includeItem("marlincooked", new ItemStack(ItemRegistry.searedMarlin));
        Tropicraft.encyclopedia.includeItem("mineralsand", new ItemStack(BlockRegistry.sands, 1, 4));
        Tropicraft.encyclopedia.includeItem("mixer", new ItemStack(BlockRegistry.drinkMixer));
        //  Tropicraft.encyclopedia.includeItem("nigeljournal", new ItemStack(TCItemRegistry.nigelJournal));
        Tropicraft.encyclopedia.includeItem("orange", new ItemStack(ItemRegistry.orange));
        Tropicraft.encyclopedia.includeItem("orangeade", MixerRecipes.getItemStack(Drink.orangeade));
        Tropicraft.encyclopedia.includeItem("orangesapling", new ItemStack(BlockRegistry.saplings, 1, 3));
        Tropicraft.encyclopedia.includeItem("orchid", new ItemStack(BlockRegistry.flowers, 1, 2));
        Tropicraft.encyclopedia.includeItem("pabshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.PAB.getMeta()));
        Tropicraft.encyclopedia.includeItem("palmplanks", new ItemStack(BlockRegistry.planks, 1, 0)); //0 is palm, 1 is mahogany
        Tropicraft.encyclopedia.includeItem("palmwood", new ItemStack(BlockRegistry.logs, 1, 0));
        Tropicraft.encyclopedia.includeItem("palmsapling", new ItemStack(BlockRegistry.saplings, 1, 0));
        Tropicraft.encyclopedia.includeItem("palmfence", new ItemStack(BlockRegistry.palmFence));
        Tropicraft.encyclopedia.includeItem("palmfencegate", new ItemStack(BlockRegistry.palmFenceGate));
        Tropicraft.encyclopedia.includeItem("pathos", new ItemStack(BlockRegistry.flowers, 1, 8));
        Tropicraft.encyclopedia.includeItem("pearlb", new ItemStack(ItemRegistry.blackPearl));
        Tropicraft.encyclopedia.includeItem("pearlw", new ItemStack(ItemRegistry.whitePearl));
        Tropicraft.encyclopedia.includeItem("pineapple", new ItemStack(BlockRegistry.pineapple, 1, 0));
        Tropicraft.encyclopedia.includeItem("pineapplecubes", new ItemStack(ItemRegistry.pineappleCubes));
        Tropicraft.encyclopedia.includeItem("pinacolada", MixerRecipes.getItemStack(Drink.pinaColada));
        Tropicraft.encyclopedia.includeItem("portalstarter",  new ItemStack(ItemRegistry.portalEnchanter));
        Tropicraft.encyclopedia.includeItem("purifiedsand", new ItemStack(BlockRegistry.sands, 0, 0));
        Tropicraft.encyclopedia.includeItem("reeds", new ItemStack(Items.REEDS));
        Tropicraft.encyclopedia.includeItem("rubenautilus", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.RUBE.getMeta()));
        Tropicraft.encyclopedia.includeItem("scaleboots", new ItemStack(ItemRegistry.scaleBoots));
        Tropicraft.encyclopedia.includeItem("scalechestplate", new ItemStack(ItemRegistry.scaleChestplate));
        Tropicraft.encyclopedia.includeItem("scalehelm", new ItemStack(ItemRegistry.scaleHelmet));
        Tropicraft.encyclopedia.includeItem("scaleleggings", new ItemStack(ItemRegistry.scaleLeggings));
        Tropicraft.encyclopedia.includeItem("scubaflippers", new ItemStack(ItemRegistry.pinkFlippers));
        Tropicraft.encyclopedia.includeItem("scubaflippers", new ItemStack(ItemRegistry.yellowFlippers));
        Tropicraft.encyclopedia.includeItem("scubachestplate", new ItemStack(ItemRegistry.pinkChestplateGear));
        Tropicraft.encyclopedia.includeItem("scubachestplate", new ItemStack(ItemRegistry.yellowChestplateGear));
        Tropicraft.encyclopedia.includeItem("scubagoggles", new ItemStack(ItemRegistry.pinkScubaGoggles));
        Tropicraft.encyclopedia.includeItem("scubagoggles", new ItemStack(ItemRegistry.yellowScubaGoggles));
        Tropicraft.encyclopedia.includeItem("divecomputer", new ItemStack(ItemRegistry.diveComputer));
        Tropicraft.encyclopedia.includeItem("weightbelt", new ItemStack(ItemRegistry.yellowWeightBelt));
        Tropicraft.encyclopedia.includeItem("weightbelt", new ItemStack(ItemRegistry.pinkWeightBelt));
        Tropicraft.encyclopedia.includeItem("ponybottle", new ItemStack(ItemRegistry.yellowPonyBottle));
        Tropicraft.encyclopedia.includeItem("ponybottle", new ItemStack(ItemRegistry.pinkPonyBottle));
        Tropicraft.encyclopedia.includeItem("bcd", new ItemStack(ItemRegistry.pinkBCD));
        Tropicraft.encyclopedia.includeItem("bcd", new ItemStack(ItemRegistry.yellowBCD));
        Tropicraft.encyclopedia.includeItem("regulator", new ItemStack(ItemRegistry.yellowRegulator));
        Tropicraft.encyclopedia.includeItem("regulator", new ItemStack(ItemRegistry.pinkRegulator));
        Tropicraft.encyclopedia.includeItem("scubatank", new ItemStack(ItemRegistry.pinkScubaTank));
        Tropicraft.encyclopedia.includeItem("scubatank", new ItemStack(ItemRegistry.yellowScubaTank));
        Tropicraft.encyclopedia.includeItem("seaurchinroe", new ItemStack(ItemRegistry.seaUrchinRoe));
        Tropicraft.encyclopedia.includeItem("sifter", new ItemStack(BlockRegistry.sifter));
        //TODO Tropicraft.encyclopedia.includeItem("smeltedzircon", new ItemStack(ItemRegistry.ore, 1, 4));
        //TODO Tropicraft.encyclopedia.includeItem("snaretrap", new ItemStack(ItemRegistry.snareTrap));
        //TODO Tropicraft.encyclopedia.includeItem("snorkel", new ItemStack(ItemRegistry.snorkel));
        Tropicraft.encyclopedia.includeItem("solonoxshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.SOLO.getMeta()));
        Tropicraft.encyclopedia.includeItem("starfishshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.STARFISH.getMeta()));
        Tropicraft.encyclopedia.includeItem("summering", new ItemStack(ItemRegistry.recordSummering));
        Tropicraft.encyclopedia.includeItem("tikitorch", new ItemStack(BlockRegistry.tikiTorch));
        Tropicraft.encyclopedia.includeItem("thatchblock", new ItemStack(BlockRegistry.bundles, 1, 0));
        Tropicraft.encyclopedia.includeItem("thatchfence", new ItemStack(BlockRegistry.thatchFence));
        Tropicraft.encyclopedia.includeItem("thatchfencegate", new ItemStack(BlockRegistry.thatchFenceGate));
        Tropicraft.encyclopedia.includeItem("thetribe", new ItemStack(ItemRegistry.recordTheTribe));
        Tropicraft.encyclopedia.includeItem("tradewinds", new ItemStack(ItemRegistry.recordTradeWinds));
        Tropicraft.encyclopedia.includeItem("trimix", new ItemStack(ItemRegistry.trimix));
        Tropicraft.encyclopedia.includeItem("tropiframe", new ItemStack(ItemRegistry.bambooItemFrame));
        Tropicraft.encyclopedia.includeItem("turtleshell", new ItemStack(ItemRegistry.shell, 1, TropicraftShells.TURTLE.getMeta()));
        Tropicraft.encyclopedia.includeItem("waterwand", new ItemStack(ItemRegistry.waterWand));
        Tropicraft.encyclopedia.includeItem("zircon", new ItemStack(ItemRegistry.zircon));
        Tropicraft.encyclopedia.includeItem("fishingrod", new ItemStack(ItemRegistry.fishingRod));

        //Tropicraft.encyclopedia.includeItem("zirconium", new ItemStack(TCItemRegistry.ore, 1, 3));
    }

    @SideOnly(Side.CLIENT)
    public static void addToEncyclopedia(ItemStack itemstack, Object obj[]) {
        Tropicraft.encyclopedia.includeRecipe(itemstack, obj);
    }

    public static void createRecipe(boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
        if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            addToEncyclopedia(itemstack, obj);
        }

        GameRegistry.addRecipe(itemstack, obj);
    }

    public static void createRecipe(boolean isServer, boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
        if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            addToEncyclopedia(itemstack, obj);
        }

        GameRegistry.addRecipe(itemstack, obj);
    }

    public static void createShapelessRecipe(boolean addToEncyclopedia, ItemStack itemstack, Object obj[]) {
        //cannot add shapeless recipes to the encyclopedia yet
        /*if (addToEncyclopedia && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			addToEncyclopedia(itemstack, obj);
		}*/
        GameRegistry.addShapelessRecipe(itemstack, obj);
    }
}
