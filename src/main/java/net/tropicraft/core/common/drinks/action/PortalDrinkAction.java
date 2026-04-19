package net.tropicraft.core.common.drinks.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.Optional;

public record PortalDrinkAction(
        ResourceKey<Level> dimension,
        Optional<LootItemCondition> condition
) implements DrinkAction {
    public static final MapCodec<PortalDrinkAction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(PortalDrinkAction::dimension),
            LootItemCondition.DIRECT_CODEC.optionalFieldOf("condition").forGetter(PortalDrinkAction::condition)
    ).apply(i, PortalDrinkAction::new));

    @Override
    public void onDrink(ServerPlayer player) {
        LootParams params = new LootParams.Builder(player.level())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.COMMAND);
        LootContext context = new LootContext.Builder(params).create(Optional.empty());
        if (condition.isEmpty() || condition.get().test(context)) {
            TropicraftDimension.teleportPlayerWithPortal(player, dimension);
        }
    }

    @Override
    public MapCodec<PortalDrinkAction> codec() {
        return CODEC;
    }
}
