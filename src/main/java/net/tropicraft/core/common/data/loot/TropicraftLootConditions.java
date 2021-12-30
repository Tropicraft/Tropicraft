package net.tropicraft.core.common.data.loot;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.tropicraft.Constants;

public final class TropicraftLootConditions {
    public static final LootConditionType MATCH_SWORD = register("match_sword", new MatchSwordCondition.Serializer());

    private static LootConditionType register(String name, ILootSerializer<? extends ILootCondition> serializer) {
        LootConditionType type = new LootConditionType(serializer);
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Constants.MODID, name), type);
    }
}
