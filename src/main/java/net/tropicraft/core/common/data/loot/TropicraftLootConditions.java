package net.tropicraft.core.common.data.loot;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.tropicraft.Constants;

public final class TropicraftLootConditions {
    public static final LootItemConditionType MATCH_SWORD = register("match_sword", new MatchSwordCondition.MatchSwordSerializer());

    private static LootItemConditionType register(String name, Serializer<? extends LootItemCondition> serializer) {
        LootItemConditionType type = new LootItemConditionType(serializer);
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Constants.MODID, name), type);
    }
}
