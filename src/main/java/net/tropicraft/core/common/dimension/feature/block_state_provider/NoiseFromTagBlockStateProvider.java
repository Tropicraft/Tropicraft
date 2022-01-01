package net.tropicraft.core.common.dimension.feature.block_state_provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

import java.util.List;
import java.util.Random;

public final class NoiseFromTagBlockStateProvider extends BlockStateProvider {
    public static final Codec<NoiseFromTagBlockStateProvider> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Tag.codec(BlockTags::getAllTags).fieldOf("tag").forGetter(c -> c.tag)
        ).apply(instance, NoiseFromTagBlockStateProvider::new);
    });

    public final Tag<Block> tag;

    public NoiseFromTagBlockStateProvider(Tag<Block> tag) {
        this.tag = tag;
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return TropicraftBlockStateProviders.NOISE_FROM_TAG.get();
    }

    @Override
    public BlockState getState(Random random, BlockPos pos) {
        List<Block> blocks = this.tag.getValues();
        if (blocks.isEmpty()) {
            return Blocks.AIR.defaultBlockState();
        }

        double noise = Biome.BIOME_INFO_NOISE.getValue(pos.getX() / 48.0, pos.getZ() / 48.0, false);
        noise = Mth.clamp((1.0 + noise) / 2.0, 0.0, 0.9999);

        Block block = blocks.get(Mth.floor(noise * blocks.size()));
        return block.defaultBlockState();
    }
}
