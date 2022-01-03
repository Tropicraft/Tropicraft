package net.tropicraft.core.common.dimension.feature.block_state_provider;

import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;

public final class TropicraftBlockStateProviders {
    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, Constants.MODID);

    public static final RegistryObject<BlockStateProviderType<?>> NOISE_FROM_TAG = BLOCK_STATE_PROVIDERS.register("noise_from_tag", () -> new BlockStateProviderType<>(NoiseFromTagBlockStateProvider.CODEC));
}
