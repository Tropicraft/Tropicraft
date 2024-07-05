package net.tropicraft.core.common.dimension.feature.block_state_provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

public final class NoiseFromTagBlockStateProvider extends BlockStateProvider {
    public static final MapCodec<NoiseFromTagBlockStateProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(o -> o.blocks)
    ).apply(i, NoiseFromTagBlockStateProvider::new));

    public final HolderSet<Block> blocks;

    public NoiseFromTagBlockStateProvider(HolderSet<Block> blocks) {
        this.blocks = blocks;
    }

    public NoiseFromTagBlockStateProvider(TagKey<Block> blocks) {
        this(BuiltInRegistries.BLOCK.getOrCreateTag(blocks));
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return TropicraftBlockStateProviders.NOISE_FROM_TAG.get();
    }

    @Override
    public BlockState getState(RandomSource random, BlockPos pos) {
        double noise = Biome.BIOME_INFO_NOISE.getValue(pos.getX() / 48.0, pos.getZ() / 48.0, false);
        noise = Mth.clamp((1.0 + noise) / 2.0, 0.0, 0.9999);

        final Holder<Block> block = blocks.get(Mth.floor(noise * blocks.size()));
        return block.value().defaultBlockState();
    }
}
