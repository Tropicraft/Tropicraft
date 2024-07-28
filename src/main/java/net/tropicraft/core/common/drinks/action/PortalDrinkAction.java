package net.tropicraft.core.common.drinks.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.Optional;

public record PortalDrinkAction(
        ResourceKey<Level> dimension,
        Optional<HolderSet<EntityType<?>>> whileRiding,
        int minTimeOfDay,
        int maxTimeOfDay
) implements DrinkAction {
    public static final MapCodec<PortalDrinkAction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(PortalDrinkAction::dimension),
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("while_riding").forGetter(PortalDrinkAction::whileRiding),
            Codec.intRange(0, Level.TICKS_PER_DAY).fieldOf("min_time_of_day").forGetter(PortalDrinkAction::minTimeOfDay),
            Codec.intRange(0, Level.TICKS_PER_DAY).fieldOf("max_time_of_day").forGetter(PortalDrinkAction::maxTimeOfDay)
    ).apply(i, PortalDrinkAction::new));

    @Override
    public void onDrink(ServerPlayer player) {
        if (whileRiding.isPresent()) {
            Entity vehicle = player.getVehicle();
            if (vehicle == null || !whileRiding.get().contains(vehicle.getType().builtInRegistryHolder())) {
                return;
            }
        }
        if (timeMatches(player.level())) {
            TropicraftDimension.teleportPlayerWithPortal(player, dimension);
        }
    }

    private boolean timeMatches(Level level) {
        long timeDay = level.getDayTime() % Level.TICKS_PER_DAY;
        return timeDay > minTimeOfDay && timeDay < maxTimeOfDay;
    }

    @Override
    public MapCodec<PortalDrinkAction> codec() {
        return CODEC;
    }
}
