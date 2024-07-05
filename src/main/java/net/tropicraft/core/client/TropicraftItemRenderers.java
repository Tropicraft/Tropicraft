package net.tropicraft.core.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tropicraft.core.client.tileentity.SimpleItemStackRenderer;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;

import java.util.function.Supplier;

public final class TropicraftItemRenderers {
    public static IClientItemExtensions bambooChest() {
        return renderItemAsBlockEntity(() -> new BambooChestBlockEntity(TropicraftBlocks.BAMBOO_CHEST_ENTITY.get(), BlockPos.ZERO, TropicraftBlocks.BAMBOO_CHEST.get().defaultBlockState()));
    }

    public static IClientItemExtensions drinkMixer() {
        return renderItemAsBlockEntity(() -> new DrinkMixerBlockEntity(TropicraftBlocks.DRINK_MIXER_ENTITY.get(), BlockPos.ZERO, TropicraftBlocks.DRINK_MIXER.get().defaultBlockState()));
    }

    public static IClientItemExtensions airCompressor() {
        return renderItemAsBlockEntity(() -> new AirCompressorBlockEntity(TropicraftBlocks.AIR_COMPRESSOR_ENTITY.get(), BlockPos.ZERO, TropicraftBlocks.AIR_COMPRESSOR.get().defaultBlockState()));
    }

    public static IClientItemExtensions renderItemAsBlockEntity(Supplier<BlockEntity> blockEntityFactory) {
        return new IClientItemExtensions() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new SimpleItemStackRenderer<>(blockEntityFactory);
            }
        };
    }
}
