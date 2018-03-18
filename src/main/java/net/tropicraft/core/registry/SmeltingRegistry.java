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

@Mod.EventBusSubscriber(modid = Info.MODID)
public class SmeltingRegistry extends TropicraftRegistry {

    public static void init() {
        GameRegistry.addSmelting(ItemRegistry.frogLeg, new ItemStack(ItemRegistry.cookedFrogLeg), 0.35F);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.coffeeBeans, 1, 0), new ItemStack(ItemRegistry.coffeeBeans, 1, 1), 0.35F);
        GameRegistry.addSmelting(BlockRegistry.sands, new ItemStack(Blocks.GLASS), 4);
        GameRegistry.addSmelting(ItemRegistry.freshMarlin, new ItemStack(ItemRegistry.searedMarlin), 6);
        GameRegistry.addSmelting(BlockRegistry.logs, new ItemStack(Items.COAL, 1, 1), 3); // metadata 1 = charcoal
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
