package net.tropicraft.core.common.block.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MODID);

    public static final RegistryObject<BlockEntityType<BambooChestBlockEntity>> BAMBOO_CHEST = BLOCK_ENTITIES.register(
            "bamboo_chest", () -> new BlockEntityType<>(BambooChestBlockEntity::new, Sets.newHashSet(TropicraftBlocks.BAMBOO_CHEST.get()), null));
    public static final RegistryObject<BlockEntityType<SifterBlockEntity>> SIFTER = BLOCK_ENTITIES.register(
            "sifter", () -> new BlockEntityType<>(SifterBlockEntity::new, Sets.newHashSet(TropicraftBlocks.SIFTER.get()), null));
    public static final RegistryObject<BlockEntityType<DrinkMixerBlockEntity>> DRINK_MIXER = BLOCK_ENTITIES.register(
            "drink_mixer", () -> new BlockEntityType<>(DrinkMixerBlockEntity::new, Sets.newHashSet(TropicraftBlocks.DRINK_MIXER.get()), null));
    public static final RegistryObject<BlockEntityType<AirCompressorBlockEntity>> AIR_COMPRESSOR = BLOCK_ENTITIES.register(
            "air_compressor", () -> new BlockEntityType<>(AirCompressorBlockEntity::new, Sets.newHashSet(TropicraftBlocks.AIR_COMPRESSOR.get()), null));
    public static final RegistryObject<BlockEntityType<VolcanoBlockEntity>> VOLCANO = BLOCK_ENTITIES.register(
            "tile_entity_volcano", () -> new BlockEntityType<>(VolcanoBlockEntity::new, Sets.newHashSet(TropicraftBlocks.VOLCANO.get()), null));
}
