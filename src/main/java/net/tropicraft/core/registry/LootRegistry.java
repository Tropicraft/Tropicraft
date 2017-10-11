package net.tropicraft.core.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.tropicraft.Info;

public class LootRegistry extends TropicraftRegistry {

    public static ResourceLocation buriedTreasure;
    
    public static void postInit() {
        buriedTreasure = LootTableList.register(new ResourceLocation(Info.MODID, "buried_treasure"));
    }
}
