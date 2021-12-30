package net.tropicraft.core.common.data.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.tropicraft.core.common.TropicraftTags;

import java.util.Set;

// TODO: when we have a properly standard forge tag for swords we can remove this
public final class MatchSwordCondition implements ILootCondition {
    public static ILootCondition.IBuilder builder() {
        return MatchSwordCondition::new;
    }

    @Override
    public LootConditionType getConditionType() {
        return TropicraftLootConditions.MATCH_SWORD;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.TOOL);
    }

    @Override
    public boolean test(LootContext ctx) {
        ItemStack stack = ctx.get(LootParameters.TOOL);
        if (stack != null) {
            return stack.getItem() instanceof SwordItem || stack.getItem().isIn(TropicraftTags.Items.SWORDS);
        } else {
            return false;
        }
    }

    public static class Serializer implements ILootSerializer<MatchSwordCondition> {
        @Override
        public void serialize(JsonObject root, MatchSwordCondition condition, JsonSerializationContext ctx) {
        }

        @Override
        public MatchSwordCondition deserialize(JsonObject root, JsonDeserializationContext ctx) {
            return new MatchSwordCondition();
        }
    }
}
