package net.tropicraft.core.common.dimension.feature.block_state_provider;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;

public final class TropicraftBlockStateProviders {
    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS = DeferredRegister.create(Registries.BLOCK_STATE_PROVIDER_TYPE, Constants.MODID);

    public static final DeferredHolder<BlockStateProviderType<?>, BlockStateProviderType<?>> NOISE_FROM_TAG = BLOCK_STATE_PROVIDERS.register("noise_from_tag", () -> new BlockStateProviderType<>(NoiseFromTagBlockStateProvider.CODEC));
}
