package net.tropicraft.core.common.data;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface WorldgenDataConsumer<T> {
    Holder<T> register(ResourceLocation id, T value);

    default Holder<T> register(ResourceKey<T> id, T value) {
        return this.register(id.location(), value);
    }
}
