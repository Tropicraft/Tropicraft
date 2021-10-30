package net.tropicraft.core.common.data;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface WorldgenDataConsumer<T> {
    T register(ResourceLocation id, T entry);

    default T register(ResourceKey<T> id, T entry) {
        return this.register(id.location(), entry);
    }
}
