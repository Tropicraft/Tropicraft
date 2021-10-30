package net.tropicraft.core.common.dimension.feature.block_state_provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;

import java.util.List;
import java.util.Random;

public final class NoiseFromTagBlockStateProvider extends BlockStateProvider {
    public static final Codec<NoiseFromTagBlockStateProvider> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                ITag.codec(() -> TagCollectionManager.getInstance().getBlocks()).fieldOf("tag").forGetter(c -> c.tag)
        ).apply(instance, NoiseFromTagBlockStateProvider::new);
    });

    public final ITag<Block> tag;

    public NoiseFromTagBlockStateProvider(ITag<Block> tag) {
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
        noise = MathHelper.clamp((1.0 + noise) / 2.0, 0.0, 0.9999);

        Block block = blocks.get(MathHelper.floor(noise * blocks.size()));
        return block.defaultBlockState();
    }
}
