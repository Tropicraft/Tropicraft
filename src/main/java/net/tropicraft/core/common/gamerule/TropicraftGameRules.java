package net.tropicraft.core.common.gamerule;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;

public class TropicraftGameRules {
    public static final DeferredRegister<GameRule<?>> REGISTER = DeferredRegister.create(Registries.GAME_RULE, Tropicraft.ID);

    public static final DeferredHolder<GameRule<?>, GameRule<Boolean>> UNRESTRICTED_EXPLODING_COCONUTS = registerBoolean("unrestricted_exploding_coconuts", GameRuleCategory.PLAYER, false);
    public static final DeferredHolder<GameRule<?>, GameRule<Boolean>> SPAWN_MONSTERS_IN_TROPICS = registerBoolean("spawn_monsters_in_tropics", GameRuleCategory.SPAWNING, false);

    private static DeferredHolder<GameRule<?>, GameRule<Boolean>> registerBoolean(String id, GameRuleCategory category, boolean defaultValue) {
        return REGISTER.register(id, () -> new GameRule<>(category, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, defaultValue, FeatureFlagSet.of()));
    }
}
