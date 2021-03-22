package net.tropicraft.core.common.data;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;

public interface WorldgenDataConsumer<T> {
    T register(ResourceLocation id, T entry);

    default T register(RegistryKey<T> id, T entry) {
        return this.register(id.getLocation(), entry);
    }
}
