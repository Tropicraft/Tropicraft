package net.tropicraft.core.common.data.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.tropicraft.core.common.TropicraftTags;

import java.util.Set;

// TODO: when we have a properly standard forge tag for swords we can remove this
public final class MatchSwordCondition implements LootItemCondition {
    public static LootItemCondition.Builder builder() {
        return MatchSwordCondition::new;
    }

    @Override
    public LootItemConditionType getType() {
        return TropicraftLootConditions.MATCH_SWORD;
    }


    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }

    @Override
    public boolean test(LootContext ctx) {
        ItemStack stack = ctx.getParam(LootContextParams.TOOL);
        if (stack != null) {
            return stack.getItem() instanceof SwordItem || stack.is(TropicraftTags.Items.SWORDS);
        } else {
            return false;
        }
    }

    public static class SwordSerializer implements Serializer<MatchSwordCondition> {
        @Override
        public void serialize(JsonObject root, MatchSwordCondition condition, JsonSerializationContext ctx) {
        }

        @Override
        public MatchSwordCondition deserialize(JsonObject root, JsonDeserializationContext ctx) {
            return new MatchSwordCondition();
        }
    }
}
