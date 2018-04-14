package net.tropicraft.core.registry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Info;
import net.tropicraft.core.common.enums.TropicraftOres;

@Mod.EventBusSubscriber(modid = Info.MODID)
public class SmeltingRegistry extends TropicraftRegistry {

    public static void init() {
        GameRegistry.addSmelting(ItemRegistry.frogLeg, new ItemStack(ItemRegistry.cookedFrogLeg), 0.35F);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.coffeeBeans, 1, 0), new ItemStack(ItemRegistry.coffeeBeans, 1, 1), 0.35F);
        GameRegistry.addSmelting(BlockRegistry.sands, new ItemStack(Blocks.GLASS), 4);
        GameRegistry.addSmelting(ItemRegistry.freshMarlin, new ItemStack(ItemRegistry.searedMarlin), 6);
        GameRegistry.addSmelting(BlockRegistry.logs, new ItemStack(Items.COAL, 1, 1), 3); // metadata 1 = charcoal
        GameRegistry.addSmelting(ItemRegistry.rawNori, new ItemStack(ItemRegistry.toastedNori), 2);
        GameRegistry.addSmelting(ItemRegistry.rawRay, new ItemStack(ItemRegistry.cookedRay), 4);
        GameRegistry.addSmelting(ItemRegistry.rawTropicalFish, new ItemStack(ItemRegistry.cookedTropicalFish), 4);
        GameRegistry.addSmelting(ItemRegistry.rawRiverFish, new ItemStack(ItemRegistry.cookedRiverFish), 3.5F);
        
        // Silk touched ore smelting
        Block ore = BlockRegistry.ore;
        GameRegistry.addSmelting(TropicraftOres.AZURITE.makeStack(ore), new ItemStack(ItemRegistry.azurite), 0.7F);
        GameRegistry.addSmelting(TropicraftOres.EUDIALYTE.makeStack(ore), new ItemStack(ItemRegistry.eudialyte), 0.7F);
        GameRegistry.addSmelting(TropicraftOres.ZIRCON.makeStack(ore), new ItemStack(ItemRegistry.zircon), 0.7F);
    }

    /**
     * Burn times for tropicraft slabs
     * @param event
     */
    @SubscribeEvent
    public static void slabBurnTimeHandler(FurnaceFuelBurnTimeEvent event) {
        if (!event.getItemStack().isEmpty()) {
            Block block = Block.getBlockFromItem(event.getItemStack().getItem());
            // Slab burn times
            if (block == BlockRegistry.slabs &&
                    (event.getItemStack().getItemDamage() == 3 ||
                    event.getItemStack().getItemDamage() == 4)) {
                event.setBurnTime(150);
            }

            // Cancel stair burns
            if (block == BlockRegistry.chunkStairs ||
                    block == BlockRegistry.thatchStairs ||
                    block == BlockRegistry.bambooStairs) {
                event.setBurnTime(0);
            }
        }
    }
}
