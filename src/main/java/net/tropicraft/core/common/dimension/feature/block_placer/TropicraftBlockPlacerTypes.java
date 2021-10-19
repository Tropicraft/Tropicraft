package net.tropicraft.core.common.dimension.feature.block_placer;

import net.minecraft.world.gen.blockplacer.BlockPlacerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;

public final class TropicraftBlockPlacerTypes {
    public static final DeferredRegister<BlockPlacerType<?>> BLOCK_PLACER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES, Constants.MODID);

    public static final RegistryObject<BlockPlacerType<?>> HUGE_PLANT = BLOCK_PLACER_TYPES.register("huge_plant", () -> new BlockPlacerType<>(HugePlantBlockPlacer.CODEC));
}
